package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerHitsplat;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerWidget;

public class W_Arclight extends ChargedItemInfoBox {
    private final int ARCLIGHT_ATTACK_STAB = 386;
    private final int ARCLIGHT_ATTACK_SLASH = 390;

    public W_Arclight(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.ARCLIGHT, client, client_thread, configs, items, plugin);
        this.config_key = ChargesImprovedConfig.arclight;
        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.ARCLIGHT),
        };
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your arclight has (?<charges>.+) charges? left."),
            new TriggerChatMessage("Your arclight can perform (?<charges>.+) more attacks."),
            new TriggerChatMessage("Your arclight has degraded.", 0)
        };
        this.triggers_hitsplats = new TriggerHitsplat[]{
            new TriggerHitsplat(false, true, 1, new int[]{ARCLIGHT_ATTACK_STAB, ARCLIGHT_ATTACK_SLASH})
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("You combine Darklight with the shards and it seems to glow in your hands creating Arclight. Your Arclight has (?<charges>.+) charges.")
        };
    }
}
