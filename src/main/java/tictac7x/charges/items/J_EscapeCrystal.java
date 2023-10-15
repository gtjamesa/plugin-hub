package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.triggers.TriggerVarbit;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

public class J_EscapeCrystal extends ChargedItemWithStatus {
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
        super(ItemKey.ESCAPE_CRYSTAL, ItemID.ESCAPE_CRYSTAL, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.escape_crystal;

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.ESCAPE_CRYSTAL).quantityCharges().hideOverlay(),
        };
        this.triggersVarbits = new TriggerVarbit[]{
            new TriggerVarbit(VARBIT_ESCAPE_CRYSTAL_ACTIVATED, 0).deactivate(),
            new TriggerVarbit(VARBIT_ESCAPE_CRYSTAL_ACTIVATED, 1).activate(),
        };
    }
}
