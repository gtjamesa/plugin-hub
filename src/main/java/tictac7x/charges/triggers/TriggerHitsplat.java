package tictac7x.charges.triggers;

import net.runelite.api.HitsplatID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerHitsplat {
    public final boolean self;
    public final int hitsplat_id;
    public final boolean equipped;
    public final int discharges;
    @Nullable public final int[] animation;

    public TriggerHitsplat(final boolean self, final boolean equipped, final int discharges) {
        this.self = self;
        this.hitsplat_id = HitsplatID.DAMAGE_ME;
        this.equipped = equipped;
        this.discharges = discharges;
        this.animation = null;
    }

    public TriggerHitsplat(final boolean self, final boolean equipped, final int discharges, @Nonnull final int[] animation) {
        this.self = self;
        this.hitsplat_id = HitsplatID.DAMAGE_ME;
        this.equipped = equipped;
        this.discharges = discharges;
        this.animation = animation;
    }
}
