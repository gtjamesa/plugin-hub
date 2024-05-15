package tictac7x.charges.item.triggers;

import tictac7x.charges.store.ItemContainerType;

import java.util.Optional;

public class OnItemContainerChanged extends TriggerBase {
    public final ItemContainerType itemContainerType;

    public Optional<Boolean> fillStorageFromInventory = Optional.empty();
    public Optional<Boolean> fillStorageFromInventorySingle = Optional.empty();
    public Optional<Boolean> emptyStorageToInventory = Optional.empty();

    public OnItemContainerChanged(final ItemContainerType itemContainerType) {
        this.itemContainerType = itemContainerType;
    }

    public OnItemContainerChanged fillStorageFromInventoryAll() {
        this.fillStorageFromInventory = Optional.of(true);
        return this;
    }

    public OnItemContainerChanged fillStorageFromInventorySingle() {
        this.fillStorageFromInventorySingle = Optional.of(true);
        return this;
    }

    public OnItemContainerChanged emptyStorageToInventory() {
        this.emptyStorageToInventory = Optional.of(true);
        return this;
    }
}
