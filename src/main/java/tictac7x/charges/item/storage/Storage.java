package tictac7x.charges.item.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Storage {
    private final String storageConfigKey;
    private final ConfigManager configManager;
    private final Store store;

    private final Gson gson = new Gson();
    protected List<StorageItem> storage = new ArrayList<>();

    public Optional<Integer> maximumTotalQuantity = Optional.empty();
    private Optional<Integer> maximumIndividualQuantity = Optional.empty();
    private Optional<int[]> storeableItems = Optional.empty();


    public Storage(final String configKey, final ConfigManager configManager, final ClientThread clientThread, final Store store) {
        this.storageConfigKey = configKey + "_storage";
        this.configManager = configManager;
        this.store = store;

        clientThread.invokeLater(() -> loadStorage());
    }

    public Storage maximumTotalQuantity(final int quantity) {
        this.maximumTotalQuantity = Optional.of(quantity);
        return this;
    }

    public Storage maximumIndividualQuantity(final int quantity) {
        this.maximumIndividualQuantity = Optional.of(quantity);
        return this;
    }

    public Storage storeableItems(final int ...itemIds) {
        this.storeableItems = Optional.of(itemIds);
        return this;
    }

    public void empty() {
        storage = getNewStorage();
        save();
    }

    public void add(final int itemId, final int quantity) {
        final Optional<StorageItem> item = getItem(itemId);
        put(itemId, (item.isPresent() ? item.get().getQuantity() : 0) + quantity);
    }

    public void put(final int itemId, int quantity) {
        if (itemId == Charges.UNKNOWN) return;

        // Check for individual maximum quantity.
        if (maximumIndividualQuantity.isPresent()) {
            quantity = Math.min(quantity, maximumIndividualQuantity.get());

        }

        // Check for total maximum quantity.
        if (maximumTotalQuantity.isPresent()) {
            int totalQuantity = 0;

            for (final StorageItem storageItem : storage) {
                totalQuantity += storageItem.itemId == itemId ? quantity : storageItem.getQuantity();
            }

            if (totalQuantity > maximumTotalQuantity.get()) {
                quantity -= totalQuantity - maximumTotalQuantity.get();
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

        for (final int itemId : storeableItems.get()) {
            add(itemId, store.getInventoryItemCount(itemId));
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
            for (final int itemId : storeableItems.get()) {
                storage.add(new StorageItem(itemId, 0));
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
}
