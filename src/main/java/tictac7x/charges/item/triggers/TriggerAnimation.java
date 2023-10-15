package tictac7x.charges.item.triggers;

import tictac7x.charges.store.MenuEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TriggerAnimation {
    public final int animation_id;

    public int charges;
    public boolean decrease_charges;
    public boolean equipped;
    public Optional<MenuEntry> menuEntry = Optional.empty();
    public List<Integer> unallowed_items = new ArrayList<>();

    public TriggerAnimation(final int animation_id) {
        this.animation_id = animation_id;
    }

    public TriggerAnimation increaseCharges(final int charges) {
        this.charges = charges;
        this.decrease_charges = false;
        return this;
    }

    public TriggerAnimation decreaseCharges(final int decharges) {
        this.charges = decharges;
        this.decrease_charges = true;
        return this;
    }

    public TriggerAnimation equipped() {
        this.equipped = true;
        return this;
    }

    public TriggerAnimation unallowedItems(final int ...unallowedItemsIds) {
        for (final int unallowedItemId : unallowedItemsIds) {
            this.unallowed_items.add(unallowedItemId);
        }
        return this;
    }

    public TriggerAnimation menuEntry(final String target, final String option) {
        this.menuEntry = Optional.of(new MenuEntry(target, option));
        return this;
    }
}
