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

public class J_ExplorerRing extends ChargedItemInfoBox {
    public J_ExplorerRing(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.EXPLORERS_RING, client, client_thread, configs, items, infoboxes, config, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.EXPLORERS_RING),
            new TriggerItem(ItemID.EXPLORERS_RING_1),
            new TriggerItem(ItemID.EXPLORERS_RING_2),
            new TriggerItem(ItemID.EXPLORERS_RING_3),
            new TriggerItem(ItemID.EXPLORERS_RING_4),
        };
    }
}
