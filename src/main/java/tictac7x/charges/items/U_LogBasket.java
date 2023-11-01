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
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class U_LogBasket extends ChargedItemWithStorage {
    private final int[] storeableItems = new int[]{
        ItemID.LOGS,
        ItemID.ACHEY_TREE_LOGS,
        ItemID.OAK_LOGS,
        ItemID.WILLOW_LOGS,
        ItemID.TEAK_LOGS,
        ItemID.JUNIPER_LOGS,
        ItemID.MAPLE_LOGS,
        ItemID.MAHOGANY_LOGS,
        ItemID.ARCTIC_PINE_LOGS,
        ItemID.YEW_LOGS,
        ItemID.BLISTERWOOD_LOGS,
        ItemID.MAGIC_LOGS,
        ItemID.REDWOOD_LOGS
    };

    public U_LogBasket(
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
        super(ChargesImprovedConfig.log_basket, ItemKey.LOG_BASKET, ItemID.LOG_BASKET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        storage.maximumTotalQuantity(28).storeableItems(storeableItems);

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.LOG_BASKET),
            new TriggerItem(ItemID.OPEN_LOG_BASKET),
            new TriggerItem(ItemID.FORESTRY_BASKET),
            new TriggerItem(ItemID.OPEN_FORESTRY_BASKET),
        };
        this.triggers = new TriggerBase[] {
            new OnChatMessage("(Your|The) basket is empty.").onItemClick().emptyStorage(),
            new OnChatMessage("You empty your basket( into the bank)?.").emptyStorage(),
            new OnChatMessage("The basket contains:").stringConsumer(s -> {
                storage.empty();

                final Pattern pattern = Pattern.compile("(?<quantity>\\d+).x.(?<logs>.*?)(,|$)");
                final Matcher matcher = pattern.matcher(s);

                while (matcher.find()) {
                    final int quantity = Integer.parseInt(matcher.group("quantity"));
                    final int itemId = getLogsIdFromName(matcher.group("logs"));
                    storage.put(itemId, quantity);
                }
            }).onItemClick(),
            new OnChatMessage("You get some (?<logs>.+).").consumer(m -> {
                final int itemId = getLogsIdFromName(m.group("logs"));
                storage.add(itemId, 1);
            }).specificItem(ItemID.OPEN_LOG_BASKET, ItemID.OPEN_FORESTRY_BASKET),
        };
        // TODO
//        this.triggersMenuOptionClicked = new TriggerMenuOptionClicked[]{
//            new TriggerMenuOptionClicked("Fill").fillStorageFromInventory(storage),
//            new TriggerMenuOptionClicked("Empty").atBank().emptyStorage(storage),
//            new TriggerMenuOptionClicked("Use").use(storeableItems).fillStorageFromInventory(storage),
//        };
//        this.triggersMenusEntriesAdded = new TriggerMenuEntryAdded[]{
//            new TriggerMenuEntryAdded("Destroy").hide(),
//        };
    }

    private int getLogsIdFromName(final String logs) {
        switch (logs.toLowerCase()) {
            case "logs":
                return ItemID.LOGS;
            case "achey tree logs":
                return ItemID.ACHEY_TREE_LOGS;
            case "oak logs":
                return ItemID.OAK_LOGS;
            case "willow logs":
                return ItemID.WILLOW_LOGS;
            case "teak logs":
                return ItemID.TEAK_LOGS;
            case "maple logs":
                return ItemID.MAPLE_LOGS;
            case "mahogany logs":
                return ItemID.MAHOGANY_LOGS;
            case "arctic pine logs":
                return ItemID.ARCTIC_PINE_LOGS;
            case "yew logs":
                return ItemID.YEW_LOGS;
            case "blisterwood logs":
                return ItemID.BLISTERWOOD_LOGS;
            case "magic logs":
                return ItemID.MAGIC_LOGS;
            case "redwood logs":
                return ItemID.REDWOOD_LOGS;
        }

        return Charges.UNKNOWN;
    }
}
