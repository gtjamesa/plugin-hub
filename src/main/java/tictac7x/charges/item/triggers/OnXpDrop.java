package tictac7x.charges.item.triggers;

import net.runelite.api.Skill;

public class OnXpDrop extends TriggerBase {
    public final Skill skill;

    public OnXpDrop(final Skill skill) {
        this.skill = skill;
    }
}
