package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.item.triggers.OnItemPickup;
import tictac7x.charges.item.triggers.TriggerBase;

public class ListenerOnItemPickup extends ListenerBase {
    public ListenerOnItemPickup(final Client client, final ChargedItemBase chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
    }

    public void trigger() {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase)) continue;

            final OnItemPickup trigger = (OnItemPickup) triggerBase;
            boolean triggerUsed = false;

            if (trigger.pickUpToStorage.isPresent()) {
                // Find picked up item.
                for (final StorageItem item : trigger.items) {
                    if (chargedItem.store.inMenuTargets(item.itemId)) {
                        ((ChargedItemWithStorage) chargedItem).storage.add(item.itemId, 1);
                        triggerUsed = true;
                        break;
                    }
                }
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase) {
        if (!(triggerBase instanceof OnItemPickup)) return false;
        final OnItemPickup trigger = (OnItemPickup) triggerBase;

        // Pick up to storage check.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        // Take check.
        if (!chargedItem.store.inMenuOptions("Take")) {
            return false;
        }

        // Item check.
        boolean validItem = false;
        for (final StorageItem item : trigger.items) {
            if (chargedItem.store.inMenuTargets(item.itemId)) {
                validItem = true;
                break;
            }
        }
        if (!validItem) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }
}
