package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TriggerWidget {
    @Nonnull public final String message;

    public int group_id;
    public int child_id;
    public Integer sub_child_id;

    public boolean increase_dynamically;

    @Nullable public Consumer<String> consumer;
    @Nullable public Integer charges;

    public TriggerWidget(final int group_id, final int child_id, final int sub_child_id, @Nonnull final String message) {
        this.group_id = group_id;
        this.child_id = child_id;
        this.sub_child_id = sub_child_id;
        this.message = message;
    }

    public TriggerWidget fixedCharges(final int charges) {
        this.charges = charges;
        return this;
    }
}
