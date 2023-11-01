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

public class U_SeedBox extends ChargedItem {
    public U_SeedBox(
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
        super(ItemKey.SEED_BOX, ItemID.SEED_BOX, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.seed_box;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.SEED_BOX).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_SEED_BOX).zeroChargesIsPositive(),
        };
        this.triggers = new TriggerBase[] {
            new OnChatMessage("(The|Your) seed box is( now| already)? empty.").fixedCharges(0),
            new OnChatMessage("Stored (?<charges>.+) x .* seed in your seed box.").increaseDynamically(),
            new OnChatMessage("You put (?<charges>.+) x .* seed straight into your open seed box.").increaseDynamically(),
            new OnChatMessage("Emptied (?<charges>.+) x .* seed to your inventory.").decreaseDynamically(),
            new OnChatMessage("The seed box contains:").fixedCharges(0),
            new OnChatMessage("(?<charges>.+) x .* seed.").increaseDynamically(),
        };

        // TODO
//        this.triggersMenusEntriesAdded = new TriggerMenuEntryAdded[]{
//            new TriggerMenuEntryAdded("Destroy").hide(),
//        };
    }
}
