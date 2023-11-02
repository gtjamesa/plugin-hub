package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerBase;

public abstract class ListenerBase {
    protected final Client client;
    protected final ChargedItem chargedItem;
    protected final Notifier notifier;
    protected final ChargesImprovedConfig config;

    public ListenerBase(final Client client, final ChargedItem chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        this.client = client;
        this.chargedItem = chargedItem;
        this.notifier = notifier;
        this.config = config;
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

        // Consumer.
        if (trigger.consumer.isPresent()) {
            trigger.consumer.get().run();
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
//            System.out.println("A");
            return false;
        }

        // On item click check.
        if (trigger.onItemClick.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.item_id)) {
//            System.out.println("B");
            return false;
        }

        // Menu option check.
        if (trigger.onMenuOption.isPresent() && chargedItem.store.notInMenuOptions(trigger.onMenuOption.get())) {
//            System.out.println("C");
            return false;
        }

        // Menu target check.
        if (trigger.onMenuTargets.isPresent() && chargedItem.store.notInMenuTargets(trigger.onMenuTargets.get())) {
//            System.out.println("WASD");
            return false;
        }

        // Menu impostor id check.
        if (trigger.onMenuImpostorId.isPresent() && chargedItem.store.notInMenuImpostors(trigger.onMenuImpostorId.get())) {
//            System.out.println("D");
            return false;
        }

        // Equipped check.
        if (trigger.isEquipped.isPresent() && !chargedItem.store.equipmentContainsItem(chargedItem.item_id)) {
//            System.out.println("E");
            return false;
        }

        // Use check.
        if (trigger.use.isPresent() && (chargedItem.store.notInMenuTargets(chargedItem.item_id) || chargedItem.store.notInMenuTargets(trigger.use.get()))) {
//            System.out.println("F");
            return false;
        }

        // Empty storage check.
        if (trigger.emptyStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
//            System.out.println("G");
            return false;
        }

        // Pick up to storage check.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
//            System.out.println("H");
            return false;
        }

        // Is activated check.
        if (trigger.isActivated.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
//            System.out.println("NOT ACTIVATED");
            return false;
        }

        // Activate check.
        if (trigger.activate.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
//            System.out.println("I");
            return false;
        }

        // Deactivate check.
        if (trigger.deactivate.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
//            System.out.println("J");
            return false;
        }

        return true;
    }
}
