package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class AhrimRobeskirt extends ChargedItemInfoBox {
    public AhrimRobeskirt(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.AHRIMS_ROBESKIRT, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{
                ItemID.AHRIMS_ROBESKIRT,
                ItemID.AHRIMS_ROBESKIRT_100,
                ItemID.AHRIMS_ROBESKIRT_75,
                ItemID.AHRIMS_ROBESKIRT_50,
                ItemID.AHRIMS_ROBESKIRT_25,
                ItemID.AHRIMS_ROBESKIRT_0
        };
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.AHRIMS_ROBESKIRT, 100),
            new TriggerItem(ItemID.AHRIMS_ROBESKIRT_100, 100),
            new TriggerItem(ItemID.AHRIMS_ROBESKIRT_75, 75),
            new TriggerItem(ItemID.AHRIMS_ROBESKIRT_50, 50),
            new TriggerItem(ItemID.AHRIMS_ROBESKIRT_25, 25),
            new TriggerItem(ItemID.AHRIMS_ROBESKIRT_0, 0)
        };
    }
}