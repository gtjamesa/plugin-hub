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
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
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
    private final ChargesItem infobox_id;
    public int item_id;
    protected final Client client;
    protected final ClientThread client_thread;
    protected final ItemManager items;
    protected final InfoBoxManager infoboxes;
    protected final ConfigManager configs;
    protected final ChatMessageManager chat_messages;
    protected final ChargesImprovedConfig config;

    @Nullable
    public ItemContainer inventory;
    @Nullable protected ItemContainer equipment;

    @Nullable protected String config_key;
    @Nullable protected String[] extra_config_keys;
    @Nullable protected TriggerChatMessage[] triggers_chat_messages;
    @Nullable protected TriggerAnimation[] triggers_animations;
    @Nullable protected TriggerGraphic[] triggers_graphics;
    @Nullable protected TriggerHitsplat[] triggers_hitsplats;
    @Nullable protected TriggerItem[] triggers_items;
    @Nullable protected TriggerWidget[] triggers_widgets;
    @Nullable protected TriggerReset[] triggers_resets;
    protected TriggerMenuOption[] triggers_menu_options = new TriggerMenuOption[]{};

    protected boolean needs_to_be_equipped_for_infobox;
    private boolean in_equipment;
    private boolean in_inventory;
    @Nullable String menu_option;
    @Nullable String menu_target;
    private int animation = -1;
    private int graphic = -1;
    protected int charges = -1;
    protected boolean charges_from_name = false;

    private String tooltip;
    private boolean render = false;
    public boolean zero_charges_is_positive = false;

    public ChargedItemInfoBox(
        final ChargesItem infobox_id,
        final int item_id,
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(items.getImage(item_id), plugin);
        this.infobox_id = infobox_id;
        this.item_id = item_id;
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;
        this.infoboxes = infoboxes;
        this.chat_messages = chat_messages;
        this.config = config;

        client_thread.invokeLater(() -> {
            this.loadChargesFromConfig();
            this.updateTooltip();
            this.onChargesUpdated();
        });
    }

    @Override
    public String getName() {
        return super.getName() + "_" + this.item_id;
    }

    @Override
    public String getText() {
        return ChargesImprovedPlugin.getChargesMinified(this.needs_to_be_equipped_for_infobox && !this.in_equipment ? 0 : this.charges);
    }

    @Override
    public String getTooltip() {
        return this.tooltip;
    }

    @Override
    public Color getTextColor() {
        return getText().equals("?") ? config.getColorUnknown() : getText().equals("0") && !this.zero_charges_is_positive ? config.getColorEmpty() : config.getColorDefault();
    }

    private boolean isAllowed() {
        return !config.getHiddenInfoboxes().contains(this.infobox_id);
    }

    @Override
    public boolean render() {
        return config.showInfoboxes() && this.isAllowed() && this.render;
    }

    public int getCharges() {
        return this.charges;
    }

    public void onItemContainersChanged(@Nonnull final ItemContainer inventory, @Nonnull final ItemContainer equipment) {
        // Save inventory and equipment item containers for other uses.
        this.inventory = inventory;
        this.equipment = equipment;

        // No trigger items to detect charges.
        if (triggers_items == null) return;

        boolean in_equipment = false;
        boolean in_inventory = false;
        boolean render = false;
        Integer charges = null;

        for (final TriggerItem trigger_item : triggers_items) {
            // Find out if item is equipped.
            final boolean in_equipment_item = equipment.contains(trigger_item.item_id);
            final boolean in_inventory_item = inventory.contains(trigger_item.item_id);

            // Find out if infobox should be rendered.
            if (in_inventory_item || in_equipment_item) {
                render = true;

                // Update infobox item picture and tooltip dynamically based on the items if use has different variant of it.
                if (trigger_item.item_id != item_id) {
                    this.updateInfobox(trigger_item.item_id);
                }

                if (in_equipment_item) in_equipment = true;
                if (in_inventory_item) in_inventory = true;
            }

            // Item not found, don't calculate charges.
            if (!in_equipment_item && !in_inventory_item) continue;

            // Find out charges for the item.
            if (trigger_item.charges != null) {
                if (charges == null) charges = 0;
                charges += inventory.count(trigger_item.item_id) * trigger_item.charges;
                charges += equipment.count(trigger_item.item_id) * trigger_item.charges;
            }
        }

        // Update infobox variables for other triggers.
        this.in_equipment = in_equipment;
        this.in_inventory = in_inventory;
        this.render = render;
        if (charges != null) this.charges = charges;
    }

    public void onChatMessage(final ChatMessage event) {
        if (
            // No chat messages triggers.
            triggers_chat_messages == null ||
            // Message type that we are not interested in.
            event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM ||
            // No config to save charges to.
            this.config_key == null ||
            // Not in inventory nor in equipment.
            (!this.in_inventory && !this.in_equipment)
        ) return;

        final String message = event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");

        for (final TriggerChatMessage chat_message : triggers_chat_messages) {
            // Menu target check.
            if (
                chat_message.menu_target &&
                (this.menu_target == null ||
                (!this.menu_target.equals(items.getItemComposition(this.item_id).getName())))
            ) continue;

            // Item needs to be equipped but isn't.
            if (chat_message.equipped && !this.in_equipment) continue;

            final Pattern regex = chat_message.message;
            final Matcher matcher = regex.matcher(message);

            // Message does not match the pattern.
            if (!matcher.find()) continue;

            // Increase charges by fixed amount.
            if (chat_message.increase_charges != null) {
                this.increaseCharges(chat_message.increase_charges);

            // Decrease charges by fixed amount.
            } else if (chat_message.decrease_charges != null) {
                this.decreaseCharges(chat_message.decrease_charges);

            // Set charges to fixed amount.
            } else if (chat_message.fixed_charges != null) {
                this.setCharges(chat_message.fixed_charges);

            // Custom consumer.
            } else if (chat_message.consumer != null) {
                chat_message.consumer.accept(message);

            // Set charges dynamically from the chat message.
            } else if (matcher.group("charges") != null) {
                setCharges(Integer.parseInt(matcher.group("charges").replaceAll(",", "").replaceAll("\\.", "")));

            // No trigger found.
            } else {
                continue;
            }

            // Check extra matches groups.
            if (extra_config_keys != null) {
                for (final String extra_group : extra_config_keys) {
                    final String extra = matcher.group(extra_group);
                    if (extra != null) {
                        setConfiguration(config_key + "_" + extra_group, extra.replaceAll(",", ""));
                    }
                }
            }

            // Chat message used, no need to check other messages.
            return;
        }
    }
    public void onWidgetLoaded(final WidgetLoaded event) {
        if (triggers_widgets == null || config_key == null) return;

        client_thread.invokeLater(() -> {
            for (final TriggerWidget trigger_widget : triggers_widgets) {
                Widget widget = client.getWidget(trigger_widget.group_id, trigger_widget.child_id);
                if (trigger_widget.sub_child_id != null && widget != null) widget = widget.getChild(trigger_widget.sub_child_id);
                if (widget == null) continue;

                final String message = widget.getText().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");
                final Pattern regex = Pattern.compile(trigger_widget.message);
                final Matcher matcher = regex.matcher(message);
                if (!matcher.find()) continue;

                // Charges amount is fixed.
                if (trigger_widget.charges != null) {
                    setCharges(trigger_widget.charges);
                // Charges amount has custom logic.
                } else if (trigger_widget.consumer != null) {
                    trigger_widget.consumer.accept(message);
                // Charges amount is dynamic.
                } else if (matcher.group("charges") != null) {
                    setCharges(Integer.parseInt(matcher.group("charges").replaceAll(",", "")));
                }

                // Check extra matches groups.
                if (extra_config_keys != null) {
                    for (final String extra_group : extra_config_keys) {
                        final String extra = matcher.group(extra_group);
                        if (extra != null) setConfiguration(config_key + "_" + extra_group, extra);
                    }
                }
            }
        });
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

            // Menu target check.
            if (trigger_animation.menu_target && (this.menu_target == null || !this.menu_target.equals(items.getItemComposition(this.item_id).getName()))) continue;

            // Menu option check.
            if (trigger_animation.menu_option != null && (this.menu_option == null || !this.menu_option.contains(trigger_animation.menu_option))) continue;

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

            // Menu option check.
            if (trigger_graphic.menu_option != null && (this.menu_option == null || !this.menu_option.equals(trigger_graphic.menu_option))) continue;

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
            if (trigger_hitsplat.animations != null) {
                boolean valid = false;
                for (final int animation : trigger_hitsplat.animations) {
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


    public void onMenuOptionClicked(final MenuOptionClicked event) {
        final String menu_target = event.getMenuTarget().replaceAll("</?col.*?>", "");
        final String menu_option = event.getMenuOption();

        if (
            // Not menu.
            menu_target.length() == 0 ||
            // Item not in inventory nor equipment.
            !this.in_inventory && !this.in_equipment ||
            // Menu option not found.
            menu_option == null || menu_option.length() == 0
        ) {
            return;
        }


        // Save menu option and target for other triggers to use.
        this.menu_option = menu_option;
        this.menu_target = menu_target;

        for (final TriggerMenuOption trigger_menu_option : this.triggers_menu_options) {
            if (!trigger_menu_option.option.equals(menu_option)) continue;

            // Fixed charges.
            if (trigger_menu_option.fixed_charges != null) {
                this.setCharges(trigger_menu_option.fixed_charges);

            // Custom consumer.
            } else if (trigger_menu_option.consumer != null) {
                trigger_menu_option.consumer.accept(menu_option);

            // Trigger not used.
            } else continue;

            // Menu option trigger used.
            return;
        }
    }

    public void resetCharges() {
        if (this.triggers_resets == null) return;

        // Send message about item charges being reset if player has it on them.
        if (this.in_equipment || this.in_inventory) {
            client_thread.invokeLater(() -> {
                chat_messages.queue(QueuedMessage.builder()
                    .type(ChatMessageType.CONSOLE)
                    .runeLiteFormattedMessage("<colHIGHLIGHT>" + items.getItemComposition(item_id).getName() + " daily charges have been reset.")
                    .build()
                );
            });
        }

        // Check for item resets.
        for (final TriggerReset trigger_reset : this.triggers_resets) {
            // Same item variations have different amount of charges.
            if (trigger_reset.item_id != null) {
                if (this.item_id == trigger_reset.item_id) {
                    this.setCharges(trigger_reset.charges);
                }

            // All variants of the item reset to the same amount of charges.
            } else {
                this.setCharges(trigger_reset.charges);
            }
        }
    }

    private void loadChargesFromConfig() {
        if (config_key == null) return;
        this.charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));

    }

    public void setCharges(final int charges) {
        this.charges = charges;
        this.onChargesUpdated();

        if (config_key != null) {
            this.setConfiguration(config_key, charges);
        }
    }

    private void decreaseCharges(final int charges) {
        if (this.charges - charges < 0) return;
        setCharges(this.charges - charges);
    }

    public void increaseCharges(final int charges) {
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
        this.updateTooltip();

        // Image.
        setImage(items.getImage(item_id));
        infoboxes.updateInfoBoxImage(this);
    }

    private void updateTooltip() {
        this.tooltip = items.getItemComposition(item_id).getName() + (needs_to_be_equipped_for_infobox && !in_equipment ? " - Needs to be equipped" : "");
    }

    protected void onChargesUpdated() {}
}


