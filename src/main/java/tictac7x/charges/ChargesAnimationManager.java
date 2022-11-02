package tictac7x.charges;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargesAnimationManager {
    private final Client client;
    private final ConfigManager configs;
    private final ChargesConfig config;

    private static final int TRIDENT_OF_THE_SEAS_ANIMATION = 1167;

    public ChargesAnimationManager(final Client client, final ConfigManager configs, final ChargesConfig config) {
        this.client = client;
        this.configs = configs;
        this.config = config;
    }

    public void onAnimationChanged(final AnimationChanged event) {
        if (event.getActor() == client.getLocalPlayer()) {
            checkTridentOfTheSeasAnimation(event.getActor().getAnimation());
        }
    }

    private boolean checkTridentOfTheSeasAnimation(final int animation) {
        if (animation == TRIDENT_OF_THE_SEAS_ANIMATION) {
            configs.setConfiguration(ChargesConfig.group, ChargesConfig.trident_of_the_seas, config.getTridentOfTheSeasCharges() - 1);
            return true;
        }

        return false;
    }
}
