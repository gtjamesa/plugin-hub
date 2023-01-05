package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerHitsplat;

public class CrystalShield extends ChargedItemInfoBox {
    public CrystalShield(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.CRYSTAL_SHIELD, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{ItemID.CRYSTAL_SHIELD};
        this.config_key = ChargesImprovedConfig.crystal_shield;
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your crystal shield has (?<charges>.+) charges remaining.")
        };
        this.triggers_hitsplats = new TriggerHitsplat[]{
            new TriggerHitsplat(true, true, HitsplatID.DAMAGE_ME,1)
        };
    }
}
