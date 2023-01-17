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

public class J_XericTalisman extends ChargedItemInfoBox {
    public J_XericTalisman(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.XERICS_TALISMAN, client, client_thread, configs, items, infoboxes, config, plugin);
        this.config_key = ChargesImprovedConfig.xeric_talisman;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.XERICS_TALISMAN_INERT, 0),
            new TriggerItem(ItemID.XERICS_TALISMAN),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("The talisman has one charge.", 1),
            new TriggerChatMessage("The talisman has (?<charges>.+) charges."),
            new TriggerChatMessage("Your talisman has run out of charges.", 0),
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(3865, 1, false)
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("Your talisman now has one charge."),
            new TriggerWidget("Your talisman now has (?<charges>.+) charges?.")
        };
    }
}
