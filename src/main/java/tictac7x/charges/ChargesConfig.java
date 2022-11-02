package tictac7x.charges;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import static tictac7x.charges.ChargesConfig.group;

@ConfigGroup(group)
public interface ChargesConfig extends Config {
    String group = "tictac7x-charges";

    String trident_of_the_seas = "trident_of_the_seas";

    @ConfigItem(
        keyName = trident_of_the_seas,
        name = "Trident of the seas",
        description = "Trident of the seas charges",
        position = 1
    ) default int getTridentOfTheSeasCharges() { return 0; }
}
