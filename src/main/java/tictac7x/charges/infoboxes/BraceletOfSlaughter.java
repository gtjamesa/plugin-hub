package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerChatMessage;

public class BraceletOfSlaughter extends ChargedItemInfoBox {
    public BraceletOfSlaughter(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.BRACELET_OF_SLAUGHTER, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{ItemID.BRACELET_OF_SLAUGHTER};
        this.config_key = ChargesImprovedConfig.bracelet_of_slaughter;
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your bracelet of slaughter has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your bracelet of slaughter prevents your slayer count from decreasing. It has (?<charges>.+) charges? left."),
        };
    }
}
