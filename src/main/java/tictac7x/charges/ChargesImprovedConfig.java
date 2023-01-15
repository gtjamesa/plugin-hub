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
    String expeditious_bracelet = "expeditious_bracelet";
    String bracelet_of_slaughter = "bracelet_of_slaughter";
    String bottomless_compost_bucket = "bottomless_compost_bucket";
    String bottomless_compost_bucket_type = "bottomless_compost_bucket_type";
    String skull_sceptre = "skull_sceptre";
    String iban_staff = "iban_staff";
    String xeric_talisman = "xeric_talisman";
    String pharaoh_sceptre = "pharaoh_sceptre";
    String falador_shield = "falador_shield";

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
        keyName = expeditious_bracelet,
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

    @ConfigItem(
        keyName = bottomless_compost_bucket,
        name = "Bottomless compost bucket",
        description = "Bottomless compost bucket charges",
        position = 6
    ) default int getBottomlessCompostBucketCharges() { return -1; }

    @ConfigItem(
        keyName = bottomless_compost_bucket_type,
        name = "Bottomless compost bucket type",
        description = "Bottomless compost bucket type",
        position = 7
    ) default String getBottomlessCompostBucketType() { return ""; }

    @ConfigItem(
        keyName = skull_sceptre,
        name = "Skull sceptre",
        description = "Skull sceptre charges",
        position = 8
    ) default int getSkullSceptreCharges() { return -1; }

    @ConfigItem(
        keyName = skull_sceptre,
        name = "Iban staff",
        description = "Iban staff charges",
        position = 9
    ) default int getIbanStaffCharges() { return -1; }

    @ConfigItem(
        keyName = xeric_talisman,
        name = "Xeric talisman",
        description = "Xeric talisman charges",
        position = 10
    ) default int getXericTalismanCharges() { return -1; }

    @ConfigItem(
        keyName = pharaoh_sceptre,
        name = "Pharaoh sceptre",
        description = "Pharaoh sceptre charges",
        position = 11
    ) default int getPharaohSceptreCharges() { return -1; }

    @ConfigItem(
        keyName = falador_shield,
        name = "Falador shield",
        description = "Falador shield charges",
        position = 12
    ) default int getFaladorShieldCharges() { return -1; }
}
