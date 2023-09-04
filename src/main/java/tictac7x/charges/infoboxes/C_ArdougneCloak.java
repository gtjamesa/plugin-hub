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
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerReset;

import static tictac7x.charges.ChargesImprovedPlugin.CHARGES_UNLIMITED;

public class C_ArdougneCloak extends ChargedItemInfoBox {
    public C_ArdougneCloak(
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
        super(ChargesItem.ARDOUGNE_CLOAK, ItemID.ARDOUGNE_CLOAK, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, plugin);
        this.config_key = ChargesImprovedConfig.ardougne_cloak;

        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.ARDOUGNE_CLOAK_2),
            new TriggerItem(ItemID.ARDOUGNE_CLOAK_3),
            new TriggerItem(ItemID.ARDOUGNE_CLOAK_4).fixedCharges(CHARGES_UNLIMITED)
        };

        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("You have used (?<used>.+) of your (?<total>.+) Ardougne Farm teleports for today.").useDifference()
        };

        this.triggers_resets = new TriggerReset[]{
            new TriggerReset(3).dynamicItem(ItemID.ARDOUGNE_CLOAK_2),
            new TriggerReset(5).dynamicItem(ItemID.ARDOUGNE_CLOAK_3),
        };
    }
}
