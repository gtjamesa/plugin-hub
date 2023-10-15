package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerWidget;

import java.util.regex.Matcher;

public class OnWidgetLoaded {
    final ChargedItem chargedItem;
    final Client client;
    final ConfigManager configs;
    final ClientThread clientThread;

    public OnWidgetLoaded(final ChargedItem chargedItem, final ConfigManager configs, final Client client, final ClientThread clientThread) {
        this.chargedItem = chargedItem;
        this.client = client;
        this.configs = configs;
        this.clientThread = clientThread;
    }

    public void trigger(final WidgetLoaded event) {
        clientThread.invokeLater(() -> {
            for (final TriggerWidget trigger : chargedItem.triggersWidgets) {
                if (!isValidTrigger(event, trigger)) continue;

                // Find correct widget.
                Widget widget = client.getWidget(trigger.group_id, trigger.child_id);
                if (trigger.sub_child_id != null && widget != null) widget = widget.getChild(trigger.sub_child_id);
                if (widget == null) continue;

                // Widget text does not match trigger message.
                final String message = widget.getText().replaceAll("</?col.*?>", "").replaceAll("<br>", " ");
                final Matcher matcher = trigger.message.matcher(message);
                if (!matcher.find()) continue;

                // Charges amount is fixed.
                if (trigger.charges != null) {
                    chargedItem.setCharges(trigger.charges);

                // Charges amount has custom logic.
                } else if (trigger.consumer != null) {
                    trigger.consumer.accept(message);

                // Charges amount is dynamic.
                } else if (matcher.group("charges") != null) {
                    final int charges = Integer.parseInt(matcher.group("charges").replaceAll(",", ""));

                    // Charges increased dynamically.
                    if (trigger.increase_dynamically) {
                        chargedItem.increaseCharges(charges);

                    // Charges set dynamically.
                    } else {
                        chargedItem.setCharges(charges);
                    }
                }

                // Check extra matches groups.
                if (chargedItem.extra_config_keys != null) {
                    for (final String extra_group : chargedItem.extra_config_keys) {
                        try {
                            final String extra = matcher.group(extra_group);
                            if (extra != null) configs.setConfiguration(ChargesImprovedConfig.group, chargedItem.config_key + "_" + extra_group, extra);
                        } catch (final Exception ignored) {}
                    }
                }
            }
        });
    }

    private boolean isValidTrigger(final WidgetLoaded event, final TriggerWidget trigger) {
        // Wrong widget group.
        if (event.getGroupId() != trigger.group_id) return false;

        return true;
    }
}
