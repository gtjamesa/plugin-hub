package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.ItemDespawned;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.OnItemPickup;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;

public class ListenerOnItemPickup extends ListenerBase {
    public ListenerOnItemPickup(final Client client, final ChargedItemBase chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
    }

    public void trigger(final ItemDespawned event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;

            final OnItemPickup trigger = (OnItemPickup) triggerBase;
            boolean triggerUsed = false;

            if (trigger.pickUpToStorage.isPresent()) {
                ((ChargedItemWithStorage) chargedItem).storage.add(event.getItem().getId(), event.getItem().getQuantity());
                triggerUsed = true;
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final ItemDespawned event) {
        if (!(triggerBase instanceof OnItemPickup)) return false;
        final OnItemPickup trigger = (OnItemPickup) triggerBase;

        // Correct item check.
        boolean correctItem = false;
        for (final TriggerItem triggerItem : chargedItem.items) {
            if (triggerItem.itemId == event.getItem().getId()) {
                correctItem = true;
                break;
            }
        } if (!correctItem) {
            return false;
        }

        // Pick up to storage check.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        // By one check.
        if (trigger.isByOne.isPresent() && trigger.isByOne.get() && event.getItem().getQuantity() > 1) {
            return false;
        }

        // Menu option check.
        if (!chargedItem.store.inMenuOptions("Take")) {
            return false;
        }

        // Menu target check.
        if (!chargedItem.store.inMenuTargets(event.getItem().getId())) {
            return false;
        }

        // Player location check.
        if (client.getLocalPlayer().getWorldLocation().distanceTo(event.getTile().getWorldLocation()) > 1) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }
}
