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

import javax.annotation.Nullable;

public class U_BottomlessCompostBucket extends ChargedItemInfoBox {
    public U_BottomlessCompostBucket(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997, client, client_thread, configs, items, plugin);
        this.config_key = ChargesImprovedConfig.bottomless_compost_bucket;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.BOTTOMLESS_COMPOST_BUCKET, 0),
            new TriggerItem(ItemID.BOTTOMLESS_COMPOST_BUCKET_22997),
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(8197, 1),
            new TriggerAnimation(832, 2, new int[]{ItemID.BUCKET}, "Take")
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your bottomless compost bucket has a single use of (?<type>.+) ?compost remaining.", 1),
            new TriggerChatMessage("Your bottomless compost bucket has (?<charges>.+) uses of (?<type>.+) ?compost remaining."),
            new TriggerChatMessage("Your bottomless compost bucket doesn't currently have any compost in it!(?<type>.*)", 0)
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("Your bottomless compost bucket is currently holding one use of (?<type>.+?) ?compost.", 1),
            new TriggerWidget("Your bottomless compost bucket is currently holding (?<charges>.+) uses of (?<type>.+?) ?compost."),
            new TriggerWidget("You discard the contents of your bottomless compost bucket.(?<type>.*)", 0),
            new TriggerWidget("You fill your bottomless compost bucket with .* buckets? of (?<type>.+?) ?compost. Your bottomless compost bucket now contains a total of (?<charges>.+) uses.")
        };
        this.extra_groups = new String[]{"type"};
    }

    @Override
    public String getTooltip() {
        final @Nullable String type = getCompostType();
        final String tooltip_type = charges == 0 ? " (empty)" : charges > 0 && type != null && type.length() > 0 ? " (" + type + ")" : " (unknown)";
        return super.getTooltip() + tooltip_type;
    }
    
    private String getCompostType() {
        return configs.getConfiguration(ChargesImprovedConfig.group, ChargesImprovedConfig.bottomless_compost_bucket_type);
    }
}
