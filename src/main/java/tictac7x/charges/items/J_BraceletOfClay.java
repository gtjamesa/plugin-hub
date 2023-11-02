package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnItemContainerChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.store.ItemContainerType;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerItem;

public class J_BraceletOfClay extends ChargedItem {
    public J_BraceletOfClay(
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
        super(ItemKey.BRACELET_OF_CLAY, ItemID.BRACELET_OF_CLAY, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.bracelet_of_clay;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.BRACELET_OF_CLAY).needsToBeEquipped(),
        };
        this.triggers = new TriggerBase[] {
            // Check.
            new OnChatMessage("You can mine (?<charges>.+) more pieces? of soft clay before your bracelet crumbles to dust.").setDynamically(),

            // Mine clay.
            new OnItemContainerChanged(ItemContainerType.INVENTORY).isEquipped().onMenuOption("Mine").onMenuTarget("Clay rocks").consumer(() -> {
                final int clayBefore = store.getPreviousInventoryItemQuantity(ItemID.SOFT_CLAY);
                final int clayAfter = store.getInventoryItemQuantity(ItemID.SOFT_CLAY);
                decreaseCharges(clayAfter - clayBefore);
            }),

            // Mine soft clay.
            new OnItemContainerChanged(ItemContainerType.INVENTORY).isEquipped().onMenuOption("Mine").onMenuTarget("Soft clay rocks").consumer(() -> {
                final int clayBefore = store.getPreviousInventoryItemQuantity(ItemID.SOFT_CLAY);
                final int clayAfter = store.getInventoryItemQuantity(ItemID.SOFT_CLAY);

                // At least 2 soft clay was mined.
                if (clayAfter - clayBefore >= 2) {
                    decreaseCharges(1);
                }
            }),

            // Crumbles.
            new OnChatMessage("Your bracelet of clay crumbles to dust.").fixedCharges(28).notification("Your clay bracelet crumbles to dust.")
        };
    }
}
