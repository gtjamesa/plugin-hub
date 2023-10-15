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
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerAnimation;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;

public class J_RingOfShadows extends ChargedItem {
    public J_RingOfShadows(
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
        super(ItemKey.RING_OF_SHADOWS, ItemID.RING_OF_SHADOWS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.ring_of_shadows;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.RING_OF_SHADOWS_UNCHARGED).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SHADOWS)
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your ring of shadows has (?<charges>.+) charges? remaining."),
            new TriggerChatMessage("You add (?<charges>.+) charges? to the ring of shadows.$"),
            new TriggerChatMessage("You add .+ charges? to the ring of shadows. It now has (?<charges>.+) charges?."),
        };
        this.triggersAnimations = new TriggerAnimation[]{
            new TriggerAnimation(10134).decreaseCharges(1),
        };
    }
}
