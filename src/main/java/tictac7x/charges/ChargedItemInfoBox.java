package tictac7x.charges;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import tictac7x.charges.triggers.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargedItemInfoBox extends InfoBox {
    protected final int item_id;
    protected final Client client;
    protected final ClientThread client_thread;
    protected final ItemManager items;
    protected final ConfigManager configs;

    @Nullable
    protected ItemContainer inventory;

    @Nullable
    protected ItemContainer equipment;

    @Nullable
    protected int[] item_ids_to_render;

    @Nullable
    protected String config_key;

    @Nullable
    protected TriggerChatMessage[] triggers_chat_messages;

    @Nullable
    protected TriggerAnimation[] triggers_animations;

    @Nullable
    protected TriggerHitsplat[] triggers_hitsplats;

    @Nullable
    protected TriggerItem[] triggers_items;

    @Nullable
    protected TriggerWidget[] triggers_widgets;

    private int charges = -1;
    private boolean render = false;

    public ChargedItemInfoBox(final int item_id, final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(items.getImage(item_id), plugin);
        this.item_id = item_id;
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;
        loadChargesFromConfig();
    }

    @Override
    public String getName() {
        return super.getName() + "_" + item_id;
    }

    @Override
    public String getText() {
        return charges >= 0 ? String.valueOf(charges) : "?";
    }

    @Override
    public Color getTextColor() {
        return charges == 0 ? Color.red : null;
    }

    @Override
    public boolean render() {
        return render;
    }

    public void onItemContainersChanged(@Nonnull final ItemContainer inventory, @Nonnull final ItemContainer equipment) {
        this.inventory = inventory;
        this.equipment = equipment;

        this.render = false;
        if (item_ids_to_render != null) {
            for (final int item_id : item_ids_to_render) {
                if (inventory.contains(item_id) || equipment.contains(item_id)) {
                    this.render = true;
                    break;
                }
            }
        }

        if (triggers_items != null) {
            int charges = 0;

            for (final TriggerItem trigger_item : triggers_items) {
                charges += inventory.count(trigger_item.item_id) * trigger_item.charges;
                charges += equipment.count(trigger_item.item_id) * trigger_item.charges;
            }

            this.charges = charges;
        }
    }

    public void onChatMessage(final ChatMessage event) {
        if (
            event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM ||
            this.config_key == null ||
            triggers_chat_messages == null
        ) return;

        final String message = event.getMessage();

        for (final TriggerChatMessage chat_message : triggers_chat_messages) {
            final Pattern regex = Pattern.compile(chat_message.message);
            final Matcher matcher = regex.matcher(message);
            if (!matcher.find()) continue;

            setCharges(chat_message.charges != null
                // Charges amount is fixed.
                ? chat_message.charges
                // Charges amount is dynamic and extracted from the message.
                : Integer.parseInt(matcher.group("charges").replaceAll(",", ""))
            );
        }
    }
    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(config_key)) {
            this.charges = Integer.parseInt(event.getNewValue());
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        if (triggers_animations == null || event.getActor() != client.getLocalPlayer() || this.charges < 0) return;

        for (final TriggerAnimation animation : triggers_animations) {
            if (animation.animation_id == event.getActor().getAnimation()) {
                decreaseCharges(animation.discharges);
            }
        }
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        if (triggers_hitsplats == null) return;

        for (final TriggerHitsplat hitsplat : triggers_hitsplats) {
            if (
                hitsplat.self && event.getActor() == client.getLocalPlayer() &&
                event.getHitsplat().getHitsplatType() == hitsplat.hitsplat_id &&
                equipment != null && hitsplat.equipped && equipment.contains(item_id)
            ) {
                decreaseCharges(hitsplat.discharges);
            }
        }
    }


    public void onWidgetLoaded(final WidgetLoaded event) {
        if (triggers_widgets == null || config_key == null) return;

        client_thread.invokeLater(() -> {
            for (final TriggerWidget trigger_widget : triggers_widgets) {
                final Widget widget = client.getWidget(trigger_widget.widget_group, trigger_widget.widget_child);
                if (widget == null) continue;

                final Pattern regex = Pattern.compile(trigger_widget.message);
                final Matcher matcher = regex.matcher(widget.getText().replaceAll("<br>", ""));
                if (!matcher.find()) continue;

                setCharges(trigger_widget.charges != null
                    // Charges amount is fixed.
                    ? trigger_widget.charges
                    // Charges amount is dynamic and extracted from the message.
                    : Integer.parseInt(matcher.group("charges").replaceAll(",", ""))
                );
            }
        });
    }

    private void loadChargesFromConfig() {
        client_thread.invokeLater(() -> {
            if (config_key == null) return;
            this.charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));
        });
    }

    private void setCharges(final int charges) {
        if (config_key == null) return;
        configs.setConfiguration(ChargesImprovedConfig.group, config_key, charges);
    }

    private void decreaseCharges(final int charges) {
        if (this.charges < 0) return;
        setCharges(this.charges - charges);
    }
}


