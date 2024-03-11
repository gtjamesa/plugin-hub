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
import tictac7x.charges.item.triggers.*;
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

            // Uncharge.
            new OnChatMessage("You uncharge your Ring of the Elements.").fixedCharges(0),

            // Teleport.
            new OnChatMessage("Your ring of elements has (?<charges>.+) charge remaining.").setDynamically(),

            // Check from uncharge dialog not near bank.
            new OnChatMessage("The Ring of the Elements has (?<charges>.+) charges?. You can uncharge it at a bank.").setDynamically(),

            // Check from uncharge dialog at bank.
            new OnWidgetLoaded(219, 1, 0, "Ring of the Elements charges: (?<charges>.+). Uncharge it?").setDynamically(),

            // Teleport to air/water/earth/fire altars.
            new OnGraphicChanged(2060, 2061, 2062, 2063).onItemClick().decreaseCharges(1),
        };
    }
}