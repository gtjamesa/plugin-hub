package tictac7x.charges.item.triggers;

public class OnItemDespawned extends TriggerBase {
    public final int[] despawnedItemIds;

    public OnItemDespawned(final int ... despawnedItemIds) {
        this.despawnedItemIds = despawnedItemIds;
    }
}
