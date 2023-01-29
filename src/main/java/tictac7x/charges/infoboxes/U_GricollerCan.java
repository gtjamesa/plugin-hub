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

import javax.annotation.Nullable;

public class U_GricollerCan extends ChargedItemInfoBox {
    public U_GricollerCan(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final InfoBoxManager infoboxes, final ChargesImprovedConfig config, final Plugin plugin) {
        super(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997, client, client_thread, configs, items, infoboxes, config, plugin);
        this.config_key = ChargesImprovedConfig.gricoller_can;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.GRICOLLERS_CAN),
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(2293, 1).unallowedItems(new int[]{
                ItemID.WATERING_CAN1, ItemID.WATERING_CAN2,
                ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
                ItemID.WATERING_CAN5, ItemID.WATERING_CAN6,
                ItemID.WATERING_CAN6, ItemID.WATERING_CAN7,
                ItemID.WATERING_CAN8
            })
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Watering can charges remaining: (?<charges>.+)%").onItemClick(),
            new TriggerChatMessage("You water .*").onItemClick()
        };
    }
}
