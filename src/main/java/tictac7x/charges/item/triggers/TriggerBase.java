package tictac7x.charges.item.triggers;

import tictac7x.charges.item.storage.StoreableItem;

import java.util.Optional;

public abstract class TriggerBase {
    // Checks.
    public Optional<int[]> onSpecificItem = Optional.empty();
    public Optional<String[]> onMenuOption = Optional.empty();
    public Optional<String[]> onMenuTarget = Optional.empty();
    public Optional<int[]> onMenuImpostor = Optional.empty();
    public Optional<Boolean> onItemClick = Optional.empty();
    public Optional<StoreableItem[]> onUse = Optional.empty();
    public Optional<Boolean> isEquipped = Optional.empty();

    // Actions.
    public Optional<Integer> fixedCharges = Optional.empty();
    public Optional<Integer> increaseCharges = Optional.empty();
    public Optional<Integer> decreaseCharges = Optional.empty();
    public Optional<Runnable> consumer = Optional.empty();

    public Optional<String> notificationCustom = Optional.empty();

    // Storage.
    public Optional<Boolean> emptyStorage = Optional.empty();
    public Optional<Boolean> pickUpToStorage = Optional.empty();
    public Optional<int[]> addToStorage = Optional.empty();

    // Activity.
    public Optional<Boolean> isActivated = Optional.empty();
    public Optional<Boolean> activate = Optional.empty();
    public Optional<Boolean> deactivate = Optional.empty();

    public TriggerBase fixedCharges(final int charges) {
        this.fixedCharges = Optional.of(charges);
        return this;
    }

    public TriggerBase increaseCharges(final int charges) {
        this.increaseCharges = Optional.of(charges);
        return this;
    }

    public TriggerBase decreaseCharges(final int charges) {
        this.decreaseCharges = Optional.of(charges);
        return this;
    }

    public TriggerBase emptyStorage() {
        this.emptyStorage = Optional.of(true);
        return this;
    }

    public TriggerBase onSpecificItem(final int ...itemIds) {
        this.onSpecificItem = Optional.of(itemIds);
        return this;
    }

    public TriggerBase onMenuOption(final String ...options) {
        this.onMenuOption = Optional.of(options);
        return this;
    }

    public TriggerBase onMenuTarget(final String ...targets) {
        this.onMenuTarget = Optional.of(targets);
        return this;
    }

    public TriggerBase onMenuImpostor(final int ...impostorIds) {
        this.onMenuImpostor = Optional.of(impostorIds);
        return this;
    }

    public TriggerBase onItemClick() {
        this.onItemClick = Optional.of(true);
        return this;
    }

    public TriggerBase pickUpToStorage() {
        this.pickUpToStorage = Optional.of(true);
        return this;
    }

    public TriggerBase onUse(final Optional<StoreableItem[]> storeableItems) {
        this.onUse = storeableItems;
        return this;
    }

    public TriggerBase activate() {
        this.activate = Optional.of(true);
        return this;
    }

    public TriggerBase deactivate() {
        this.deactivate = Optional.of(true);
        return this;
    }

    public TriggerBase notification(final String notification) {
        this.notificationCustom = Optional.of(notification);
        return this;
    }

    public TriggerBase isEquipped() {
        this.isEquipped = Optional.of(true);
        return this;
    }

    public TriggerBase consumer(final Runnable consumer) {
        this.consumer = Optional.of(consumer);
        return this;
    }

    public TriggerBase isActivated() {
        this.isActivated = Optional.of(true);
        return this;
    }

    public TriggerBase addToStorage(final int itemId, final int quantity) {
        this.addToStorage = Optional.of(new int[]{itemId, quantity});
        return this;
    }

    public TriggerBase addToStorage(final int itemId) {
        this.addToStorage = Optional.of(new int[]{itemId, 1});
        return this;
    }
}
