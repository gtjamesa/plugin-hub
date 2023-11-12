package tictac7x.charges.item.triggers;

import tictac7x.charges.store.HitsplatTarget;

import java.util.Optional;

public class OnHitsplatApplied extends TriggerBase {
    public final HitsplatTarget hitsplatTarget;

    public Optional<Boolean> moreThanZeroDamage = Optional.empty();

    public OnHitsplatApplied(final HitsplatTarget hitsplatTarget) {
        this.hitsplatTarget = hitsplatTarget;
    }

    public OnHitsplatApplied moreThanZeroDamage() {
        this.moreThanZeroDamage = Optional.of(true);
        return this;
    }
}
