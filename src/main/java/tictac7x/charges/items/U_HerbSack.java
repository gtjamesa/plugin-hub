package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
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
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnXpDrop;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

import static tictac7x.charges.store.ItemContainerType.BANK;
import static tictac7x.charges.store.ItemContainerType.INVENTORY;

public class U_HerbSack extends ChargedItemWithStorage {
    private final int[] storeableItems = new int[]{
        ItemID.GRIMY_GUAM_LEAF,
        ItemID.GRIMY_MARRENTILL,
        ItemID.GRIMY_TARROMIN,
        ItemID.GRIMY_HARRALANDER,
        ItemID.GRIMY_RANARR_WEED,
        ItemID.GRIMY_TOADFLAX,
        ItemID.GRIMY_IRIT_LEAF,
        ItemID.GRIMY_AVANTOE,
        ItemID.GRIMY_KWUARM,
        ItemID.GRIMY_SNAPDRAGON,
        ItemID.GRIMY_CADANTINE,
        ItemID.GRIMY_LANTADYME,
        ItemID.GRIMY_DWARF_WEED,
        ItemID.GRIMY_TORSTOL
    };

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
        storage.maximumIndividualQuantity(30).storeableItems(storeableItems);

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.HERB_SACK).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_HERB_SACK).zeroChargesIsPositive(),
        };
        this.triggers = new TriggerBase[] {
            // Check or empty.
            new OnChatMessage("The herb sack is empty.").emptyStorage(),
            // Pickup.
            new OnChatMessage("You put the Grimy (?<herb>.+) herb into your herb sack.").consumer(m -> {
                storage.add(getHerbIdFromName(m.group("herb")), 1);
            }),
            // Check.
            new OnChatMessage("You look in your herb sack and see:").emptyStorage(),
            new OnChatMessage("(?<quantity>.+) x Grimy (?<herb>.+)").consumer(m -> {
                storage.put(getHerbIdFromName(m.group("herb")), Integer.parseInt(m.group("quantity")));
            }),
            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventory().onMenuOption("Fill"),
            // Empty to inventory.
            new OnItemContainerChanged(INVENTORY).emptyStorageToInventory().onMenuOption("Empty"),
            // Empty to bank.
            new OnItemContainerChanged(BANK).onMenuOption("Empty").emptyStorage(),
            // Pick guam leaf.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39816).consumer(() -> {
                storage.add(ItemID.GRIMY_GUAM_LEAF, 1);
            }),
            // Pick marrentill.
            // Pick tarromin.
            // Pick harralander.
            // Pick ranarr.
            // Pick toadflax.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39781).consumer(() -> {
                storage.add(ItemID.GRIMY_TOADFLAX, 1);
            }),
            // Pick irit leaf.
            // Pick avantoe.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39846).consumer(() -> {
                storage.add(ItemID.GRIMY_AVANTOE, 1);
            }),
            // Pick kwuarm.
            // Pick snapdragon
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39811).consumer(() -> {
                storage.add(ItemID.GRIMY_SNAPDRAGON, 1);
            }),
            // Pick cadantine.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39791).consumer(() -> {
                storage.add(ItemID.GRIMY_CADANTINE, 1);
            }),
            // Pick lantadyme.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39796).consumer(() -> {
                storage.add(ItemID.GRIMY_LANTADYME, 1);
            }),
            // Pick dwarf weed.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39801).consumer(() -> {
                storage.add(ItemID.GRIMY_DWARF_WEED, 1);
            }),
            // Pick torstol.
            // Pick irit leaf.
            new OnXpDrop(Skill.FARMING).onMenuOption("Pick").onMenuTarget("Herbs").onMenuImpostorId(39771, 39841).consumer(() -> {
                storage.add(ItemID.GRIMY_IRIT_LEAF, 1);
            }),
            // Hide destroy option.
            new OnMenuEntryAdded("Destroy").hide()
        };
    }

    private int getHerbIdFromName(final String herb) {
        switch (herb.toLowerCase()) {
            case "guam leaf":
                return ItemID.GRIMY_GUAM_LEAF;
            case "marrentill":
                return ItemID.GRIMY_MARRENTILL;
            case "tarromin":
                return ItemID.GRIMY_TARROMIN;
            case "harralander":
                return ItemID.GRIMY_HARRALANDER;
            case "ranarr weed":
                return ItemID.GRIMY_RANARR_WEED;
            case "toadflax":
                return ItemID.GRIMY_TOADFLAX;
            case "irit leaf":
                return ItemID.GRIMY_IRIT_LEAF;
            case "avantoe":
                return ItemID.GRIMY_AVANTOE;
            case "kwuarm":
                return ItemID.GRIMY_KWUARM;
            case "snapdragon":
                return ItemID.GRIMY_SNAPDRAGON;
            case "cadantine":
                return ItemID.GRIMY_CADANTINE;
            case "lantadyme":
                return ItemID.GRIMY_LANTADYME;
            case "dwarf weed":
                return ItemID.GRIMY_DWARF_WEED;
            case "torstol":
                return ItemID.GRIMY_TORSTOL;
        }

        return Charges.UNKNOWN;
    }
}
