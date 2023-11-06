package tictac7x.charges.items;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

import java.lang.reflect.Field;

@Slf4j
public class U_SeedBox extends ChargedItemWithStorage {
    public U_SeedBox(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Plugin plugin
    ) {
        super(ChargesImprovedConfig.seed_box, ItemKey.SEED_BOX, ItemID.SEED_BOX, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.SEED_BOX),
            new TriggerItem(ItemID.OPEN_SEED_BOX),
        };
        this.triggers = new TriggerBase[] {
            // Check or empty.
            new OnChatMessage("(The|Your) seed box is( now| already)? empty.").emptyStorage(),

            // Empty into inventory.
            new OnChatMessage("Emptied (?<quantity>.+) x (?<seed>.+) seed to your inventory.").consumer(m -> {
                final int seed = getSeedIdFromName(m.group("seed"));
                final int quantity = Integer.parseInt(m.group("quantity"));
                storage.remove(seed, quantity);
            }),

            // Check or add seed into box.
            new OnChatMessage("(Stored |You put )?(?<quantity>.+) x (?<seed>.+?) seed( straight into| in)?( your seed box)?.").consumer(m -> {
                final int seed = getSeedIdFromName(m.group("seed"));
                final int quantity = Integer.parseInt(m.group("quantity"));
                storage.add(seed, quantity);
            }),

            // Check first message.
            new OnChatMessage("The seed box contains:").emptyStorage(),

            // Hide destroy.
            new OnMenuEntryAdded("Destroy").hide()
        };
    }

    private int getSeedIdFromName(final String seed) {
        for (final Field field : ItemID.class.getDeclaredFields()) {
            if (field.getName().equals(seed.toUpperCase().replaceAll(" ", "_") + "_SEED")) {
                try {
                    return (Integer) field.get(ItemID.class);
                } catch (final Exception ignored) {
                    return Charges.UNKNOWN;
                }
            }
        }

        return Charges.UNKNOWN;
    }
}
