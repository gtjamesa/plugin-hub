package tictac7x.charges.item.triggers;

import java.util.Optional;
import java.util.function.Consumer;

public class OnVarbitChanged extends TriggerBase {
    public final int varbitId;

    public Optional<Integer> isVarbitValue = Optional.empty();
    public Optional<Consumer<Integer>> varbitValueConsumer = Optional.empty();
    public Optional<Boolean> setDynamically = Optional.empty();

    public OnVarbitChanged(final int varbitId) {
        this.varbitId = varbitId;
    }

    public OnVarbitChanged isVarbitValue(final int varbitValue) {
        this.isVarbitValue = Optional.of(varbitValue);
        return this;
    }

    public OnVarbitChanged varbitValueConsumer(final Consumer<Integer> consumer) {
        this.varbitValueConsumer = Optional.of(consumer);
        return this;
    }

    public OnVarbitChanged setDynamically() {
        this.setDynamically = Optional.of(true);
        return this;
    }
}
