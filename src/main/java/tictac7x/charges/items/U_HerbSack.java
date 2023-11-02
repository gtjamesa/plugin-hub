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

import static tictac7x.charges.store.ItemContainerType.BANK;
import static tictac7x.charges.store.ItemContainerType.INVENTORY;

public class U_HerbSack extends ChargedItemWithStorage {
    public U_HerbSack(
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
        super(ChargesImprovedConfig.herb_sack, ItemKey.HERB_SACK, ItemID.HERB_SACK, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        storage = storage.maximumIndividualQuantity(30).storeableItems(
            new StoreableItem(ItemID.GRIMY_GUAM_LEAF, "Guam leaf"),
            new StoreableItem(ItemID.GRIMY_MARRENTILL, "Marrentill"),
            new StoreableItem(ItemID.GRIMY_TARROMIN, "Tarromin"),
            new StoreableItem(ItemID.GRIMY_HARRALANDER, "Harralander"),
            new StoreableItem(ItemID.GRIMY_RANARR_WEED, "Ranarr weed"),
            new StoreableItem(ItemID.GRIMY_TOADFLAX, "Toadflax"),
            new StoreableItem(ItemID.GRIMY_IRIT_LEAF, "Irit leaf"),
            new StoreableItem(ItemID.GRIMY_AVANTOE, "Avantoe"),
            new StoreableItem(ItemID.GRIMY_KWUARM, "Kwuarm"),
            new StoreableItem(ItemID.GRIMY_SNAPDRAGON, "Snapdragon"),
            new StoreableItem(ItemID.GRIMY_CADANTINE, "Cadantine"),
            new StoreableItem(ItemID.GRIMY_LANTADYME, "Lantadyme"),
            new StoreableItem(ItemID.GRIMY_DWARF_WEED, "Dwarf weed"),
            new StoreableItem(ItemID.GRIMY_TORSTOL, "Torstol")
        );

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.HERB_SACK).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_HERB_SACK).zeroChargesIsPositive(),
        };
        this.triggers = new TriggerBase[] {
            // Check or empty.
            new OnChatMessage("The herb sack is empty.").emptyStorage(),

            // Pickup.
            new OnChatMessage("You put the Grimy (?<herb>.+) herb into your herb sack.").consumer(m -> {
                storage.add(getStorageItemFromName(m.group("herb")), 1);
            }),

            // Check.
            new OnChatMessage("You look in your herb sack and see:").emptyStorage(),
            new OnChatMessage("(?<quantity>.+) x Grimy (?<herb>.+)").consumer(m -> {
                storage.put(getStorageItemFromName(m.group("herb")), Integer.parseInt(m.group("quantity")));
            }),

            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().isMenuOption("Fill"),

            // Empty to inventory.
            new OnItemContainerChanged(INVENTORY).emptyStorageToInventory().isMenuOption("Empty"),

            // Empty to bank.
            new OnItemContainerChanged(BANK).isMenuOption("Empty").emptyStorage(),

            // Pick guam leaf.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs", "Guam herbs").isMenuImpostorId(26828, 39816).addToStorage(ItemID.GRIMY_GUAM_LEAF, 1),
            new OnMenuEntryAdded().isReplaceImpostorId(26828, 39816).replaceTarget("Herbs", "Guam herbs"),

            // Pick marrentill.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs", "Marrentill herbs").isMenuImpostorId(39751, 39821).addToStorage(ItemID.GRIMY_MARRENTILL, 1),
            new OnMenuEntryAdded().isReplaceImpostorId(39751, 39821).replaceTarget("Herbs", "Marrentill herbs"),

            // Pick tarromin.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs", "Tarromin herbs").isMenuImpostorId(39756, 39826).addToStorage(ItemID.GRIMY_TARROMIN, 1),
            new OnMenuEntryAdded().isReplaceImpostorId(39756, 39826).replaceTarget("Herbs", "Tarromin herbs"),

            // Pick harralander.
            // Pick ranarr.
            // Pick toadflax.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39781).consumer(() -> {
                storage.add(ItemID.GRIMY_TOADFLAX, 1);
            }),

            // Pick irit leaf.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs", "Irit herbs").isMenuImpostorId(39771).addToStorage(ItemID.GRIMY_IRIT_LEAF, 1),
            new OnMenuEntryAdded().isReplaceImpostorId(39771).replaceTarget("Herbs", "Irit herbs"),

            // Pick avantoe.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39846).consumer(() -> {
                storage.add(ItemID.GRIMY_AVANTOE, 1);
            }),

            // Pick kwuarm.
            // Pick snapdragon
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39811).consumer(() -> {
                storage.add(ItemID.GRIMY_SNAPDRAGON, 1);
            }),

            // Pick cadantine.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39791).consumer(() -> {
                storage.add(ItemID.GRIMY_CADANTINE, 1);
            }),

            // Pick lantadyme.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39796).consumer(() -> {
                storage.add(ItemID.GRIMY_LANTADYME, 1);
            }),

            // Pick dwarf weed.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39801).consumer(() -> {
                storage.add(ItemID.GRIMY_DWARF_WEED, 1);
            }),

            // Pick torstol.
            // Pick irit leaf.
            new OnXpDrop(Skill.FARMING).isMenuOption("Pick").isMenuTarget("Herbs").isMenuImpostorId(39771, 39841).consumer(() -> {
                storage.add(ItemID.GRIMY_IRIT_LEAF, 1);
            }),

            // Hide destroy option.
            new OnMenuEntryAdded("Destroy").hide()
        };
    }
}
