package tictac7x.charges.triggers;

import net.runelite.api.Skill;
import tictac7x.charges.ChargesImprovedConfig;
import java.util.Optional;

public class TriggerStat {
    public final Skill skill;
    public Optional<Integer> discharges = Optional.empty();
    public Optional<String[]> extra_config = Optional.empty();

    public TriggerStat(final Skill skill) {
        this.skill = skill;
    }

    public TriggerStat decreaseCharges(final int discharges) {
        this.discharges = Optional.of(discharges);
        return this;
    }

    public TriggerStat extraConfig(final String key, final ChargesImprovedConfig.Status status) {
        extra_config = Optional.of(new String[]{key, status.toString()});
        return this;
    }
}
