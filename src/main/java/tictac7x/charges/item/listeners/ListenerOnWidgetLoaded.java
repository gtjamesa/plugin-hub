package tictac7x.charges.item.listeners;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnWidgetLoaded;
import tictac7x.charges.item.triggers.TriggerBase;

import java.util.regex.Matcher;

@Slf4j
public class ListenerOnWidgetLoaded extends ListenerBase {
    public ListenerOnWidgetLoaded(final Client client, final ChargedItem chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
    }

    public void trigger(final WidgetLoaded event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            boolean triggerUsed = false;
            final OnWidgetLoaded trigger = (OnWidgetLoaded) triggerBase;
            final Widget widget = getWidget(trigger);

            final String text = getCleanText(widget.getText());
            final Matcher matcher = trigger.text.matcher(text);
            matcher.find();

            if (trigger.setDynamically.isPresent()) {
                chargedItem.setCharges(getCleanCharges(matcher.group("charges")));
                triggerUsed = true;
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final WidgetLoaded event) {
        if (!(triggerBase instanceof OnWidgetLoaded)) return false;
        final OnWidgetLoaded trigger = (OnWidgetLoaded) triggerBase;
        final Widget widget = getWidget(trigger);

        // Widget group check.
        if (event.getGroupId() != trigger.groupId) {
            return false;
        }

        // Widget existance check.
        if (widget == null) {
            return false;
        }

        // Text check.
        final String text = getCleanText(widget.getText());
        final Matcher matcher = trigger.text.matcher(text);
        if (!matcher.find()) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }

    private String getCleanText(final String text) {
        return text.replaceAll("</?col.*?>", "").replaceAll("<br>", " ");
    }

    private int getCleanCharges(final String charges) {
        return Integer.parseInt(charges.replaceAll(",", "").replaceAll("\\.", ""));
    }

    private Widget getWidget(final OnWidgetLoaded trigger) {
        Widget widget = client.getWidget(trigger.groupId, trigger.childId);

        if (widget != null) {
            widget = widget.getChild(trigger.subChildId);
        }

        return widget;
    }
}
