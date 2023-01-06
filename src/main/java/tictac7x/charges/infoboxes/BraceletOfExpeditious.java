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

public class BraceletOfExpeditious extends ChargedItemInfoBox {
    public BraceletOfExpeditious(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.EXPEDITIOUS_BRACELET, client, client_thread, configs, items, plugin);
        this.config_key = ChargesImprovedConfig.bracelet_of_expeditious;
        this.needs_to_be_equipped = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.EXPEDITIOUS_BRACELET),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your expeditious bracelet has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your expeditious bracelet helps you progress your slayer task faster. It has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your Expeditious Bracelet helps has crumbled to dust.", 30)
        };
    }
}
