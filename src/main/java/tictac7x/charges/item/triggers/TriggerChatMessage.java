package tictac7x.charges.item.triggers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class TriggerChatMessage {
    public final Pattern message;

    public boolean menu_target;
    public boolean equipped;
    public boolean multiple_charges;
    public Optional<Boolean> increaseDynamically = Optional.empty();
    public boolean decrease_dynamically;
    public boolean use_difference;
    public boolean notification;
    public boolean activate;
    public boolean deactivate;

    public List<Integer> specific_items = new ArrayList<>();
    @Nullable public Pattern ignore_message;
    @Nullable public Integer fixed_charges;
    @Nullable public Integer decrease_charges;
    @Nullable public Integer increase_charges;
    @Nullable public String notification_message;

    public TriggerChatMessage(@Nonnull final String message) {
        this.message = Pattern.compile(message);
    }

    public TriggerChatMessage fixedCharges(final int charges) {
        this.fixed_charges = charges;
        return this;
    }

    public TriggerChatMessage onItemClick() {
        this.menu_target = true;
        return this;
    }

    public TriggerChatMessage increaseCharges(final int charges) {
        this.increase_charges = charges;
        return this;
    }

    public TriggerChatMessage decreaseCharges(final int charges) {
        this.decrease_charges = charges;
        return this;
    }

    public TriggerChatMessage equipped() {
        this.equipped = true;
        return this;
    }

    public TriggerChatMessage multipleCharges() {
        this.multiple_charges = true;
        return this;
    }

    public TriggerChatMessage increaseDynamically() {
        this.increaseDynamically = Optional.of(true);
        return this;
    }

    public TriggerChatMessage decreaseDynamically() {
        this.decrease_dynamically = true;
        return this;
    }

    public TriggerChatMessage notification() {
        this.notification = true;
        return this;
    }

    public TriggerChatMessage notification(final String message) {
        this.notification = true;
        this.notification_message = message;
        return this;
    }

    public TriggerChatMessage useDifference() {
        this.use_difference = true;
        return this;
    }

    public TriggerChatMessage ignore(final String ignore_message) {
        this.ignore_message = Pattern.compile(ignore_message);
        return this;
    }

    public TriggerChatMessage specificItem(final int ...item_ids) {
        for (final int item_id : item_ids) {
            this.specific_items.add(item_id);
        }
        return this;
    }

    public TriggerChatMessage activate() {
        this.activate = true;
        return this;
    }

    public TriggerChatMessage deactivate() {
        this.deactivate = true;
        return this;
    }
}
