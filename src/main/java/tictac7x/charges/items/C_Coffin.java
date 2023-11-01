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
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

public class C_Coffin extends ChargedItem {
    private final int MAX_CHARGES_BRONZE_COFFIN = 3;
    private final int MAX_CHARGES_STEEL_COFFIN = 8;
    private final int MAX_CHARGES_BLACK_COFFIN = 14;
    private final int MAX_CHARGES_SILVER_COFFIN = 20;
    private final int MAX_CHARGES_GOLD_COFFIN = 28;

    public C_Coffin(
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
        super(ItemKey.COFFIN, ItemID.GOLD_COFFIN, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.coffin;

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.BROKEN_COFFIN).fixedCharges(0),
            new TriggerItem(ItemID.BRONZE_COFFIN).negativeFullCharges(MAX_CHARGES_BRONZE_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_BRONZE_COFFIN).negativeFullCharges(MAX_CHARGES_BRONZE_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.STEEL_COFFIN).negativeFullCharges(MAX_CHARGES_STEEL_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_STEEL_COFFIN).negativeFullCharges(MAX_CHARGES_STEEL_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.BLACK_COFFIN).negativeFullCharges(MAX_CHARGES_BLACK_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_BLACK_COFFIN).negativeFullCharges(MAX_CHARGES_BLACK_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.SILVER_COFFIN).negativeFullCharges(MAX_CHARGES_SILVER_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_SILVER_COFFIN).negativeFullCharges(MAX_CHARGES_SILVER_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.GOLD_COFFIN).negativeFullCharges(MAX_CHARGES_GOLD_COFFIN).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_GOLD_COFFIN).negativeFullCharges(MAX_CHARGES_GOLD_COFFIN).zeroChargesIsPositive(),
        };

        this.triggers = new TriggerBase[] {
            new OnChatMessage("You put the .+ remains into your open coffin.").increaseCharges(1),
                // TODO to storage
//            new OnChatMessage("Loar (.+) / Phrin (.+) / Riyl (.+) / Asyn (.+) / Fiyr (.+) / Urium (.+)").multipleCharges(),
            new OnChatMessage("Your coffin is empty.").fixedCharges(0),
            new OnChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.BRONZE_COFFIN, ItemID.OPEN_BRONZE_COFFIN).fixedCharges(MAX_CHARGES_BRONZE_COFFIN),
            new OnChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.STEEL_COFFIN, ItemID.OPEN_STEEL_COFFIN).fixedCharges(MAX_CHARGES_STEEL_COFFIN),
            new OnChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.BLACK_COFFIN, ItemID.OPEN_BLACK_COFFIN).fixedCharges(MAX_CHARGES_BLACK_COFFIN),
            new OnChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.SILVER_COFFIN, ItemID.OPEN_SILVER_COFFIN).fixedCharges(MAX_CHARGES_SILVER_COFFIN),
            new OnChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.GOLD_COFFIN, ItemID.OPEN_GOLD_COFFIN).fixedCharges(MAX_CHARGES_GOLD_COFFIN),
        };


        // TODO
//        this.triggersItemContainers = new TriggerItemContainer[]{
//            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Bronze coffin", "Fill").menuEntry("Open bronze coffin", "Fill").increaseByInventoryDifference(),
//            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Steel coffin", "Fill").menuEntry("Open steel coffin", "Fill").increaseByInventoryDifference(),
//            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Black coffin", "Fill").menuEntry("Open black coffin", "Fill").increaseByInventoryDifference(),
//            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Silver coffin", "Fill").menuEntry("Open silver coffin", "Fill").increaseByInventoryDifference(),
//            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Gold coffin", "Fill").menuEntry("Open gold coffin", "Fill").increaseByInventoryDifference(),
//        };
    }
}
