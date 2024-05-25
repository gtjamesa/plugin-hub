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
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.Storage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnWidgetLoaded;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.Store;

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
            // Check.
            new OnWidgetLoaded(823,21,10).itemQuantityConsumer(quantity -> storage.put(ItemID.ANIMAINFUSED_BARK, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,22).itemQuantityConsumer(quantity -> storage.put(ItemID.NATURE_OFFERINGS, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,34).itemQuantityConsumer(quantity -> storage.put(ItemID.FORESTERS_RATION, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,46).itemQuantityConsumer(quantity -> storage.put(ItemID.SECATEURS_ATTACHMENT, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,58).itemQuantityConsumer(quantity -> storage.put(ItemID.LEAVES, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,70).itemQuantityConsumer(quantity -> storage.put(ItemID.OAK_LEAVES, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,82).itemQuantityConsumer(quantity -> storage.put(ItemID.WILLOW_LEAVES, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,94).itemQuantityConsumer(quantity -> storage.put(ItemID.MAPLE_LEAVES, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,106).itemQuantityConsumer(quantity -> storage.put(ItemID.YEW_LEAVES, quantity)).multiTrigger(),
            new OnWidgetLoaded(823,21,118).itemQuantityConsumer(quantity -> storage.put(ItemID.MAGIC_LEAVES, quantity)).multiTrigger(),

            // Get leaves while chopping wood.
            new OnChatMessage("Some (?<leaves>.+) leaves fall to the ground and you place them into your Forestry kit.").matcherConsumer(m -> {
                storage.add(getStorageItemFromName(m.group("leaves")), 1);
            }),

            // Get bark from an event.
            new OnChatMessage("You've been awarded (?<bark>.+) Anima-infused bark.").matcherConsumer(m -> {
                storage.add(ItemID.ANIMAINFUSED_BARK, Integer.parseInt(m.group("bark")));
            }),

            new OnMenuEntryAdded("Destroy").hide(),
        };
    }
}
