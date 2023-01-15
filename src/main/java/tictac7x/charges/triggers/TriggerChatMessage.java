package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class TriggerChatMessage {
    @Nonnull
    public final Pattern message;

    @Nullable
    public final Integer charges;

    public TriggerChatMessage(@Nonnull final String message, final int charges) {
        this.message = Pattern.compile(message);
        this.charges = charges;
    }

    public TriggerChatMessage(@Nonnull final String message) {
        this.message = Pattern.compile(message);
        this.charges = null;
    }
}
