package tictac7x.charges.item.triggers;

import java.util.Optional;

public class OnMenuEntryAdded extends TriggerBase {
    public final Optional<String> menuEntryOption;
    public Optional<Boolean> hide = Optional.empty();
    public Optional<String> replaceOption = Optional.empty();
    public Optional<String[]> replaceTarget = Optional.empty();
    public Optional<int[]> replaceImpostorIds = Optional.empty();

    public OnMenuEntryAdded() {
        this.menuEntryOption = Optional.empty();
    }

    public OnMenuEntryAdded(final String option) {
        this.menuEntryOption = Optional.of(option);
    }

    public OnMenuEntryAdded hide() {
        this.hide = Optional.of(true);
        return this;
    }

    public OnMenuEntryAdded replaceOption(final String option) {
        this.replaceOption = Optional.of(option);
        return this;
    }

    public OnMenuEntryAdded replaceTarget(final String target, final String replace) {
        this.replaceTarget = Optional.of(new String[]{target, replace});
        return this;
    }

    public OnMenuEntryAdded isReplaceImpostorId(final int ...impostorIds) {
        this.replaceImpostorIds = Optional.of(impostorIds);
        return this;
    }
}
