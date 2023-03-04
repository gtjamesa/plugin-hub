package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerItem;

public class BarrowsVeracBrassard extends ChargedItemInfoBox {
    public BarrowsVeracBrassard(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChatMessageManager chat_messages, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.VERACS_BRASSARD, client, client_thread, configs, items, infoboxes, chat_messages, config, plugin);
        this.charges = 100;
        this.charges_from_name = true;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.VERACS_BRASSARD).fixedCharges(100),
            new TriggerItem(ItemID.VERACS_BRASSARD_100).fixedCharges(100),
            new TriggerItem(ItemID.VERACS_BRASSARD_75).fixedCharges(75),
            new TriggerItem(ItemID.VERACS_BRASSARD_50).fixedCharges(50),
            new TriggerItem(ItemID.VERACS_BRASSARD_25).fixedCharges(25),
            new TriggerItem(ItemID.VERACS_BRASSARD_0).fixedCharges(0)
        };
    }
}