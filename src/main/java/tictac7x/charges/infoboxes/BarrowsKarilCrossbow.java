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

public class BarrowsKarilCrossbow extends ChargedItemInfoBox {
    public BarrowsKarilCrossbow(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final Plugin plugin) {
        super(ItemID.KARILS_CROSSBOW, client, client_thread, configs, items, infoboxes, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.KARILS_CROSSBOW, 100),
            new TriggerItem(ItemID.KARILS_CROSSBOW_100, 100),
            new TriggerItem(ItemID.KARILS_CROSSBOW_75, 75),
            new TriggerItem(ItemID.KARILS_CROSSBOW_50, 50),
            new TriggerItem(ItemID.KARILS_CROSSBOW_25, 25),
            new TriggerItem(ItemID.KARILS_CROSSBOW_0, 0)
        };
    }
}