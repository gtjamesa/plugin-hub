package tictac7x.charges.item.triggers;

import java.util.Optional;

public class TriggerDailyReset {
    public final int charges;
    public Optional<Integer> specificItem = Optional.empty();

    public TriggerDailyReset(final int charges) {
        this.charges = charges;
    }

    public TriggerDailyReset specificItem(final int itemId) {
        this.specificItem = Optional.of(itemId);
        return this;
    }
}
