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
import tictac7x.charges.item.triggers.OnXpDrop;
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
            // Check.
            new OnChatMessage("The nature staff has (?<charges>.+) charges?.").setDynamically(),

            // Save a nature rune.
            new OnChatMessage("Your staff saved you a nature rune.").increaseCharges(1),

            // Charge.
            new OnChatMessage("Your Bryophyta's staff now has (?<charges>.+) charges?.").setDynamically(),

            // Regular spellbook.
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Bones to Bananas",
                "Low Level Alchemy",
                "Superheat Item",
                "High Level Alchemy"
            ).decreaseCharges(1),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Make sets").isMenuTarget(
                "Emerald bolts (e)"
            ).decreaseCharges(1),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Bind",
                "Bones to Peaches"
            ).decreaseCharges(2),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Snare"
            ).decreaseCharges(3),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Entangle"
            ).decreaseCharges(4),


            // Arcuus spellbook.
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Dark Lure",
                "Harmony Island Teleport"
            ).decreaseCharges(1),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Degrime",
                "Ward of Arceuus"
            ).decreaseCharges(2),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Reanimate").isMenuTarget(
                "Basic Reanimation"
            ).decreaseCharges(2),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Reanimate").isMenuTarget(
                "Adept Reanimation",
                "Expert Reanimation"
            ).decreaseCharges(3),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Reanimate").isMenuTarget(
                "Master Reanimation"
            ).decreaseCharges(4),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Resurrect").isMenuTarget(
                "Resurrect Crops"
            ).decreaseCharges(12),

            // Lunar spellbook.
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Tan Leather",
                "Plank Make",
                "Energy Transfer"
            ).decreaseCharges(1),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Spin Flax",
                "Fertile Soil"
            ).decreaseCharges(2),
            new OnXpDrop(Skill.MAGIC).isEquipped().isMenuOption("Cast").isMenuTarget(
                "Geomancy"
            ).decreaseCharges(3),
        };
    }
}
