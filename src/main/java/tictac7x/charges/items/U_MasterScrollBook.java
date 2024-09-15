package tictac7x.charges.items;

import com.google.gson.Gson;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.OnVarbitChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Store;

public class U_MasterScrollBook extends ChargedItemWithStorage {
    public U_MasterScrollBook(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Gson gson
    ) {
        super(ChargesImprovedConfig.master_scroll_book, ItemID.MASTER_SCROLL_BOOK, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);
        storage = storage.maximumIndividualQuantity(1000).storeableItems(
            new StorageItem(ItemID.NARDAH_TELEPORT).specificOrder(1),
            new StorageItem(ItemID.DIGSITE_TELEPORT).specificOrder(2),
            new StorageItem(ItemID.FELDIP_HILLS_TELEPORT).specificOrder(3),
            new StorageItem(ItemID.LUNAR_ISLE_TELEPORT).specificOrder(4),
            new StorageItem(ItemID.MORTTON_TELEPORT).specificOrder(5),
            new StorageItem(ItemID.PEST_CONTROL_TELEPORT).specificOrder(6),
            new StorageItem(ItemID.PISCATORIS_TELEPORT).specificOrder(7),
            new StorageItem(ItemID.TAI_BWO_WANNAI_TELEPORT).specificOrder(8),
            new StorageItem(ItemID.IORWERTH_CAMP_TELEPORT).specificOrder(9),
            new StorageItem(ItemID.MOS_LEHARMLESS_TELEPORT).specificOrder(10),
            new StorageItem(ItemID.LUMBERYARD_TELEPORT).specificOrder(11),
            new StorageItem(ItemID.ZULANDRA_TELEPORT).specificOrder(12),
            new StorageItem(ItemID.KEY_MASTER_TELEPORT).specificOrder(13),
            new StorageItem(ItemID.REVENANT_CAVE_TELEPORT).specificOrder(14),
            new StorageItem(ItemID.WATSON_TELEPORT).specificOrder(15),
            new StorageItem(ItemID.GUTHIXIAN_TEMPLE_TELEPORT).specificOrder(16),
            new StorageItem(ItemID.SPIDER_CAVE_TELEPORT).specificOrder(17)
        );

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.MASTER_SCROLL_BOOK_EMPTY),
            new TriggerItem(ItemID.MASTER_SCROLL_BOOK),
        };

        this.triggers = new TriggerBase[]{
            new OnVarbitChanged(5672).varbitValueConsumer(charges -> storage.put(ItemID.NARDAH_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5673).varbitValueConsumer(charges -> storage.put(ItemID.DIGSITE_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5674).varbitValueConsumer(charges -> storage.put(ItemID.FELDIP_HILLS_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5675).varbitValueConsumer(charges -> storage.put(ItemID.LUNAR_ISLE_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5676).varbitValueConsumer(charges -> storage.put(ItemID.MORTTON_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5677).varbitValueConsumer(charges -> storage.put(ItemID.PEST_CONTROL_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5678).varbitValueConsumer(charges -> storage.put(ItemID.PISCATORIS_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5679).varbitValueConsumer(charges -> storage.put(ItemID.TAI_BWO_WANNAI_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5680).varbitValueConsumer(charges -> storage.put(ItemID.IORWERTH_CAMP_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5681).varbitValueConsumer(charges -> storage.put(ItemID.MOS_LEHARMLESS_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5682).varbitValueConsumer(charges -> storage.put(ItemID.LUMBERYARD_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5683).varbitValueConsumer(charges -> storage.put(ItemID.ZULANDRA_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(5684).varbitValueConsumer(charges -> storage.put(ItemID.KEY_MASTER_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(6056).varbitValueConsumer(charges -> storage.put(ItemID.REVENANT_CAVE_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(8253).varbitValueConsumer(charges -> storage.put(ItemID.WATSON_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(10967).varbitValueConsumer(charges -> storage.put(ItemID.GUTHIXIAN_TEMPLE_TELEPORT, charges)).multiTrigger(),
            new OnVarbitChanged(10995).varbitValueConsumer(charges -> storage.put(ItemID.SPIDER_CAVE_TELEPORT, charges)).multiTrigger(),
        };
    }
}
