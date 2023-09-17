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
import tictac7x.charges.storage.ChargesItem;
import tictac7x.charges.storage.Storage;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerItemContainer;

public class U_LogBasket extends ChargedItemInfoBox {
    private final int MAX_CHARGES = 28;

    public U_LogBasket(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Storage storage,
        final Plugin plugin
    ) {
        super(ChargesItem.LOG_BASKET, ItemID.LOG_BASKET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, storage, plugin);
        this.config_key = ChargesImprovedConfig.log_basket;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.LOG_BASKET),
            new TriggerItem(ItemID.OPEN_LOG_BASKET),
            new TriggerItem(ItemID.FORESTRY_BASKET),
            new TriggerItem(ItemID.OPEN_FORESTRY_BASKET),
        };
        this.zero_charges_is_positive = true;
        this.negative_full_charges = MAX_CHARGES;
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("(Your|The) basket is empty.").onItemClick().fixedCharges(0),
            new TriggerChatMessage("You bank all your logs.").onItemClick().fixedCharges(0),
            new TriggerChatMessage("The basket contains:").onItemClick().multipleCharges(),
            new TriggerChatMessage("(You get some.* logs.|The nature offerings enabled you to chop an extra log.)").specificItem(ItemID.OPEN_LOG_BASKET).specificItem(ItemID.OPEN_FORESTRY_BASKET).increaseCharges(1),
        };
        this.triggers_item_containers = new TriggerItemContainer[]{
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Open log basket").menuOption("Fill").increaseByDifference(),
            new TriggerItemContainer(InventoryID.INVENTORY.getId()).menuTarget("Log basket").menuOption("Fill").increaseByDifference(),
        };
    }
}
