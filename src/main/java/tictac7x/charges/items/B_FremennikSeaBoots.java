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
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerDailyReset;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerMenuOptionClicked;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class B_FremennikSeaBoots extends ChargedItem {
    public B_FremennikSeaBoots(
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
        super(ItemKey.FREMENNIK_SEA_BOOTS, ItemID.FREMENNIK_SEA_BOOTS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.fremennik_sea_boots;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.FREMENNIK_SEA_BOOTS_3)
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("You have already used your available teleport for today. Try again tomorrow when the boots have recharged.").fixedCharges(0),
        };
        this.triggersMenuOptionClicked = new TriggerMenuOptionClicked[]{
            new TriggerMenuOptionClicked("Teleport").itemId(ItemID.FREMENNIK_SEA_BOOTS_3).decreaseCharges(1),
        };
        this.triggersResetsDaily = new TriggerDailyReset[]{
            new TriggerDailyReset(1).specificItem(ItemID.FREMENNIK_SEA_BOOTS_3),
        };
    }
}
