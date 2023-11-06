package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.ItemActivity;
import tictac7x.charges.store.Store;

import java.awt.Color;
import java.util.Optional;

public class ChargedItemWithStatus extends ChargedItem {

    public ChargedItemWithStatus(String configKey, ItemKey itemKey, int itemId, Client client, ClientThread clientThread, ConfigManager configManager, ItemManager itemManager, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, Notifier notifier, ChargesImprovedConfig config, Store store) {
        super(configKey, itemKey, itemId, client, clientThread, configManager, itemManager, infoBoxManager, chatMessageManager, notifier, config, store);
    }

    public boolean isDeactivated() {
        if (!getConfigStatusKey().isPresent()) {
            return false;
        }

        final Optional<String> status = Optional.ofNullable(configManager.getConfiguration(ChargesImprovedConfig.group, getConfigStatusKey().get()));

        if (!status.isPresent()) {
            return false;
        }

        return status.get().equals(ItemActivity.DEACTIVATED.toString());
    }

    public boolean isActivated() {
        if (!getConfigStatusKey().isPresent()) {
            return false;
        }

        final Optional<String> status = Optional.ofNullable(configManager.getConfiguration(ChargesImprovedConfig.group, getConfigStatusKey().get()));

        if (!status.isPresent()) {
            return false;
        }

        return status.get().equals(ItemActivity.ACTIVATED.toString());
    }

    public Optional<String> getConfigStatusKey() {
        if (configKey.isPresent()) {
            return Optional.of(configKey.get() + "_status");
        }

        return Optional.empty();
    }

    public void deactivate() {
        setActivity(ItemActivity.DEACTIVATED);
    }

    public void activate() {
        setActivity(ItemActivity.ACTIVATED);
    }

    private void setActivity(final ItemActivity status) {
        if (getConfigStatusKey().isPresent()) {
            configManager.setConfiguration(ChargesImprovedConfig.group, getConfigStatusKey().get(), status);
        }
    }

    @Override
    public Color getTextColor() {
        if (isActivated()) {
            return config.getColorActivated();
        }

        if (isDeactivated()) {
            return config.getColorEmpty();
        }

        return super.getTextColor();
    }
}
