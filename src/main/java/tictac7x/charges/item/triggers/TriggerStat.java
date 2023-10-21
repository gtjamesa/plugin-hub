package tictac7x.charges.item.triggers;

import net.runelite.api.Skill;
import tictac7x.charges.store.MenuEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TriggerStat {
    public final Skill skill;
    public boolean isActivated;
    public boolean onXpDrop;
    public Optional<Integer> increaseCharges = Optional.empty();
    public Optional<Integer> decreaseCharges = Optional.empty();
    public Optional<MenuEntry> menuEntry = Optional.empty();
    public List<Integer> specificItems = new ArrayList<>();
    public Optional<Integer> belowCharges = Optional.empty();

    public TriggerStat(final Skill skill) {
        this.skill = skill;
    }

    public TriggerStat increaseCharges(final int charges) {
        this.increaseCharges = Optional.of(charges);
        return this;
    }

    public TriggerStat decreaseCharges(final int charges) {
        this.decreaseCharges = Optional.of(charges);
        return this;
    }

    public TriggerStat isActivated() {
        this.isActivated = true;
        return this;
    }

    public TriggerStat menuEntry(final String target, final String option) {
        this.menuEntry = Optional.of(new MenuEntry(target, option));
        return this;
    }

    public TriggerStat specificItem(final int ...itemIds) {
        for (final int itemId : itemIds) {
            this.specificItems.add(itemId);
        }
        return this;
    }

    public TriggerStat belowCharges(final int charges) {
        this.belowCharges = Optional.of(charges);
        return this;
    }

    public TriggerStat onXpDrop() {
        this.onXpDrop = true;
        return this;
    }
}
