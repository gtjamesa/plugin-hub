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

public class J_XericsTalisman extends ChargedItem {
    public J_XericsTalisman(
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
        super(ItemKey.XERICS_TALISMAN, ItemID.XERICS_TALISMAN, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.xerics_talisman;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.XERICS_TALISMAN_INERT).fixedCharges(0),
            new TriggerItem(ItemID.XERICS_TALISMAN),
        };
        this.triggers = new TriggerBase[]{
            new OnChatMessage("The talisman has one charge.").onItemClick(),
            new OnChatMessage("The talisman has (?<charges>.+) charges.").onItemClick(),
            new OnChatMessage("Your talisman now has one charge.").onItemClick(),
            new OnChatMessage("Your talisman now has (?<charges>.+) charges?.").onItemClick(),
        };

        // TODO
//        this.triggersAnimations = new TriggerAnimation[]{
//            new TriggerAnimation(3865).decreaseCharges(1)
//        };
//        this.triggersWidgets = new TriggerWidget[]{
//            new TriggerWidget(187, 0, 1, "The talisman has (?<charges>.+) charges.")
//        };
    }
}
