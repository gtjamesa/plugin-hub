package tictac7x.charges.item.triggers;

import tictac7x.charges.item.Storage;

import java.util.Optional;

public class TriggerItemDespawned {
    public final int[] itemIds;

    public Optional<Integer> specificItem = Optional.empty();
    public Optional<Storage> pickUpToStorage = Optional.empty();

    public TriggerItemDespawned(final int ...itemIds) {
        this.itemIds = itemIds;
    }

    public TriggerItemDespawned specificItem(final int itemId) {
        this.specificItem = Optional.of(itemId);
        return this;
    }

    public TriggerItemDespawned pickUpToStorage(final Storage storage) {
        this.pickUpToStorage = Optional.of(storage);
        return this;
    }
}
