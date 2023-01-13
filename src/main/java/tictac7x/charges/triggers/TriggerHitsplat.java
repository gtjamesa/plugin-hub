package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerHitsplat {
    public final boolean self;
    public final boolean successful;
    public final boolean equipped;
    public final int discharges;
    @Nullable public final int[] animation;

    public TriggerHitsplat(final boolean self, final boolean successful, final int discharges, final boolean equipped) {
        this.self = self;
        this.successful = successful;
        this.equipped = equipped;
        this.discharges = discharges;
        this.animation = null;
    }

    public TriggerHitsplat(final boolean self, final boolean successful, final int discharges, final boolean equipped, @Nonnull final int[] animation) {
        this.self = self;
        this.successful = successful;
        this.equipped = equipped;
        this.discharges = discharges;
        this.animation = animation;
    }
}
