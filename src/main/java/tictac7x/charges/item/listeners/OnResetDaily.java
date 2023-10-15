package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerDailyReset;

public class OnResetDaily {
    final ChargedItem chargedItem;

    public OnResetDaily(final ChargedItem chargedItem) {
        this.chargedItem = chargedItem;
    }

    public void trigger() {
        for (final TriggerDailyReset trigger : chargedItem.triggersResetsDaily) {
            if (!isValidTrigger(trigger)) continue;

            chargedItem.setCharges(trigger.charges);
        }
    }
    
    private boolean isValidTrigger(final TriggerDailyReset trigger) {
        // Wrong item.
        if (trigger.specificItem.isPresent() && chargedItem.item_id != trigger.specificItem.get()) {
            return false;
        }

        return true;
    }
}
