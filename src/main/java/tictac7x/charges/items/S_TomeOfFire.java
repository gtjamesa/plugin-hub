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
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnGraphicChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class S_TomeOfFire extends ChargedItem {
    public S_TomeOfFire(
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
        super(ItemKey.TOME_OF_FIRE, ItemID.TOME_OF_FIRE, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.tome_of_fire;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.TOME_OF_FIRE_EMPTY).fixedCharges(0),
            new TriggerItem(ItemID.TOME_OF_FIRE).needsToBeEquipped(),
        };

        this.triggers = new TriggerBase[] {
            // Check.
            new OnChatMessage("Your tome currently holds (?<charges>.+) charges?.").increaseDynamically().onItemClick(),

            // Attack with regular spellbook fire spells.
            new OnGraphicChanged(99, 126, 129, 155, 1464).isEquipped().decreaseCharges(1)
        };
    }
}
