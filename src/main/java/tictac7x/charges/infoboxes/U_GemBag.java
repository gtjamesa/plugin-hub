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
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.InventoryType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerItemContainer;

public class U_GemBag extends ChargedItem {
    public U_GemBag(
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
        super(ItemKey.GEM_BAG, ItemID.GEM_BAG, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store, plugin);
        this.config_key = ChargesImprovedConfig.gem_bag;
        this.zero_charges_is_positive = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.GEM_BAG_12020),
            new TriggerItem(ItemID.OPEN_GEM_BAG),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("The gem bag is empty.").fixedCharges(0),
            new TriggerChatMessage("Sapphires: (.+) / Emeralds: (.+) / Rubies: (.+) Diamonds: (.+) / Dragonstones: (.+)").multipleCharges(),
            new TriggerChatMessage("You just (found|mined) (a|an) (Sapphire|Ruby|Emerald|Diamond)").specificItem(ItemID.OPEN_GEM_BAG).increaseCharges(1),
        };
        this.triggers_item_containers = new TriggerItemContainer[]{
            new TriggerItemContainer(InventoryType.INVENTORY).menuTarget("Gem bag").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(InventoryType.INVENTORY).menuTarget("Open gem bag").menuOption("Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(InventoryType.BANK).menuTarget("Gem bag").menuOption("Empty").decreaseByBankDifference(),
            new TriggerItemContainer(InventoryType.BANK).menuTarget("Open gem bag").menuOption("Empty").decreaseByBankDifference(),
        };
    }
}
