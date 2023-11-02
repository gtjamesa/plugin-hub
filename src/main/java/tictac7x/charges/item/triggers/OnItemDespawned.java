package tictac7x.charges.item.triggers;

import tictac7x.charges.item.storage.StoreableItem;

import java.util.Optional;

public class OnItemDespawned extends TriggerBase {
    public final StoreableItem[] despawnedItemIds;

    public OnItemDespawned(final Optional<StoreableItem[]> despawnedItems) {
        if (despawnedItems.isPresent()) {
            this.despawnedItemIds = despawnedItems.get();
        } else {
            this.despawnedItemIds = new StoreableItem[]{};
        }
    }
}
