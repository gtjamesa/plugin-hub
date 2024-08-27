package tictac7x.charges.items;

import com.google.gson.Gson;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnWidgetLoaded;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemContainerId;
import tictac7x.charges.store.Store;

import static tictac7x.charges.store.ItemContainerId.INVENTORY;

public class C_ForestryKit extends ChargedItemWithStorage {
    public C_ForestryKit(
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
        super(ChargesImprovedConfig.forestry_kit, ItemID.FORESTRY_KIT, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, gson);

        this.storage = storage.storeableItems(
            new StorageItem(ItemID.ANIMAINFUSED_BARK),
            new StorageItem(ItemID.NATURE_OFFERINGS),
            new StorageItem(ItemID.FORESTERS_RATION),
            new StorageItem(ItemID.SECATEURS_ATTACHMENT),
            new StorageItem(ItemID.LEAVES).displayName("Regular leaves").checkName("regular"),
            new StorageItem(ItemID.OAK_LEAVES).checkName("oak"),
            new StorageItem(ItemID.WILLOW_LEAVES).checkName("willow"),
            new StorageItem(ItemID.MAPLE_LEAVES).checkName("maple"),
            new StorageItem(ItemID.YEW_LEAVES).checkName("yew"),
            new StorageItem(ItemID.MAGIC_LEAVES).checkName("magic")
        );

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.FORESTRY_KIT),
        };

        this.triggers = new TriggerBase[]{
            // View contents.
            new OnItemContainerChanged(ItemContainerId.FORESTRY_KIT).updateStorage(),

            // Get leaves while chopping wood.
            new OnChatMessage("Some (?<leaves>.+) leaves fall to the ground and you place them into your Forestry kit.").matcherConsumer(m -> {
                storage.add(getStorageItemFromName(m.group("leaves")), 1);
            }),

            // Get leaves from event.
            new OnChatMessage("You've been awarded (?<amount>.+) piles of (?<leaves>.+) leaves which you put into your Forestry kit.").matcherConsumer(m -> {
                storage.add(getStorageItemFromName(m.group("leaves")), Integer.parseInt(m.group("amount")));
            }),

            // Get bark from an event.
            new OnChatMessage("You've been awarded (?<bark>.+) Anima-infused bark.").matcherConsumer(m -> {
                storage.add(ItemID.ANIMAINFUSED_BARK, Integer.parseInt(m.group("bark")));
            }),

            // Use ration when choping.
            new OnChatMessage("You consume a Forester's ration to fuel a mighty chop.").consumer(() -> {
                storage.remove(ItemID.FORESTERS_RATION, 1);
            }),

            // Out of rations.
            new OnChatMessage("You've eaten your last Forester's ration.").consumer(() -> {
                storage.put(ItemID.FORESTERS_RATION, 0);
            }),

            // Fill from inventory.
            new OnItemContainerChanged(INVENTORY).fillStorageFromInventoryAll().onItemClick().onMenuOption("Fill"),

            new OnMenuEntryAdded("Destroy").hide(),
        };
    }
}
