package tictac7x.charges.triggers;

import net.runelite.api.HitsplatID;

import javax.annotation.Nullable;

public class TriggerHitsplat {
    public final boolean self;
    public final boolean successful;
    public final boolean equipped;
    public final int discharges;
    @Nullable public final Integer animation_unallowed;

    public TriggerHitsplat(final boolean self, final boolean successful, final int discharges, final boolean equipped) {
        this.self = self;
        this.successful = successful;
        this.equipped = equipped;
        this.discharges = discharges;
        this.animation_unallowed = null;
    }

    public TriggerHitsplat(final boolean self, final boolean successful, final int discharges, final boolean equipped, final int animation_unallowed) {
        this.self = self;
        this.successful = successful;
        this.equipped = equipped;
        this.discharges = discharges;
        this.animation_unallowed = animation_unallowed;
    }
}
