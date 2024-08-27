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

public class U_FurPouch extends ChargedItemWithStorage {
    public U_FurPouch(
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
        super(ChargesImprovedConfig.fur_pouch, ItemID.SMALL_FUR_POUCH, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);
        this.storage = storage
            .storeableItems(
                new StorageItem(ItemID.SUNLIGHT_ANTELOPE_FUR).checkName("Sunlight antelope fur"),
                new StorageItem(ItemID.KYATT_FUR).checkName("Kyatt fur")
            );

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.SMALL_FUR_POUCH).maxCharges(14),
            new TriggerItem(ItemID.SMALL_FUR_POUCH_OPEN).maxCharges(14),
            new TriggerItem(ItemID.MEDIUM_FUR_POUCH).maxCharges(21),
            new TriggerItem(ItemID.MEDIUM_FUR_POUCH_OPEN).maxCharges(21),
            new TriggerItem(ItemID.LARGE_FUR_POUCH).maxCharges(28),
            new TriggerItem(ItemID.LARGE_FUR_POUCH_OPEN).maxCharges(28),
        };

        this.triggers = new TriggerBase[]{
            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventoryAll().onMenuOption("Fill"),

            // Empty to inventory.
            new OnItemContainerChanged(INVENTORY).emptyStorageToInventory().onMenuOption("Empty"),

            // Empty to bank.
            new OnMenuOptionClicked("Empty").atBank().emptyStorage(),

            // Hide destroy option.
            new OnMenuEntryAdded("Destroy").hide(),

            new OnChatMessage("You've caught a sunlight antelope!").onSpecificItem(ItemID.SMALL_FUR_POUCH_OPEN, ItemID.LARGE_FUR_POUCH_OPEN).addToStorage(ItemID.SUNLIGHT_ANTELOPE_FUR),
            new OnChatMessage("You've caught a sabretoothed kyatt!").onSpecificItem(ItemID.SMALL_FUR_POUCH_OPEN, ItemID.LARGE_FUR_POUCH_OPEN).addToStorage(ItemID.KYATT_FUR),
        };
    }
}
