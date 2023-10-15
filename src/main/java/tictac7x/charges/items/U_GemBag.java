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
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;

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
        super(ItemKey.GEM_BAG, ItemID.GEM_BAG, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.gem_bag;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.GEM_BAG_12020).zeroChargesIsPositive(),
            new TriggerItem(ItemID.OPEN_GEM_BAG).zeroChargesIsPositive(),
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("The gem bag is empty.").fixedCharges(0),
            new TriggerChatMessage("Sapphires: (.+) / Emeralds: (.+) / Rubies: (.+) Diamonds: (.+) / Dragonstones: (.+)").multipleCharges(),
            new TriggerChatMessage("You just (found|mined) (a|an) (Sapphire|Ruby|Emerald|Diamond)").specificItem(ItemID.OPEN_GEM_BAG).increaseCharges(1),
        };
        this.triggersItemContainers = new TriggerItemContainer[]{
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Gem bag", "Fill").menuEntry("Open gem bag", "Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.BANK).menuEntry("Gem bag", "Empty").menuEntry("Open gem bag", "Empty").decreaseByBankDifference(),
        };
    }
}
