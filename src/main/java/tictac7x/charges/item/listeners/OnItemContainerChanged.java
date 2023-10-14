package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;

public class OnItemContainerChanged {
    final ChargedItem chargedItem;
    final Client client;

    public OnItemContainerChanged(final ChargedItem chargedItem, final Client client) {
        this.chargedItem = chargedItem;
        this.client = client;
    }

    public void trigger(final ItemContainerChanged event) {
        if (event.getItemContainer() == null) return;

        // Find items difference before items are overridden.
        final int inventory_items_difference = chargedItem.store.getInventoryItemsDifference(event);
        final int bank_items_difference = chargedItem.store.getBankItemsDifference(event);

        for (final TriggerItemContainer trigger : chargedItem.triggersItemContainers) {
            // Item container is wrong.
            if (trigger.inventory_id != event.getContainerId()) continue;

            // Menu target check.
            if (trigger.menu_target != null && !chargedItem.store.inMenuTargets(trigger.menu_target)) continue;

            // Menu option check.
            if (trigger.menu_option != null && !chargedItem.store.inMenuOptions(trigger.menu_option)) continue;

            // Fixed charges.
            if (trigger.fixed_charges != null) {
                chargedItem.setCharges(trigger.fixed_charges);

            // Increase charges by different of inventory.
            } else if (trigger.increase_by_inventory_difference) {
                chargedItem.increaseCharges(inventory_items_difference);

            // Decrease charges by difference of inventory.
            } else if (trigger.decrease_by_inventory_difference) {
                chargedItem.decreaseCharges(inventory_items_difference);

            // Decrease charges by difference of bank.
            } else if (trigger.decrease_by_bank_difference) {
                chargedItem.decreaseCharges(bank_items_difference);

            // Charges dynamically based on the items of the item container.
            } else  {
                chargedItem.setCharges(event.getItemContainer().count());
            }
        }

        boolean in_inventory = false;
        boolean in_equipment = false;
        boolean render = false;
        Integer charges = null;

        for (final TriggerItem trigger_item : chargedItem.triggersItems) {
            // Item trigger has varbit check.
            if (
                trigger_item.varbit_id != null &&
                trigger_item.varbit_value != null &&
                client.getVarbitValue(trigger_item.varbit_id) != trigger_item.varbit_value
            ) {
                continue;
            }

            // Negative item check.
            chargedItem.is_negative = trigger_item.is_negative;

            // Find out if item is equipped.
            final boolean in_inventory_item = chargedItem.store.inventory != null && chargedItem.store.inventory.contains(trigger_item.item_id);
            final boolean in_equipment_item = chargedItem.store.equipment != null && chargedItem.store.equipment.contains(trigger_item.item_id);

            // Item not found, don't calculate charges.
            if (!in_equipment_item && !in_inventory_item) continue;

            // Item found.
            render = true;
            if (in_inventory_item) in_inventory = true;
            if (in_equipment_item) in_equipment = true;

            // Update infobox item picture and tooltip dynamically based on the items if use has different variant of it.
            if (trigger_item.item_id != chargedItem.item_id) {
                chargedItem.updateInfobox(trigger_item.item_id);
            }

            // Find out charges for the item.
            if (trigger_item.fixed_charges != null) {
                if (charges == null) charges = 0;
                charges += chargedItem.store.inventory != null ? chargedItem.store.inventory.count(trigger_item.item_id) * trigger_item.fixed_charges : 0;
                charges += chargedItem.store.equipment != null ? chargedItem.store.equipment.count(trigger_item.item_id) * trigger_item.fixed_charges : 0;
                // Find out charges based on the amount of item.
            } else if (trigger_item.quantity_charges) {
                if (charges == null) charges = 0;
                charges += chargedItem.store.inventory != null ? chargedItem.store.inventory.count(trigger_item.item_id) : 0;
                charges += chargedItem.store.equipment != null ? chargedItem.store.equipment.count(trigger_item.item_id) : 0;
            }
        }

        // Update infobox variables for other triggers.
        this.chargedItem.render = render;
        this.chargedItem.in_inventory = in_inventory;
        this.chargedItem.in_equipment = in_equipment;
        if (charges != null) this.chargedItem.charges = charges;
        chargedItem.updateTooltip();
    }
}
