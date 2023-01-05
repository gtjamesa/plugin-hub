package tictac7x.charges;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerHitsplat;

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

    protected int[] item_ids_to_render;
    protected String config_key;
    protected TriggerChatMessage[] chat_messages;
    protected TriggerAnimation[] animations;
    protected TriggerHitsplat[] hitsplats;

    protected ItemContainer inventory;
    protected ItemContainer equipment;

    private @Nullable Integer charges = null;
    private boolean render = false;

    public ChargedItemInfoBox(final int item_id, final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(items.getImage(item_id), plugin);
        this.item_id = item_id;
        this.client = client;
        this.client_thread = client_thread;
        this.configs = configs;
        this.items = items;

        this.item_ids_to_render = new int[]{};
        this.config_key = null;
        this.chat_messages = new TriggerChatMessage[]{};
        this.animations = new TriggerAnimation[]{};
        this.hitsplats = new TriggerHitsplat[]{};

        loadChargesFromConfig();
    }

    @Override
    public String getName() {
        return super.getName() + "_" + item_id;
    }

    @Override
    public String getText() {
        return charges != null ? String.valueOf(charges) : "?";
    }

    @Override
    public Color getTextColor() {
        return charges != null && charges == 0 ? Color.red : null;
    }

    @Override
    public boolean render() {
        return render;
    }

    public void onItemContainersChanged(final ItemContainer inventory, final ItemContainer equipment) {
        this.inventory = inventory;
        this.equipment = equipment;

        for (final int item_id : item_ids_to_render) {
            if (inventory.contains(item_id) || equipment.contains(item_id)) {
                this.render = true;
                return;
            }
        }

        this.render = false;
    }

    public void onChatMessage(final ChatMessage event) {
        if (event.getType() == ChatMessageType.GAMEMESSAGE && this.config_key != null) {
            final String message = event.getMessage();

            for (final TriggerChatMessage chat_message : chat_messages) {
                final Pattern regex = Pattern.compile(chat_message.message);
                final Matcher matcher = regex.matcher(message);
                if (matcher.find()) {
                    configs.setConfiguration(ChargesImprovedConfig.group, config_key, Integer.parseInt(matcher.group(chat_message.group).replaceAll(",", "")));
                }
            }
        }
    }

    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(config_key)) {
            this.charges = Integer.parseInt(event.getNewValue());
        }
    }

    public void onAnimationChanged(final AnimationChanged event) {
        if (event.getActor() == client.getLocalPlayer()) {

            for (final TriggerAnimation animation : animations) {
                if (animation.animation_id == event.getActor().getAnimation() && this.charges != null) {
                    decreaseCharges(animation.discharges);
                }
            }
        }
    }

    public void onHitsplatApplied(final HitsplatApplied event) {
        for (final TriggerHitsplat hitsplat : hitsplats) {
            if (hitsplat.self && event.getActor() == client.getLocalPlayer() && event.getHitsplat().getHitsplatType() == hitsplat.hitsplat_id && hitsplat.equipped && equipment.contains(item_id)) {
                decreaseCharges(hitsplat.discharges);
            }
        }
    }

    private void loadChargesFromConfig() {
        client_thread.invokeLater(() -> {
            if (config_key != null) {
                this.charges = Integer.parseInt(configs.getConfiguration(ChargesImprovedConfig.group, config_key));
            }
        });
    }

    private void setCharges(final int charges) {
        configs.setConfiguration(ChargesImprovedConfig.group, config_key, charges);
    }

    private void decreaseCharges(final int charges) {
        if (this.charges != null) {
            setCharges(this.charges - charges);
        }
    }
}


