package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerWidget {
    @Nonnull
    public final String message;

    @Nullable
    public Integer charges;

    public int group_id;
    public int child_id;

    public TriggerWidget(@Nonnull final String message) {
        this.message = message;
        this.group_id = 193;
        this.child_id = 2;
    }

    public TriggerWidget customWidget(final int group_id, final int child_id) {
        this.group_id = group_id;
        this.child_id = child_id;
        return this;
    }

    public TriggerWidget fixedCharges(final int charges) {
        this.charges = charges;
        return this;
    }
}
