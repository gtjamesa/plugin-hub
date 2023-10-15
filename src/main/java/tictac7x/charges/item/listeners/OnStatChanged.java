package tictac7x.charges.item.listeners;

import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerStat;

public class OnStatChanged {
    final ChargedItem chargedItem;
    final ConfigManager configs;

    public OnStatChanged(final ChargedItem chargedItem, final ConfigManager configs) {
        this.chargedItem = chargedItem;
        this.configs = configs;
    }

    public void trigger(final StatChanged event) {
        for (final TriggerStat trigger : chargedItem.triggersStat) {
            if (!isValidTrigger(event, trigger)) continue;

            if (trigger.discharges.isPresent()) {
                chargedItem.decreaseCharges(trigger.discharges.get());
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final StatChanged event, final TriggerStat trigger) {
        // Skill check.
        if (trigger.skill != event.getSkill()) return false;

        // Extra config check.
        if (
            trigger.extra_config.isPresent() &&
            !configs.getConfiguration(ChargesImprovedConfig.group, trigger.extra_config.get().key).equals(trigger.extra_config.get().activity.toString())
        ) return false;

        return true;
    }
}
