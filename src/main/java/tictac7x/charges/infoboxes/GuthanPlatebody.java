package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class GuthanPlatebody extends ChargedItemInfoBox {
    public GuthanPlatebody(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.GUTHANS_PLATEBODY, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{
                ItemID.GUTHANS_PLATEBODY,
                ItemID.GUTHANS_PLATEBODY_100,
                ItemID.GUTHANS_PLATEBODY_75,
                ItemID.GUTHANS_PLATEBODY_50,
                ItemID.GUTHANS_PLATEBODY_25,
                ItemID.GUTHANS_PLATEBODY_0
        };
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.GUTHANS_PLATEBODY, 100),
            new TriggerItem(ItemID.GUTHANS_PLATEBODY_100, 100),
            new TriggerItem(ItemID.GUTHANS_PLATEBODY_75, 75),
            new TriggerItem(ItemID.GUTHANS_PLATEBODY_50, 50),
            new TriggerItem(ItemID.GUTHANS_PLATEBODY_25, 25),
            new TriggerItem(ItemID.GUTHANS_PLATEBODY_0, 0)
        };
    }
}