package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerGraphic;
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
        if (trigger.target.isPresent() && !event.getMenuTarget().equals(trigger.target.get())) return false;

        // Item ID check.
        if (trigger.itemId.isPresent() && event.getItemId() != trigger.itemId.get()) return false;

        return true;
    }
}
