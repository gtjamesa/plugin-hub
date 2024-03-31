package tictac7x.charges.items;

import com.google.gson.Gson;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class J_Camulet extends ChargedItem {
    public J_Camulet(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Gson gson
    ) {
        super(ChargesImprovedConfig.camulet, ItemKey.CAMULET, ItemID.CAMULET, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.CAMULET),
        };

        this.triggers = new TriggerBase[] {
            // Check.
            new OnChatMessage("Your Camulet has (?<charges>.+) charges? left.").setDynamically(),

            // Recharge.
            new OnChatMessage("You recharge the Camulet using camel dung. Yuck!").fixedCharges(4),

            // Trying to charge fully charged.
            new OnChatMessage("The Camulet is already fully charged.").fixedCharges(4),

            // Unlimited charges.
            new OnChatMessage("The Camulet has unlimited charges.").fixedCharges(Charges.UNLIMITED),

            // Replace check.
            new OnMenuEntryAdded("Check-charge").replaceOption("Check"),
        };
    }
}
