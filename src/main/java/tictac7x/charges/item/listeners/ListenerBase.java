package tictac7x.charges.item.listeners;

import net.runelite.client.Notifier;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerBase;

public abstract class ListenerBase {
    protected final ChargedItem chargedItem;
    protected final Notifier notifier;

    public ListenerBase(final ChargedItem chargedItem, final Notifier notifier) {
        this.chargedItem = chargedItem;
        this.notifier = notifier;
    }

    boolean trigger(final TriggerBase trigger) {
        boolean triggerUsed = false;

        // Fixed charges.
        if (trigger.fixedCharges.isPresent()) {
            chargedItem.setCharges(trigger.fixedCharges.get());
            triggerUsed = true;
        }

        // Increase charges.
        if (trigger.increaseCharges.isPresent()) {
            chargedItem.increaseCharges(trigger.increaseCharges.get());
            triggerUsed = true;
        }

        // Decrease charges.
        if (trigger.decreaseCharges.isPresent()) {
            chargedItem.decreaseCharges(trigger.decreaseCharges.get());
            triggerUsed = true;
        }

        // Empty storage.
        if (trigger.emptyStorage.isPresent()) {
            ((ChargedItemWithStorage) chargedItem).storage.empty();
            triggerUsed = true;
        }

        // Activate.
        if (trigger.activate.isPresent()) {
            ((ChargedItemWithStatus) chargedItem).activate();
            triggerUsed = true;
        }

        // Deactivate.
        if (trigger.deactivate.isPresent()) {
            ((ChargedItemWithStatus) chargedItem).deactivate();
            triggerUsed = true;
        }

        // Notification
        if (trigger.notificationCustom.isPresent()) {
            notifier.notify(trigger.notificationCustom.get());
            triggerUsed = true;
        }

        return triggerUsed;
    }

    boolean isValidTrigger(final TriggerBase trigger) {
        // Specific item check.
        specificItemCheck: if (trigger.specificItem.isPresent()) {
            for (final int itemId : trigger.specificItem.get()) {
                if (chargedItem.store.inventoryContainsItem(itemId) || chargedItem.store.equipmentContainsItem(itemId)) {
                    break specificItemCheck;
                }
            }

            return false;
        }

        // On item click check.
        if (trigger.onItemClick.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.item_id)) {
            return false;
        }

        // Menu option check.
        if (trigger.onMenuOption.isPresent() && chargedItem.store.notInMenuOptions(trigger.onMenuOption.get())) {
            return false;
        }

        // Menu option without menu target check.
        if (trigger.onMenuOption.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.item_id)) {
            return false;
        }

        // Equipped check.
        if (trigger.equipped.isPresent() && !chargedItem.store.equipmentContainsItem(chargedItem.item_id)) {
            return false;
        }

        // Use check.
        if (trigger.use.isPresent() && (chargedItem.store.notInMenuTargets(chargedItem.item_id) || chargedItem.store.notInMenuTargets(trigger.use.get()))) {
            return false;
        }

        // Empty storage check.
        if (trigger.emptyStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        // Pick up to storage check.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        // Activate check.
        if (trigger.activate.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
            return false;
        }

        // Deactivate check.
        if (trigger.deactivate.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
            return false;
        }

        return true;
    }
}
