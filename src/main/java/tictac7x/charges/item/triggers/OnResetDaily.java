package tictac7x.charges.item.triggers;

import java.util.Optional;

public class OnResetDaily extends TriggerBase {
    public Optional<Integer> resetSpecificItem = Optional.empty();

    public OnResetDaily(final int charges) {
        this.fixedCharges = Optional.of(charges);
    }

    public OnResetDaily specificItem(final int itemId) {
        this.resetSpecificItem = Optional.of(itemId);
        return this;
    }
}
