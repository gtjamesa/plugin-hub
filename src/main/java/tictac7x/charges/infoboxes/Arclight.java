package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;

public class Arclight extends ChargedItemInfoBox {
    public Arclight(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.ARCLIGHT, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{ItemID.ARCLIGHT};
        this.config_key = ChargesImprovedConfig.arclight;
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your arclight has (?<charges>.+) charges? left.", 1)
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(390, 1)
        };
    }
}
