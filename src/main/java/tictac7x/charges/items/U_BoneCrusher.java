package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.ChargedItemWithStatus;
import tictac7x.charges.item.triggers.OnChatMessage;
import tictac7x.charges.item.triggers.OnMenuEntryAdded;
import tictac7x.charges.item.triggers.OnXpDrop;
import tictac7x.charges.item.triggers.TriggerBase;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class U_BoneCrusher extends ChargedItemWithStatus {
    public U_BoneCrusher(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Store store,
        final Plugin plugin
    ) {
        super(ChargesImprovedConfig.bone_crusher, ItemKey.BONE_CRUSHER, ItemID.BONECRUSHER, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);

        this.items = new TriggerItem[]{
            new TriggerItem(ItemID.BONECRUSHER),
            new TriggerItem(ItemID.BONECRUSHER_NECKLACE)
        };

        this.triggers = new TriggerBase[] {
            // Check.
            new OnChatMessage("The bonecrusher( necklace)? has no charges.").fixedCharges(0),
            new OnChatMessage("The bonecrusher( necklace)? has one charge.").fixedCharges(1),
            new OnChatMessage("(The|Your) bonecrusher( necklace)? has (?<charges>.+) charges?( left)?. It is active").setDynamically().activate(),
            new OnChatMessage("(The|Your) bonecrusher( necklace)? has (?<charges>.+) charges?( left)?. It has been deactivated").setDynamically().deactivate(),
            // Uncharge.
            new OnChatMessage("You remove all the charges from the bonecrusher( necklace)?.").fixedCharges(0),
            new OnChatMessage("The bonecrusher( necklace)? has (?<charges>.+) charges? left.").setDynamically(),
            // Ran out.
            new OnChatMessage("Your bonecrusher( necklace)? has run out of charges.").notification().fixedCharges(0),
            // Activate.
            new OnChatMessage("The bonecrusher( necklace)? has been deactivated").deactivate(),
            // Deactivate.
            new OnChatMessage("The bonecrusher( necklace)? is active").activate(),
            // Automatic bury.
            new OnXpDrop(Skill.PRAYER).isActivated().decreaseCharges(1),
            // Hide destroy.
            new OnMenuEntryAdded("Destroy").hide(),
        };
    }
}
