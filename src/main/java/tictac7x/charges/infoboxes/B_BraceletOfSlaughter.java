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
import tictac7x.charges.triggers.TriggerItem;

public class B_BraceletOfSlaughter extends ChargedItemInfoBox {
    public B_BraceletOfSlaughter(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.BRACELET_OF_SLAUGHTER, client, client_thread, configs, items, plugin);
        this.config_key = ChargesImprovedConfig.bracelet_of_slaughter;
        this.needs_to_be_equipped = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.BRACELET_OF_SLAUGHTER),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your bracelet of slaughter has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your bracelet of slaughter prevents your slayer count from decreasing. It has (?<charges>.+) charges? left."),
        };
    }
}
