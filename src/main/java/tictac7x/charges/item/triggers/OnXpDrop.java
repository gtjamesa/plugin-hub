package tictac7x.charges.item.triggers;

import net.runelite.api.Skill;

import java.util.Optional;
import java.util.function.Consumer;

public class OnXpDrop extends TriggerBase {
    public final Skill skill;
    public final Optional<Integer> amount;

    public OnXpDrop(final Skill skill) {
        this.skill = skill;
        this.amount = Optional.empty();
    }

    public OnXpDrop(final Skill skill, final int amount) {
        this.skill = skill;
        this.amount = Optional.of(amount);
    }
}
