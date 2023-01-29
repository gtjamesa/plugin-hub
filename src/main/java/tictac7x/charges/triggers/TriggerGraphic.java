package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerGraphic {
    public final int graphic_id;
    public final int charges;
    public final boolean decrease_charges;
    public final boolean equipped;
    @Nullable public int[] unallowed_items;
    @Nullable public String menu_option;

    public TriggerGraphic(final int graphic_id, final int discharges, final boolean equipped) {
        this.graphic_id = graphic_id;
        this.charges = discharges;
        this.equipped = equipped;
        this.decrease_charges = true;
    }
}
