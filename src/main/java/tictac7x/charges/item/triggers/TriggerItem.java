package tictac7x.charges.item.triggers;

import javax.annotation.Nullable;
import java.util.Optional;

public class TriggerItem {
    public final int item_id;
    public boolean quantity_charges;
    public boolean hide_overlay;
    public boolean needsToBeEquipped;
    public boolean zeroChargesIsPositive;
    public Optional<Integer> negativeFullCharges = Optional.empty();

    @Nullable public Integer fixed_charges;

    public TriggerItem(final int item_id) {
        this.item_id = item_id;
    }

    public TriggerItem fixedCharges(final int charges) {
        this.fixed_charges = charges;
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

    public TriggerItem needsToBeEquipped() {
        this.needsToBeEquipped = true;
        return this;
    }

    public TriggerItem zeroChargesIsPositive() {
        this.zeroChargesIsPositive = true;
        return this;
    }

    public TriggerItem negativeFullCharges(final int charges) {
        this.negativeFullCharges = Optional.of(charges);
        return this;
    }
}
