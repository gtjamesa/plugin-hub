package tictac7x.charges.item.triggers;

import java.util.Optional;

public class TriggerMenuOptionClicked {
    public final String option;

    public Optional<Integer> itemId = Optional.empty();
    public Optional<String> target = Optional.empty();
    public Optional<Integer> decreaseCharges = Optional.empty();

    public TriggerMenuOptionClicked(final String option) {
        this.option = option;
    }

    public TriggerMenuOptionClicked target(final String target) {
        this.target = Optional.of(target);
        return this;
    }

    public TriggerMenuOptionClicked itemId(final int itemId) {
        this.itemId = Optional.of(itemId);
        return this;
    }

    public TriggerMenuOptionClicked decreaseCharges(final int charges) {
        this.decreaseCharges = Optional.of(charges);
        return this;
    }
}
