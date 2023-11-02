package tictac7x.charges.item.listeners;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
                log.debug(chargedItem.getItemName() + " menu option replaced");
                event.getMenuEntry().setOption(trigger.replaceOption.get());
                triggerUsed = true;
            }

            if (trigger.replaceTarget.isPresent()) {
                if (event.getTarget().contains(trigger.replaceTarget.get()[0])) {
                    log.debug(chargedItem.getItemName() + " menu target replaced: " + trigger.replaceTarget.get()[0] + " -> " + trigger.replaceTarget.get()[1]);
                    event.getMenuEntry().setTarget(event.getTarget().replaceAll(trigger.replaceTarget.get()[0], trigger.replaceTarget.get()[1]));
                    triggerUsed = true;
                }
            }

            if (trigger.hide.isPresent() && trigger.menuEntryOption.isPresent()) {
                final List<MenuEntry> newMenuEntries = new ArrayList<>();

                for (final MenuEntry entry : client.getMenuEntries()) {
                    if (!entry.getOption().equals(trigger.menuEntryOption.get())) {
                        newMenuEntries.add(entry);
                    } else {
                        log.debug(chargedItem.getItemName() + " menu entry hidden: " + entry.getOption());
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
        if (!trigger.replaceImpostorIds.isPresent() && event.getMenuEntry().getItemId() != chargedItem.item_id) {
            return false;
        }

        // Hide config check.
        if (trigger.hide.isPresent() && !config.hideDestroy()) {
            return false;
        }

        // Menu entry option check.
        if (trigger.menuEntryOption.isPresent() && !event.getOption().equals(trigger.menuEntryOption.get())) {
            return false;
        }

        // Menu option replace check.
        if (trigger.replaceOption.isPresent() && (
            !trigger.menuEntryOption.isPresent() ||
            !config.useCommonMenuEntries() ||
            !event.getOption().equals(trigger.menuEntryOption.get())
        )) {
            return false;
        }

        // Menu target replace check.
        menuReplaceTargetCheck: if (trigger.replaceTarget.isPresent()) {
            if (event.getTarget().contains(trigger.replaceTarget.get()[0])) {
                break menuReplaceTargetCheck;
            }

            return false;
        }

        // Menu replace impostor id check.
        replaceImpostorIdCheck: if (trigger.replaceImpostorIds.isPresent()) {
            for (final int impostorId : trigger.replaceImpostorIds.get()) {
                try {
                    if (client.getObjectDefinition(event.getMenuEntry().getIdentifier()).getImpostor().getId() == impostorId) {
                        break replaceImpostorIdCheck;
                    }
                } catch (final Exception ignored) {}
            }

            return false;
        }

        return super.isValidTrigger(trigger);
    }
}
