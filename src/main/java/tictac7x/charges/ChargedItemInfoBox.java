package tictac7x.charges;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.triggers.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargedItemInfoBox extends InfoBox {
    protected int item_id;
    protected final Client client;
    protected final ClientThread client_thread;
    protected final ItemManager items;
    protected final InfoBoxManager infoboxes;
    protected final ConfigManager configs;

    @Nullable protected ItemContainer inventory;
    @Nullable protected ItemContainer equipment;
    @Nullable protected String menu_option;

    protected boolean needs_to_be_equipped_for_infobox;
    protected boolean equipped;

    @Nullable protected String config_key;
    @Nullable protected TriggerChatMessage[] triggers_chat_messages;
    @Nullable protected TriggerAnimation[] triggers_animations;
    @Nullable protected TriggerGraphic[] triggers_graphics;
    @Nullable protected TriggerHitsplat[] triggers_hitsplats;
    @Nullable protected TriggerItem[] triggers_items;
    @Nullable protected TriggerWidget[] triggers_widgets;
    @Nullable protected String[] extra_groups;

    protected int animation = -1;
    protected int graphic = -1;
    protected int charges = -1;
    protected String tooltip;
    private boolean render = false;

    public ChargedItemInfoBox(final int item_id, final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final Plugin plugin) {
        super(items.getImage(item_id), plugin);
        this.item_id = item_id;
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;
        this.infoboxes = infoboxes;
        loadChargesFromConfig();
    }

    @Override
    public String getName() {
        return super.getName() + "_" + item_id;
    }

    @Override
    public String getText() {
        return this.charges == -1 ? "?" : needs_to_be_equipped_for_infobox && !equipped ? "0" : String.valueOf(charges);
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }

    @Override
    public Color getTextColor() {
        return getText().equals("?") ? Color.orange : getText().equals("0") ? Color.red : Color.white;
    }

    @Override
    public boolean render() {
        return render;
    }

    public void onItemContainersChanged(@Nonnull final ItemContainer inventory, @Nonnull final ItemContainer equipment) {
        // Save inventory and equipment item containers for other uses.
        this.inventory = inventory;
        this.equipment = equipment;

        // No trigger items to detect charges.
        if (triggers_items == null) return;

        this.equipped = false;
        this.render = false;

        for (final TriggerItem trigger_item : triggers_items) {
            // Find out if infobox should be rendered.
            if (inventory.contains(trigger_item.item_id) || equipment.contains(trigger_item.item_id)) {
                this.render = true;

                // Update infobox item picture and tooltip dynamically based on the items if use has different variant of it.
                if (trigger_item.item_id != item_id) {
                    this.updateInfobox(trigger_item.item_id);
                }
            }

            // Find out if item is equipped.
            if (equipment.contains(trigger_item.item_id)) {
                this.equipped = true;
            }

            // Check if inventory or equipment contains trigger item.
            if (!inventory.contains(trigger_item.item_id) && !equipment.contains(trigger_item.item_id)) continue;

            // Find out charges for the item.
            if (trigger_item.charges != null) {
                int charges = 0;
                charges += inventory.count(trigger_item.item_id) * trigger_item.charges;
                charges += equipment.count(trigger_item.item_id) * trigger_item.charges;
                this.charges = charges;
            }
        }
    }

    public void onChatMessage(final ChatMessage event) {
        if (
            event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM ||
            this.config_key == null ||
            triggers_chat_messages == null
        ) return;

        final String message = event.getMessage().replaceAll("</?col.*?>", "");

        for (final TriggerChatMessage chat_message : triggers_chat_messages) {
            final Pattern regex = Pattern.compile(chat_message.message);
            final Matcher matcher = regex.matcher(message);
            if (!matcher.find()) continue;

            // Check default "charges" group.
            setCharges(chat_message.charges != null
                // Charges amount is fixed.
                ? chat_message.charges
                // Charges amount is dynamic and extracted from the message.
                : Integer.parseInt(matcher.group("charges").replaceAll(",", ""))
            );

            // Check extra matches groups.
            if (extra_groups != null) {
                for (final String extra_group : extra_groups) {
                    final String extra = matcher.group(extra_group);
                    if (extra != null) setConfiguration(config_key + "_" + extra_group, extra);
                }
            }

            return;
        }
    }
    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(config_key)) {
            this.charges = Integer.parseInt(event.getNewValue());
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return;

        // Save animation ID for others to use.
        this.animation = event.getActor().getAnimation();

        // No animations to check.
        if (inventory == null || equipment == null || triggers_animations == null || this.charges == -1 || this.triggers_items == null) return;

        // Check all animation triggers.
        for (final TriggerAnimation trigger_animation : triggers_animations) {
            // Valid animation id check.
            if (trigger_animation.animation_id != event.getActor().getAnimation()) continue;

            // Menu option check.
            if (trigger_animation.menu_option != null && menu_option != null && !menu_option.equals(trigger_animation.menu_option)) continue;

            // Unallowed items check.
            if (trigger_animation.unallowed_items != null) {
                boolean unallowed_items = false;
                for (final int item_id : trigger_animation.unallowed_items) {
                    if (inventory.contains(item_id) || equipment.contains(item_id)) {
                        unallowed_items = true;
                        break;
                    }
                }
                if (unallowed_items) continue;
            }

            // Equipped check.
            if (trigger_animation.equipped) {
                boolean equipped = false;
                for (final TriggerItem trigger_item : triggers_items) {
                    if (equipment.contains(trigger_item.item_id)) {
                        equipped = true;
                        break;
                    }
                }
                if (!equipped) continue;
            }

            // Valid trigger, modify charges.
            if (trigger_animation.decrease_charges) {
                decreaseCharges(trigger_animation.charges);
            } else {
                increaseCharges(trigger_animation.charges);
            }
        }
    }

    public void onGraphicChanged(final GraphicChanged event) {
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return;

        // Save animation ID for others to use.
        this.graphic = event.getActor().getGraphic();

        // No animations to check.
        if (inventory == null || equipment == null || triggers_graphics == null || this.charges == -1 || this.triggers_items == null) return;

        // Check all animation triggers.
        for (final TriggerGraphic trigger_graphic : triggers_graphics) {
            // Valid animation id check.
            if (trigger_graphic.graphic_id != event.getActor().getGraphic()) continue;

            // Menu option check.
            if (trigger_graphic.menu_option != null && menu_option != null && !menu_option.equals(trigger_graphic.menu_option)) continue;

            // Unallowed items check.
            if (trigger_graphic.unallowed_items != null) {
                boolean unallowed_items = false;
                for (final int item_id : trigger_graphic.unallowed_items) {
                    if (inventory.contains(item_id) || equipment.contains(item_id)) {
                        unallowed_items = true;
                        break;
                    }
                }
                if (unallowed_items) continue;
            }

            // Equipped check.
            if (trigger_graphic.equipped) {
                boolean equipped = false;
                for (final TriggerItem trigger_item : triggers_items) {
                    if (equipment.contains(trigger_item.item_id)) {
                        equipped = true;
                        break;
                    }
                }
                if (!equipped) continue;
            }

            // Valid trigger, modify charges.
            if (trigger_graphic.decrease_charges) {
                decreaseCharges(trigger_graphic.charges);
            } else {
                increaseCharges(trigger_graphic.charges);
            }
        }
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        if (triggers_hitsplats == null) return;

        // Check all hitsplat triggers.
        for (final TriggerHitsplat trigger_hitsplat : triggers_hitsplats) {
            // Player check.
            if (trigger_hitsplat.self && event.getActor() != client.getLocalPlayer()) continue;

            // Enemy check.
            if (!trigger_hitsplat.self && (event.getActor() == client.getLocalPlayer() || event.getHitsplat().isOthers())) continue;

            // Hitsplat type check.
            if (trigger_hitsplat.hitsplat_id != event.getHitsplat().getHitsplatType()) continue;

            // Equipped check.
            if (trigger_hitsplat.equipped && triggers_items != null && equipment != null) {
                boolean equipped = false;
                for (final TriggerItem trigger_item : triggers_items) {
                    if (equipment.contains(trigger_item.item_id)) {
                        equipped = true;
                        break;
                    }
                }
                if (!equipped) continue;
            }

            // Animation check.
            if (trigger_hitsplat.animation != null) {
                boolean valid = false;
                for (final int animation : trigger_hitsplat.animation) {
                    if (animation == this.animation) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) continue;
            }

            // Valid hitsplat, modify charges.
            decreaseCharges(trigger_hitsplat.discharges);
        }
    }


    public void onWidgetLoaded(final WidgetLoaded event) {
        if (triggers_widgets == null || config_key == null) return;

        client_thread.invokeLater(() -> {
            for (final TriggerWidget trigger_widget : triggers_widgets) {
                final Widget widget = client.getWidget(trigger_widget.widget_group, trigger_widget.widget_child);
                if (widget == null) continue;

                final Pattern regex = Pattern.compile(trigger_widget.message);
                final String message = widget.getText().replaceAll("<br>", " ");
                final Matcher matcher = regex.matcher(message);
                if (!matcher.find()) continue;


                // Check default "charges" group.
                setCharges(trigger_widget.charges != null
                    // Charges amount is fixed.
                    ? trigger_widget.charges
                    // Charges amount is dynamic and extracted from the message.
                    : Integer.parseInt(matcher.group("charges").replaceAll(",", ""))
                );

                // Check extra matches groups.
                if (extra_groups != null) {
                    for (final String extra_group : extra_groups) {
                        final String extra = matcher.group(extra_group);
                        if (extra != null) setConfiguration(config_key + "_" + extra_group, extra);
                    }
                }
            }
        });
    }

    public void onMenuOptionClicked(final MenuOptionClicked event) {
        this.menu_option = event.getMenuOption();
    }

    private void loadChargesFromConfig() {
        client_thread.invokeLater(() -> {
            if (config_key == null) return;
            this.charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));
        });
    }

    private void setCharges(final int charges) {
        if (config_key == null) return;
        this.charges = charges;
        setConfiguration(config_key, charges);
    }

    private void decreaseCharges(final int charges) {
        if (this.charges - charges < 0) return;
        setCharges(this.charges - charges);
    }

    private void increaseCharges(final int charges) {
        if (this.charges == -1) return;
        setCharges(this.charges + charges);
    }

    private void setConfiguration(final String key, @Nonnull final String value) {
        configs.setConfiguration(ChargesImprovedConfig.group, key, value);
    }

    private void setConfiguration(final String key, final int value) {
        configs.setConfiguration(ChargesImprovedConfig.group, key, value);
    }

    private void updateInfobox(final int item_id) {
        // Item id.
        this.item_id = item_id;

        // Tooltip.
        this.tooltip = items.getItemComposition(item_id).getName() + (needs_to_be_equipped_for_infobox && !equipped ? " - Needs to be equipped" : "");

        // Image.
        setImage(items.getImage(item_id));
        infoboxes.updateInfoBoxImage(this);
    }
}


