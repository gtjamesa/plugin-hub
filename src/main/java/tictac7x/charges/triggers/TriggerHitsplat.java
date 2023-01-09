package tictac7x.charges.triggers;

import net.runelite.api.HitsplatID;

public class TriggerHitsplat {
    public final boolean self;
    public final boolean equipped;
    public final int hitsplat_id;
    public final int discharges;

    public TriggerHitsplat(final boolean self, final int hitsplat_id, final int discharges, final boolean equipped) {
        this.self = self;
        this.equipped = equipped;
        this.hitsplat_id = hitsplat_id;
        this.discharges = discharges;
    }
}
