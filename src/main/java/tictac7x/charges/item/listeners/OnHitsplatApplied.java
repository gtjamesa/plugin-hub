package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.Hitsplat;
import net.runelite.api.events.HitsplatApplied;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerHitsplat;

public class OnHitsplatApplied {
    final ChargedItem chargedItem;
    final Client client;

    public OnHitsplatApplied(final ChargedItem chargedItem, final Client client) {
        this.chargedItem = chargedItem;
        this.client = client;
    }

    public void trigger(final HitsplatApplied event) {
        final Hitsplat hitsplat = event.getHitsplat();

        // Check all hitsplat triggers.
        for (final TriggerHitsplat trigger : chargedItem.triggersHitsplats) {
            // Player check.
            if (trigger.self && event.getActor() != client.getLocalPlayer()) continue;

            // Enemy check.
            if (!trigger.self && (event.getActor() == client.getLocalPlayer() || hitsplat.isOthers())) continue;

            // Hitsplat type check.
            if (trigger.hitsplat_id != hitsplat.getHitsplatType()) continue;

            // Equipped check.
            if (trigger.equipped && !chargedItem.in_equipment) continue;

            // Non zero check.
            if (trigger.non_zero && hitsplat.getAmount() == 0) continue;

            // Valid hitsplat, modify charges.
            chargedItem.decreaseCharges(trigger.discharges);
        }
    }
}
