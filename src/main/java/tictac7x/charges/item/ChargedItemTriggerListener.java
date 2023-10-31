package tictac7x.charges.item;

import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemDespawned;
import tictac7x.charges.item.listeners.OnChatMessageNew;
import tictac7x.charges.item.listeners.OnItemContainerChangedNew;
import tictac7x.charges.item.listeners.OnItemDespawnedNew;

public class ChargedItemTriggerListener {
    private final ChargedItem chargedItem;

    private final OnChatMessageNew onChatMessage = new OnChatMessageNew();
    private final OnItemContainerChangedNew onItemContainerChanged = new OnItemContainerChangedNew();
    private final OnItemDespawnedNew onItemDespawned;

    public ChargedItemTriggerListener(final ChargedItem chargedItem) {
        this.chargedItem = chargedItem;
        this.onItemDespawned = new OnItemDespawnedNew(chargedItem);
    }

    public void onChatMessage(final ChatMessage event) {
        for (final ChargedItemTrigger trigger : chargedItem.triggers) {
            if (!onChatMessage.isValidTrigger(trigger, event)) continue;
            if (!isValidTrigger(trigger)) continue;

            // Chat message specific triggers.
            if (onChatMessage.onTrigger(trigger, event)) {
                return;

            // General triggers.
            } else if (onTrigger(trigger)) {
                return;
            }
        }
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        for (final ChargedItemTrigger trigger : chargedItem.triggers) {
            if (!onItemContainerChanged.isValidTrigger(trigger, event)) continue;
            if (!isValidTrigger(trigger)) continue;

            // General triggers.
            if (onTrigger(trigger)) {
                return;
            }
        }
    }

    public void onItemDespawned(final ItemDespawned event) {
        for (final ChargedItemTrigger trigger : chargedItem.triggers) {
            if (!onItemDespawned.isValidTrigger(trigger, event)) continue;
            if (!isValidTrigger(trigger)) continue;

            // Item despawned specific triggers.
            if (onItemDespawned.onTrigger(trigger, event)) {
                return;
            // General triggers.
            } else if (onTrigger(trigger)) {
                return;
            }
        }
    }

    private boolean onTrigger(final ChargedItemTrigger trigger) {
        // Increase charges.
        if (trigger.increaseCharges.isPresent()) {
            chargedItem.increaseCharges(trigger.increaseCharges.get());
            return true;

        // Decrease charges.
        } else if (trigger.decreaseCharges.isPresent()) {
            chargedItem.decreaseCharges(trigger.decreaseCharges.get());
            return true;

        // Empty storage.
        } else if (trigger.emptyStorage.isPresent()) {
            ((ChargedItemWithStorage) chargedItem).storage.empty();
            return true;

        // Fill storage from inventory.
        } else if (trigger.fillStorageFromInventory.isPresent()) {
            ((ChargedItemWithStorage) chargedItem).storage.fillFromInventory();
            return true;
        }

        return false;
    }

    private boolean isValidTrigger(final ChargedItemTrigger trigger) {
        // Specific item check.
        if (trigger.specificItem.isPresent() &&
            !chargedItem.store.inventoryContainsItem(trigger.specificItem.get()) &&
            !chargedItem.store.equipmentContainsItem(trigger.specificItem.get())
        ) {
            return false;
        }

        // Menu option check.
        if (trigger.onMenuOption.isPresent() && chargedItem.store.notInMenuOptions(trigger.onMenuOption.get())) {
            return false;
        }

        // Empty storage check.
        if (trigger.emptyStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        // Fill storage from inventory check.
        if (trigger.fillStorageFromInventory.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        // Pick up to storage.
        if (trigger.pickUpToStorage.isPresent() && !(chargedItem instanceof ChargedItemWithStorage)) {
            return false;
        }

        return true;
    }
}
