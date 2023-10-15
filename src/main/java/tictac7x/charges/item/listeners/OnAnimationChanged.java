package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerAnimation;

public class OnAnimationChanged {
    final ChargedItem chargedItem;
    final Client client;

    public OnAnimationChanged(final ChargedItem chargedItem, final Client client) {
        this.chargedItem = chargedItem;
        this.client = client;
    }

    public void trigger(final AnimationChanged event) {
        for (final TriggerAnimation trigger : chargedItem.triggersAnimations) {
            if (!isValidTrigger(event, trigger)) continue;

            if (trigger.decrease_charges) {
                chargedItem.decreaseCharges(trigger.charges);
            } else {
                chargedItem.increaseCharges(trigger.charges);
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final AnimationChanged event, final TriggerAnimation trigger) {
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return false;

        // Valid animation id check.
        if (trigger.animation_id != event.getActor().getAnimation()) return false;

        // Unallowed items check.
        if (trigger.unallowed_items != null) {
            for (final int item_id : trigger.unallowed_items) {
                if (chargedItem.store.inventoryContainsItem(item_id) || chargedItem.store.equipmentContainsItem(item_id)) {
                    return false;
                }
            }
        }

        // Equipped check.
        if (trigger.equipped && !chargedItem.in_equipment) return false;

        // Menu target check.
        if (trigger.menu_target != null && chargedItem.store.notInMenuTargets(trigger.menu_target)) return false;

        // Menu option check.
        if (trigger.menu_option != null && chargedItem.store.notInMenuOptions(trigger.menu_option)) return false;

        return true;
    }
}
