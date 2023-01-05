package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;

public class ExpeditiousBracelet extends ChargedItemInfoBox {
    public ExpeditiousBracelet(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.EXPEDITIOUS_BRACELET, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{ItemID.EXPEDITIOUS_BRACELET};
        this.config_key = ChargesImprovedConfig.expeditious_bracelet;
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your expeditious bracelet has (.+) charges? left.", 1),
            new TriggerChatMessage("Your expeditious bracelet helps you progress your slayer task faster. It has (.+) charges? left.", 1),
            new TriggerChatMessage("Your Expeditious Bracelet helps has crumbled to dust.", 30)
        };
    }
}
