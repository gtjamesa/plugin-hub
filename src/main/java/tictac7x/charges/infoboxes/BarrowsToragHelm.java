package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsToragHelm extends ChargedItemInfoBox {
    public BarrowsToragHelm(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.TORAGS_HELM, client, client_thread, configs, items, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.TORAGS_HELM, 100),
            new TriggerItem(ItemID.TORAGS_HELM_100, 100),
            new TriggerItem(ItemID.TORAGS_HELM_75, 75),
            new TriggerItem(ItemID.TORAGS_HELM_50, 50),
            new TriggerItem(ItemID.TORAGS_HELM_25, 25),
            new TriggerItem(ItemID.TORAGS_HELM_0, 0)
        };
    }
}