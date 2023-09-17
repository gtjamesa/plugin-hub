package tictac7x.charges.storage;

import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;

import javax.annotation.Nullable;

public class Storage {
    @Nullable
    public ItemContainer inventory = null;
    @Nullable
    public ItemContainer equipment = null;
    @Nullable
    public Item[] inventory_items = null;

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory = event.getItemContainer();
        } else if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
            equipment = event.getItemContainer();
        }
    }

    public void onInventoryItemsChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory_items = event.getItemContainer().getItems();
        }
    }
}
