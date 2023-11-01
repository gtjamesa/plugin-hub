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
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

public class U_GricollersCan extends ChargedItem {
    public U_GricollersCan(
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
        super(ItemKey.GRICOLLERS_CAN, ItemID.GRICOLLERS_CAN, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.gricollers_can;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.GRICOLLERS_CAN),
        };
        // TODO
//        this.triggersAnimations = new TriggerAnimation[]{
//            new TriggerAnimation(2293).unallowedItems(
//                ItemID.WATERING_CAN1, ItemID.WATERING_CAN2,
//                ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
//                ItemID.WATERING_CAN5, ItemID.WATERING_CAN6,
//                ItemID.WATERING_CAN6, ItemID.WATERING_CAN7,
//                ItemID.WATERING_CAN8
//            ).decreaseCharges(1),
//        };
        this.triggers = new TriggerBase[] {
            new OnChatMessage("Watering can charges remaining: (?<charges>.+)%").setDynamically().onItemClick(),
            new OnChatMessage("You water").onItemClick().decreaseCharges(1),
            new OnChatMessage("You fill the watering can").onItemClick().fixedCharges(1000),
        };
    }
}
