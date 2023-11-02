package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.StoreableItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnXpDrop;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tictac7x.charges.store.ItemContainerType.BANK;
import static tictac7x.charges.store.ItemContainerType.INVENTORY;

public class U_LogBasket extends ChargedItemWithStorage {
    private Optional<StoreableItem> lastLogs = Optional.empty();
    private int infernalQuantityTracker = 0;

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
        storage.maximumTotalQuantity(28).storeableItems(
            new StoreableItem(ItemID.LOGS, "Logs"),
            new StoreableItem(ItemID.ACHEY_TREE_LOGS, "Achey tree logs"),
            new StoreableItem(ItemID.OAK_LOGS, "Oak logs"),
            new StoreableItem(ItemID.WILLOW_LOGS, "Willow logs"),
            new StoreableItem(ItemID.TEAK_LOGS, "Teak logs"),
            new StoreableItem(ItemID.JUNIPER_LOGS, "Juniper logs"),
            new StoreableItem(ItemID.MAPLE_LOGS, "Maple logs"),
            new StoreableItem(ItemID.MAHOGANY_LOGS, "Mahogany logs"),
            new StoreableItem(ItemID.ARCTIC_PINE_LOGS, "Arctic pine logs"),
            new StoreableItem(ItemID.YEW_LOGS, "Yew logs"),
            new StoreableItem(ItemID.BLISTERWOOD_LOGS, "Blisterwood logs"),
            new StoreableItem(ItemID.MAGIC_LOGS,"Magic logs"),
            new StoreableItem(ItemID.REDWOOD_LOGS, "Redwood logs")
        );

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.LOG_BASKET),
            new TriggerItem(ItemID.OPEN_LOG_BASKET),
            new TriggerItem(ItemID.FORESTRY_BASKET),
            new TriggerItem(ItemID.OPEN_FORESTRY_BASKET),
        };
        this.triggers = new TriggerBase[] {
            // Check while empty.
            new OnChatMessage("(Your|The) basket is empty.").onItemClick().emptyStorage().consumer(() -> {
                infernalQuantityTracker = 0;
                lastLogs = Optional.empty();
            }),

            // Empty to bank.
            new OnChatMessage("You empty your basket( into the bank)?.").onItemClick().emptyStorage().consumer(() -> {
                infernalQuantityTracker = 0;
                lastLogs = Optional.empty();
            }),

            // Check.
            new OnChatMessage("The basket contains:").stringConsumer(s -> {
                storage.empty();

                final Pattern pattern = Pattern.compile("(?<quantity>\\d+).x.(?<logs>.*?)(,|$)");
                final Matcher matcher = pattern.matcher(s);

                while (matcher.find()) {
                    storage.put(getStorageItemFromName(matcher.group("logs")), Integer.parseInt(matcher.group("quantity")));
                }

                infernalQuantityTracker = getCharges();
            }).onItemClick(),

            // Chop.
            new OnChatMessage("You get some (?<logs>.+).").consumer(m -> {
                lastLogs = getStorageItemFromName(m.group("logs"));
                storage.add(lastLogs, 1);
                infernalQuantityTracker += 1;
            }).onSpecificItem(ItemID.OPEN_LOG_BASKET, ItemID.OPEN_FORESTRY_BASKET),

            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onMenuOption("Fill"),
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onUse(storage.getStoreableItems()),

            // Empty to inventory.
            new OnItemContainerChanged(INVENTORY).emptyStorageToInventory().onMenuOption("Empty"),

            // Empty to bank.
            new OnItemContainerChanged(BANK).onMenuOption("Empty").emptyStorage(),

            // Hide destroy.
            new OnMenuEntryAdded("Destroy").hide(),

            // Infernal axe support.
            new OnXpDrop(Skill.FIREMAKING).onMenuOption("Chop down", "Cut").consumer(() -> {
                System.out.println(infernalQuantityTracker);
                if (infernalQuantityTracker < 29 && lastLogs.isPresent()) {
                    storage.remove(lastLogs, 1);
                    infernalQuantityTracker -= 1;
                }
            }).onSpecificItem(ItemID.OPEN_LOG_BASKET, ItemID.OPEN_FORESTRY_BASKET),
        };
    }

}
