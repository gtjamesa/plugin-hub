package tictac7x.charges.item.triggers;

import tictac7x.charges.item.storage.StorageItem;

import java.util.Optional;

public class OnItemDespawned extends TriggerBase {
    public final StorageItem[] despawnedItemIds;

    public OnItemDespawned(final Optional<StorageItem[]> despawnedItems) {
        if (despawnedItems.isPresent()) {
            this.despawnedItemIds = despawnedItems.get();
        } else {
            this.despawnedItemIds = new StorageItem[]{};
        }
    }
}
