package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerAnimation {
    public final int animation_id;
    public final int charges;
    public final boolean decrease_charges;
    @Nullable public final int[] unallowed_items;
    @Nullable public final String menu_option;

    public TriggerAnimation(final int animation_id, final int discharges) {
        this.animation_id = animation_id;
        this.charges = discharges;
        this.unallowed_items = null;
        this.decrease_charges = true;
        this.menu_option = null;
    }

    public TriggerAnimation(final int animation_id, final int charges, @Nonnull final int[] unallowed_items, @Nonnull final String menu_option) {
        this.animation_id = animation_id;
        this.charges = charges;
        this.unallowed_items = unallowed_items;
        this.decrease_charges = false;
        this.menu_option = menu_option;
    }
}
