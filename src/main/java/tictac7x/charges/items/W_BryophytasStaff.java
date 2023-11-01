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
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
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
        this.triggers = new TriggerBase[] {
            new OnChatMessage("The nature staff has (?<charges>.+) charges?.").setDynamically(),
            new OnChatMessage("Your staff saved you a nature rune.").increaseCharges(1),
            new OnChatMessage("Your Bryophyta's staff now has (?<charges>.+) charges?.").setDynamically()
        };
        // TODO
//        this.triggersGraphics = new TriggerGraphic[]{
//            new TriggerGraphic(112).decreaseCharges(1).equipped(),
//            new TriggerGraphic(113).decreaseCharges(1).equipped(),
//        };
//        this.triggersStats = new TriggerStat[]{
//            new TriggerStat(Skill.MAGIC).isEquipped().menuOption("Reanimate").menuTarget("Basic Reanimation", "Expert Reanimation").decreaseCharges(2),
//            new TriggerStat(Skill.MAGIC).isEquipped().menuOption("Reanimate").menuTarget("Adept Reanimation", "Expert Reanimation").decreaseCharges(3),
//            new TriggerStat(Skill.MAGIC).isEquipped().menuOption("Reanimate").menuTarget("Adept Reanimation", "Master Reanimation").decreaseCharges(4),
//            new TriggerStat(Skill.MAGIC).isEquipped().menuEntry("Cast", "Spin Flax").decreaseCharges(2),
//        };
    }
}
