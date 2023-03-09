package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
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
import tictac7x.charges.triggers.TriggerWidget;

public class J_ExpeditiousBracelet extends ChargedItemInfoBox {
    public J_ExpeditiousBracelet(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.EXPEDITIOUS_BRACELET, ItemID.EXPEDITIOUS_BRACELET, client, client_thread, configs, items, infoboxes, chat_messages, config, plugin);
        this.config_key = ChargesImprovedConfig.expeditious_bracelet;
        this.needs_to_be_equipped_for_infobox = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.EXPEDITIOUS_BRACELET),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your expeditious bracelet has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your expeditious bracelet helps you progress your slayer( task)? faster. It has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your expeditious bracelet helps you progress your slayer faster. It then crumbles to dust.").fixedCharges(30)
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("The bracelet shatters. Your next expeditious bracelet will start afresh from (?<charges>.+) charges.")
        };
    }
}
