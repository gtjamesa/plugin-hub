package tictac7x.charges.item.listeners;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.store.ItemActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnChatMessage {
    final ChargedItem chargedItem;
    final ConfigManager configs;
    final Notifier notifier;

    public OnChatMessage(final ChargedItem chargedItem, final ConfigManager configs, final Notifier notifier) {
        this.chargedItem = chargedItem;
        this.configs = configs;
        this.notifier = notifier;
    }

    public void trigger(final ChatMessage event) {
        final String message = event.getMessage().replaceAll("</?col.*?>", "").replaceAll("<br>", " ").replaceAll("&nbsp;", "");

        for (final TriggerChatMessage trigger : chargedItem.triggersChatMessages) {
            if (!isValidTrigger(event, trigger)) continue;

            // Check trigger message pattern here, because we need the matcher object to extract charges from the message.
            final Matcher matcher = trigger.message.matcher(message);
            if (!matcher.find()) continue;

            // Notification.
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
                try {
                    final int used = Integer.parseInt(matcher.group("used"));
                    final int total = Integer.parseInt(matcher.group("total"));
                    chargedItem.setCharges(total - used);
                } catch (final Exception ignored) {
                }

            // Empty storage.
            } else if (trigger.emptyStorage.isPresent()) {
                trigger.emptyStorage.get().empty();

            // Set charges dynamically from the chat message.
            } else {
                try {
                    final int charges = Integer.parseInt(matcher.group("charges").replaceAll(",", "").replaceAll("\\.", ""));

                    // Increase dynamically.
                    if (trigger.increaseDynamically.isPresent()) {
                        chargedItem.increaseCharges(charges);

                    // Decrease dynamically.
                    } else if (trigger.decrease_dynamically) {
                        chargedItem.decreaseCharges(charges);

                    // Set dynamically.
                    } else {
                        chargedItem.setCharges(charges);
                    }
                } catch (final Exception ignored) {}
            }

            // Consumer
            if (trigger.consumer.isPresent()) {
                trigger.consumer.get().accept(matcher);
            }

            // String consumer.
            if (trigger.stringConsumer.isPresent()) {
                trigger.stringConsumer.get().accept(message);
            }

            // Charged item needs to be set as activated.
            if (trigger.activate) {
                chargedItem.activityCallback(ItemActivity.ACTIVATED);
            // Charged item needs to be set as deactivated.
            } else if (trigger.deactivate) {
                chargedItem.activityCallback(ItemActivity.DEACTIVATED);
            }

            // Check extra matches groups.
            if (chargedItem.extra_config_keys != null) {
                for (final String extra_group : chargedItem.extra_config_keys) {
                    try {
                        configs.setConfiguration(ChargesImprovedConfig.group, chargedItem.config_key + "_" + extra_group, matcher.group(extra_group).replaceAll(",", ""));
                    } catch (final Exception ignored) {}
                }
            }

            // Trigger used.
            return;
        }
    }

    private boolean isValidTrigger(final ChatMessage event, final TriggerChatMessage trigger) {
        final String message = event.getMessage();

        // Message type that we are not interested in.
        if (
            event.getType() != ChatMessageType.GAMEMESSAGE &&
            event.getType() != ChatMessageType.SPAM &&
            event.getType() != ChatMessageType.MESBOX &&
            event.getType() != ChatMessageType.DIALOG
        ) return false;

        // No config to save charges to.
        if (chargedItem.config_key == null) return false;

        // Message should be ignored.
        if (trigger.ignore_message != null) {
            final Matcher ignore_matcher = trigger.ignore_message.matcher(message);
            if (ignore_matcher.find()) return false;
        }

        // Specific item check.
        if (!trigger.specific_items.isEmpty() && !trigger.specific_items.contains(chargedItem.item_id)) return false;

        // Menu target check.
        if (trigger.menu_target && chargedItem.store.notInMenuTargets(chargedItem.getItemName())) return false;

        // Item needs to be equipped.
        if (trigger.equipped && !chargedItem.isEquipped()) return false;

        return true;
    }
}
