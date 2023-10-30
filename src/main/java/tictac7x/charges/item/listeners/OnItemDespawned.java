package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.ItemDespawned;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerItemDespawned;

import java.util.Arrays;

public class OnItemDespawned {
    private final ChargedItem chargedItem;
    private final Client client;

    public OnItemDespawned(final ChargedItem chargedItem, final Client client) {
        this.chargedItem = chargedItem;
        this.client = client;
    }

    public void trigger(final ItemDespawned event) {
        for (final TriggerItemDespawned trigger : chargedItem.triggersItemDespawned) {
            if (!isValidTrigger(event, trigger)) continue;

            if (trigger.pickUpToStorage.isPresent()) {
                trigger.pickUpToStorage.get().add(
                    event.getItem().getId(),
                    event.getItem().getQuantity()
                );
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final ItemDespawned event, final TriggerItemDespawned trigger) {
        // Item id check.
        if (Arrays.stream(trigger.itemIds).noneMatch(itemId -> itemId == event.getItem().getId())) return false;

        // Specific item check.
        if (trigger.specificItem.isPresent() && chargedItem.item_id != trigger.specificItem.get()) return false;

        // Pickup check.
        if (trigger.pickUpToStorage.isPresent()) {
            // Location check.
            if (client.getLocalPlayer().getWorldLocation().distanceTo(event.getTile().getWorldLocation()) != 0) return false;

            // Menu option check.
            if (chargedItem.store.notInMenuOptions("Take")) return false;

            // Menu target check.
            targetChecker: if (trigger.pickUpToStorage.isPresent()) {
                for (final int itemId : trigger.itemIds) {
                    if (chargedItem.store.inMenuTargets(itemId)) {
                        break targetChecker;
                    }
                }

                return false;
            }
        }

        return true;
    }
}
