package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.listeners.OnVarbitChanged;
import tictac7x.charges.store.ItemActivity;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class ChargedItemWithStorage extends ChargedItem {
    protected final Storage storage;

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
        this.storage = new Storage(configKey, configs, client_thread, store);
    }

    @Override
    public int getCharges() {
        return storage.getCharges();
    }
}
