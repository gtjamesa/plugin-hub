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
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemDespawned;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

import static tictac7x.charges.store.ItemContainerType.BANK;
import static tictac7x.charges.store.ItemContainerType.INVENTORY;


public class U_GemBag extends ChargedItemWithStorage {
    public U_GemBag(
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
        super(ChargesImprovedConfig.gem_bag, ItemKey.GEM_BAG, ItemID.GEM_BAG_12020, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        storage.maximumIndividualQuantity(60).storeableItems(
            new StorageItem(ItemID.UNCUT_SAPPHIRE).checkName("Sapphire").displayName("LMAO"),
            new StorageItem(ItemID.UNCUT_EMERALD).checkName("Emerald"),
            new StorageItem(ItemID.UNCUT_RUBY).checkName("Ruby"),
            new StorageItem(ItemID.UNCUT_DIAMOND).checkName("Diamond"),
            new StorageItem(ItemID.UNCUT_DRAGONSTONE).checkName("Dragonstone")
        );

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.GEM_BAG_12020).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_GEM_BAG).zeroChargesIsPositive(),
        };

        this.triggers = new TriggerBase[]{
            // Empty to bank or inventory.
            new OnChatMessage("The gem bag is( now)? empty.").emptyStorage(),

            // Empty and Check.
            new OnChatMessage("(Left in bag: )?Sapphires: (?<sapphires>.+) / Emeralds: (?<emeralds>.+) / Rubies: (?<rubies>.+) Diamonds: (?<diamonds>.+) / Dragonstones: (?<dragonstones>.+)").consumer(m -> {
                storage.put(ItemID.UNCUT_SAPPHIRE, Integer.parseInt(m.group("sapphires")));
                storage.put(ItemID.UNCUT_EMERALD, Integer.parseInt(m.group("emeralds")));
                storage.put(ItemID.UNCUT_RUBY, Integer.parseInt(m.group("rubies")));
                storage.put(ItemID.UNCUT_DIAMOND, Integer.parseInt(m.group("diamonds")));
                storage.put(ItemID.UNCUT_DRAGONSTONE, Integer.parseInt(m.group("dragonstones")));
            }),

            // Mining regular or gem rocks.
            new OnChatMessage("You just (found|mined) (a|an) (?<gem>.+)!").consumer(m -> {
                storage.add(getStorageItemFromName(m.group("gem")), 1);
            }).onSpecificItem(ItemID.OPEN_GEM_BAG),

            // Pickpocketing.
            new OnChatMessage("The following stolen loot gets added to your gem bag: Uncut (?<gem>.+) x (?<quantity>.+).").consumer(m -> {
                storage.add(getStorageItemFromName(m.group("gem")), Integer.parseInt(m.group("quantity")));
            }),

            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onMenuOption("Fill"),

            // Empty to bank.
            new OnItemContainerChanged(BANK).onMenuOption("Empty").emptyStorage(),

            // Use gem on bag
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onUse(storage.getStoreableItems()),

            // Pick up.
            new OnItemDespawned(storage.getStoreableItems()).onSpecificItem(ItemID.OPEN_GEM_BAG).pickUpToStorage(),

            // Hide destroy.
            new OnMenuEntryAdded("Destroy").hide(),
        };
    }
}
