package tictac7x.charges.item.listeners;

import net.runelite.api.events.VarbitChanged;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerItem;

public class OnVarbitChanged {
    final ChargedItem chargedItem;

    public OnVarbitChanged(final ChargedItem chargedItem) {
        this.chargedItem = chargedItem;
    }

    public void trigger(final VarbitChanged event) {
        for (final TriggerItem trigger : chargedItem.triggersItems) {
            if (!isValidTrigger(event, trigger)) continue;

            // Find out charges for the item.
            if (trigger.fixed_charges != null) {
                int charges = 0;
                charges += chargedItem.store.getInventoryItemCount(trigger.item_id) * trigger.fixed_charges;
                charges += chargedItem.store.getEquipmentItemCount(trigger.item_id) * trigger.fixed_charges;
                chargedItem.setCharges(charges);

            // Find out charges based on the amount of item.
            } else if (trigger.quantity_charges) {
                int charges = 0;
                charges += chargedItem.store.getInventoryItemCount(trigger.item_id);
                charges += chargedItem.store.getEquipmentItemCount(trigger.item_id);
                chargedItem.setCharges(charges);
            }

            chargedItem.is_negative = trigger.is_negative;

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final VarbitChanged event, final TriggerItem trigger) {
        // Item trigger does not use varbits.
        if (trigger.varbit_id == null || trigger.varbit_value == null) return false;

        // Varbit does not match the trigger item required varbit.
        if (event.getVarbitId() != trigger.varbit_id || event.getValue() != trigger.varbit_value) return false;

        return true;
    }
}
