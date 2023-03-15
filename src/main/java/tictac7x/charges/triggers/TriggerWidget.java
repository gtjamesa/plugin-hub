package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TriggerWidget {
    private final int WIDGET_DEFAULT_GROUP_ID = 193;
    private final int WIDGET_DEFAULT_CHILD_ID = 2;

    @Nullable
    public Consumer<String> consumer;

    @Nonnull
    public final String message;

    @Nullable
    public Integer charges;

    public int group_id;
    public int child_id;
    public Integer sub_child_id;

    public TriggerWidget(@Nonnull final String message) {
        this.message = message;
        this.group_id = WIDGET_DEFAULT_GROUP_ID;
        this.child_id = WIDGET_DEFAULT_CHILD_ID;
    }

    public TriggerWidget customWidget(final int group_id, final int child_id) {
        this.group_id = group_id;
        this.child_id = child_id;
        return this;
    }

    public TriggerWidget customWidget(final int group_id, final int child_id, final int sub_child_id) {
        this.group_id = group_id;
        this.child_id = child_id;
        this.sub_child_id = sub_child_id;
        return this;
    }

    public TriggerWidget fixedCharges(final int charges) {
        this.charges = charges;
        return this;
    }

    public TriggerWidget extraConsumer(final Consumer<String> consumer) {
        this.consumer = consumer;
        return this;
    }
}
