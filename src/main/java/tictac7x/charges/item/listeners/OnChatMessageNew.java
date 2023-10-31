package tictac7x.charges.item.listeners;

import net.runelite.api.events.ChatMessage;
import tictac7x.charges.item.ChargedItemTrigger;

import java.util.regex.Matcher;

public class OnChatMessageNew {
    public boolean onTrigger(final ChargedItemTrigger trigger, final ChatMessage event) {
        final String message = getCleanMessage(event);
        final Matcher matcher = trigger.onChatMessage.get().matcher(message);
        matcher.find();

        if (trigger.matcherConsumer.isPresent()) {
            trigger.matcherConsumer.get().accept(matcher);
            return true;
        }

        return false;
    }

    public boolean isValidTrigger(final ChargedItemTrigger trigger, final ChatMessage event) {
        // Chat message check.
        if (!trigger.onChatMessage.isPresent()) {
            return false;
        }

        // Message check.
        final String message = getCleanMessage(event);
        final Matcher matcher = trigger.onChatMessage.get().matcher(message);
        if (!matcher.find()) {
            return false;
        }

        return true;
    }

    private String getCleanMessage(final ChatMessage event) {
        return event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");
    }
}
