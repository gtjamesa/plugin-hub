package tictac7x.charges.item.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Storage {
    private final ChargedItemWithStorage chargedItem;
    private final String storageConfigKey;
    private final ConfigManager configManager;
    private final Store store;

    private final Gson gson = new Gson();
    protected List<StorageItem> storage = new ArrayList<>();

    public Optional<Integer> maximumTotalQuantity = Optional.empty();
    public Optional<Integer> maximumTotalQuantityWithItemEquipped = Optional.empty();
    public Optional<int[]> maximumTotalQuantityWithItemEquippedItems = Optional.empty();
    private Optional<Integer> maximumIndividualQuantity = Optional.empty();
    private Optional<StoreableItem[]> storeableItems = Optional.empty();


    public Storage(final ChargedItemWithStorage chargedItem, final String configKey, final ConfigManager configManager, final ClientThread clientThread, final Store store) {
        this.chargedItem = chargedItem;
        this.storageConfigKey = configKey + "_storage";
        this.configManager = configManager;
        this.store = store;

        clientThread.invokeLater(() -> loadStorage());
    }

    public Storage maximumTotalQuantity(final int quantity) {
        this.maximumTotalQuantity = Optional.of(quantity);
        return this;
    }

    public Storage maximumTotalQuantityWithEquippedItem(int quantity, final int ...itemIds) {
        this.maximumTotalQuantityWithItemEquipped = Optional.of(quantity);
        this.maximumTotalQuantityWithItemEquippedItems = Optional.of(itemIds);
        return this;
    }

    public Storage maximumIndividualQuantity(final int quantity) {
        this.maximumIndividualQuantity = Optional.of(quantity);
        return this;
    }

    public Storage storeableItems(final StoreableItem ...storeableItems) {
        this.storeableItems = Optional.of(storeableItems);
        return this;
    }

    public void empty() {
        storage = getNewStorage();
        save();
    }

    public void add(final int itemId, final int quantity) {
        if (getMaximumTotalQuantity().isPresent()) {
            if (getCharges() == getMaximumTotalQuantity().get()) {
                return;
            }
        }

        final Optional<StorageItem> item = getItem(itemId);
        put(itemId, (item.isPresent() ? item.get().getQuantity() : 0) + quantity);
    }

    public void add(final Optional<StoreableItem> item, final int quantity) {
        if (!item.isPresent()) return;
        add(item.get().itemId, quantity);
    }

    public void put(final Optional<StoreableItem> item, final int quantity) {
        if (!item.isPresent()) return;
        put(item.get().itemId, quantity);
    }

    public void remove(final Optional<StoreableItem> item, final int quantity) {
        if (!item.isPresent()) return;
        remove(item.get().itemId, quantity);
    }

    public void remove(final int itemId, final int quantity) {
        final Optional<StorageItem> item = getItem(itemId);
        put(itemId, (item.isPresent() ? Math.max(0, item.get().getQuantity() - quantity) : 0));
    }

    public void put(final int itemId, int quantity) {
        if (itemId == Charges.UNKNOWN) return;

        // Check for individual maximum quantity.
        if (maximumIndividualQuantity.isPresent() && quantity > maximumIndividualQuantity.get()) {
            quantity = maximumIndividualQuantity.get();
        }

        final Optional<Integer> maximumTotalQuantity = getMaximumTotalQuantity();
        if (maximumTotalQuantity.isPresent()) {
            int newTotalQuantity = 0;
            for (final StorageItem storageItem : storage) {
                newTotalQuantity += storageItem.itemId == itemId ? quantity : storageItem.getQuantity();
            }

            if (newTotalQuantity > maximumTotalQuantity.get()) {
                quantity = maximumTotalQuantity.get();
            }
        }

        final Optional<StorageItem> item = getItem(itemId);
        if (item.isPresent()) {
            item.get().setQuantity(quantity);
        } else {
            storage.add(new StorageItem(itemId, quantity));
        }

        save();
    }

    public void fillFromInventory() {
        if (!storeableItems.isPresent()) return;

        for (final StoreableItem storeableItem : storeableItems.get()) {
            add(storeableItem.itemId, store.getPreviousInventoryItemQuantity(storeableItem.itemId));
        }
    }

    public void emptyToInventory() {
        if (!store.inventory.isPresent()) return;

        int inventoryEmptySlots = store.getInventoryPreviouslyEmptySlots();

        for (final StorageItem storageItem : storage) {
            // Empty storage until 0 inventory slots left.
            if (inventoryEmptySlots == 0) break;

            final int toEmptyStorageSlots = Math.min(inventoryEmptySlots, storageItem.getQuantity());
            put(storageItem.itemId, storageItem.getQuantity() - toEmptyStorageSlots);
            inventoryEmptySlots -= toEmptyStorageSlots;
        }
    }

    public int getCharges() {
        int charges = 0;

        for (final StorageItem item : storage) {
            charges += item.getQuantity();
        }

        return charges;
    }

    public List<StorageItem> getStorage() {
        return storage;
    }

    private void loadStorage() {
        // Create new empty storage to have specific items in correct order.
        this.storage = getNewStorage();

        // Load storage from config.
        try {
            final String storageJson = configManager.getConfiguration(ChargesImprovedConfig.group, storageConfigKey);
            final Type listType = new TypeToken<ArrayList<StorageItem>>(){}.getType();
            final List<StorageItem> loadedStorage = gson.fromJson(storageJson, listType);

            for (final StorageItem loadedItem : loadedStorage) {
                final Optional<StorageItem> item = getItem(loadedItem.itemId);
                if (item.isPresent()) {
                    item.get().setQuantity(loadedItem.getQuantity());
                } else {
                    put(loadedItem.itemId, loadedItem.getQuantity());
                }
            }
        } catch (final Exception ignored) {}
    }

    private void save() {
        configManager.setConfiguration(ChargesImprovedConfig.group, storageConfigKey, gson.toJson(storage));
    }

    private List<StorageItem> getNewStorage() {
        final List<StorageItem> storage = new ArrayList<>();

        if (storeableItems.isPresent()) {
            for (final StoreableItem storeableItem : storeableItems.get()) {
                storage.add(new StorageItem(storeableItem.itemId, 0));
            }
        }

        return storage;
    }

    private Optional<StorageItem> getItem(final int itemId) {
        for (final StorageItem item : storage) {
            if (item.itemId == itemId) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    public Optional<Integer> getMaximumTotalQuantity() {
        // Maximum storage from trigger item.
        for (final TriggerItem item : chargedItem.triggersItems) {
            if (chargedItem.item_id == item.item_id && item.maxCharges.isPresent()) {
                return item.maxCharges;
            }
        }

        // Maximum storage with specific item equipped.
        if (maximumTotalQuantityWithItemEquipped.isPresent() && maximumTotalQuantityWithItemEquippedItems.isPresent() && store.equipmentContainsItem(maximumTotalQuantityWithItemEquippedItems.get())) {
            return maximumTotalQuantityWithItemEquipped;
        }

        // Maximum storage.
        if (maximumTotalQuantity.isPresent()) {
            return maximumTotalQuantity;
        }

        return Optional.empty();
    }

    public final Optional<StoreableItem> getStorageItemFromName(final String name) {
        if (!storeableItems.isPresent()) return Optional.empty();

        // Perfect match.
        for (final StoreableItem storeableItem : storeableItems.get()) {
            if (name.equalsIgnoreCase(storeableItem.name)) {
                return Optional.of(storeableItem);
            }
        }

        // Partial match.
        for (final StoreableItem storeableItem : storeableItems.get()) {
            if (name.toLowerCase().contains(storeableItem.name.toLowerCase())) {
                return Optional.of(storeableItem);
            }
        }

        return Optional.empty();
    }

    public Optional<StoreableItem[]> getStoreableItems() {
        return storeableItems;
    }
}
