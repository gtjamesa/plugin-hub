package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerBase;

public abstract class ListenerBase {
    protected final Client client;
    protected final ChargedItemBase chargedItem;
    protected final Notifier notifier;
    protected final ChargesImprovedConfig config;

    public ListenerBase(final Client client, final ChargedItemBase chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        this.client = client;
        this.chargedItem = chargedItem;
        this.notifier = notifier;
        this.config = config;
    }

    boolean trigger(final TriggerBase trigger) {
        boolean triggerUsed = false;

        // Fixed charges.
        if (trigger.fixedCharges.isPresent() && (chargedItem instanceof ChargedItem)) {
            ((ChargedItem) chargedItem).setCharges(trigger.fixedCharges.get());
            triggerUsed = true;
        }

        // Increase charges.
        if (trigger.increaseCharges.isPresent() && (chargedItem instanceof ChargedItem)) {
            ((ChargedItem) chargedItem).increaseCharges(trigger.increaseCharges.get());
            triggerUsed = true;
        }

        // Decrease charges.
        if (trigger.decreaseCharges.isPresent() && (chargedItem instanceof ChargedItem)) {
            ((ChargedItem) chargedItem).decreaseCharges(trigger.decreaseCharges.get());
            triggerUsed = true;
        }

        // Empty storage.
        if (trigger.emptyStorage.isPresent() && (chargedItem instanceof ChargedItemWithStorage)) {
            ((ChargedItemWithStorage) chargedItem).storage.empty();
            triggerUsed = true;
        }

        // Add to storage.
        if (trigger.addToStorage.isPresent() && (chargedItem instanceof ChargedItemWithStorage)) {
            ((ChargedItemWithStorage) chargedItem).storage.add(trigger.addToStorage.get()[0], trigger.addToStorage.get()[1]);
            triggerUsed = true;
        }

        // Consumer.
        if (trigger.consumer.isPresent()) {
            trigger.consumer.get().run();
            triggerUsed = true;
        }

        // Activate.
        if (trigger.activate.isPresent() && (chargedItem instanceof ChargedItemWithStatus)) {
            ((ChargedItemWithStatus) chargedItem).activate();
            triggerUsed = true;
        }

        // Deactivate.
        if (trigger.deactivate.isPresent() && (chargedItem instanceof ChargedItemWithStatus)) {
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
        specificItemCheck: if (trigger.onSpecificItem.isPresent()) {
            for (final int itemId : trigger.onSpecificItem.get()) {
                if (chargedItem.store.inventoryContainsItem(itemId) || chargedItem.store.equipmentContainsItem(itemId)) {
                    break specificItemCheck;
                }
            }
            return false;
        }

        // On item click check.
        if (trigger.onItemClick.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.itemId)) {
            return false;
        }

        // Menu option without target or impostor needs to check for self charged item.
        if (
            trigger.onMenuOption.isPresent() &&
            !trigger.onMenuTarget.isPresent() &&
            !trigger.onMenuImpostor.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.itemId)
        ) {
            return false;
        }

        // Menu option check.
        if (trigger.onMenuOption.isPresent() && chargedItem.store.notInMenuOptions(trigger.onMenuOption.get())) {
            return false;
        }

        // Menu target check.
        if (trigger.onMenuTarget.isPresent() && chargedItem.store.notInMenuTargets(trigger.onMenuTarget.get())) {
            return false;
        }

        // Menu impostor id check.
        if (trigger.onMenuImpostor.isPresent() && chargedItem.store.notInMenuImpostors(trigger.onMenuImpostor.get())) {
            return false;
        }

        // Equipped check.
        if (trigger.isEquipped.isPresent() && !chargedItem.store.equipmentContainsItem(chargedItem.itemId)) {
            return false;
        }

        // Use check.
        if (trigger.onUse.isPresent() && (chargedItem.store.notInMenuTargets(chargedItem.itemId) || chargedItem.store.notInMenuTargets(trigger.onUse.get()))) {
            return false;
        }

        // Activated check.
        if ((chargedItem instanceof ChargedItemWithStatus) && trigger.isActivated.isPresent() && trigger.isActivated.get() && !((ChargedItemWithStatus) chargedItem).isActivated()) {
            return false;
        }

        // At bank check.
        if (trigger.atBank.isPresent() && (client.getWidget(12, 1) == null) && client.getWidget(192, 0) == null) {
            return false;
        }

        return true;
    }
}
