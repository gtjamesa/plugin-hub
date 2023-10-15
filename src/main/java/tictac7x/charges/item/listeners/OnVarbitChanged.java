package tictac7x.charges.item.listeners;

import net.runelite.api.events.VarbitChanged;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.triggers.TriggerVarbit;

public class OnVarbitChanged {
    final ChargedItemWithStatus chargedItem;

    public OnVarbitChanged(final ChargedItemWithStatus chargedItem) {
        this.chargedItem = chargedItem;
    }

    public void trigger(final VarbitChanged event) {
        for (final TriggerVarbit trigger : chargedItem.triggersVarbits) {
            if (!isValidTrigger(event, trigger)) continue;

            // Activate item.
            if (trigger.activate) {
                chargedItem.activate();

            // Deactivate item.
            } else if (trigger.deactivate) {
                chargedItem.deactivate();
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final VarbitChanged event, final TriggerVarbit trigger) {
        // Wrong varbit id.
        if (event.getVarbitId() != trigger.varbitId) return false;

        // Wrong varbit value.
        if (event.getValue() != trigger.varbitValue) return false;

        return true;
    }
}
