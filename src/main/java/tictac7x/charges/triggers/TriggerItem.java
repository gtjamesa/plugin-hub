package tictac7x.charges.triggers;

import javax.annotation.Nullable;

public class TriggerItem {
    public final int item_id;

    @Nullable public Integer fixed_charges;

    public TriggerItem(final int item_id) {
        this.item_id = item_id;
    }

    public TriggerItem fixedCharges(final int charges) {
        this.fixed_charges = charges;
        return this;
    }
}
