package tictac7x.charges.items;

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
import tictac7x.charges.item.triggers.TriggerMenuEntryAdded;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;

public class S_KharedstMemoirs extends ChargedItem {
    public S_KharedstMemoirs(
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
        super(ItemKey.KHAREDSTS_MEMOIRS, ItemID.KHAREDSTS_MEMOIRS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.kharedsts_memoirs;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.KHAREDSTS_MEMOIRS),
            new TriggerItem(ItemID.BOOK_OF_THE_DEAD)
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("(Kharedst's Memoirs?)|(The Book of the Dead) now has (?<charges>.+) (memories|memory) remaining."),
            new TriggerChatMessage("(Kharedst's Memoirs?)|(The Book of the Dead) holds no charges?.").fixedCharges(0),
            new TriggerChatMessage("On the inside of the cover a message is displayed in dark ink. It reads: (?<charges>.+) (memories|memory) remain."),
            new TriggerChatMessage("(Kharedst's Memoirs?)|(The Book of the Dead) now has (?<charges>.+) charges.")
        };
        this.triggersMenusEntriesAdded = new TriggerMenuEntryAdded[]{
            new TriggerMenuEntryAdded("Reminisce").replace("Teleport"),
            new TriggerMenuEntryAdded("Destroy").hide(),
        };
    }
}
