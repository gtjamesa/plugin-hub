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

public class H_CircletOfWater extends ChargedItem {
    public H_CircletOfWater(
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
        super(ChargesImprovedConfig.circlet_of_water, ItemKey.CIRCLET_OF_WATER, ItemID.CIRCLET_OF_WATER, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.CIRCLET_OF_WATER_UNCHARGED).fixedCharges(0),
            new TriggerItem(ItemID.CIRCLET_OF_WATER).needsToBeEquipped(),
        };

        this.triggers = new TriggerBase[] {
            // Protect from heat.
            new OnChatMessage("Your circlet protects you from the desert heat.").decreaseCharges(1),

            // Check.
            new OnChatMessage("Your circlet has (?<charges>.+) charges? left.").setDynamically(),

            // Charge while empty.
            new OnChatMessage("You add (?<charges>.+) charges? to your circlet.$").setDynamically(),

            // Charge while not empty.
            new OnChatMessage("You add .+ charges? to your circlet. It now has (?<charges>.+) charges?.").setDynamically(),
        };
    }
}
