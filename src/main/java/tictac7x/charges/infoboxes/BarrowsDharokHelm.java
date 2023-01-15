package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsDharokHelm extends ChargedItemInfoBox {
    public BarrowsDharokHelm(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final Plugin plugin) {
        super(ItemID.DHAROKS_HELM, client, client_thread, configs, items, infoboxes, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.DHAROKS_HELM, 100),
            new TriggerItem(ItemID.DHAROKS_HELM_100, 100),
            new TriggerItem(ItemID.DHAROKS_HELM_75, 75),
            new TriggerItem(ItemID.DHAROKS_HELM_50, 50),
            new TriggerItem(ItemID.DHAROKS_HELM_25, 25),
            new TriggerItem(ItemID.DHAROKS_HELM_0, 0)
        };
    }
}