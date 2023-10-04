package tictac7x.charges.triggers;

import net.runelite.api.Skill;

import javax.annotation.Nullable;

public class TriggerXPDrop {
    public final Skill skill;

    @Nullable public Integer discharges;

    public TriggerXPDrop(final Skill skill) {
        this.skill = skill;
    }

    public TriggerXPDrop decreaseCharges(final int discharges) {
        this.discharges = discharges;
        return this;
    }
}
