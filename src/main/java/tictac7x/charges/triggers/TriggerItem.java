package tictac7x.charges.triggers;

import javax.annotation.Nullable;

public class TriggerItem {
    public final int item_id;
    public boolean quantity_charges;
    public boolean hide_overlay;
    public boolean is_negative;

    @Nullable public Integer varbit_id;
    @Nullable public Integer varbit_value;
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

    public TriggerItem varbitChecker(final int varbit_id, final int varbit_value) {
        this.varbit_id = varbit_id;
        this.varbit_value = varbit_value;
        return this;
    }

    public TriggerItem hideOverlay() {
        this.hide_overlay = true;
        return this;
    }

    public TriggerItem isNegative() {
        this.is_negative = true;
        return this;
    }
}
