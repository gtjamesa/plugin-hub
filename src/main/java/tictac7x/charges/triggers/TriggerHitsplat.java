package tictac7x.charges.triggers;

import net.runelite.api.HitsplatID;
import tictac7x.charges.store.ItemStatus;

import java.util.Optional;

public class TriggerHitsplat {
    public final int hitsplat_id;
    public final int discharges;

    public boolean self;
    public boolean equipped;
    public boolean non_zero;
    public Optional<String[]> extra_config = Optional.empty();

    public TriggerHitsplat(final int discharges) {
        this.hitsplat_id = HitsplatID.DAMAGE_ME;
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

    public TriggerHitsplat extraConfig(final String key, final ItemStatus status) {
        extra_config = Optional.of(new String[]{key, status.toString()});
        return this;
    }
}
