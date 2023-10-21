package tictac7x.charges.item.triggers;

import java.util.Optional;

public class TriggerMenuEntryAdded {
    public final String original;
    public Optional<String> replace = Optional.empty();
    public Optional<Boolean> hide = Optional.empty();

    public TriggerMenuEntryAdded(final String original) {
        this.original = original;
    }

    public TriggerMenuEntryAdded replace(final String replacement) {
        this.replace = Optional.of(replacement);
        return this;
    }

    public TriggerMenuEntryAdded hide() {
        this.hide = Optional.of(true);
        return this;
    }
}
