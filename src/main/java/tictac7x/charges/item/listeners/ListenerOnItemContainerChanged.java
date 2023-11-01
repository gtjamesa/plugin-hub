package tictac7x.charges.item.listeners;

import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.Notifier;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.ItemContainerType;

public class ListenerOnItemContainerChanged extends ListenerBase {
    public ListenerOnItemContainerChanged(final ChargedItem chargedItem, final Notifier notifier) {
        super(chargedItem, notifier);
    }

    public void trigger(final ItemContainerChanged event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            final OnItemContainerChanged trigger = (OnItemContainerChanged) triggerBase;

            if (super.trigger(trigger)) {
                return;
            }
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final ItemContainerChanged event) {
        if (!(triggerBase instanceof OnItemContainerChanged)) return false;
        final OnItemContainerChanged trigger = (OnItemContainerChanged) triggerBase;

        // Item container type check.
        final ItemContainer itemContainer = event.getItemContainer();
        if (
            itemContainer == null ||
            trigger.itemContainerType == ItemContainerType.INVENTORY && itemContainer.getId() != InventoryID.INVENTORY.getId() ||
            trigger.itemContainerType == ItemContainerType.BANK && itemContainer.getId() != InventoryID.BANK.getId()
        ) {
            return false;
        }

        return true;
    }
}
