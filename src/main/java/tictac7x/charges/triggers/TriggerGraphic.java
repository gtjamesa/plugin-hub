package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerGraphic {
    public final int graphic_id;
    public final int charges;
    public final boolean decrease_charges;
    public final boolean equipped;
    @Nullable public final int[] unallowed_items;
    @Nullable public final String menu_option;

    public TriggerGraphic(final int graphic_id, final int discharges, final boolean equipped) {
        this.graphic_id = graphic_id;
        this.charges = discharges;
        this.equipped = equipped;
        this.unallowed_items = null;
        this.decrease_charges = true;
        this.menu_option = null;
    }

    public TriggerGraphic(final int graphic_id, final int charges, final boolean equipped, @Nonnull final int[] unallowed_items, @Nonnull final String menu_option) {
        this.graphic_id = graphic_id;
        this.charges = charges;
        this.unallowed_items = unallowed_items;
        this.menu_option = menu_option;
        this.equipped = equipped;
        this.decrease_charges = false;
    }
}
