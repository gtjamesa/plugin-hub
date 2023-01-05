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
import tictac7x.charges.triggers.TriggerWidget;

public class TridentOfTheSeas extends ChargedItemInfoBox {
    public TridentOfTheSeas(final Client client, final ClientThread client_thread, final ConfigManager configs, final ItemManager items, final Plugin plugin) {
        super(ItemID.TRIDENT_OF_THE_SEAS, client, client_thread, configs, items, plugin);
        this.item_ids_to_render = new int[]{ItemID.TRIDENT_OF_THE_SEAS, ItemID.TRIDENT_OF_THE_SEAS_E, ItemID.TRIDENT_OF_THE_SEAS_FULL};
        this.config_key = ChargesImprovedConfig.trident_of_the_seas;
        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your Trident of the seas( \\(full\\))? has (?<charges>.+) charges( left)?.")
        };
        this.triggers_animations = new TriggerAnimation[]{
            new TriggerAnimation(1167, 1)
        };
        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget("You add .* charges? to the Trident of the seas( \\(full\\))?.New total: (?<charges>.+)", 193, 2)
        };
    }
}
