package tictac7x.charges.item.listeners;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemContainerChanged;
import tictac7x.charges.item.ChargedItemTrigger;
import tictac7x.charges.store.ItemContainerType;

import java.util.regex.Matcher;

public class OnItemContainerChangedNew {
    public boolean onTrigger(final ChargedItemTrigger trigger, final ItemContainerChanged event) {
        return false;
    }

    public boolean isValidTrigger(final ChargedItemTrigger trigger, final ItemContainerChanged event) {
        // Item container changed check.
        if (!trigger.onItemContainerChanged.isPresent()) return false;

        // Item container type check.
        final ItemContainer itemContainer = event.getItemContainer();
        if (
            itemContainer == null ||
            trigger.onItemContainerChanged.get() == ItemContainerType.INVENTORY && itemContainer.getId() != InventoryID.INVENTORY.getId() ||
            trigger.onItemContainerChanged.get() == ItemContainerType.BANK && itemContainer.getId() != InventoryID.BANK.getId()
        ) {
            return false;
        }

        return true;
    }
}
