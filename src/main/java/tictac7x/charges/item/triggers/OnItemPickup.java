package tictac7x.charges.item.triggers;

import tictac7x.charges.item.storage.StorageItem;

public class OnItemPickup extends TriggerBase {
    public final StorageItem[] items;

    public OnItemPickup(final StorageItem[] items) {
        this.items = items;
    }
}
