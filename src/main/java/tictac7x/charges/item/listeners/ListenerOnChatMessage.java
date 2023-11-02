package tictac7x.charges.item.listeners;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.TriggerBase;

import java.util.regex.Matcher;

@Slf4j
public class ListenerOnChatMessage extends ListenerBase {
    public ListenerOnChatMessage(final Client client, final ChargedItem chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
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
                log.debug(chargedItem.getItemName() + " charges dynamically set: " + getCleanCharges(matcher.group("charges")));
                chargedItem.setCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (trigger.increaseDynamically.isPresent()) {
                log.debug(chargedItem.getItemName() + " charges dynamically increased by: " + getCleanCharges(matcher.group("charges")));
                chargedItem.increaseCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (trigger.decreaseDynamically.isPresent()) {
                log.debug(chargedItem.getItemName() + " charges dynamically decreased by: " + getCleanCharges(matcher.group("charges")));
                chargedItem.decreaseCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (trigger.useDifference.isPresent()) {
                log.debug(chargedItem.getItemName() + " charges set from difference: " + (getCleanCharges(matcher.group("total")) - getCleanCharges(matcher.group("used"))));
                chargedItem.setCharges(getCleanCharges(matcher.group("total")) - getCleanCharges(matcher.group("used")));
                triggerUsed = true;
            }

            if (trigger.matcherConsumer.isPresent()) {
                log.debug(chargedItem.getItemName() + " custom matcher consumer run");
                trigger.matcherConsumer.get().accept(matcher);
                triggerUsed = true;
            }

            if (trigger.stringConsumer.isPresent()) {
                log.debug(chargedItem.getItemName() + " custom string consumer run");
                trigger.stringConsumer.get().accept(message);
                triggerUsed = true;
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final ChatMessage event) {
        if (!(triggerBase instanceof OnChatMessage)) return false;
        final OnChatMessage trigger = (OnChatMessage) triggerBase;

        // Message check.
        final String message = getCleanMessage(event);
        final Matcher matcher = trigger.message.matcher(message);
        if (!matcher.find()) {
            log.debug(chargedItem.getItemName() + " trigger check failed: chat message pattern");
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
