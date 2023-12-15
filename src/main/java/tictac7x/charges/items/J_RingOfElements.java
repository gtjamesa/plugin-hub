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
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class J_RingOfElements extends ChargedItem {
    public J_RingOfElements(
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
        super(ChargesImprovedConfig.ring_of_elements, ItemKey.RING_OF_ELEMENTS, ItemID.RING_OF_THE_ELEMENTS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.RING_OF_THE_ELEMENTS).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_THE_ELEMENTS_26818),
        };

        this.triggers = new TriggerBase[] {
            // Check uncharged.
            new OnChatMessage("Your ring of the elements has no charges remaining.").fixedCharges(0),

            // Charge by one.
            new OnChatMessage("You charge your Ring of the Elements").fixedCharges(1),

            // Teleport.
            new OnChatMessage("Your ring of elements has (?<charges>.+) charge remaining.").setDynamically(),
        };
    }
}