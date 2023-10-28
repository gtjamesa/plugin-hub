package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerMenuOptionClicked;

public class OnMenuOptionClicked {
    final ChargedItem chargedItem;
    final Client client;
    final ClientThread clientThread;

    public OnMenuOptionClicked(final ChargedItem chargedItem, final Client client, final ClientThread clientThread) {
        this.chargedItem = chargedItem;
        this.client = client;
        this.clientThread = clientThread;
    }

    public void trigger(final MenuOptionClicked event) {
        for (final TriggerMenuOptionClicked trigger : chargedItem.triggersMenuOptionClicked) {
            if (!isValidTrigger(event, trigger)) continue;

            clientThread.invokeAtTickEnd(() -> {
                if (trigger.decreaseCharges.isPresent()) {
                    chargedItem.decreaseCharges(trigger.decreaseCharges.get());
                } else if (trigger.consumer.isPresent()) {
                    trigger.consumer.get().run();
                }
            });

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

        // Equipped check.
        if (trigger.equipped.isPresent() && !chargedItem.store.equipmentContainsItem(chargedItem.item_id)) return false;

        // At bank check.
        if (trigger.atBank.isPresent() && client.getWidget(WidgetInfo.BANK_CONTAINER) == null && client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER) == null) return false;

        // Use targets check.
        if (trigger.useMenuTargets.isPresent()) {
            boolean target1Check = chargedItem.store.inMenuTargets(trigger.useMenuTargets.get()[0]);
            boolean target2Check = chargedItem.store.inMenuTargets(trigger.useMenuTargets.get()[1]);

            if (!target1Check || !target2Check) {
                return false;
            }

        }

        return true;
    }
}
