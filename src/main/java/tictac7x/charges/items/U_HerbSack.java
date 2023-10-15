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
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerStat;
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;

public class U_HerbSack extends ChargedItem {
    public U_HerbSack(
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
        super(ItemKey.HERB_SACK, ItemID.HERB_SACK, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.herb_sack;
        this.zero_charges_is_positive = true;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.HERB_SACK),
            new TriggerItem(ItemID.OPEN_HERB_SACK),
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("The herb sack is empty.").fixedCharges(0),
            new TriggerChatMessage("You put the Grimy .* herb into your herb sack.").increaseCharges(1),
            new TriggerChatMessage("You look in your herb sack and see:").fixedCharges(0),
            new TriggerChatMessage("(?<charges>.+) x Grimy").increaseDynamically(),
        };
        this.triggersItemContainers = new TriggerItemContainer[]{
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Herb sack", "Fill").menuEntry("Open herb sack", "Fill").increaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Herb sack", "Empty").menuEntry("Open herb sack", "Empty").decreaseByInventoryDifference(),
            new TriggerItemContainer(ItemContainerType.BANK).menuEntry("Herb sack", "Empty").menuEntry("Open herb sack", "Empty").decreaseByBankDifference(),
            // Edge case where herb sack is open, but there is not enough space for it in herb sack.
            new TriggerItemContainer(ItemContainerType.INVENTORY).menuEntry("Herbs", "Pick").specificItem(ItemID.OPEN_HERB_SACK).decreaseCharges(1),
        };
        this.triggersStat = new TriggerStat[]{
            new TriggerStat(Skill.FARMING).specificItem(ItemID.OPEN_HERB_SACK).menuEntry("Herbs", "Pick").increaseCharges(1),
        };
    }
}
