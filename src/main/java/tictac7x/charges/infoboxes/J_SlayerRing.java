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
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerWidget;

public class J_SlayerRing extends ChargedItemInfoBox {
    public J_SlayerRing(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.SLAYER_RING_8, client, client_thread, configs, items, infoboxes, config, plugin);
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.SLAYER_RING_1, 1),
            new TriggerItem(ItemID.SLAYER_RING_2, 2),
            new TriggerItem(ItemID.SLAYER_RING_3, 3),
            new TriggerItem(ItemID.SLAYER_RING_4, 4),
            new TriggerItem(ItemID.SLAYER_RING_5, 5),
            new TriggerItem(ItemID.SLAYER_RING_6, 6),
            new TriggerItem(ItemID.SLAYER_RING_7, 7),
            new TriggerItem(ItemID.SLAYER_RING_8, 8),
        };
    }
}
