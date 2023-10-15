package tictac7x.charges.item.triggers;

import net.runelite.api.Skill;

import java.util.Optional;

public class TriggerStat {
    public final Skill skill;
    public Optional<Integer> discharges = Optional.empty();
    public boolean isActivated;

    public TriggerStat(final Skill skill) {
        this.skill = skill;
    }

    public TriggerStat decreaseCharges(final int discharges) {
        this.discharges = Optional.of(discharges);
        return this;
    }

    public TriggerStat isActivated() {
        this.isActivated = true;
        return this;
    }
}
