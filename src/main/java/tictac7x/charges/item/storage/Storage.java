package tictac7x.charges.item.storage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Storage {
    private final ChargedItemWithStorage chargedItem;
    private final String storageConfigKey;
    private final ConfigManager configManager;
    private final Store store;
    private final Gson gson;

    protected List<StorageItem> storage = new ArrayList<>();

    public Optional<Integer> maximumTotalQuantity = Optional.empty();
    public Optional<Integer> maximumTotalQuantityWithItemEquipped = Optional.empty();
    public Optional<int[]> maximumTotalQuantityWithItemEquippedItems = Optional.empty();
    public Optional<Boolean> showIndividualCharges = Optional.empty();
    private Optional<Integer> maximumIndividualQuantity = Optional.empty();
    private Optional<StorageItem[]> storeableItems = Optional.empty();


    public Storage(final ChargedItemWithStorage chargedItem, final String configKey, final ConfigManager configManager, final Store store, final Gson gson) {
        this.chargedItem = chargedItem;
        this.storageConfigKey = configKey + "_storage";
        this.configManager = configManager;
        this.store = store;
        this.gson = gson;
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

    public Storage showIndividualCharges() {
        this.showIndividualCharges = Optional.of(true);
        return this;
    }

    public Storage storeableItems(final StorageItem... storageItems) {
        this.storeableItems = Optional.of(storageItems);
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

    public void add(final Optional<StorageItem> item, final int quantity) {
        if (!item.isPresent()) return;
        add(item.get().itemId, quantity);
    }

    public void put(final Optional<StorageItem> item, final int quantity) {
        if (!item.isPresent()) return;
        put(item.get().itemId, quantity);
    }

    public void remove(final Optional<StorageItem> item, final int quantity) {
        if (!item.isPresent()) return;
        remove(item.get().itemId, quantity);
    }

    public void remove(final int itemId, final int quantity) {
        final Optional<StorageItem> item = getItem(itemId);

        // Don't decrease quantity of unlimited storage item.
        if (item.isPresent() && item.get().getQuantity() == Charges.UNLIMITED) {
            return;
        }

        put(itemId, (item.isPresent() ? Math.max(0, item.get().getQuantity() - quantity) : 0));
    }

    public void removeAndPrioritizeInventory(final int itemId, final int quantity) {
        this.remove(itemId, Math.max(quantity - store.getInventoryItemQuantity(itemId), 0));
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
            storage.add(new StorageItem(itemId).quantity(quantity));
        }

        save();
    }

    public void fillFromInventory() {
        if (!storeableItems.isPresent()) return;

        for (final StorageItem storageItem : storeableItems.get()) {
            add(storageItem.itemId, store.getPreviousInventoryItemQuantity(storageItem.itemId));
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

    public String getIndividualCharges() {
        String charges = "";

        for (final StorageItem item : storage) {
            if (item.getQuantity() != Charges.UNLIMITED) {
                charges += item.getQuantity() + "/";
            }
        }

        return charges.replaceAll("/$", "");
    }

    public List<StorageItem> getStorage() {
        return storage;
    }

    public void loadStorage() {
        // Create new empty storage to have specific items in correct order.
        this.storage = getNewStorage();

        // Load storage from config.
        try {
            final String jsonString = configManager.getConfiguration(ChargesImprovedConfig.group, storageConfigKey);
            final JsonArray jsonStorage = (JsonArray) (new JsonParser()).parse(jsonString);

            for (final JsonElement jsonStorageItem : jsonStorage) {
                // Load item id.
                final StorageItem loadedItem = new StorageItem(jsonStorageItem.getAsJsonObject().get("itemId").getAsInt());

                // Load quantity.
                loadedItem.setQuantity(jsonStorageItem.getAsJsonObject().get("quantity").getAsInt());

                // Update previous item quantity or put new into the storage.
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
        final JsonArray jsonStorage = new JsonArray();

        for (final StorageItem storageItem : storage) {
            final JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("itemId", storageItem.itemId);
            jsonItem.addProperty("quantity", storageItem.getQuantity());
            jsonStorage.add(jsonItem);
        }

        configManager.setConfiguration(ChargesImprovedConfig.group, storageConfigKey, gson.toJson(jsonStorage));
    }

    private List<StorageItem> getNewStorage() {
        final List<StorageItem> storage = new ArrayList<>();

        if (storeableItems.isPresent()) {
            for (final StorageItem storeableItem : storeableItems.get()) {
                final StorageItem storageItem = new StorageItem(storeableItem.itemId);

                storageItem.setQuantity(0);

                if (storeableItem.checkName.isPresent()) {
                    storageItem.setCheckName(storeableItem.checkName.get());
                }

                if (storeableItem.displayName.isPresent()) {
                    storageItem.setDisplayName(storeableItem.displayName.get());
                }

                storage.add(storageItem);
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
        for (final TriggerItem item : chargedItem.items) {
            if (chargedItem.itemId == item.itemId && item.maxCharges.isPresent()) {
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

    public final Optional<StorageItem> getStorageItemFromName(final String name) {
        if (!storeableItems.isPresent()) return Optional.empty();

        // Perfect match.
        for (final StorageItem storageItem : storeableItems.get()) {
            if (storageItem.checkName.isPresent() && name.equalsIgnoreCase(storageItem.checkName.get())) {
                return Optional.of(storageItem);
            }
        }

        // Partial match.
        for (final StorageItem storageItem : storeableItems.get()) {
            if (storageItem.checkName.isPresent() && name.toLowerCase().contains(storageItem.checkName.get().toLowerCase())) {
                return Optional.of(storageItem);
            }
        }

        return Optional.empty();
    }

    public Optional<StorageItem[]> getStoreableItems() {
        return storeableItems;
    }
}
