package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnVarbitChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ChargesItemID;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class J_RingOfExplorer extends ChargedItemWithStorage {
    public J_RingOfExplorer(
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
        super(ChargesImprovedConfig.explorers_ring, ItemKey.EXPLORERS_RING, ItemID.EXPLORERS_RING_4, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        storage = storage.storeableItems(
            new StorageItem(ChargesItemID.EXPLORER_RING_LOW_ALCHEMY).displayName("Low alchemies"),
            new StorageItem(ChargesItemID.EXPLORER_RING_HIGH_ALCHEMY).displayName("High alchemies"),
            new StorageItem(ChargesItemID.EXPLORER_RING_TELEPORTS).displayName("Teleports"),
            new StorageItem(ChargesItemID.EXPLORER_RING_RESTORES).displayName("Energy restores")
        ).showIndividualCharges();

        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.EXPLORERS_RING_4),
        };

        this.triggers = new TriggerBase[]{
            // Check with all available.
            new OnChatMessage("You have (?<alchemy>.+) out of 30 alchemy charges left and (?<restores>.+) of your 3 energy restores left for today.").consumer(m -> {
                final int alchemy = Integer.parseInt(m.group("alchemy"));
                final int restores = Integer.parseInt(m.group("restores"));

                storage.empty();
                storage.put(ChargesItemID.EXPLORER_RING_LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ChargesItemID.EXPLORER_RING_HIGH_ALCHEMY, alchemy);
                storage.put(ChargesItemID.EXPLORER_RING_RESTORES, restores);
                storage.put(ChargesItemID.EXPLORER_RING_TELEPORTS, Charges.UNLIMITED);
            }).onItemClick(),

            // Check with restores used.
            new OnChatMessage("You have (?<alchemy>.+) out of 30 alchemy charges left and have exhausted the ring's run restore power for today.").consumer(m -> {
                final int alchemy = Integer.parseInt(m.group("alchemy"));

                storage.empty();
                storage.put(ChargesItemID.EXPLORER_RING_LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ChargesItemID.EXPLORER_RING_TELEPORTS, Charges.UNLIMITED);
                storage.put(ChargesItemID.EXPLORER_RING_HIGH_ALCHEMY, alchemy);
                storage.put(ChargesItemID.EXPLORER_RING_RESTORES, 0);
            }).onItemClick(),

            // Check with none available.
            new OnChatMessage("You have no alchemy charges left and have exhausted the ring's run restore power for today.").onItemClick().consumer(() -> {
                storage.put(ChargesItemID.EXPLORER_RING_LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ChargesItemID.EXPLORER_RING_TELEPORTS, Charges.UNLIMITED);
                storage.put(ChargesItemID.EXPLORER_RING_HIGH_ALCHEMY, 0);
                storage.put(ChargesItemID.EXPLORER_RING_RESTORES, 0);
            }),

            // Run energy used.
            new OnChatMessage("You have used (?<used>.+) of your (?<total>.+) restores for today.").consumer(m -> {
                final int total = Integer.parseInt(m.group("total"));
                final int used = Integer.parseInt(m.group("used"));
                storage.put(ChargesItemID.EXPLORER_RING_RESTORES, total - used);
            }).onItemClick(),

            // Try to use restores without any left.
            new OnChatMessage("You have exhausted the ring's run restore power for today.").consumer(m -> {
                storage.put(ChargesItemID.EXPLORER_RING_RESTORES, 0);
            }).onItemClick(),

            // Alchemy.
            new OnVarbitChanged(Varbits.EXPLORER_RING_ALCHS).varbitValueConsumer(alchs -> {
                storage.remove(ChargesItemID.EXPLORER_RING_LOW_ALCHEMY, 1);
                storage.remove(ChargesItemID.EXPLORER_RING_HIGH_ALCHEMY, 1);
            }),
        };
    }
}
