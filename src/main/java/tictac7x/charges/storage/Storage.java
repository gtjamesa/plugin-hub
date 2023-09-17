package tictac7x.charges.storage;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;

import java.util.Optional;

public class Storage {
    Optional<ItemContainer> inventory = Optional.empty();
    Optional<ItemContainer> equipment = Optional.empty();

    public void onItemContainerChanged(final ItemContainerChanged event) {
        if (event.getContainerId() == InventoryID.INVENTORY.getId()) {
            inventory = Optional.of(event.getItemContainer());
        } else if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
            equipment = Optional.of(event.getItemContainer());
        }
    }

    public Optional<ItemContainer> getInventory() {
        return inventory;
    }

    public Optional<ItemContainer> getEquipment() {
        return equipment;
    }
}
