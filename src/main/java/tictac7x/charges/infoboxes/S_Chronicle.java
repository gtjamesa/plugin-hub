package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerHitsplat;
import tictac7x.charges.triggers.TriggerItem;

public class S_Chronicle extends ChargedItemInfoBox {
    public S_Chronicle(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.CRYSTAL_SHIELD, client, client_thread, configs, items, infoboxes, config, plugin);
        this.config_key = ChargesImprovedConfig.chronicle;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.CHRONICLE),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your book has (?<charges>.+) charges? left.")
        };
    }
}
