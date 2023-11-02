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
import tictac7x.charges.item.triggers.OnHitsplatApplied;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.HitsplatTarget;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

public class S_CrystalShield extends ChargedItem {
    public S_CrystalShield(
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
        super(ItemKey.CRYSTAL_SHIELD, ItemID.CRYSTAL_SHIELD, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.crystal_shield;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.CRYSTAL_SHIELD),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_24127),
        };
        this.triggers = new TriggerBase[] {
            // Check.
            new OnChatMessage("Your crystal shield has (?<charges>.+) charges? remaining.").setDynamically(),

            // Get hit.
            new OnHitsplatApplied(HitsplatTarget.SELF).moreThanZeroDamage().isEquipped().decreaseCharges(1)
        };
    }
}
