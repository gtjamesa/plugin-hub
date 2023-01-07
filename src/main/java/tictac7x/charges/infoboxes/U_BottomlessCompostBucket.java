package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerAnimation;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerWidget;

public class U_BottomlessCompostBucket extends ChargedItemInfoBox {
    public U_BottomlessCompostBucket(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997, client, client_thread, configs, items, plugin);
        this.config_key = ChargesImprovedConfig.bottomless_compost_bucket;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.BOTTOMLESS_COMPOST_BUCKET),
            new TriggerItem(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997),
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(8197, 1)
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
//            new TriggerChatMessage("Your bracelet of slaughter has (?<charges>.+) charges? left."),
//            new TriggerChatMessage("Your bracelet of slaughter prevents your slayer count from decreasing. It has (?<charges>.+) charges? left."),
//            new TriggerChatMessage("Your bracelet of slaughter prevents your slayer count from decreasing. It then crumbles to dust.", MAX_CHARGES)
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("Your bottomless compost bucket is currently holding (?<charges>.+) uses of supercompost.")
        };
    }
}
