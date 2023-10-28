package tictac7x.charges.item.triggers;

import java.util.Optional;

public class TriggerMenuOptionClicked {
    public final String option;

    public Optional<int[]> itemId = Optional.empty();
    public Optional<String> target = Optional.empty();
    public Optional<Integer> decreaseCharges = Optional.empty();
    public Optional<Boolean> equipped = Optional.empty();
    public Optional<Runnable> consumer = Optional.empty();
    public Optional<Boolean> atBank = Optional.empty();
    public Optional<String[][]> useMenuTargets = Optional.empty();

    public TriggerMenuOptionClicked(final String option) {
        this.option = option;
    }

    public TriggerMenuOptionClicked use(final String[] targets1, final String[] targets2) {
        this.useMenuTargets = Optional.of(new String[][]{
            targets1,
            targets2
        });
        return this;
    }

    public TriggerMenuOptionClicked target(final String target) {
        this.target = Optional.of(target);
        return this;
    }

    public TriggerMenuOptionClicked itemId(final int ...itemId) {
        this.itemId = Optional.of(itemId);
        return this;
    }

    public TriggerMenuOptionClicked decreaseCharges(final int charges) {
        this.decreaseCharges = Optional.of(charges);
        return this;
    }

    public TriggerMenuOptionClicked equipped() {
        this.equipped = Optional.of(true);
        return this;
    }

    public TriggerMenuOptionClicked consumer(final Runnable consumer) {
        this.consumer = Optional.of(consumer);
        return this;
    }

    public TriggerMenuOptionClicked atBank() {
        this.atBank = Optional.of(true);
        return this;
    }
}
