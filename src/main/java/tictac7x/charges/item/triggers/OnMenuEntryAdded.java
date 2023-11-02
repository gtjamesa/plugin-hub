package tictac7x.charges.item.triggers;

import java.util.Optional;

public class OnMenuEntryAdded extends TriggerBase {
    public final String menuEntryOption;

    public Optional<Boolean> hide = Optional.empty();
    public Optional<String> replaceOption = Optional.empty();

    public OnMenuEntryAdded(final String option) {
        this.menuEntryOption = option;
    }

    public OnMenuEntryAdded hide() {
        this.hide = Optional.of(true);
        return this;
    }

    public TriggerBase replaceOption(final String option) {
        this.replaceOption = Optional.of(option);
        return this;
    }
}
