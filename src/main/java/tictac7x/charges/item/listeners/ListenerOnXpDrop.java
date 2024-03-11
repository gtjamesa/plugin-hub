package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.item.triggers.OnXpDrop;
import tictac7x.charges.item.triggers.TriggerBase;

public class ListenerOnXpDrop extends ListenerBase {
    public ListenerOnXpDrop(final Client client, final ChargedItemBase chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
    }

    public void trigger(final StatChanged event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            final OnXpDrop trigger = (OnXpDrop) triggerBase;
            boolean triggerUsed = false;

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final StatChanged event) {
        if (!(triggerBase instanceof OnXpDrop)) return false;
        final OnXpDrop trigger = (OnXpDrop) triggerBase;
        final Skill skill = event.getSkill();

        // Skill check.
        if (trigger.skill != skill) {
            return false;
        }

        // XP drop check.
        if (
            !chargedItem.store.getSkillXp(skill).isPresent() ||
            chargedItem.store.getSkillXp(skill).get() == event.getXp()
        ) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }
}
