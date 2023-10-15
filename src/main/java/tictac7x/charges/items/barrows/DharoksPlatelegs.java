package tictac7x.charges.items.barrows;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

public class DharoksPlatelegs extends ChargedItem {
    public DharoksPlatelegs(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Plugin plugin
    ) {
        super(ItemKey.BARROWS_GEAR, ItemID.DHAROKS_PLATELEGS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.DHAROKS_PLATELEGS).fixedCharges(100),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_100).fixedCharges(100),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_75).fixedCharges(75),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_50).fixedCharges(50),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_25).fixedCharges(25),
            new TriggerItem(ItemID.DHAROKS_PLATELEGS_0).fixedCharges(0)
        };
    }
}