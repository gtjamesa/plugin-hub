package tictac7x.charges;

import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargesChatManager {
    private final ConfigManager configs;

    private static final Pattern TRIDENT_OF_THE_SEAS_PATTERN = Pattern.compile("Your Trident of the seas has (.*) charges");

    public ChargesChatManager(final ConfigManager configs) {
        this.configs = configs;
    }

    public void onChatMessage(final ChatMessage event) {
        if (event.getType() == ChatMessageType.GAMEMESSAGE) {
            checkTridentOfTheSeasCharges(event.getMessage());
        }
    }

    private boolean checkTridentOfTheSeasCharges(final String message) {
        final Matcher matcher = TRIDENT_OF_THE_SEAS_PATTERN.matcher(message);

        if (matcher.find()) {
            configs.setConfiguration(ChargesConfig.group, ChargesConfig.trident_of_the_seas, Integer.parseInt(matcher.group(1)));
            return true;
        }

        return false;
    }
}
