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
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onMenuOption("Fill"),

            // Empty to inventory.
            new OnItemContainerChanged(INVENTORY).emptyStorageToInventory().onMenuOption("Empty"),

            // Empty to bank.
            new OnItemContainerChanged(BANK).onMenuOption("Empty").emptyStorage(),

            // Pick guam leaf.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Guam herbs").onMenuImpostor(26828, 39816).addToStorage(ItemID.GRIMY_GUAM_LEAF),
            new OnMenuEntryAdded().isReplaceImpostorId(26828, 39816).replaceTarget("Herbs", "Guam herbs"),

            // Pick marrentill.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Marrentill herbs").onMenuImpostor(39751, 39821).addToStorage(ItemID.GRIMY_MARRENTILL),
            new OnMenuEntryAdded().isReplaceImpostorId(39751, 39821).replaceTarget("Herbs", "Marrentill herbs"),

            // Pick tarromin.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Tarromin herbs").onMenuImpostor(39756, 39826).addToStorage(ItemID.GRIMY_TARROMIN),
            new OnMenuEntryAdded().isReplaceImpostorId(39756, 39826).replaceTarget("Herbs", "Tarromin herbs"),

            // Pick harralander.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Harralander herbs").onMenuImpostor(39761, 39831).addToStorage(ItemID.GRIMY_HARRALANDER),
            new OnMenuEntryAdded().isReplaceImpostorId(39761, 39831).replaceTarget("Herbs", "Harralander herbs"),

            // Pick ranarr.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Ranarr weed herbs").onMenuImpostor(39766, 39836).addToStorage(ItemID.GRIMY_RANARR_WEED),
            new OnMenuEntryAdded().isReplaceImpostorId(39766, 39836).replaceTarget("Herbs", "Ranarr weed herbs"),

            // Pick toadflax.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Toadflax herbs").onMenuImpostor(39781, 39851).addToStorage(ItemID.GRIMY_TOADFLAX),
            new OnMenuEntryAdded().isReplaceImpostorId(39781, 39851).replaceTarget("Herbs", "Toadflax herbs"),

            // Pick irit leaf.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Irit leaf herbs").onMenuImpostor(39771, 39841).addToStorage(ItemID.GRIMY_IRIT_LEAF),
            new OnMenuEntryAdded().isReplaceImpostorId(39771, 39841).replaceTarget("Herbs", "Irit leaf herbs"),

            // Pick avantoe.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Avantoe herbs").onMenuImpostor(39776, 39846).addToStorage(ItemID.GRIMY_AVANTOE),
            new OnMenuEntryAdded().isReplaceImpostorId(39776, 39846).replaceTarget("Herbs", "Avantoe herbs"),

            // Pick kwuarm.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Kwuarm herbs").onMenuImpostor(39786).addToStorage(ItemID.GRIMY_KWUARM),
            new OnMenuEntryAdded().isReplaceImpostorId(39786).replaceTarget("Herbs", "Kwuarm herbs"),

            // Pick snapdragon
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Snapdragon herbs").onMenuImpostor(39811).addToStorage(ItemID.GRIMY_SNAPDRAGON),
            new OnMenuEntryAdded().isReplaceImpostorId(39811).replaceTarget("Herbs", "Snapdragon herbs"),

            // Pick cadantine.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Cadantine herbs").onMenuImpostor(39791).addToStorage(ItemID.GRIMY_CADANTINE),
            new OnMenuEntryAdded().isReplaceImpostorId(39791).replaceTarget("Herbs", "Cadantine herbs"),

            // Pick lantadyme.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Lantadyme herbs").onMenuImpostor(39796).addToStorage(ItemID.GRIMY_LANTADYME),
            new OnMenuEntryAdded().isReplaceImpostorId(39796).replaceTarget("Herbs", "Lantadyme herbs"),

            // Pick dwarf weed.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs", "Dwarf weed herbs").onMenuImpostor(39801).addToStorage(ItemID.GRIMY_DWARF_WEED),
            new OnMenuEntryAdded().isReplaceImpostorId(39801).replaceTarget("Herbs", "Dwarf weed herbs"),

            // Pick torstol.
//            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostor(39771, 39841).consumer(() -> {
//                storage.add(ItemID.GRIMY_IRIT_LEAF);
//            }),

            // Hide destroy option.
            new OnMenuEntryAdded("Destroy").hide()
        };
    }
}
