package tictac7x.charges.item.listeners;

import net.runelite.api.events.ItemDespawned;
import net.runelite.client.Notifier;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnItemDespawned;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.ChargedItemWithStorage;

public class ListenerOnItemDespawned extends ListenerBase {
    public ListenerOnItemDespawned(final ChargedItem chargedItem, final Notifier notifier) {
        super(chargedItem, notifier);
    }

    public void trigger(final ItemDespawned event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            final OnItemDespawned trigger = (OnItemDespawned) triggerBase;

            if (trigger.pickUpToStorage.isPresent()) {
                ((ChargedItemWithStorage) chargedItem).storage.add(event.getItem().getId(), event.getItem().getQuantity());
                return;
            } else if (super.trigger(trigger)) {
                return;
            }
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final ItemDespawned event) {
        if (!(triggerBase instanceof OnItemDespawned)) return false;
        final OnItemDespawned trigger = (OnItemDespawned) triggerBase;

        // Despawned item id check.
        boolean despawnedItemCheck = false;
        for (final int itemId : trigger.despawnedItemIds) {
            if (event.getItem().getId() == itemId) {
                despawnedItemCheck = true;
                break;
            }
        }
        if (!despawnedItemCheck) return false;

        // Pick up to storage check.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) return false;

        return super.isValidTrigger(trigger);
    }
}
