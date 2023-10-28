package tictac7x.charges.item.listeners;

import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerMenuOptionClicked;

public class OnMenuOptionClicked {
    final ChargedItem chargedItem;
    final ClientThread clientThread;

    public OnMenuOptionClicked(final ChargedItem chargedItem, final ClientThread clientThread) {
        this.chargedItem = chargedItem;
        this.clientThread = clientThread;
    }

    public void trigger(final MenuOptionClicked event) {
        for (final TriggerMenuOptionClicked trigger : chargedItem.triggersMenuOptionClicked) {
            if (!isValidTrigger(event, trigger)) continue;

            clientThread.invokeLater(() -> {
                if (trigger.decreaseCharges.isPresent()) {
                    chargedItem.decreaseCharges(trigger.decreaseCharges.get());
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

        return true;
    }
}
