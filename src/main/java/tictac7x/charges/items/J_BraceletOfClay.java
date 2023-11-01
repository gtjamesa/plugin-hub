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

public class J_BraceletOfClay extends ChargedItem {
    public J_BraceletOfClay(
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
        super(ItemKey.BRACELET_OF_CLAY, ItemID.BRACELET_OF_CLAY, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.bracelet_of_clay;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.BRACELET_OF_CLAY).needsToBeEquipped(),
        };
        this.triggers = new TriggerBase[] {
            new OnChatMessage("You can mine (?<charges>.+) more pieces? of soft clay before your bracelet crumbles to dust.").setDynamically(),
            new OnChatMessage("You manage to mine some( soft)? clay.").equipped().decreaseCharges(1),
            new OnChatMessage("Your bracelet of clay crumbles to dust.").fixedCharges(28).notification("Your clay bracelet crumbles to dust.")
        };
    }
}
