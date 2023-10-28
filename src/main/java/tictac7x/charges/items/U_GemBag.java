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
import tictac7x.charges.item.triggers.TriggerItemDespawned;
import tictac7x.charges.item.triggers.TriggerMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerMenuOptionClicked;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;

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
        super(ChargesImprovedConfig.gem_bag, ItemKey.GEM_BAG, ItemID.GEM_BAG, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.gem_bag;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.GEM_BAG_12020).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_GEM_BAG).zeroChargesIsPositive(),
        };

        storage.maximumIndividualAmount(60).specificItems(
            ItemID.UNCUT_SAPPHIRE,
            ItemID.UNCUT_EMERALD,
            ItemID.UNCUT_RUBY,
            ItemID.UNCUT_DIAMOND,
            ItemID.UNCUT_DRAGONSTONE
        );

        this.triggersChatMessages = new TriggerChatMessage[]{
            // Empty to bank or inventory.
            new TriggerChatMessage("The gem bag is( now)? empty.").consumer(m -> storage.empty()),
            // Empty and Check.
            new TriggerChatMessage("(Left in bag: )?Sapphires: (?<sapphires>.+) / Emeralds: (?<emeralds>.+) / Rubies: (?<rubies>.+) Diamonds: (?<diamonds>.+) / Dragonstones: (?<dragonstones>.+)").consumer(m -> {
                storage.put(ItemID.UNCUT_SAPPHIRE, Integer.parseInt(m.group("sapphires")));
                storage.put(ItemID.UNCUT_EMERALD, Integer.parseInt(m.group("emeralds")));
                storage.put(ItemID.UNCUT_RUBY, Integer.parseInt(m.group("rubies")));
                storage.put(ItemID.UNCUT_DIAMOND, Integer.parseInt(m.group("diamonds")));
                storage.put(ItemID.UNCUT_DRAGONSTONE, Integer.parseInt(m.group("dragonstones")));
            }),
            // Mining.
            new TriggerChatMessage("You just mined (a|an) (?<gem>.+)!").consumer(m -> {
                final int itemId = getItemIdFromGem(m.group("gem"));
                storage.add(itemId, 1);
            }),
            // Pickpocketing.
            new TriggerChatMessage("The following stolen loot gets added to your gem bag: Uncut (?<gem>.+) x (?<amount>.+)").consumer(m -> {
                final int itemId = getItemIdFromGem(m.group("gem"));
                final int amount = Integer.parseInt(m.group("amount"));

                storage.add(itemId, amount);
            }),
        };

        this.triggersMenuOptionClicked = new TriggerMenuOptionClicked[]{
            new TriggerMenuOptionClicked("Fill").itemId(ItemID.GEM_BAG_12020, ItemID.OPEN_GEM_BAG).consumer(() -> {
                storage.fillFromInventory();
            }),
            new TriggerMenuOptionClicked("Empty").atBank().itemId(ItemID.GEM_BAG_12020, ItemID.OPEN_GEM_BAG).consumer(() -> {
                storage.empty();
            }),
            new TriggerMenuOptionClicked("Use").use(
                new String[]{"Gem bag", "Open gem bag"},
                new String[]{"Uncut sapphire", "Uncut emerald", "Uncut ruby", "Uncut diamond", "Uncut dragonstone"}
            ).consumer(() -> {
                storage.fillFromInventory();
            })
        };

        this.triggersItemDespawned = new TriggerItemDespawned[]{
            new TriggerItemDespawned(
                ItemID.UNCUT_SAPPHIRE,
                ItemID.UNCUT_EMERALD,
                ItemID.UNCUT_RUBY,
                ItemID.UNCUT_DIAMOND,
                ItemID.UNCUT_DRAGONSTONE
            ).specificItem(ItemID.OPEN_GEM_BAG).pickUpToStorage(storage),
        };

        this.triggersMenusEntriesAdded = new TriggerMenuEntryAdded[]{
            new TriggerMenuEntryAdded("Destroy").hide(),
        };
    }

    private int getItemIdFromGem(final String gem) {
        switch (gem.toLowerCase()) {
            case "sapphire":
                return ItemID.UNCUT_SAPPHIRE;
            case "emerald":
                return ItemID.UNCUT_EMERALD;
            case "ruby":
                return ItemID.UNCUT_RUBY;
            case "diamond":
                return ItemID.UNCUT_DIAMOND;
            case "dragonstone":
                return ItemID.UNCUT_DRAGONSTONE;
        }

        return Charges.UNKNOWN;
    }
}
