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
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesItem;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;

public class J_BraceletOfFlamtaer extends ChargedItemInfoBox {
    public J_BraceletOfFlamtaer(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.BRACELET_OF_FLAMTAER, ItemID.FLAMTAER_BRACELET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, plugin);
        this.config_key = ChargesImprovedConfig.bracelet_of_flamtaer;
        this.needs_to_be_equipped_for_infobox = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.FLAMTAER_BRACELET),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your Flamtaer bracelet helps you build the temple quicker. It has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your flamtaer bracelet has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your Flamtaer bracelet helps you build the temple quicker. It then crumbles to dust.").fixedCharges(80).notification("Your flamtaer bracelet crumbles to dust."),
            new TriggerChatMessage("The bracelet shatters. Your next Flamtaer bracelet will star afresh from 80 charges.").fixedCharges(80)
        };
    }
}
