package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerAnimation {
    public final int animation_id;
    public final int charges;
    public boolean decrease_charges;
    public boolean equipped;
    @Nullable public int[] unallowed_items;
    @Nullable public String menu_option;

    public TriggerAnimation(final int animation_id, final int discharges) {
        this.animation_id = animation_id;
        this.charges = discharges;
        this.decrease_charges = true;
    }

    public TriggerAnimation unallowedItems(final int[] unallowed_items) {
        this.unallowed_items = unallowed_items;
        return this;
    }

    public TriggerAnimation equipped() {
        this.equipped = true;
        return this;
    }
}
