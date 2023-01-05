package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class DharokPlatelegs extends ChargedItemInfoBox {
    public DharokPlatelegs(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.DHAROKS_PLATELEGS, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{
                ItemID.DHAROKS_PLATELEGS,
                ItemID.DHAROKS_PLATELEGS_100,
                ItemID.DHAROKS_PLATELEGS_75,
                ItemID.DHAROKS_PLATELEGS_50,
                ItemID.DHAROKS_PLATELEGS_25,
                ItemID.DHAROKS_PLATELEGS_0
        };
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.DHAROKS_PLATELEGS, 100),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_100, 100),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_75, 75),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_50, 50),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_25, 25),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_0, 0)
        };
    }
}