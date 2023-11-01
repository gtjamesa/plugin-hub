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

public class J_BraceletOfFlamtaer extends ChargedItem {
    public J_BraceletOfFlamtaer(
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
        super(ItemKey.BRACELET_OF_FLAMTAER, ItemID.FLAMTAER_BRACELET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.bracelet_of_flamtaer;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.FLAMTAER_BRACELET).needsToBeEquipped(),
        };
        this.triggers = new TriggerBase[]{
            new OnChatMessage("Your Flamtaer bracelet helps you build the temple quicker. It has (?<charges>.+) charges? left."),
            new OnChatMessage("Your flamtaer bracelet has (?<charges>.+) charges? left."),
            new OnChatMessage("Your Flamtaer bracelet helps you build the temple quicker. It then crumbles to dust.").fixedCharges(80).notification("Your flamtaer bracelet crumbles to dust."),
            new OnChatMessage("The bracelet shatters. Your next Flamtaer bracelet will star afresh from 80 charges.").fixedCharges(80)
        };
    }
}
