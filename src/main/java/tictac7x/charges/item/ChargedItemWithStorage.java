package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.storage.Storage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

public class ChargedItemWithStorage extends ChargedItemBase {
    public Storage storage;

    public ChargedItemWithStorage(String configKey, ItemKey itemKey, int itemId, Client client, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, Notifier notifier, ChargesImprovedConfig config, Store store) {
        super(configKey, itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
        this.storage = new Storage(this, configKey, configManager, store);
    }

    public List<StorageItem> getStorage() {
        return this.storage.getStorage();
    }

    public Optional<StorageItem> getStorageItemFromName(final String name) {
        return storage.getStorageItemFromName(name);
    }

    public int getQuantity() {
        int quantity = 0;

        int validItems = 0;
        for (final StorageItem storageItem : getStorage()) {
            if (storageItem.getQuantity() >= 0) {
                quantity += storageItem.getQuantity();
                validItems++;
            }
        }

        return validItems > 0 ? quantity : Charges.UNKNOWN;
    }

    @Override
    public String getCharges() {
        int quantity = getQuantity();

        if (quantity == Charges.UNKNOWN) {
            return "?";
        }

        return getChargesMinified(quantity);
    }

    @Override
    public void loadCharges() {
        storage.loadStorage();
    }

    @Override
    public Color getTextColor() {
        // Storage is full.
        if (storage.getMaximumTotalQuantity().isPresent() && getCharges().equals(String.valueOf(storage.getMaximumTotalQuantity().get()))) {
            return config.getColorEmpty();
        }

        // Storage is empty.
        if (getQuantity() == 0) {
            return config.getColorDefault();
        }

        return super.getTextColor();
    }

    @Override
    public void onConfigChanged(final ConfigChanged event) {
        if (configKey.isPresent() && event.getGroup().equals(ChargesImprovedConfig.group) && event.getKey().equals(configKey.get() + "_storage")) {
            loadCharges();
        }
    }
}
