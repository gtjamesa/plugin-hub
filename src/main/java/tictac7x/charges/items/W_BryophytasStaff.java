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
import tictac7x.charges.item.triggers.TriggerMenuOptionClicked;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerGraphic;
import tictac7x.charges.item.triggers.TriggerItem;

public class W_BryophytasStaff extends ChargedItem {
    public W_BryophytasStaff(
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
        super(ItemKey.BRYOPHYTAS_STAFF, ItemID.BRYOPHYTAS_STAFF, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.bryophytas_staff;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.BRYOPHYTAS_STAFF_UNCHARGED).fixedCharges(0),
            new TriggerItem(ItemID.BRYOPHYTAS_STAFF)
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("The nature staff has (?<charges>.+) charges?."),
            new TriggerChatMessage("Your staff saved you a nature rune.").increaseCharges(1),
            new TriggerChatMessage("Your Bryophyta's staff now has (?<charges>.+) charges?.")
        };
        this.triggersGraphics = new TriggerGraphic[]{
            new TriggerGraphic(112).decreaseCharges(1).equipped(),
            new TriggerGraphic(113).decreaseCharges(1).equipped(),
        };
        this.triggersMenuOptionClicked = new TriggerMenuOptionClicked[]{
            new TriggerMenuOptionClicked("Reanimate").target("Basic Reanimation").itemId(
                ItemID.ENSOULED_GOBLIN_HEAD_13448,
                ItemID.ENSOULED_MONKEY_HEAD_13451,
                ItemID.ENSOULED_MINOTAUR_HEAD_13457,
                ItemID.ENSOULED_SCORPION_HEAD_13460,
                ItemID.ENSOULED_BEAR_HEAD_13463
            ).equipped().decreaseCharges(3),
            new TriggerMenuOptionClicked("Reanimate").target("Adept Reanimation").itemId(
                ItemID.ENSOULED_DOG_HEAD_13469,
                ItemID.ENSOULED_CHAOS_DRUID_HEAD_13472,
                ItemID.ENSOULED_GIANT_HEAD_13475,
                ItemID.ENSOULED_ELF_HEAD_13481,
                ItemID.ENSOULED_HORROR_HEAD_13487
            ).equipped().decreaseCharges(3),
            new TriggerMenuOptionClicked("Reanimate").target("Expert Reanimation").itemId(
                ItemID.ENSOULED_KALPHITE_HEAD_13490,
                ItemID.ENSOULED_DAGANNOTH_HEAD_13493,
                ItemID.ENSOULED_BLOODVELD_HEAD_13496,
                ItemID.ENSOULED_TZHAAR_HEAD_13499,
                ItemID.ENSOULED_DEMON_HEAD_13502,
                ItemID.ENSOULED_HELLHOUND_HEAD_26997
            ).equipped().decreaseCharges(3),
            new TriggerMenuOptionClicked("Reanimate").target("Master Reanimation").itemId(
                ItemID.ENSOULED_AVIANSIE_HEAD_13505,
                ItemID.ENSOULED_ABYSSAL_HEAD_13508,
                ItemID.ENSOULED_DRAGON_HEAD_13511
            ).equipped().decreaseCharges(4),
        };
    }
}
