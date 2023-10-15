package tictac7x.charges.items;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.item.ChargedItem;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;
import tictac7x.charges.item.triggers.TriggerChatMessage;
import tictac7x.charges.item.triggers.TriggerHitsplat;
import tictac7x.charges.item.triggers.TriggerItem;

public class S_CrystalShield extends ChargedItem {
    public S_CrystalShield(
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
        super(ItemKey.CRYSTAL_SHIELD, ItemID.CRYSTAL_SHIELD, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, store);
        this.config_key = ChargesImprovedConfig.crystal_shield;
        this.triggersItems = new TriggerItem[]{
            new TriggerItem(ItemID.CRYSTAL_SHIELD),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_24127),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_110),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_110_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_210),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_210_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_310),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_310_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_410),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_410_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_510),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_510_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_610),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_610_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_710),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_710_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_810),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_810_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_910),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_910_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_FULL),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_FULL_I),
            new TriggerItem(ItemID.CRYSTAL_SHIELD_INACTIVE).fixedCharges(0),
            new TriggerItem(ItemID.NEW_CRYSTAL_SHIELD),
            new TriggerItem(ItemID.NEW_CRYSTAL_SHIELD_4235),
            new TriggerItem(ItemID.NEW_CRYSTAL_SHIELD_16890),
            new TriggerItem(ItemID.NEW_CRYSTAL_SHIELD_I_16891),
            new TriggerItem(ItemID.NEW_CRYSTAL_SHIELD_I),
        };
        this.triggersChatMessages = new TriggerChatMessage[]{
            new TriggerChatMessage("Your crystal shield has (?<charges>.+) charges? remaining.")
        };
        this.triggersHitsplats = new TriggerHitsplat[]{
            new TriggerHitsplat(1).onSelf().equipped().nonZero()
        };
    }
}
