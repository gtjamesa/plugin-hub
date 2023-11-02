package tictac7x.charges.item.triggers;

import java.util.Optional;

public class OnVarbitChanged extends TriggerBase {
    public final int varbitId;

    public Optional<Integer> isVarbitValue = Optional.empty();

    public OnVarbitChanged(final int varbitId) {
        this.varbitId = varbitId;
    }

    public OnVarbitChanged isVarbitValue(final int varbitValue) {
        this.isVarbitValue = Optional.of(varbitValue);
        return this;
    }
}
