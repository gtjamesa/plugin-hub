package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.ChargesItem;
import tictac7x.charges.store.Store;
import tictac7x.charges.triggers.TriggerItem;

public class J_EscapeCrystal extends ChargedItemInfoBox {
    final private int VARBIT_ESCAPE_CRYSTAL_ACTIVATED = 14838;

    public J_EscapeCrystal(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Plugin plugin
    ) {
        super(ChargesItem.ESCAPE_CRYSTAL, ItemID.ESCAPE_CRYSTAL, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, plugin);
        this.config_key = ChargesImprovedConfig.escape_crystal;
        this.needs_to_be_equipped_for_infobox = true;

        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.ESCAPE_CRYSTAL).varbitChecker(VARBIT_ESCAPE_CRYSTAL_ACTIVATED, 0).quantityCharges().isNegative().hideOverlay(),
            new TriggerItem(ItemID.ESCAPE_CRYSTAL).varbitChecker(VARBIT_ESCAPE_CRYSTAL_ACTIVATED, 1).quantityCharges().hideOverlay()
        };
    }
}
