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

public class BarrowsToragPlatelegs extends ChargedItemInfoBox {
    public BarrowsToragPlatelegs(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.TORAGS_PLATELEGS, client, client_thread, configs, items, infoboxes, config, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.TORAGS_PLATELEGS).fixedCharges(100),
            new TriggerItem(ItemID.TORAGS_PLATELEGS_100).fixedCharges(100),
            new TriggerItem(ItemID.TORAGS_PLATELEGS_75).fixedCharges(75),
            new TriggerItem(ItemID.TORAGS_PLATELEGS_50).fixedCharges(50),
            new TriggerItem(ItemID.TORAGS_PLATELEGS_25).fixedCharges(25),
            new TriggerItem(ItemID.TORAGS_PLATELEGS_0).fixedCharges(0)
        };
    }
}