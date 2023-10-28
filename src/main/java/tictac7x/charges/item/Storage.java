package tictac7x.charges.item;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Storage {
    private final String storageConfigKey;
    private final ConfigManager configManager;
    private final Store store;

    private final Gson gson = new Gson();
    private Map<Integer, Integer> storage = new LinkedHashMap<>();

    private Optional<Integer> maximumIndividualAmount = Optional.empty();
    private Optional<int[]> specificItems = Optional.empty();


    public Storage(final String configKey, final ConfigManager configManager, final ClientThread clientThread, final Store store) {
        this.storageConfigKey = configKey + "_storage";
        this.configManager = configManager;
        this.store = store;

        clientThread.invokeLater(this::load);
    }

    public void empty() {
        storage.clear();
        save();
    }

    public void add(final int itemId, final int amount) {
        put(itemId, storage.getOrDefault(itemId, 0) + amount);
    }

    public void put(final int itemId, int amount) {
        if (itemId == Charges.UNKNOWN) return;

        // Override amount to maximum individual amount if necessary.
        if (maximumIndividualAmount.isPresent()) {
            amount = Math.min(amount, maximumIndividualAmount.get());
        }

        storage.put(itemId,  amount);
        save();
    }

    private void load() {
        // Create new empty storage to have specific items in correct order.
        final Map<Integer, Integer> newEmptyStorage = getNewEmptyStorage();

        // Load storage from config.
        String storageJson = configManager.getConfiguration(ChargesImprovedConfig.group, storageConfigKey);
        final Optional<Map<Integer, Integer>> loadedStorage = Optional.ofNullable(gson.fromJson(storageJson, new TypeToken<Map<Integer, Integer>>() {}.getType()));

        // Insert items from loaded storage to new empty storage.
        if (loadedStorage.isPresent()) {
            for (final int itemId : loadedStorage.get().keySet()) {
                newEmptyStorage.put(itemId, loadedStorage.get().get(itemId));
            }
        }

        this.storage = newEmptyStorage;
    }

    private void save() {
        configManager.setConfiguration(ChargesImprovedConfig.group, storageConfigKey, gson.toJson(storage));
    }

    private Map<Integer, Integer> getNewEmptyStorage() {
        final Map<Integer, Integer> storage = new LinkedHashMap<>();

        if (specificItems.isPresent()) {
            for (final int itemId : specificItems.get()) {
                storage.put(itemId, 0);
            }
        }

        return storage;
    }

    public Storage maximumIndividualAmount(final int amount) {
        this.maximumIndividualAmount = Optional.of(amount);
        return this;
    }

    public Storage specificItems(final int ...itemIds) {
        this.specificItems = Optional.of(itemIds);
        return this;
    }

    public void fillFromInventory() {
        if (!specificItems.isPresent()) return;

        for (final int itemId : specificItems.get()) {
            add(itemId, store.getInventoryItemCount(itemId));
        }
    }

    public int getCharges() {
        int charges = 0;

        for (final int amount : storage.values()) {
            charges += amount;
        }

        return charges;
    }
}
