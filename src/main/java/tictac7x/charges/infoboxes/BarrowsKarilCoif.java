package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsKarilCoif extends ChargedItemInfoBox {
    public BarrowsKarilCoif(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.KARILS_COIF, client, client_thread, configs, items, infoboxes, config, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.KARILS_COIF, 100),
            new TriggerItem(ItemID.KARILS_COIF_100, 100),
            new TriggerItem(ItemID.KARILS_COIF_75, 75),
            new TriggerItem(ItemID.KARILS_COIF_50, 50),
            new TriggerItem(ItemID.KARILS_COIF_25, 25),
            new TriggerItem(ItemID.KARILS_COIF_0, 0)
        };
    }
}