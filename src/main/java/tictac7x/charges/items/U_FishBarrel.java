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
import tictac7x.charges.item.storage.StoreableItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tictac7x.charges.store.ItemContainerType.BANK;
import static tictac7x.charges.store.ItemContainerType.INVENTORY;

public class U_FishBarrel extends ChargedItemWithStorage {
    private Optional<StoreableItem> lastCaughtFish = Optional.empty();

    public U_FishBarrel(
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
        super(ChargesImprovedConfig.fish_barrel, ItemKey.FISH_BARREL, ItemID.FISH_BARREL, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        storage = storage.maximumTotalQuantity(28).storeableItems(
            new StoreableItem(ItemID.RAW_SHRIMPS, "Shrimp"),
            new StoreableItem(ItemID.RAW_SARDINE, "Sardine"),
            new StoreableItem(ItemID.RAW_HERRING, "Herring"),
            new StoreableItem(ItemID.RAW_ANCHOVIES, "Anchovies"),
            new StoreableItem(ItemID.RAW_MACKEREL, "Mackerel"),
            new StoreableItem(ItemID.RAW_TROUT, "Trout"),
            new StoreableItem(ItemID.RAW_COD, "Cod"),
            new StoreableItem(ItemID.RAW_PIKE, "Pike"),
            new StoreableItem(ItemID.RAW_SLIMY_EEL, "Slimy swamp eel"),
            new StoreableItem(ItemID.RAW_SALMON, "Salmon"),
            new StoreableItem(ItemID.RAW_TUNA, "Tuna"),
            new StoreableItem(ItemID.RAW_RAINBOW_FISH, "Rainbow fish"),
            new StoreableItem(ItemID.RAW_CAVE_EEL, "Cave eel"),
            new StoreableItem(ItemID.RAW_LOBSTER, "Lobster"),
            new StoreableItem(ItemID.RAW_BASS, "Bass"),
            new StoreableItem(ItemID.LEAPING_TROUT, "Leaping trout"),
            new StoreableItem(ItemID.RAW_SWORDFISH, "Swordfish"),
            new StoreableItem(ItemID.RAW_LAVA_EEL, "Lava eel"),
            new StoreableItem(ItemID.LEAPING_SALMON, "Leaping salmon"),
            new StoreableItem(ItemID.RAW_MONKFISH, "Monkfish"),
            new StoreableItem(ItemID.RAW_KARAMBWAN, "Karambwan"),
            new StoreableItem(ItemID.LEAPING_STURGEON, "Leaping sturgeon"),
            new StoreableItem(ItemID.RAW_SHARK, "Shark"),
            new StoreableItem(ItemID.INFERNAL_EEL, "Infernal eel"),
            new StoreableItem(ItemID.RAW_ANGLERFISH, "Anglerfish"),
            new StoreableItem(ItemID.RAW_DARK_CRAB, "Dark crab"),
            new StoreableItem(ItemID.SACRED_EEL, "Sacred eel")
        );

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.FISH_BARREL),
            new TriggerItem(ItemID.OPEN_FISH_BARREL),
            new TriggerItem(ItemID.FISH_SACK_BARREL),
            new TriggerItem(ItemID.OPEN_FISH_SACK_BARREL),
        };
        this.triggers = new TriggerBase[]{
            // Check or empty already empty.
            new OnChatMessage("(Your|The) barrel is empty.").onItemClick().emptyStorage(),

            // Catch fish.
            new OnChatMessage("You catch (a|an) (?<fish>.+).").consumer(m -> {
                lastCaughtFish = getStorageItemFromName(m.group("fish"));
                storage.add(lastCaughtFish, 1);
            }).onSpecificItem(ItemID.OPEN_FISH_BARREL, ItemID.OPEN_FISH_SACK_BARREL),

            // Extra fish.
            new OnChatMessage(".* enabled you to catch an extra fish.").onSpecificItem(ItemID.OPEN_FISH_BARREL, ItemID.OPEN_FISH_SACK_BARREL).consumer(() -> {
                storage.add(lastCaughtFish, 1);
            }),

            // Check.
            new OnChatMessage("The barrel contains:").stringConsumer(s -> {
                storage.empty();

                final Pattern pattern = Pattern.compile("(?<quantity>\\d+).x.(?<fish>.*?)(,|$)");
                final Matcher matcher = pattern.matcher(s);

                while (matcher.find()) {
                    storage.put(getStorageItemFromName(matcher.group("fish")), Integer.parseInt(matcher.group("quantity")));
                }
            }).onItemClick(),

            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onMenuOption("Fill"),

            // Empty to bank.
            new OnItemContainerChanged(BANK).onMenuOption("Empty").emptyStorage(),

            // Hide destroy.
            new OnMenuEntryAdded("Destroy").hide(),
        };
    }
}
