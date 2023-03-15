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

public class S_BookOfTheDead extends ChargedItemInfoBox {
    public S_BookOfTheDead(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.BOOK_OF_THE_DEAD, ItemID.BOOK_OF_THE_DEAD, client, client_thread, configs, items, infoboxes, chat_messages, config, plugin);
        this.config_key = ChargesImprovedConfig.book_of_the_dead;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.KHAREDSTS_MEMOIRS),
            new TriggerItem(ItemID.BOOK_OF_THE_DEAD)
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("The Book of the Dead now has (?<charges>.+) (memories|memory) remaining."),
            new TriggerChatMessage("The Book of the Dead holds no charges.").fixedCharges(0)
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("On the inside of the cover a message is displayed in dark ink. It reads: (?<charges>.+) memories? remain."),
            new TriggerWidget("The Book of the Dead now has (?<charges>.+) charges.")
        };
    }
}
