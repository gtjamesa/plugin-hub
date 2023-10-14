package tictac7x.charges.infoboxes;

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
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;

public class C_Coffin extends ChargedItem {
    private final int SIZE_BRONZE_COFFIN = 3;
    private final int SIZE_STEEL_COFFIN = 8;
    private final int SIZE_BLACK_COFFIN = 14;
    private final int SIZE_SILVER_COFFIN = 20;
    private final int SIZE_GOLD_COFFIN = 28;

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
        super(ItemKey.COFFIN, ItemID.GOLD_COFFIN, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, plugin);
        this.config_key = ChargesImprovedConfig.coffin;
        this.zero_charges_is_positive = true;

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.BROKEN_COFFIN).fixedCharges(0),
            new TriggerItem(ItemID.BRONZE_COFFIN).maxCharges(SIZE_BRONZE_COFFIN),
            new TriggerItem(ItemID.OPEN_BRONZE_COFFIN).maxCharges(SIZE_BRONZE_COFFIN),
            new TriggerItem(ItemID.STEEL_COFFIN).maxCharges(SIZE_STEEL_COFFIN),
            new TriggerItem(ItemID.OPEN_STEEL_COFFIN).maxCharges(SIZE_STEEL_COFFIN),
            new TriggerItem(ItemID.BLACK_COFFIN).maxCharges(SIZE_BLACK_COFFIN),
            new TriggerItem(ItemID.OPEN_BLACK_COFFIN).maxCharges(SIZE_BLACK_COFFIN),
            new TriggerItem(ItemID.SILVER_COFFIN).maxCharges(SIZE_SILVER_COFFIN),
            new TriggerItem(ItemID.OPEN_SILVER_COFFIN).maxCharges(SIZE_SILVER_COFFIN),
            new TriggerItem(ItemID.GOLD_COFFIN).maxCharges(SIZE_GOLD_COFFIN),
            new TriggerItem(ItemID.OPEN_GOLD_COFFIN).maxCharges(SIZE_GOLD_COFFIN),
        };

        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("You put the .+ remains into your open coffin.").increaseCharges(1),
            new TriggerChatMessage("Loar (.+) / Phrin (.+) / Riyl (.+) / Asyn (.+) / Fiyr (.+) / Urium (.+)").multipleCharges(),
            new TriggerChatMessage("Your coffin is empty.").fixedCharges(0),
            new TriggerChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.BRONZE_COFFIN, ItemID.OPEN_BRONZE_COFFIN).fixedCharges(SIZE_BRONZE_COFFIN),
            new TriggerChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.STEEL_COFFIN, ItemID.OPEN_STEEL_COFFIN).fixedCharges(SIZE_STEEL_COFFIN),
            new TriggerChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.BLACK_COFFIN, ItemID.OPEN_BLACK_COFFIN).fixedCharges(SIZE_BLACK_COFFIN),
            new TriggerChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.SILVER_COFFIN, ItemID.OPEN_SILVER_COFFIN).fixedCharges(SIZE_SILVER_COFFIN),
            new TriggerChatMessage("You cannot store anymore shade remains in this coffin.").specificItem(ItemID.GOLD_COFFIN, ItemID.OPEN_GOLD_COFFIN).fixedCharges(SIZE_GOLD_COFFIN),
        };

        this.triggersItemContainers = new TriggerItemContainer[]{
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Bronze coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Open bronze coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Steel coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Open steel coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Black coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Open black coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Silver coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Open silver coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Gold coffin").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Open gold coffin").menuOption("Fill").increaseByInventoryDifference(),
        };
    }
}
