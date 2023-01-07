package tictac7x.charges.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TriggerChatMessage {
    @Nonnull
    public final String message;

    @Nullable
    public final Integer charges;

    public TriggerChatMessage(@Nonnull final String message, final int charges) {
        this.message = message;
        this.charges = charges;
    }

    public TriggerChatMessage(@Nonnull final String message) {
        this.message = message;
        this.charges = null;
    }
}
