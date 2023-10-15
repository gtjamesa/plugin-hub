package tictac7x.charges.item.triggers;

import tictac7x.charges.store.ItemActivity;

import java.util.Optional;

public class TriggerHitsplat {
    public final int discharges;

    public boolean self;
    public boolean equipped;
    public boolean non_zero;
    public boolean isActivated;

    public TriggerHitsplat(final int discharges) {
        this.discharges = discharges;
    }

    public TriggerHitsplat onSelf() {
        this.self = true;
        return this;
    }

    public TriggerHitsplat onEnemy() {
        this.self = false;
        return this;
    }

    public TriggerHitsplat equipped() {
        this.equipped = true;
        return this;
    }

    public TriggerHitsplat nonZero() {
        this.non_zero = true;
        return this;
    }

    public TriggerHitsplat isActivated() {
        this.isActivated = true;
        return this;
    }
}
