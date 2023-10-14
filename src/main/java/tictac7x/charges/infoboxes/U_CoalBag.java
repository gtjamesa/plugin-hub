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
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;

public class U_CoalBag extends ChargedItem {
    public U_CoalBag(
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
        super(ItemKey.COAL_BAG, ItemID.COAL_BAG, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, plugin);
        this.config_key = ChargesImprovedConfig.coal_bag;
        this.negative_full_charges = 27;
        this.zero_charges_is_positive = true;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.COAL_BAG_12019),
            new TriggerItem(ItemID.OPEN_COAL_BAG),
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("The coal bag is( now)? empty.").fixedCharges(0),
            new TriggerChatMessage("The coal bag( still)? contains one piece of coal.").fixedCharges(1),
            new TriggerChatMessage("The coal bag( still)? contains (?<charges>.+) pieces of coal."),
            new TriggerChatMessage("You manage to mine some coal.").specificItem(ItemID.OPEN_COAL_BAG).increaseCharges(1),
        };
    }
}
