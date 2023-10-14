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
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return;

        // Check all animation triggers.
        animationTriggerLooper: for (final TriggerAnimation trigger_animation : chargedItem.triggersAnimations) {
            // Valid animation id check.
            if (trigger_animation.animation_id != event.getActor().getAnimation()) continue;

            // Unallowed items check.
            if (trigger_animation.unallowed_items != null) {
                for (final int item_id : trigger_animation.unallowed_items) {
                    if (chargedItem.store.inventory.contains(item_id) || chargedItem.store.equipment.contains(item_id)) {
                        continue animationTriggerLooper;
                    }
                }
            }

            // Equipped check.
            if (trigger_animation.equipped && !chargedItem.in_equipment) continue;

            // Menu target check.
            if (trigger_animation.menu_target != null && !chargedItem.store.inMenuTargets(trigger_animation.menu_target)) continue;

            // Menu option check.
            if (trigger_animation.menu_option != null && !chargedItem.store.inMenuOptions(trigger_animation.menu_option)) continue;

            // Valid trigger, modify charges.
            if (trigger_animation.decrease_charges) {
                chargedItem.decreaseCharges(trigger_animation.charges);
            } else {
                chargedItem.increaseCharges(trigger_animation.charges);
            }

            return;
        }
    }
}
