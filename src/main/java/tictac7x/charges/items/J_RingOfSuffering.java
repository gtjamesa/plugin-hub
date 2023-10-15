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
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.ItemActivity;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerHitsplat;
import tictac7x.charges.item.triggers.TriggerItem;

public class J_RingOfSuffering extends ChargedItemWithStatus {
    public J_RingOfSuffering(
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
        super(ItemKey.RING_OF_SUFFERING, ItemID.RING_OF_SUFFERING, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.ring_of_suffering;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.RING_OF_SUFFERING).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_I).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_I_25246).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_I_26761).fixedCharges(0),
            new TriggerItem(ItemID.RING_OF_SUFFERING_R),
            new TriggerItem(ItemID.RING_OF_SUFFERING_RI),
            new TriggerItem(ItemID.RING_OF_SUFFERING_RI_25248),
            new TriggerItem(ItemID.RING_OF_SUFFERING_RI_26762),
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            // Check
            new TriggerChatMessage("Your ring currently has (?<charges>.+) recoil charges? remaining. The recoil effect is currently enabled.").onItemClick().activate(),
            new TriggerChatMessage("Your ring currently has (?<charges>.+) recoil charges? remaining. The recoil effect is currently disabled.").onItemClick().deactivate(),
            // Charge
            new TriggerChatMessage("You load your ring with .+ rings? of recoil. It now has (?<charges>.+) recoil charges."),
        };
        this.triggersHitsplats = new TriggerHitsplat[]{
            new TriggerHitsplat(1).equipped().onSelf().extraConfig(getConfigStatusKey().get(), ItemActivity.ACTIVATED),
        };
    }
}
