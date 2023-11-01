package tictac7x.charges.item.triggers;

import tictac7x.charges.store.ItemContainerType;

public class OnItemContainerChanged extends TriggerBase {
    public final ItemContainerType itemContainerType;

    public OnItemContainerChanged(final ItemContainerType itemContainerType) {
        this.itemContainerType = itemContainerType;
    }
}
