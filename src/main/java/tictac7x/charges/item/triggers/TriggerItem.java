package tictac7x.charges.item.triggers;

import javax.annotation.Nullable;

public class TriggerItem {
    public final int item_id;
    public boolean quantity_charges;
    public boolean hide_overlay;

    @Nullable public Integer fixed_charges;
    @Nullable public Integer max_charges;

    public TriggerItem(final int item_id) {
        this.item_id = item_id;
    }

    public TriggerItem fixedCharges(final int charges) {
        this.fixed_charges = charges;
        return this;
    }

    public TriggerItem maxCharges(final int charges) {
        this.max_charges = charges;
        return this;
    }

    public TriggerItem quantityCharges() {
        this.quantity_charges = true;
        return this;
    }

    public TriggerItem hideOverlay() {
        this.hide_overlay = true;
        return this;
    }
}
