package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesItem;
import tictac7x.charges.triggers.*;

public class W_TridentOfTheSeas extends ChargedItemInfoBox {
    public W_TridentOfTheSeas(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.TRIDENT_OF_THE_SEAS, ItemID.TRIDENT_OF_THE_SEAS, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, plugin);
        this.config_key = ChargesImprovedConfig.trident_of_the_seas;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.UNCHARGED_TRIDENT).fixedCharges(0),
            new TriggerItem(ItemID.UNCHARGED_TRIDENT_E).fixedCharges(0),
            new TriggerItem(ItemID.UNCHARGED_TOXIC_TRIDENT).fixedCharges(0),
            new TriggerItem(ItemID.UNCHARGED_TOXIC_TRIDENT_E).fixedCharges(0),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SEAS),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SEAS_FULL).fixedCharges(2500),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SWAMP),
            new TriggerItem(ItemID.TRIDENT_OF_THE_SWAMP_E),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your Trident of the (seas|swamp)( \\(e\\))? has one charge.").fixedCharges(1),
            new TriggerChatMessage("Your Trident of the (seas|swamp)( \\((full|e)\\))?( only)? has (?<charges>.+) charges?( left)?."),
            new TriggerChatMessage("You add .* charges? to the Trident of the (seas|swamp)( \\(e\\))?. New total: (?<charges>.+)")
        };
        this.triggers_graphics = new TriggerGraphic[]{
            new TriggerGraphic(1251).decreaseCharges(1).equipped()
        };
    }
}
