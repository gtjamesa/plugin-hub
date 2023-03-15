package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TriggerMenuOption {
    @Nonnull
    public final String option;

    @Nullable
    public Integer fixed_charges;

    @Nullable
    public Consumer<String> consumer;

    public TriggerMenuOption(@Nonnull final String option) {
        this.option = option;
    }

    public TriggerMenuOption fixedCharges(final int charges) {
        this.fixed_charges = charges;
        return this;
    }

    public TriggerMenuOption extraConsumer(final Consumer<String> consumer) {
        this.consumer = consumer;
        return this;
    }
}
