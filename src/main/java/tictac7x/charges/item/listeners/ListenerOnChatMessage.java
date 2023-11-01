package tictac7x.charges.item.listeners;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.TriggerBase;

import java.util.regex.Matcher;

public class ListenerOnChatMessage extends ListenerBase {
    public ListenerOnChatMessage(final ChargedItem chargedItem, final Notifier notifier) {
        super(chargedItem, notifier);
    }

    public void trigger(final ChatMessage event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            boolean triggerUsed = false;
            final OnChatMessage trigger = (OnChatMessage) triggerBase;

            final String message = getCleanMessage(event);
            final Matcher matcher = trigger.message.matcher(message);
            matcher.find();

            if (trigger.setDynamically.isPresent()) {
                chargedItem.setCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (trigger.increaseDynamically.isPresent()) {
                chargedItem.increaseCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (trigger.decreaseDynamically.isPresent()) {
                chargedItem.decreaseCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (trigger.useDifference.isPresent()) {
                chargedItem.setCharges(getCleanCharges(matcher.group("total")) - getCleanCharges(matcher.group("used")));
                triggerUsed = true;
            }

            if (trigger.matcherConsumer.isPresent()) {
                trigger.matcherConsumer.get().accept(matcher);
                triggerUsed = true;
            }

            if (trigger.stringConsumer.isPresent()) {
                trigger.stringConsumer.get().accept(message);
                triggerUsed = true;
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(TriggerBase triggerBase, final ChatMessage event) {
        if (!(triggerBase instanceof OnChatMessage)) return false;
        final OnChatMessage trigger = (OnChatMessage) triggerBase;

        // Message check.
        final String message = getCleanMessage(event);
        final Matcher matcher = trigger.message.matcher(message);
        if (!matcher.find()) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }

    private String getCleanMessage(final ChatMessage event) {
        return event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");
    }

    private int getCleanCharges(final String charges) {
        return Integer.parseInt(charges.replaceAll(",", "").replaceAll("\\.", ""));
    }
}
