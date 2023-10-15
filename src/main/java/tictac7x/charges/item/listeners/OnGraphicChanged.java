package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.GraphicChanged;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerGraphic;

public class OnGraphicChanged {
    final ChargedItem chargedItem;
    final Client client;

    public OnGraphicChanged(final ChargedItem chargedItem, final Client client) {
        this.chargedItem = chargedItem;
        this.client = client;
    }

    public void trigger(final GraphicChanged event) {
        for (final TriggerGraphic trigger : chargedItem.triggersGraphics) {
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

    private boolean isValidTrigger(final GraphicChanged event, final TriggerGraphic trigger) {
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return false;

        // Valid animation id check.
        if (!event.getActor().hasSpotAnim(trigger.graphic_id)) return false;

        // Equipped check.
        if (trigger.equipped && !chargedItem.isEquipped()) return false;

        return true;
    }
}
