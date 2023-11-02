package tictac7x.charges.item.listeners;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.TriggerBase;

@Slf4j
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
            log.debug(chargedItem.getItemName() + " fixed charges set: " + trigger.fixedCharges.get());
            chargedItem.setCharges(trigger.fixedCharges.get());
            triggerUsed = true;
        }

        // Increase charges.
        if (trigger.increaseCharges.isPresent()) {
            log.debug(chargedItem.getItemName() + " increase charges by: " + trigger.increaseCharges.get());
            chargedItem.increaseCharges(trigger.increaseCharges.get());
            triggerUsed = true;
        }

        // Decrease charges.
        if (trigger.decreaseCharges.isPresent()) {
            log.debug(chargedItem.getItemName() + " decrease charges by: " + trigger.decreaseCharges.get());
            chargedItem.decreaseCharges(trigger.decreaseCharges.get());
            triggerUsed = true;
        }

        // Empty storage.
        if (trigger.emptyStorage.isPresent()) {
            log.debug(chargedItem.getItemName() + " storage emptied");
            ((ChargedItemWithStorage) chargedItem).storage.empty();
            triggerUsed = true;
        }

        // Add to storage.
        if (trigger.addToStorage.isPresent()) {
            log.debug(chargedItem.getItemName() + " add to storage: " + trigger.addToStorage.get()[0] + " (" + trigger.addToStorage.get()[1] + ")");
            ((ChargedItemWithStorage) chargedItem).storage.add(trigger.addToStorage.get()[0], trigger.addToStorage.get()[1]);
            triggerUsed = true;
        }

        // Consumer.
        if (trigger.consumer.isPresent()) {
            log.debug(chargedItem.getItemName() + " custom consumer run");
            trigger.consumer.get().run();
            triggerUsed = true;
        }

        // Activate.
        if (trigger.activate.isPresent()) {
            log.debug(chargedItem.getItemName() + " activated");
            ((ChargedItemWithStatus) chargedItem).activate();
            triggerUsed = true;
        }

        // Deactivate.
        if (trigger.deactivate.isPresent()) {
            log.debug(chargedItem.getItemName() + " deactivated");
            ((ChargedItemWithStatus) chargedItem).deactivate();
            triggerUsed = true;
        }

        // Notification
        if (trigger.notificationCustom.isPresent()) {
            log.debug(chargedItem.getItemName() + " custom notification sent");
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
            log.debug(chargedItem.getItemName() + " trigger failed: specific item check");
            return false;
        }

        // On item click check.
        if (trigger.onItemClick.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.item_id)) {
            log.debug(chargedItem.getItemName() + " trigger failed: on item click");
            return false;
        }

        // Menu option without target or impostor needs to check for self charged item.
        if (trigger.onMenuOption.isPresent() && !trigger.onMenuTarget.isPresent() && !trigger.onMenuImpostor.isPresent() && chargedItem.store.notInMenuTargets(chargedItem.item_id)) {
            log.debug(chargedItem.getItemName() + " trigger failed: menu option without specific target");
            return false;
        }

        // Menu option check.
        if (trigger.onMenuOption.isPresent() && chargedItem.store.notInMenuOptions(trigger.onMenuOption.get())) {
            log.debug(chargedItem.getItemName() + " trigger failed: menu option check");
            return false;
        }

        // Menu target check.
        if (trigger.onMenuTarget.isPresent() && chargedItem.store.notInMenuTargets(trigger.onMenuTarget.get())) {
            log.debug(chargedItem.getItemName() + " trigger failed: menu target check");
            return false;
        }

        // Menu impostor id check.
        if (trigger.onMenuImpostor.isPresent() && chargedItem.store.notInMenuImpostors(trigger.onMenuImpostor.get())) {
            log.debug(chargedItem.getItemName() + " trigger failed: menu impostor check");
            return false;
        }

        // Equipped check.
        if (trigger.isEquipped.isPresent() && !chargedItem.store.equipmentContainsItem(chargedItem.item_id)) {
            log.debug(chargedItem.getItemName() + " trigger failed: equipment contains check");
            return false;
        }

        // Use check.
        if (trigger.onUse.isPresent() && (chargedItem.store.notInMenuTargets(chargedItem.item_id) || chargedItem.store.notInMenuTargets(trigger.onUse.get()))) {
            log.debug(chargedItem.getItemName() + " trigger failed: use check");
            return false;
        }

        // Empty storage check.
        if (trigger.emptyStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            log.debug(chargedItem.getItemName() + " trigger failed: empty storage check");
            return false;
        }

        // Pick up to storage check.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            log.debug(chargedItem.getItemName() + " trigger failed: pickup to storage check");
            return false;
        }

        // Add to storage check.
        if (trigger.addToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            log.debug(chargedItem.getItemName() + " trigger failed: add to storage check");
            return false;
        }

        // Is activated check.
        if (trigger.isActivated.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
            log.debug(chargedItem.getItemName() + " trigger failed: is activated check");
            return false;
        }

        // Activate check.
        if (trigger.activate.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
            log.debug(chargedItem.getItemName() + " trigger failed: activate check");
            return false;
        }

        // Deactivate check.
        if (trigger.deactivate.isPresent() && !(chargedItem instanceof ChargedItemWithStatus)) {
            log.debug(chargedItem.getItemName() + " trigger failed: deactivate check");
            return false;
        }

        return true;
    }
}
