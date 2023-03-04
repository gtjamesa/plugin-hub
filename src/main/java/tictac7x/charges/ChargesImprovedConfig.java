package tictac7x.charges;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.Color;

import static tictac7x.charges.ChargesImprovedConfig.group;

@ConfigGroup(group)
public interface ChargesImprovedConfig extends Config {
    String group = "tictac7x-charges";
    String version = "version";

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
    String ash_sanctifier = "ash_sanctifier";
    String bone_crusher = "bone_crusher";
    String gricoller_can = "gricoller_can";
    String chronicle = "chonicle";
    String soul_bearer = "soul_bearer";
    String bryophyta_staff = "bryophyta_staff";

    @ConfigItem(
        keyName = version,
        name = version,
        description = version,
        hidden = true
    ) default String getVersion() { return ""; };

    @ConfigSection(
        name = "Colors",
        description = "Colors of item overlays",
        position = 1
    ) String colors = "Colors";

        @ConfigItem(
            keyName = "colors_default",
            name = "Default",
            description = "Color of default charges",
            position = 1,
            section = colors
        ) default Color getColorDefault() { return Color.white; }

        @ConfigItem(
            keyName = "colors_unknown",
            name = "Unknown",
            description = "Color of unknown charges",
            position = 2,
            section = colors
        ) default Color getColorUnknown() { return Color.gray; }

        @ConfigItem(
            keyName = "colors_empty",
            name = "Empty",
            description = "Color of empty charges",
            position = 3,
            section = colors
        ) default Color getColorEmpty() { return Color.red; }

    @ConfigSection(
        name = "Debug",
        description = "Values of charges for all items under the hood",
        position = 99,
        closedByDefault = true
    ) String debug = "Debug";

        @ConfigItem(
            keyName = trident_of_the_seas,
            name = "Trident of the seas",
            description = "Trident of the seas charges",
            position = 1,
            section = debug
        ) default int getTridentOfTheSeasCharges() { return -1; }

        @ConfigItem(
            keyName = arclight,
            name = "Arclight",
            description = "Arclight charges",
            position = 2,
            section = debug
        ) default int getArclightCharges() { return -1; }

        @ConfigItem(
            keyName = crystal_shield,
            name = "Crystal shield",
            description = "Crystal shield charges",
            position = 3,
            section = debug
        ) default int getCrystalShieldCharges() { return -1; }

        @ConfigItem(
            keyName = expeditious_bracelet,
            name = "Expeditious bracelet",
            description = "Expeditious bracelet charges",
            position = 4,
            section = debug
        ) default int getBraceletOfExpeditiousCharges() { return -1; }

        @ConfigItem(
            keyName = bracelet_of_slaughter,
            name = "Bracelet of slaughter",
            description = "Bracelet of slaughter charges",
            position = 5,
            section = debug
        ) default int getBraceletOfSlaughterCharges() { return -1; }

        @ConfigItem(
            keyName = bottomless_compost_bucket,
            name = "Bottomless compost bucket",
            description = "Bottomless compost bucket charges",
            position = 6,
            section = debug
        ) default int getBottomlessCompostBucketCharges() { return -1; }

        @ConfigItem(
            keyName = bottomless_compost_bucket_type,
            name = "Bottomless compost bucket type",
            description = "Bottomless compost bucket type",
            position = 7,
            section = debug
        ) default String getBottomlessCompostBucketType() { return ""; }

        @ConfigItem(
            keyName = skull_sceptre,
            name = "Skull sceptre",
            description = "Skull sceptre charges",
            position = 8,
            section = debug
        ) default int getSkullSceptreCharges() { return -1; }

        @ConfigItem(
            keyName = skull_sceptre,
            name = "Iban staff",
            description = "Iban staff charges",
            position = 9,
            section = debug
        ) default int getIbanStaffCharges() { return -1; }

        @ConfigItem(
            keyName = xeric_talisman,
            name = "Xeric talisman",
            description = "Xeric talisman charges",
            position = 10,
            section = debug
        ) default int getXericTalismanCharges() { return -1; }

        @ConfigItem(
            keyName = pharaoh_sceptre,
            name = "Pharaoh sceptre",
            description = "Pharaoh sceptre charges",
            position = 11,
            section = debug
        ) default int getPharaohSceptreCharges() { return -1; }

        @ConfigItem(
            keyName = falador_shield,
            name = "Falador shield",
            description = "Falador shield charges",
            position = 12,
            section = debug
        ) default int getFaladorShieldCharges() { return -1; }

        @ConfigItem(
            keyName = ash_sanctifier,
            name = "Ash sanctifier",
            description = "Ash sanctifier charges",
            position = 13,
            section = debug
        ) default int getAshSanctifierCharges() { return -1; }

        @ConfigItem(
            keyName = bone_crusher,
            name = "Bone crusher",
            description = "Bone crusher charges",
            position = 14,
            section = debug
        ) default int getBoneCrusherCharges() { return -1; }

        @ConfigItem(
            keyName = gricoller_can,
            name = "Gricoller's can",
            description = "Gricoller's can charges",
            position = 15,
            section = debug
        ) default int getGricollerCanCharges() { return -1; }

        @ConfigItem(
            keyName = chronicle,
            name = "Chronicle",
            description = "Chronicle charges",
            position = 16,
            section = debug
        ) default int getChronicleCharges() { return -1; }

        @ConfigItem(
            keyName = soul_bearer,
            name = "Soul bearer",
            description = "Soul bearer charges",
            position = 17,
            section = debug
        ) default int getSoulBearerCharges() { return -1; }

        @ConfigItem(
            keyName = bryophyta_staff,
            name = "Bryophyta's Staff",
            description = "Bryophyta's Staff charges",
            position = 18,
            section = debug
        ) default int getBryophytaStaffCharges() { return -1; }
}
