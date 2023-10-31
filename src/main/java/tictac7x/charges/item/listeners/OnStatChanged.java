package tictac7x.charges.item.listeners;

import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerStat;
import tictac7x.charges.store.Store;

public class OnStatChanged {
    private final ChargedItem chargedItem;
    private final Store store;

    public OnStatChanged(final ChargedItem chargedItem) {
        this.chargedItem = chargedItem;
        this.store = chargedItem.store;
    }

    public void trigger(final StatChanged event) {
        for (final TriggerStat trigger : chargedItem.triggersStats) {
            if (!isValidTrigger(event, trigger)) continue;

            // Increase charges.
            if (trigger.increaseCharges.isPresent()) {
                chargedItem.increaseCharges(trigger.increaseCharges.get());

            // Decrease charges.
            } else if (trigger.decreaseCharges.isPresent()) {
                chargedItem.decreaseCharges(trigger.decreaseCharges.get());
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final StatChanged event, final TriggerStat trigger) {
        // Skill check.
        if (trigger.skill != event.getSkill()) return false;

        // XP drop check.
        if ((!chargedItem.store.getSkillExperience(trigger.skill).isPresent() || event.getXp() == chargedItem.store.getSkillExperience(trigger.skill).get())) return false;

        // Activated check.
        if (trigger.isActivated && !chargedItem.isActivated()) return false;

//        // Menu option check.
//        if (trigger.menuEntry.isPresent() && chargedItem.store.notInMenuEntries(trigger.menuEntry.get())) return false;

        // Specific item check.
        if (!trigger.specificItems.isEmpty() && !trigger.specificItems.contains(chargedItem.item_id)) return false;

        // Below charges check.
        if (trigger.belowCharges.isPresent() && chargedItem.getCharges() >= trigger.belowCharges.get()) return false;

        // Is equipped check.
        if (trigger.isEquipped.isPresent() && !store.equipmentContainsItem(chargedItem.item_id)) return false;

        return true;
    }
}
