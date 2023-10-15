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
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;

public class U_LogBasket extends ChargedItem {
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
        final Store store,
        final Plugin plugin
    ) {
        super(ItemKey.LOG_BASKET, ItemID.LOG_BASKET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.log_basket;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.LOG_BASKET),
            new TriggerItem(ItemID.OPEN_LOG_BASKET),
            new TriggerItem(ItemID.FORESTRY_BASKET),
            new TriggerItem(ItemID.OPEN_FORESTRY_BASKET),
        };
        this.zero_charges_is_positive = true;
        this.negative_full_charges = MAX_CHARGES;
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("(Your|The) basket is empty.").onItemClick().fixedCharges(0),
            new TriggerChatMessage("You bank all your logs.").onItemClick().fixedCharges(0),
            new TriggerChatMessage("The basket contains:").onItemClick().multipleCharges(),
            new TriggerChatMessage("(You get some.* logs.|The nature offerings enabled you to chop an extra log.)").specificItem(ItemID.OPEN_LOG_BASKET).specificItem(ItemID.OPEN_FORESTRY_BASKET).increaseCharges(1),
        };
        this.triggersItemContainers = new TriggerItemContainer[]{
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Open log basket").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuTarget("Log basket").menuOption("Fill").increaseByInventoryDifference(),
        };
    }
}
