package tictac7x.charges.items;

import com.google.gson.Gson;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorageMultipleCharges;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.*;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

class ExplorersRingStorageItemId {
    public static final int TELEPORTS = -1000;
    public static final int LOW_ALCHEMY = -1001;
    public static final int HIGH_ALCHEMY = -1002;
    public static final int ENERGY_RESTORES = -1003;
}

public class J_RingOfExplorer extends ChargedItemWithStorageMultipleCharges {
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
        final Gson gson
    ) {
        super(ChargesImprovedConfig.explorers_ring, ItemID.EXPLORERS_RING_1, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);
        storage = storage.storeableItems(
            new StorageItem(ExplorersRingStorageItemId.LOW_ALCHEMY).displayName("Low alchemies"),
            new StorageItem(ExplorersRingStorageItemId.HIGH_ALCHEMY).displayName("High alchemies"),
            new StorageItem(ExplorersRingStorageItemId.TELEPORTS).displayName("Teleports"),
            new StorageItem(ExplorersRingStorageItemId.ENERGY_RESTORES).displayName("Energy restores")
        ).showIndividualCharges();

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.EXPLORERS_RING_1),
            new TriggerItem(ItemID.EXPLORERS_RING_2),
            new TriggerItem(ItemID.EXPLORERS_RING_3),
            new TriggerItem(ItemID.EXPLORERS_RING_4),
        };

        this.triggers = new TriggerBase[]{
            // Check with all available.
            new OnChatMessage("You have (?<alchemy>.+) out of 30 alchemy charges left and (?<restores>.+) of your 3 energy restores left for today.").matcherConsumer(m -> {
                final int alchemy = Integer.parseInt(m.group("alchemy"));
                final int restores = Integer.parseInt(m.group("restores"));

                storage.empty();
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, alchemy);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, restores);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, Charges.UNLIMITED);
            }).onItemClick(),

            // Check with all available.
            new OnChatMessage("You have (?<alchemy>.+) out of 30 alchemy charges left and (?<restores>.+) of your 3 energy restores left for today.").matcherConsumer(m -> {
                final int alchemy = Integer.parseInt(m.group("alchemy"));
                final int restores = Integer.parseInt(m.group("restores"));

                storage.empty();
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, alchemy);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, restores);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, Charges.UNLIMITED);
            }).onItemClick(),

            // Check with restores used.
            new OnChatMessage("You have (?<alchemy>.+) out of 30 alchemy charges left and have exhausted the ring's run restore power for today.").matcherConsumer(m -> {
                final int alchemy = Integer.parseInt(m.group("alchemy"));

                storage.empty();
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, Charges.UNLIMITED);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, alchemy);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 0);
            }).onItemClick(),

            // Check with none available.
            new OnChatMessage("You have no alchemy charges left and have exhausted the ring's run restore power for today.").onItemClick().consumer(() -> {
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, Charges.UNLIMITED);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, Charges.UNLIMITED);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, 0);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 0);
            }),

            // Run energy used.
            new OnChatMessage("You have used (?<used>.+) of your (?<total>.+) restores for today.").matcherConsumer(m -> {
                final int total = Integer.parseInt(m.group("total"));
                final int used = Integer.parseInt(m.group("used"));
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, total - used);
            }).onItemClick(),

            // Try to use restores without any left.
            new OnChatMessage("You have exhausted the ring's run restore power for today.").matcherConsumer(m -> {
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 0);
            }).onItemClick(),

            // Alchemy.
            new OnVarbitChanged(Varbits.EXPLORER_RING_ALCHS).varbitValueConsumer(alchs -> {
                storage.remove(ExplorersRingStorageItemId.LOW_ALCHEMY, 1);
                storage.remove(ExplorersRingStorageItemId.HIGH_ALCHEMY, 1);
            }),

            // Teleport.
            new OnChatMessage("You have used (?<used>.+) of your (?<total>.+) Cabbage teleports for today.").matcherConsumer(m -> {
                final int total = Integer.parseInt(m.group("total"));
                final int used = Integer.parseInt(m.group("used"));
                storage.put(ExplorersRingStorageItemId.TELEPORTS, total - used);
            }).onItemClick(),

            new OnResetDaily().specificItem(ItemID.EXPLORERS_RING_1).consumer(() -> {
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, 30);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, 0);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 2);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, 0);
            }),

            new OnResetDaily().specificItem(ItemID.EXPLORERS_RING_2).consumer(() -> {
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, 30);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 3);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, 0);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, 3);
            }),

            new OnResetDaily().specificItem(ItemID.EXPLORERS_RING_3).consumer(() -> {
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, 30);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, 0);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 4);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, Charges.UNLIMITED);
            }),

            new OnResetDaily().specificItem(ItemID.EXPLORERS_RING_4).consumer(() -> {
                storage.put(ExplorersRingStorageItemId.LOW_ALCHEMY, 30);
                storage.put(ExplorersRingStorageItemId.HIGH_ALCHEMY, 30);
                storage.put(ExplorersRingStorageItemId.ENERGY_RESTORES, 3);
                storage.put(ExplorersRingStorageItemId.TELEPORTS, Charges.UNLIMITED);
            }),
        };
    }
}
