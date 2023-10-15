package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.Hitsplat;
import net.runelite.api.HitsplatID;
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
        for (final TriggerHitsplat trigger : chargedItem.triggersHitsplats) {
            if (!isValidTrigger(event, trigger)) continue;

            chargedItem.decreaseCharges(trigger.discharges);

            // Trigger used.
            return;
        }
    }
    
    private boolean isValidTrigger(final HitsplatApplied event, final TriggerHitsplat trigger) {
        final Hitsplat hitsplat = event.getHitsplat();

        // Player check.
        if (trigger.self && event.getActor() != client.getLocalPlayer()) return false;

        // Enemy check.
        if (!trigger.self && (event.getActor() == client.getLocalPlayer() || hitsplat.isOthers())) return false;

        // Hitsplat id check.
        if (hitsplat.getHitsplatType() != HitsplatID.DAMAGE_ME) return false;

        // Equipped check.
        if (trigger.equipped && !chargedItem.in_equipment) return false;

        // Non zero check.
        if (trigger.non_zero && hitsplat.getAmount() == 0) return false;

        // Activated check.
        if (trigger.isActivated && !chargedItem.isActivated()) return false;

        return true;
    }
}
