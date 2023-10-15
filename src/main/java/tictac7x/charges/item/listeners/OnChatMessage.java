package tictac7x.charges.item.listeners;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerChatMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnChatMessage {
    final ChargedItem chargedItem;
    final Notifier notifier;

    public OnChatMessage(final ChargedItem chargedItem, final Notifier notifier) {
        this.chargedItem = chargedItem;
        this.notifier = notifier;
    }

    public void trigger(final ChatMessage event) {
        if (
            // Message type that we are not interested in.
            event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.MESBOX ||
            // No config to save charges to.
            chargedItem.config_key == null ||
            // Not in inventory or in equipment.
            (!chargedItem.in_inventory && !chargedItem.in_equipment)
        ) return;

        final String message = event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");

        for (final TriggerChatMessage trigger : chargedItem.triggersChatMessages) {
            final Matcher matcher = trigger.message.matcher(message);

            // Message should be ignored.
            if (trigger.ignore_message != null) {
                final Matcher ignore_matcher = trigger.ignore_message.matcher(message);
                if (ignore_matcher.find()) return;
            }

            // Specific item check.
            if (!trigger.specific_items.isEmpty() && !trigger.specific_items.contains(chargedItem.item_id)) continue;

            // Message does not match the pattern.
            if (!matcher.find()) continue;

            // Menu target check.
            if (trigger.menu_target && chargedItem.store.notInMenuTargets(chargedItem.getItemName())) continue;

            // Item needs to be equipped.
            if (trigger.equipped && !chargedItem.in_equipment) continue;

            // Notifications.
            if (trigger.notification) {
                notifier.notify(trigger.notification_message != null ? trigger.notification_message : message);
            }

            // Increase charges by fixed amount.
            if (trigger.increase_charges != null) {
                chargedItem.increaseCharges(trigger.increase_charges);

            // Decrease charges by fixed amount.
            } else if (trigger.decrease_charges != null) {
                chargedItem.decreaseCharges(trigger.decrease_charges);

            // Set charges to fixed amount.
            } else if (trigger.fixed_charges != null) {
                chargedItem.setCharges(trigger.fixed_charges);

            // Set charges from multiple amounts.
            } else if (trigger.multiple_charges) {
                int charges = 0;

                final Matcher matcher_multiple = Pattern.compile(".*?(\\d+)").matcher(message);
                while (matcher_multiple.find()) {
                    charges += Integer.parseInt(matcher_multiple.group(1));
                }

                chargedItem.setCharges(charges);

            // Charges need to be calculated from total - used.
            } else if (trigger.use_difference) {
                final int used = Integer.parseInt(matcher.group("used"));
                final int total = Integer.parseInt(matcher.group("total"));
                chargedItem.setCharges(total - used);

            // Set charges dynamically from the chat message.
            } else {
                try {
                    final int charges = Integer.parseInt(matcher.group("charges").replaceAll(",", "").replaceAll("\\.", ""));

                    // Increase dynamically.
                    if (trigger.increase_dynamically) {
                        chargedItem.increaseCharges(charges);
                        // Decrease dynamically.
                    } else if (trigger.decrease_dynamically) {
                        chargedItem.decreaseCharges(charges);
                    } else {
                        chargedItem.setCharges(charges);
                    }
                } catch (final Exception ignored) {}
            }

            // Extra consumer.
            if (trigger.extra_consumer != null) {
                trigger.extra_consumer.accept(message);
            }

            // Check extra matches groups.
            if (chargedItem.extra_config_keys != null) {
                for (final String extra_group : chargedItem.extra_config_keys) {
                    try {
                        chargedItem.setConfiguration(chargedItem.config_key + "_" + extra_group, matcher.group(extra_group).replaceAll(",", ""));
                    } catch (final Exception ignored) {}
                }
            }

            // Chat message used, no need to check other messages.
            return;
        }
    }
}
