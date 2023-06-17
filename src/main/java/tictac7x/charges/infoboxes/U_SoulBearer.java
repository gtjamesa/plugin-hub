package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesItem;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerWidget;

import javax.annotation.Nullable;

public class U_SoulBearer extends ChargedItemInfoBox {
    public U_SoulBearer(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.SOUL_BEARER, ItemID.SOUL_BEARER, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, plugin);
        this.config_key = ChargesImprovedConfig.soul_bearer;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.SOUL_BEARER),
            new TriggerItem(ItemID.DAMAGED_SOUL_BEARER).fixedCharges(0),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("The soul bearer has (?<charges>.+) charges?."),
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("Your soul bearer carries the ensouled heads to your bank. It has (?<charges>.+) charges? left.")
        };
    }
}
