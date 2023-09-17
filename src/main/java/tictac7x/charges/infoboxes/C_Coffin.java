package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.ChargesItem;
import tictac7x.charges.store.Store;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerItemContainer;

public class C_Coffin extends ChargedItemInfoBox {
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
        super(ChargesItem.COFFIN, ItemID.GOLD_COFFIN, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, plugin);
        this.config_key = ChargesImprovedConfig.coffin;
        this.zero_charges_is_positive = true;

        this.triggers_items = new TriggerItem[]{
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

        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("You put the .+ remains into your open coffin.").increaseCharges(1),
            new TriggerChatMessage("Loar (.+) / Phrin (.+) / Riyl (.+) / Asyn (.+) / Fiyr (.+) / Urium (.+)").multipleCharges(),
            new TriggerChatMessage("Your coffin is empty.").fixedCharges(0),
            new TriggerChatMessage("You cannot store anymore shade remains in this coffin.").extraConsumer((message) -> {
                switch (item_id) {
                    case ItemID.BRONZE_COFFIN:
                    case ItemID.OPEN_BRONZE_COFFIN:
                        setCharges(SIZE_BRONZE_COFFIN);
                        return;

                    case ItemID.STEEL_COFFIN:
                    case ItemID.OPEN_STEEL_COFFIN:
                        setCharges(SIZE_STEEL_COFFIN);
                        return;

                    case ItemID.BLACK_COFFIN:
                    case ItemID.OPEN_BLACK_COFFIN:
                        setCharges(SIZE_BLACK_COFFIN);
                        return;

                    case ItemID.SILVER_COFFIN:
                    case ItemID.OPEN_SILVER_COFFIN:
                        setCharges(SIZE_SILVER_COFFIN);
                        return;

                    case ItemID.GOLD_COFFIN:
                    case ItemID.OPEN_GOLD_COFFIN:
                        setCharges(SIZE_GOLD_COFFIN);
                        return;
                }
            })
        };

        this.triggers_item_containers = new TriggerItemContainer[]{
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Bronze coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Open bronze coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Steel coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Open steel coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Black coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Open black coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Silver coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Open silver coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Gold coffin").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Open gold coffin").menuOption("Fill").increaseByDifference(),
        };
    }
}
