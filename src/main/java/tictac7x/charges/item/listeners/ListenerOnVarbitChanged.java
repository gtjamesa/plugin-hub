package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.item.triggers.OnVarbitChanged;
import tictac7x.charges.item.triggers.TriggerBase;

public class ListenerOnVarbitChanged extends ListenerBase {
    public ListenerOnVarbitChanged(final Client client, final ChargedItemBase chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
    }

    public void trigger(final VarbitChanged event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            final OnVarbitChanged trigger = (OnVarbitChanged) triggerBase;
            boolean triggerUsed = false;

            // Varbit value consumer.
            if (trigger.varbitValueConsumer.isPresent()) {
                trigger.varbitValueConsumer.get().accept(event.getValue());
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final VarbitChanged event) {
        if (!(triggerBase instanceof OnVarbitChanged)) return false;
        final OnVarbitChanged trigger = (OnVarbitChanged) triggerBase;

        // Varbit id check.
        if (event.getVarbitId() != trigger.varbitId) {
            return false;
        }

        // Varbit value check.
        if (trigger.isVarbitValue.isPresent() && event.getValue() != trigger.isVarbitValue.get()) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }
}
