package tictac7x.charges.triggers;

import net.runelite.api.HitsplatID;

public class TriggerHitsplat {
    public final boolean self;
    public final boolean successful;
    public final boolean equipped;
    public final int discharges;

    public TriggerHitsplat(final boolean self, final boolean successful, final int discharges, final boolean equipped) {
        this.self = self;
        this.successful = successful;
        this.equipped = equipped;
        this.discharges = discharges;
    }
}
