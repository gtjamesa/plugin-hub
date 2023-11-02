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

public class W_SanguinestiStaff extends ChargedItem {
    public W_SanguinestiStaff(
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
        super(ItemKey.SANGUINESTI_STAFF, ItemID.SANGUINESTI_STAFF, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.sanguinesti_staff;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.SANGUINESTI_STAFF),
            new TriggerItem(ItemID.SANGUINESTI_STAFF_UNCHARGED).fixedCharges(0),
            new TriggerItem(ItemID.HOLY_SANGUINESTI_STAFF),
            new TriggerItem(ItemID.HOLY_SANGUINESTI_STAFF_UNCHARGED).fixedCharges(0),
        };
        this.triggers = new TriggerBase[] {
            // Check.
            new OnChatMessage("Your (Holy s|S)anguinesti staff has (?<charges>.+) charges? remaining.").setDynamically(),

            // Charge partially full.
            new OnChatMessage("You apply an additional .+ charges? to your Sanguinesti staff. It now has (?<charges>.+) charges? in total.").setDynamically(),

            // Charge empty.
            new OnChatMessage("You apply (?<charges>.+) charges to your Sanguinesti staff.").setDynamically()
        };
    }
}
