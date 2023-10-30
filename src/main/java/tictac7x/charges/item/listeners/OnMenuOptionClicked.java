package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerMenuOptionClicked;

public class OnMenuOptionClicked {
    final ChargedItem chargedItem;
    final Client client;
    final ClientThread clientThread;
    final ItemManager itemManager;

    public OnMenuOptionClicked(final ChargedItem chargedItem, final Client client, final ClientThread clientThread, final ItemManager itemManager) {
        this.chargedItem = chargedItem;
        this.client = client;
        this.clientThread = clientThread;
        this.itemManager = itemManager;
    }

    public void trigger(final MenuOptionClicked event) {
        for (final TriggerMenuOptionClicked trigger : chargedItem.triggersMenuOptionClicked) {
            if (!isValidTrigger(event, trigger)) continue;

            if (trigger.decreaseCharges.isPresent()) {
                chargedItem.decreaseCharges(trigger.decreaseCharges.get());
            } else if (trigger.fillStorageFromInventory.isPresent()) {
                trigger.fillStorageFromInventory.get().fillFromInventory();
            } else if (trigger.consumer.isPresent()) {
                trigger.consumer.get().run();
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final MenuOptionClicked event, final TriggerMenuOptionClicked trigger) {
        // Option check.
        if (!event.getMenuOption().equals(trigger.option)) return false;

        // Target check.
        if (trigger.target.isPresent() && !event.getMenuTarget().contains(trigger.target.get())) return false;

        // Item ID check.
        itemIdCheck: if (trigger.itemId.isPresent()) {
            for (final int itemId : trigger.itemId.get()) {
                if (event.getItemId() == itemId) {
                    break itemIdCheck;
                }
            }

            return false;
        }

        // Non-specific item check.
        if (!trigger.target.isPresent() && !trigger.itemId.isPresent()) {
            if (event.getItemId() != chargedItem.item_id) {
                return false;
            }
        }

        // Equipped check.
        if (trigger.equipped.isPresent() && !chargedItem.store.equipmentContainsItem(chargedItem.item_id)) return false;

        // At bank check.
        if (trigger.atBank.isPresent() && client.getWidget(WidgetInfo.BANK_CONTAINER) == null && client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER) == null) return false;

        // Use check.
        if (trigger.use.isPresent()) {
            boolean mainItemCheck = chargedItem.store.inMenuTargets(itemManager.getItemComposition(chargedItem.item_id).getName());
            boolean useItemCheck = chargedItem.store.inMenuTargets(trigger.use.get());

            if (!mainItemCheck || !useItemCheck) {
                return false;
            }

        }

        return true;
    }
}
