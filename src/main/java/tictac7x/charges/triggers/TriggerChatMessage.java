package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class TriggerChatMessage {
    @Nonnull
    public final Pattern message;

    @Nullable
    public Consumer<String> consumer;

    @Nullable
    public Integer fixed_charges;

    @Nullable
    public Integer decrease_charges;

    @Nullable
    public Integer increase_charges;

    public boolean menu_target;

    public boolean equipped;

    public TriggerChatMessage(@Nonnull final String message) {
        this.message = Pattern.compile(message);
    }

    public TriggerChatMessage fixedCharges(final int charges) {
        this.fixed_charges = charges;
        return this;
    }

    public TriggerChatMessage onItemClick() {
        this.menu_target = true;
        return this;
    }

    public TriggerChatMessage increaseCharges(final int charges) {
        this.increase_charges = charges;
        return this;
    }

    public TriggerChatMessage decreaseCharges(final int charges) {
        this.decrease_charges = charges;
        return this;
    }

    public TriggerChatMessage equipped() {
        this.equipped = true;
        return this;
    }

    public TriggerChatMessage extraConsumer(final Consumer<String> consumer) {
        this.consumer = consumer;
        return this;
    }
}
