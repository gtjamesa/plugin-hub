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
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnMenuOptionClicked;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Store;

import static tictac7x.charges.store.ItemContainerId.INVENTORY;

public class U_MeatPouch extends ChargedItemWithStorage {
    public U_MeatPouch(
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
        super(ChargesImprovedConfig.meat_pouch, ItemID.SMALL_MEAT_POUCH, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);
        this.storage = storage.storeableItems(
            new StorageItem(ItemID.RAW_SUNLIGHT_ANTELOPE).checkName("Raw sunlight antelope"),
            new StorageItem(ItemID.RAW_KYATT).checkName("Raw kyatt")
        );

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.SMALL_MEAT_POUCH).maxCharges(14),
            new TriggerItem(ItemID.SMALL_MEAT_POUCH_OPEN).maxCharges(14),
            new TriggerItem(ItemID.LARGE_MEAT_POUCH).maxCharges(28),
            new TriggerItem(ItemID.LARGE_MEAT_POUCH_OPEN).maxCharges(28),
        };

        this.triggers = new TriggerBase[]{
            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventoryAll().onMenuOption("Fill"),

            // Empty to bank.
            new OnMenuOptionClicked("Empty").atBank().emptyStorage(),

            // Hide destroy option.
            new OnMenuEntryAdded("Destroy").hide(),

            new OnChatMessage("You've caught a sunlight antelope!").onSpecificItem(ItemID.SMALL_MEAT_POUCH_OPEN, ItemID.LARGE_MEAT_POUCH_OPEN).addToStorage(ItemID.RAW_SUNLIGHT_ANTELOPE),
            new OnChatMessage("You've caught a sabretoothed kyatt!").onSpecificItem(ItemID.SMALL_MEAT_POUCH_OPEN, ItemID.LARGE_MEAT_POUCH_OPEN).addToStorage(ItemID.RAW_KYATT),
        };
    }
}
