package tictac7x.charges;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import static tictac7x.charges.ChargesImprovedConfig.group;

@ConfigGroup(group)
public interface ChargesImprovedConfig extends Config {
    String group = "tictac7x-charges";

    String trident_of_the_seas = "trident_of_the_seas";
    String arclight = "arclight";
    String crystal_shield = "crystal_shield";

    @ConfigItem(
        keyName = trident_of_the_seas,
        name = "Trident of the seas",
        description = "Trident of the seas charges",
        position = 1
    ) default String getTridentOfTheSeasCharges() { return ""; }

    @ConfigItem(
        keyName = arclight,
        name = "Arclight",
        description = "Arclight charges",
        position = 2
    ) default String getArclightCharges() { return ""; }

    @ConfigItem(
        keyName = crystal_shield,
        name = "Crystal shield",
        description = "Crystal shield charges",
        position = 3
    ) default String getCrystalShieldCharges() { return ""; }
}
