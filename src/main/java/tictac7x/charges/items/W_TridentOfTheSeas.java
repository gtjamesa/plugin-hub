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
import tictac7x.charges.item.triggers.OnGraphicChanged;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class W_TridentOfTheSeas extends ChargedItem {
    public W_TridentOfTheSeas(
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
        super(ChargesImprovedConfig.trident_of_the_seas, ItemKey.TRIDENT_OF_THE_SEAS, ItemID.TRIDENT_OF_THE_SEAS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.UNCHARGED_TRIDENT).fixedCharges(0),
            new TriggerItem(ItemID.UNCHARGED_TRIDENT_E).fixedCharges(0),
            new TriggerItem(ItemID.UNCHARGED_TOXIC_TRIDENT).fixedCharges(0),
            new TriggerItem(ItemID.UNCHARGED_TOXIC_TRIDENT_E).fixedCharges(0),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SEAS),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SEAS_FULL).fixedCharges(2500),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SWAMP),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SWAMP_E),
        };

        this.triggers = new TriggerBase[] {
            new OnChatMessage("Your Trident of the (seas|swamp)( \\(e\\))? has run out of charges.").notification().fixedCharges(0),
            new OnChatMessage("Your Trident of the (seas|swamp)( \\(e\\))? has one charge.").fixedCharges(1),
            new OnChatMessage("Your Trident of the (seas|swamp)( \\(e\\))? only has (?<charges>.+) charges left!").notification().setDynamically(),
            new OnChatMessage("Your Trident of the (seas|swamp)( \\((full|e)\\))? has (?<charges>.+) charges?( left)?.").setDynamically(),
            new OnChatMessage("You add .* charges? to the Trident of the (seas|swamp)( \\(e\\))?. New total: (?<charges>.+)").setDynamically(),
            // Attack.
            new OnGraphicChanged(1251).isEquipped().decreaseCharges(1)
        };
    }
}
