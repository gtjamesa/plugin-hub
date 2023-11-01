package tictac7x.charges.item.triggers;

import java.util.Optional;

public abstract class TriggerBase {
    // Checks.
    public Optional<int[]> specificItem = Optional.empty();
    public Optional<String> onMenuOption = Optional.empty();
    public Optional<int[]> use = Optional.empty();
    public Optional<Boolean> onItemClick = Optional.empty();
    public Optional<Boolean> equipped = Optional.empty();

    // Actions.
    public Optional<Integer> fixedCharges = Optional.empty();
    public Optional<Integer> increaseCharges = Optional.empty();
    public Optional<Integer> decreaseCharges = Optional.empty();
    public Optional<Boolean> activate = Optional.empty();
    public Optional<Boolean> deactivate = Optional.empty();
    public Optional<String> notificationCustom = Optional.empty();

    // Storage.
    public Optional<Boolean> emptyStorage = Optional.empty();
    public Optional<Boolean> pickUpToStorage = Optional.empty();

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

    public TriggerBase specificItem(final int ...itemIds) {
        this.specificItem = Optional.of(itemIds);
        return this;
    }

    public TriggerBase onMenuOption(final String option) {
        this.onMenuOption = Optional.of(option);
        return this;
    }

    public TriggerBase pickUpToStorage() {
        this.pickUpToStorage = Optional.of(true);
        return this;
    }

    public TriggerBase use(final int ...itemIds) {
        this.use = Optional.of(itemIds);
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

    public TriggerBase onItemClick() {
        this.onItemClick = Optional.of(true);
        return this;
    }

    public TriggerBase notification(final String notification) {
        this.notificationCustom = Optional.of(notification);
        return this;
    }

    public TriggerBase equipped() {
        this.equipped = Optional.of(true);
        return this;
    }
}
