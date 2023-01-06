package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsDharokPlatebody extends ChargedItemInfoBox {
    public BarrowsDharokPlatebody(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.DHAROKS_PLATEBODY, client, client_thread, configs, items, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.DHAROKS_PLATEBODY, 100),
            new TriggerItem(ItemID.DHAROKS_PLATEBODY_100, 100),
            new TriggerItem(ItemID.DHAROKS_PLATEBODY_75, 75),
            new TriggerItem(ItemID.DHAROKS_PLATEBODY_50, 50),
            new TriggerItem(ItemID.DHAROKS_PLATEBODY_25, 25),
            new TriggerItem(ItemID.DHAROKS_PLATEBODY_0, 0)
        };
    }
}