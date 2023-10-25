package tictac7x.charges.item.listeners;

import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerDailyReset;
import tictac7x.charges.item.triggers.TriggerItem;

import java.util.ArrayList;
import java.util.List;

public class OnResetDaily {
    final ChargedItem chargedItem;
    final ChargesImprovedConfig config;

    public OnResetDaily(final ChargedItem chargedItem, final ChargesImprovedConfig config) {
        this.chargedItem = chargedItem;
        this.config = config;
    }

    public void trigger() {
        for (final TriggerDailyReset trigger : chargedItem.triggersResetsDaily) {
            if (!isValidTrigger(trigger)) continue;

            chargedItem.setCharges(trigger.charges);
        }
    }
    
    private boolean isValidTrigger(final TriggerDailyReset trigger) {
        final List<Integer> items = new ArrayList<>();

        for (final String itemString : config.getStorage().split(",")) {
            try {
                items.add(Integer.parseInt(itemString));
            } catch (final Exception ignored) {}
        }

        // Specific item check.
        if (trigger.specificItem.isPresent() && !items.contains(trigger.specificItem.get())) return false;

        // General item check.
        if (!trigger.specificItem.isPresent()) {
            boolean check = false;

            for (final TriggerItem triggerItem : chargedItem.triggersItems) {
                if (items.contains(triggerItem.item_id)) {
                    check = true;
                    break;
                }
            }

            if (!check) return false;
        }

        return true;
    }
}
