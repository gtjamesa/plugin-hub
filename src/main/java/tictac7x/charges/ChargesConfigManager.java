package tictac7x.charges;

import net.runelite.client.events.ConfigChanged;

import java.awt.Color;

public class ChargesConfigManager {
    public void onConfigChanged(final ConfigChanged event) {
        if (event.getGroup().equals(ChargesConfig.group)) {
            checkTridentOfTheSeasConfig(event);
        }
    }

    private void updateInfoboxText(final ChargesInfoBox infobox, final int count, final boolean percentage) {
        if (infobox != null) {
            infobox.setText(count + (percentage ? "%" : ""));
            infobox.setColor(count == 0 ? Color.red : null);
        }
    }

    private void checkTridentOfTheSeasConfig(final ConfigChanged event) {
        if (event.getKey().equals(ChargesConfig.trident_of_the_seas)) {
            updateInfoboxText(ChargesPlugin.INFOBOX_TRIDENT_OF_THE_SEAS, Integer.parseInt(event.getNewValue()), false);
        }
    }
}
