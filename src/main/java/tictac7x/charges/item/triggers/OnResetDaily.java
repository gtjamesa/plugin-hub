package tictac7x.charges.item.triggers;

import java.util.Optional;

public class OnResetDaily extends TriggerBase {
    public final int resetCharges;
    public Optional<Integer> resetSpecificItem = Optional.empty();

    public OnResetDaily(final int charges) {
        this.resetCharges = charges;
    }

    public OnResetDaily specificItem(final int itemId) {
        this.resetSpecificItem = Optional.of(itemId);
        return this;
    }
}
