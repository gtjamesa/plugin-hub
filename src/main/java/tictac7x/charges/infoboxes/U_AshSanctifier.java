package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedStatusItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.ItemStatus;
import tictac7x.charges.store.Store;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerStat;

public class U_AshSanctifier extends ChargedStatusItem {
    public U_AshSanctifier(
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
        super(ItemKey.ASH_SANCTIFIER, ItemID.ASH_SANCTIFIER, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, plugin);
        this.config_key = ChargesImprovedConfig.ash_sanctifier;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.ASH_SANCTIFIER),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            // Check
            new TriggerChatMessage("(The|Your) ash sanctifier has (?<charges>.+) charges?( left)?. It has been deactivated").extraConsumer(message -> deactivate()),
            new TriggerChatMessage("(The|Your) ash sanctifier has (?<charges>.+) charges?( left)?. It is active").extraConsumer(message -> activate()),
            // Activate
            new TriggerChatMessage("The ash sanctifier is active and ready to scatter ashes.").extraConsumer(message -> activate()),
            // Deactivate
            new TriggerChatMessage("The ash sanctifier has been deactivated, and will not scatter ashes now.").extraConsumer(message -> deactivate()),
        };
        this.triggers_stats = new TriggerStat[]{
            new TriggerStat(Skill.PRAYER).decreaseCharges(1).extraConfig(getConfigStatusKey(), ItemStatus.ACTIVATED),
        };
    }
}
