package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.storage.Storage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.storage.StoreableItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

public class ChargedItemWithStorage extends ChargedItem {
    public Storage storage;

    public ChargedItemWithStorage(
        final String configKey,
        final ItemKey infobox_id,
        final int item_id, Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store
    ) {
        super(
            infobox_id,
            item_id,
            client,
            client_thread,
            configs,
            items,
            infoboxes,
            chat_messages,
            notifier,
            config,
            store
        );
        this.config_key = configKey;
        this.storage = new Storage(this, configKey, configs, client_thread, store);
    }

    @Override
    public int getCharges() {
        return storage.getCharges();
    }

    public List<StorageItem> getStorage() {
        return this.storage.getStorage();
    }

    @Override
    public Color getTextColor() {
        if (getCharges() == 0) {
            return config.getColorDefault();
        }

        if (storage.getMaximumTotalQuantity().isPresent() && getCharges() == storage.getMaximumTotalQuantity().get()) {
            return config.getColorEmpty();
        }

        return super.getTextColor();
    }

    public Optional<StoreableItem> getStorageItemFromName(final String name) {
        return storage.getStorageItemFromName(name);
    }
}
