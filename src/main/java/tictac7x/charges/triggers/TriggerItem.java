package tictac7x.charges.triggers;

import javax.annotation.Nullable;

public class TriggerItem {
    public final int item_id;

    @Nullable
    public final Integer charges;

    public TriggerItem(final int item_id, final int charges) {
        this.item_id = item_id;
        this.charges = charges;
    }

    public TriggerItem(final int item_id) {
        this.item_id = item_id;
        this.charges = null;
    }
}
