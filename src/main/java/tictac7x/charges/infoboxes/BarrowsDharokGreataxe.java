package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsDharokGreataxe extends ChargedItemInfoBox {
    public BarrowsDharokGreataxe(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.DHAROKS_GREATAXE, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{
                ItemID.DHAROKS_GREATAXE,
                ItemID.DHAROKS_GREATAXE_100,
                ItemID.DHAROKS_GREATAXE_75,
                ItemID.DHAROKS_GREATAXE_50,
                ItemID.DHAROKS_GREATAXE_25,
                ItemID.DHAROKS_GREATAXE_0
        };
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.DHAROKS_GREATAXE, 100),
            new TriggerItem(ItemID.DHAROKS_GREATAXE_100, 100),
            new TriggerItem(ItemID.DHAROKS_GREATAXE_75, 75),
            new TriggerItem(ItemID.DHAROKS_GREATAXE_50, 50),
            new TriggerItem(ItemID.DHAROKS_GREATAXE_25, 25),
            new TriggerItem(ItemID.DHAROKS_GREATAXE_0, 0)
        };
    }
}