package tictac7x.charges.items;

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
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnXpDrop;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class U_AshSanctifier extends ChargedItemWithStatus {
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
        super(ChargesImprovedConfig.ash_sanctifier, ItemKey.ASH_SANCTIFIER, ItemID.ASH_SANCTIFIER, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.ASH_SANCTIFIER),
        };
        this.triggers = new TriggerBase[]{
            // Check.
            new OnChatMessage("(The|Your) ash sanctifier has (?<charges>.+) charges?( left)?. It has been deactivated").deactivate(),
            new OnChatMessage("(The|Your) ash sanctifier has (?<charges>.+) charges?( left)?. It is active").activate(),

            // Activate.
            new OnChatMessage("The ash sanctifier is active and ready to scatter ashes.").activate(),

            // Deactivate.
            new OnChatMessage("The ash sanctifier has been deactivated, and will not scatter ashes now.").deactivate(),

            // Automatic scatter.
            new OnXpDrop(Skill.PRAYER).isActivated().decreaseCharges(1),

            // Hide destroy.
            new OnMenuEntryAdded("Destroy").hide(),
        };
    }
}
