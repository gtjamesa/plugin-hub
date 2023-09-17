package tictac7x.charges.triggers;

import net.runelite.api.HitsplatID;

import javax.annotation.Nullable;

public class TriggerHitsplat {
    public final int hitsplat_id;
    public final int discharges;

    public boolean self;
    public boolean equipped;


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

    public TriggerHitsplat onAnimations(final int[] animations) {
        return this;
    }
}
