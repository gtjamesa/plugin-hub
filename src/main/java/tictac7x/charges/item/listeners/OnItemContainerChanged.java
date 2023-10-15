package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.item.triggers.TriggerItemContainer;

import java.util.Optional;

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
            if (!isValidTrigger(event, trigger)) continue;

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
        Optional<Integer> charges = Optional.empty();

        for (final TriggerItem trigger_item : chargedItem.triggersItems) {
            // Find out if item is equipped.
            final boolean in_inventory_item = chargedItem.store.inventoryContainsItem(trigger_item.item_id);
            final boolean in_equipment_item = chargedItem.store.equipmentContainsItem(trigger_item.item_id);

            // Item not found, don't calculate charges.
            if (!in_equipment_item && !in_inventory_item) continue;

            // Item found.
            if (in_inventory_item) in_inventory = true;
            if (in_equipment_item) in_equipment = true;

            // Update item id.
            if (trigger_item.item_id != chargedItem.item_id) {
                chargedItem.item_id = trigger_item.item_id;
            }

            // Find out charges for the item.
            if (trigger_item.fixed_charges != null) {
                charges = Optional.of(
                    chargedItem.store.getEquipmentItemCount(trigger_item.item_id) * trigger_item.fixed_charges +
                    chargedItem.store.getInventoryItemCount(trigger_item.item_id) * trigger_item.fixed_charges
                );

            // Find out charges based on the amount of item.
            } else if (trigger_item.quantity_charges) {
                charges = Optional.of(
                    chargedItem.store.getEquipmentItemCount(trigger_item.item_id) +
                    chargedItem.store.getInventoryItemCount(trigger_item.item_id)
                );
            }
        }

        // Update infobox variables for other triggers.
        chargedItem.in_inventory = in_inventory;
        chargedItem.in_equipment = in_equipment;
        charges.ifPresent(chargedItem::setCharges);
    }

    private boolean isValidTrigger(final ItemContainerChanged event, final TriggerItemContainer trigger) {
        // Item container is wrong.
        if (trigger.inventory_id != event.getContainerId()) return false;

        // Menu target check.
        if (trigger.menu_target != null && chargedItem.store.notInMenuTargets(trigger.menu_target)) return false;

        // Menu option check.
        if (trigger.menu_option != null && chargedItem.store.notInMenuOptions(trigger.menu_option)) return false;

        return true;
    }
}
