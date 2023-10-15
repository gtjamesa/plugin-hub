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
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerGraphic;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerDailyReset;

public class S_FaladorShield extends ChargedItem {
    public S_FaladorShield(
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
        super(ItemKey.FALADOR_SHIELD, ItemID.FALADOR_SHIELD, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.falador_shield;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.FALADOR_SHIELD_3),
            new TriggerItem(ItemID.FALADOR_SHIELD_4),
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("You have one remaining charge for today.").onItemClick().fixedCharges(1),
            new TriggerChatMessage("You have two remaining charges for today.").onItemClick().fixedCharges(2),
            new TriggerChatMessage("You have already used (both )?your charge(s)? for today.").onItemClick().fixedCharges(0),
            new TriggerChatMessage("You have already used all available recharges today. Try again tomorrow when the shield has recharged.").onItemClick().fixedCharges(0)
        };
        this.triggersGraphics = new TriggerGraphic[]{
            new TriggerGraphic(321).decreaseCharges(1)
        };
        this.triggersResetsDaily = new TriggerDailyReset[]{
            new TriggerDailyReset(1).specificItem(ItemID.FALADOR_SHIELD_3),
            new TriggerDailyReset(2).specificItem(ItemID.FALADOR_SHIELD_4)
        };
    }
}
