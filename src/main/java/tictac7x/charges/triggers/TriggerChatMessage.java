package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class TriggerChatMessage {
    @Nonnull
    public final Pattern message;

    @Nullable
    public Integer charges;

    public boolean menu_target;

    public TriggerChatMessage(@Nonnull final String message) {
        this.message = Pattern.compile(message);
    }

    public TriggerChatMessage fixedCharges(final int charges) {
        this.charges = charges;
        return this;
    }

    public TriggerChatMessage onItemClick() {
        this.menu_target = true;
        return this;
    }
}
