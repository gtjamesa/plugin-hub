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
    String bracelet_of_expeditious = "expeditious_bracelet";
    String bracelet_of_slaughter = "bracelet_of_slaughter";

    @ConfigItem(
        keyName = trident_of_the_seas,
        name = "Trident of the seas",
        description = "Trident of the seas charges",
        position = 1
    ) default int getTridentOfTheSeasCharges() { return -1; }

    @ConfigItem(
        keyName = arclight,
        name = "Arclight",
        description = "Arclight charges",
        position = 2
    ) default int getArclightCharges() { return -1; }

    @ConfigItem(
        keyName = crystal_shield,
        name = "Crystal shield",
        description = "Crystal shield charges",
        position = 3
    ) default int getCrystalShieldCharges() { return -1; }

    @ConfigItem(
        keyName = bracelet_of_expeditious,
        name = "Expeditious bracelet",
        description = "Expeditious bracelet charges",
        position = 4
    ) default int getBraceletOfExpeditiousCharges() { return -1; }

    @ConfigItem(
        keyName = bracelet_of_slaughter,
        name = "Bracelet of slaughter",
        description = "Bracelet of slaughter charges",
        position = 5
    ) default int getBraceletOfSlaughterCharges() { return -1; }
}
