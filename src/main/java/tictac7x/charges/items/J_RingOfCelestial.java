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

public class J_RingOfCelestial extends ChargedItem {
    public J_RingOfCelestial(
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
        super(ItemKey.CELESTIAL_RING, ItemID.CELESTIAL_RING, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.celestial_ring;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.CELESTIAL_RING_UNCHARGED).fixedCharges(0),
            new TriggerItem(ItemID.CELESTIAL_RING).needsToBeEquipped()
        };
        this.triggers = new TriggerBase[] {
            new OnChatMessage("You add .+ charges? to your Celestial ring. It now has (?<charges>.+) charges?.").setDynamically(),
            new OnChatMessage("You add (?<charges>.+) charges? to your Celestial ring.").setDynamically(),
            new OnChatMessage("You fully uncharge your Celestial ring.").fixedCharges(0),
            new OnChatMessage("Your Celestial ring has (?<charges>.+) charges?.").setDynamically(),
            new OnChatMessage("Your Celestial ring has run out of charges").notification().fixedCharges(0),
                // TODO
//            new OnChatMessage("You manage to mine").ignore("You manage to mine some pay-dirt.").equipped().decreaseCharges(1)
        };
    }
}
