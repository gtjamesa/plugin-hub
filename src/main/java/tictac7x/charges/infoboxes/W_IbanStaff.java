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
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerWidget;

public class W_IbanStaff extends ChargedItemInfoBox {
    public W_IbanStaff(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.IBANS_STAFF, client, client_thread, configs, items, infoboxes, config, plugin);
        this.config_key = ChargesImprovedConfig.iban_staff;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.IBANS_STAFF),
            new TriggerItem(ItemID.IBANS_STAFF_1410),
            new TriggerItem(ItemID.IBANS_STAFF_U),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("You have (?<charges>.+) charges left on the staff."),
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(708, 1, true)
        };
    }
}
