package tictac7x.charges.item.listeners;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.item.triggers.TriggerMenuEntryAdded;

import java.util.ArrayList;
import java.util.List;

public class OnMenuEntryAdded {
    final ChargedItem chargedItem;
    final Client client;
    final ChargesImprovedConfig config;

    public OnMenuEntryAdded(final ChargedItem chargedItem, final Client client, final ChargesImprovedConfig config) {
        this.chargedItem = chargedItem;
        this.client = client;
        this.config = config;
    }

    public void trigger(final MenuEntryAdded event) {
        for (final TriggerMenuEntryAdded trigger : chargedItem.triggersMenusEntriesAdded) {
            if (!isValidTrigger(event, trigger)) continue;

            // Replace option.
            if (config.useCommonMenuEntries() && trigger.replace.isPresent()) {
                event.getMenuEntry().setOption(trigger.replace.get());

            // Hide option.
            } else if (config.hideDestroy() && trigger.hide.isPresent()) {
                final List<MenuEntry> newMenuEntries = new ArrayList<>();

                for (final MenuEntry entry : client.getMenuEntries()) {
                    if (!entry.getOption().equals(trigger.original)) {
                        newMenuEntries.add(entry);
                    }
                }
                client.setMenuEntries(newMenuEntries.toArray(new MenuEntry[0]));
            }
        }
    }

    private boolean isValidTrigger(final MenuEntryAdded event, final TriggerMenuEntryAdded trigger) {
        // Item check.
        if (event.getMenuEntry().getItemId() != chargedItem.item_id) return false;

        // Option check.
        if (!event.getOption().equals(trigger.original)) return false;

        return true;
    }
}
