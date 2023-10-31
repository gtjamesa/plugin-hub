package tictac7x.charges.item.listeners;

import net.runelite.api.events.ItemDespawned;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemTrigger;
import tictac7x.charges.item.ChargedItemWithStorage;

public class OnItemDespawnedNew {
    private final ChargedItem chargedItem;

    public OnItemDespawnedNew(final ChargedItem chargedItem) {
        this.chargedItem = chargedItem;
    }

    public boolean onTrigger(final ChargedItemTrigger trigger, final ItemDespawned event) {
        if (trigger.pickUpToStorage.isPresent()) {
            ((ChargedItemWithStorage) chargedItem).storage.add(event.getItem().getId(), event.getItem().getQuantity());
            return true;
        }

        return false;
    }

    public boolean isValidTrigger(final ChargedItemTrigger trigger, final ItemDespawned event) {
        // Despawned item check.
        if (!trigger.onItemDespawned.isPresent()) {
            return false;
        }

        // Despawned item id check.
        despawnedItemCheck: if (trigger.onItemDespawned.isPresent()) {
            final int despawnedItemId = event.getItem().getId();

            for (final int itemId : trigger.onItemDespawned.get()) {
                if (despawnedItemId == itemId) {
                    break despawnedItemCheck;
                }
            }

            return false;
        }

        return true;
    }
}
