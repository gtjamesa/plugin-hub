package tictac7x.charges.item.triggers;

import net.runelite.api.Skill;
import tictac7x.charges.store.ConfigKeyActivity;
import tictac7x.charges.store.ItemActivity;
import java.util.Optional;

public class TriggerStat {
    public final Skill skill;
    public Optional<Integer> discharges = Optional.empty();
    public Optional<ConfigKeyActivity> extra_config = Optional.empty();

    public TriggerStat(final Skill skill) {
        this.skill = skill;
    }

    public TriggerStat decreaseCharges(final int discharges) {
        this.discharges = Optional.of(discharges);
        return this;
    }

    public TriggerStat extraConfig(final String key, final ItemActivity status) {
        extra_config = Optional.of(new ConfigKeyActivity(key, status));
        return this;
    }
}
