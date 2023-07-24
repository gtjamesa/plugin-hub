package tictac7x.charges.infoboxes;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargedItemInfoBox;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesItem;
import tictac7x.charges.triggers.TriggerChatMessage;
import tictac7x.charges.triggers.TriggerItem;
import tictac7x.charges.triggers.TriggerReset;
import tictac7x.charges.triggers.TriggerWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class C_MagicCape extends ChargedItemInfoBox {
    private final int MAGIC_CAPE_T = 9763;
    private final String regex_changed = "You have changed your spellbook (?<changed>.+)/(?<total>.+) times today.";

    public C_MagicCape(
        final Client client,
        final ClientThread client_thread,
        final ConfigManager configs,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ChatMessageManager chat_messages,
        final Notifier notifier,
        final ChargesImprovedConfig config,
        final Plugin plugin
    ) {
        super(ChargesItem.MAGIC_CAPE, ItemID.MAGIC_CAPE, client, client_thread, configs, items, infoboxes, chat_messages, notifier, config, plugin);
        this.config_key = ChargesImprovedConfig.magic_cape;

        this.triggers_items = new TriggerItem[]{
            new TriggerItem(ItemID.MAGIC_CAPE),
            new TriggerItem(MAGIC_CAPE_T)
        };

        this.triggers_chat_messages = new TriggerChatMessage[]{
            new TriggerChatMessage(regex_changed).extraConsumer(message -> {
                final Matcher matcher = Pattern.compile(regex_changed).matcher(message);
                if (matcher.find()) {
                    final int used = Integer.parseInt(matcher.group("changed"));
                    final int total = Integer.parseInt(matcher.group("total"));
                    setCharges(total - used);
                }
            }),
        };

        this.triggers_widgets = new TriggerWidget[]{
            new TriggerWidget(219,1,0, "Choose spellbook: \\((?<charges>.+)/.+ left\\)")
        };

        this.triggers_resets = new TriggerReset[]{
            new TriggerReset(5)
        };
    }
}
