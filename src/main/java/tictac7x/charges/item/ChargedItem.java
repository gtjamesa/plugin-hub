package tictac7x.charges.item;

import com.google.gson.Gson;
import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.item.triggers.TriggerItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class ChargedItem extends ChargedItemBase {
    public ChargedItem(String configKey, ItemKey itemKey, int itemId, Client client, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, Notifier notifier, ChargesImprovedConfig config, Store store, final Gson gson) {
        super(configKey, itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    public ChargedItem(ItemKey itemKey, int itemId, Client client, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, Notifier notifier, ChargesImprovedConfig config, Store store, final Gson gson) {
        super(itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    @Override
    public String getCharges() {
        for (final TriggerItem triggerItem : items) {
            if (triggerItem.itemId == itemId && triggerItem.fixedCharges.isPresent()) {
                return getChargesMinified(triggerItem.fixedCharges.get());
            }
        }

        if (getChargesFromConfig() == Charges.UNLIMITED) {
            return "∞";
        }

        if (getChargesFromConfig() >= 0) {
            return getChargesMinified(getChargesFromConfig());
        }

        return "?";
    }

    public void setCharges(int charges) {
        if (!configKey.isPresent()) return;

        charges =
            // Unlimited
            charges == Charges.UNLIMITED ? charges :

            // 0 -> charges
            Math.max(0, charges);

        if (this.getChargesFromConfig() != charges) {
            configManager.setConfiguration(ChargesImprovedConfig.group, configKey.get(), charges);
        }
    }

    public void decreaseCharges(final int charges) {
        setCharges(this.getChargesFromConfig() - charges);
    }

    public void increaseCharges(final int charges) {
        setCharges(this.getChargesFromConfig() + charges);
    }

    private int getChargesFromConfig() {
        if (configKey.isPresent()) {
            return Integer.parseInt(configManager.getConfiguration(ChargesImprovedConfig.group, configKey.get()));
        }

        return Charges.UNKNOWN;
    }
}


