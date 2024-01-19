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
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.Store;

public class ChargedItem extends ChargedItemBase {
    public int charges = Charges.UNKNOWN;

    public ChargedItem(String configKey, ItemKey itemKey, int itemId, Client client, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, Notifier notifier, ChargesImprovedConfig config, Store store, final Gson gson) {
        super(configKey, itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    public ChargedItem(ItemKey itemKey, int itemId, Client client, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, Notifier notifier, ChargesImprovedConfig config, Store store, final Gson gson) {
        super(itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    @Override
    public String getCharges() {
        if (charges >= 0) {
            return getChargesMinified(charges);
        }

        if (charges == Charges.UNLIMITED) {
            return "∞";
        }

        return super.getCharges();
    }

    @Override
    public void loadCharges() {
        if (configKey.isPresent()) {
            try {
                charges = Integer.parseInt(configManager.getConfiguration(ChargesImprovedConfig.group, configKey.get()));
            } catch (final Exception ignored) {}
        }
    }

    public void setCharges(final int charges) {
        final int newCharges =
            // Unlimited
            charges == Charges.UNLIMITED ? charges :

            // 0 -> charges
            Math.max(0, charges);

        if (this.charges != newCharges) {
            this.charges = newCharges;

            if (configKey.isPresent()) {
                configManager.setConfiguration(ChargesImprovedConfig.group, configKey.get(), this.charges);
            }
        }
    }

    public void decreaseCharges(final int charges) {
        setCharges(this.charges - charges);
    }

    public void increaseCharges(final int charges) {
        setCharges(this.charges + charges);
    }
}


