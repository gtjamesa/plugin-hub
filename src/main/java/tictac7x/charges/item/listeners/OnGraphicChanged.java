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
        // Player check.
        if (event.getActor() != client.getLocalPlayer()) return;

        // Check all animation triggers.
        for (final TriggerGraphic trigger_graphic : chargedItem.triggersGraphics) {
            // Valid animation id check.
            if (!event.getActor().hasSpotAnim(trigger_graphic.graphic_id)) continue;

            // Equipped check.
            if (trigger_graphic.equipped && !chargedItem.in_equipment) continue;

            // Valid trigger, modify charges.
            if (trigger_graphic.decrease_charges) {
                chargedItem.decreaseCharges(trigger_graphic.charges);
            } else {
                chargedItem.increaseCharges(trigger_graphic.charges);
            }
        }
    }
}
