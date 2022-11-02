package tictac7x.charges;

import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargesWidgetManager {
    private final Client client;
    private final ConfigManager configs;

    private static final Pattern TRIDENT_OF_THE_SEAS_PATTERN = Pattern.compile("You add (a charge| [0-9]+ charges) to the Trident of the seas( \\(full\\))?.<br>New total: (?<charges>.*)");
    private static final int TRIDENT_OF_THE_SEAS_GROUP = 193;
    private static final int TRIDENT_OF_THE_SEAS_CHILD = 2;

    public ChargesWidgetManager(final Client client, final ConfigManager configs) {
        this.client = client;
        this.configs = configs;
    }

    public void onWidgetLoaded(final WidgetLoaded event) {
        checkTridentOfTheSeasCharges(event);
    }

    private boolean checkTridentOfTheSeasCharges(final WidgetLoaded event) {
        if (event.getGroupId() == TRIDENT_OF_THE_SEAS_GROUP) {
            final Widget widget = client.getWidget(TRIDENT_OF_THE_SEAS_GROUP, TRIDENT_OF_THE_SEAS_CHILD);

            if (widget != null) {
                final Matcher matcher = TRIDENT_OF_THE_SEAS_PATTERN.matcher(widget.getText());

                if (matcher.find()) {
                    configs.setConfiguration(ChargesConfig.group, ChargesConfig.trident_of_the_seas, Integer.parseInt(matcher.group("charges").replace(",", "")));
                    return true;
                }
            }
        }

        return false;
    }
}
