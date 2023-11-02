package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.Notifier;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerBase;

import java.util.ArrayList;
import java.util.List;

public class ListenerOnMenuEntryAdded extends ListenerBase {
    public ListenerOnMenuEntryAdded(final Client client, final ChargedItem chargedItem, final Notifier notifier, final ChargesImprovedConfig config) {
        super(client, chargedItem, notifier, config);
    }

    public void trigger(final MenuEntryAdded event) {
        for (final TriggerBase triggerBase : chargedItem.triggers) {
            if (!isValidTrigger(triggerBase, event)) continue;
            final OnMenuEntryAdded trigger = (OnMenuEntryAdded) triggerBase;
            boolean triggerUsed = false;

            if (trigger.replaceOption.isPresent()) {
                event.getMenuEntry().setOption(trigger.replaceOption.get());
                triggerUsed = true;
            }

            if (trigger.hide.isPresent()) {
                final List<MenuEntry> newMenuEntries = new ArrayList<>();

                for (final MenuEntry entry : client.getMenuEntries()) {
                    if (!entry.getOption().equals(trigger.menuEntryOption)) {
                        newMenuEntries.add(entry);
                    }
                }
                client.setMenuEntries(newMenuEntries.toArray(new MenuEntry[0]));

                triggerUsed = true;
            }

            if (super.trigger(trigger)) {
                triggerUsed = true;
            }

            if (triggerUsed) return;
        }
    }

    public boolean isValidTrigger(final TriggerBase triggerBase, final MenuEntryAdded event) {
        if (!(triggerBase instanceof OnMenuEntryAdded)) return false;
        final OnMenuEntryAdded trigger = (OnMenuEntryAdded) triggerBase;

        // Item id check.
        if (event.getMenuEntry().getItemId() != chargedItem.item_id) {
            return false;
        }

        // Hide config check.
        if (trigger.hide.isPresent() && !config.hideDestroy()) {
            return false;
        }

        // Menu entry option check.
        if (!event.getOption().equals(trigger.menuEntryOption)) {
            return false;
        }

        // Menu entry replace check.
        if (trigger.replaceOption.isPresent() && (
            !config.useCommonMenuEntries() ||
            !event.getOption().equals(trigger.menuEntryOption)
        )) {
            return false;
        }

        return super.isValidTrigger(trigger);
    }
}
