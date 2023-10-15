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
        for (final TriggerItem trigger_item : chargedItem.triggersItems) {
            if (
                // Item trigger does not use varbits.
                trigger_item.varbit_id == null || trigger_item.varbit_value == null ||
                // Varbit does not match the trigger item required varbit.
                event.getVarbitId() != trigger_item.varbit_id || event.getValue() != trigger_item.varbit_value
            ) continue;

            // Find out charges for the item.
            if (trigger_item.fixed_charges != null) {
                int charges = 0;
                charges += chargedItem.store.getInventoryItemCount(trigger_item.item_id) * trigger_item.fixed_charges;
                charges += chargedItem.store.getEquipmentItemCount(trigger_item.item_id) * trigger_item.fixed_charges;
                chargedItem.setCharges(charges);

            // Find out charges based on the amount of item.
            } else if (trigger_item.quantity_charges) {
                int charges = 0;
                charges += chargedItem.store.getInventoryItemCount(trigger_item.item_id);
                charges += chargedItem.store.getEquipmentItemCount(trigger_item.item_id);
                chargedItem.setCharges(charges);
            }

            chargedItem.is_negative = trigger_item.is_negative;
        }
    }
}
