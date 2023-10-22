package tictac7x.charges;

import net.runelite.client.config.*;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemKey;
import tictac7x.charges.store.ItemActivity;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Set;

import static tictac7x.charges.ChargesImprovedConfig.group;

@ConfigGroup(group)
public interface ChargesImprovedConfig extends Config {
    String group = "tictac7x-charges";
    String version = "version";
    String date = "date";
    String infoboxes_show = "infoboxes_show";
    String infoboxes_hidden = "infoboxes_hidden";
    String item_overlays_show = "item_overlays_show";
    String item_overlays_hidden = "item_overlays_hidden";

    String arclight = "arclight";
    String ash_sanctifier = "ash_sanctifier";
    String ash_sanctifier_status = "ash_sanctifier_status";
    String bone_crusher = "bone_crusher";
    String bone_crusher_status = "bone_crusher_status";
    String kharedsts_memoirs = "kharedsts_memoirs";
    String bottomless_compost_bucket = "bottomless_compost_bucket";
    String bottomless_compost_bucket_type = "bottomless_compost_bucket_type";
    String bracelet_of_slaughter = "bracelet_of_slaughter";
    String bryophytas_staff = "bryophytas_staff";
    String celestial_ring = "celestial_ring";
    String escape_crystal = "escape_crystal";
    String escape_crystal_status = "escape_crystal_status";
    String chronicle = "chronicle";
    String crystal_shield = "crystal_shield";
    String crystal_bow = "crystal_bow";
    String crystal_halberd = "crystal_halberd";
    String expeditious_bracelet = "expeditious_bracelet";
    String falador_shield = "falador_shield";
    String fish_barrel = "fish_barrel";
    String gricollers_can = "gricollers_can";
    String ibans_staff = "ibans_staff";
    String pharaohs_sceptre = "pharaohs_sceptre";
    String ring_of_suffering = "ring_of_suffering";
    String ring_of_suffering_status = "ring_of_suffering_status";
    String sanguinesti_staff = "sanguinesti_staff";
    String skull_sceptre = "skull_sceptre";
    String soul_bearer = "soul_bearer";
    String trident_of_the_seas = "trident_of_the_seas";
    String xerics_talisman = "xerics_talisman";
    String dragonfire_shield = "dragonfire_shield";
    String toxic_staff_of_the_dead = "toxic_staff_of_the_dead";
    String camulet = "camulet";
    String circlet_of_water = "circlet_of_water";
    String teleport_crystal = "teleport_crystal";
    String bracelet_of_clay = "bracelet_of_clay";
    String coffin = "coffin";
    String bracelet_of_flamtaer = "bracelet_of_flamtaer";
    String ring_of_recoil = "ring_of_recoil";
    String log_basket = "log_basket";
    String ardougne_cloak = "ardougne_cloak";
    String magic_cape = "magic_cape";
    String gem_bag = "gem_bag";
    String seed_box = "seed_box";
    String crystal_helm = "crystal_helm";
    String crystal_body = "crystal_body";
    String crystal_legs = "crystal_legs";
    String ring_of_shadows = "ring_of_shadows";
    String coal_bag = "coal_bag";
    String herb_sack = "herb_sack";
    String strange_old_lockpick = "strange_old_lockpick";
    String desert_amulet = "desert_amulet";
    String tome_of_fire = "tome_of_fire";

    @ConfigSection(
        name = "General",
        description = "General settings",
        position = 1
    ) String general = "general";

        @ConfigItem(
            keyName = "bank_overlays",
            name = "Show in bank",
            description = "Show charges of the items in bank",
            position = 1,
            section = general
        ) default boolean showBankOverlays() { return true; }

        @ConfigItem(
            keyName = "menu_replacements",
            name = "Common menu entries",
            description = "Use common menu entries like \"Teleport\" and \"Check\"",
            position = 2,
            section = general
        ) default boolean useCommonMenuEntries() { return true; }

        @ConfigItem(
            keyName = "hide_destroy",
            name = "Hide destroy menu entries",
            description = "Hide destroy menu entry from items that make no sense to destroy",
            position = 3,
            section = general
        ) default boolean hideDestroy() { return true; }

    @ConfigSection(
        name = "Colors",
        description = "Colors of item overlays",
        position = 2
    ) String colors = "colors";

        @Alpha
        @ConfigItem(
            keyName = "colors_default",
            name = "Default",
            description = "Color of default charges",
            position = 1,
            section = colors
        ) default Color getColorDefault() { return Color.white; }

        @Alpha
        @ConfigItem(
            keyName = "colors_unknown",
            name = "Unknown",
            description = "Color of unknown charges",
            position = 2,
            section = colors
        ) default Color getColorUnknown() { return Color.gray; }

        @Alpha
        @ConfigItem(
            keyName = "colors_empty",
            name = "Empty",
            description = "Color of empty charges",
            position = 3,
            section = colors
        ) default Color getColorEmpty() { return Color.red; }

        @Alpha
        @ConfigItem(
            keyName = "colors_activated",
            name = "Activated",
            description = "Color of activated charges",
            position = 4,
            section = colors
        ) default Color getColorActivated() { return Color.green; }

    @ConfigSection(
        name = "Infoboxes",
        description = "Select items to hide infoboxes for",
        position = 3,
        closedByDefault = true
    ) String infoboxes = "infoboxes";

        @ConfigItem(
            keyName = infoboxes_show,
            name = "Show infoboxes",
            description = "Whether to allow showing infoboxes for items",
            position = 1,
            section = infoboxes
        ) default boolean showInfoboxes() {
        return true;
    }

        @ConfigItem(
            keyName = infoboxes_hidden,
            name = "Hidden",
            description = "Select items to hide infoboxes for (use ctrl + click to select multiple items)",
            position = 2,
            section = infoboxes
        ) default Set<ItemKey> getHiddenInfoboxes() {
            return EnumSet.noneOf(ItemKey.class);
        }

    @ConfigSection(
        name = "Item Overlays",
        description = "Select items to hide item overlays for",
        position = 4,
        closedByDefault = true
    ) String item_overlays = "item_overlays";

        @ConfigItem(
            keyName = item_overlays_show,
            name = "Show item overlays",
            description = "Whether to allow showing overlays for items",
            position = 1,
            section = item_overlays
        ) default boolean showItemOverlays() {
            return true;
        }

        @ConfigItem(
            keyName = item_overlays_hidden,
            name = "Hidden",
            description = "Select items to hide item overlays for (use ctrl + click to select multiple items)",
            position = 2,
            section = item_overlays
        ) default Set<ItemKey> getHiddenItemOverlays() {
        return EnumSet.noneOf(ItemKey.class);
    }

    @ConfigSection(
        name = "Debug",
        description = "Values of charges for all items under the hood",
        position = 99,
        closedByDefault = true
    ) String debug = "debug";

        @ConfigItem(
            keyName = version,
            name = "Version",
            description = "Version of the plugin for update message",
            section = debug,
            position = 1
        ) default String getVersion() { return ""; }

        @ConfigItem(
            keyName = date,
            name = "Date",
            description = "Date to check for charges reset when logging in",
            section = debug,
            position = 2
        ) default String getResetDate() { return ""; }

        @ConfigItem(
            keyName = arclight,
            name = arclight,
            description = arclight,
            section = debug
        ) default int getArclightCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ash_sanctifier,
            name = ash_sanctifier,
            description = ash_sanctifier,
            section = debug
        ) default int getAshSanctifierCharges() { return Charges.UNKNOWN; }

            @ConfigItem(
                keyName = ash_sanctifier_status,
                name = ash_sanctifier_status,
                description = ash_sanctifier_status,
                section = debug
            ) default ItemActivity getAshSanctifierStatus() { return ItemActivity.ACTIVATED; }

        @ConfigItem(
            keyName = bone_crusher,
            name = bone_crusher,
            description = bone_crusher,
            section = debug
        ) default int getBoneCrusherCharges() { return Charges.UNKNOWN; }

            @ConfigItem(
                keyName = bone_crusher_status,
                name = bone_crusher_status,
                description = bone_crusher_status,
                section = debug
            ) default ItemActivity getBoneCrusherStatus() { return ItemActivity.ACTIVATED; }

        @ConfigItem(
            keyName = kharedsts_memoirs,
            name = kharedsts_memoirs,
            description = kharedsts_memoirs,
            section = debug
        ) default int getKharedstsMemoirsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bottomless_compost_bucket,
            name = bottomless_compost_bucket,
            description = bottomless_compost_bucket,
            section = debug
        ) default int getBottomlessCompostBucketCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bottomless_compost_bucket_type,
            name = bottomless_compost_bucket_type,
            description = bottomless_compost_bucket_type,
            section = debug
        ) default String getBottomlessCompostBucketType() { return ""; }

        @ConfigItem(
            keyName = bracelet_of_slaughter,
            name = bracelet_of_slaughter,
            description = bracelet_of_slaughter,
            section = debug
        ) default int getBraceletOfSlaughterCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bryophytas_staff,
            name = bryophytas_staff,
            description = bryophytas_staff,
            section = debug
        ) default int getBryophytasStaffCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = celestial_ring,
            name = celestial_ring,
            description = celestial_ring,
            section = debug
        ) default int getCelestialRingCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = chronicle,
            name = chronicle,
            description = chronicle,
            section = debug
        ) default int getChronicleCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_shield,
            name = crystal_shield,
            description = crystal_shield,
            section = debug
        ) default int getCrystalShieldCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_bow,
            name = crystal_bow,
            description = crystal_bow,
            section = debug
        ) default int getCrystalBowCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = expeditious_bracelet,
            name = expeditious_bracelet,
            description = expeditious_bracelet,
            section = debug
        ) default int getBraceletOfExpeditiousCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = falador_shield,
            name = falador_shield,
            description = falador_shield,
            section = debug
        ) default int getFaladorShieldCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = fish_barrel,
            name = fish_barrel,
            description = fish_barrel,
            section = debug
        ) default int getFishBarrelCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = gricollers_can,
            name = gricollers_can,
            description = gricollers_can,
            section = debug
        ) default int getGricollersCanCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ibans_staff,
            name = ibans_staff,
            description = ibans_staff,
            section = debug
        ) default int getIbansStaffCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = pharaohs_sceptre,
            name = pharaohs_sceptre,
            description = pharaohs_sceptre,
            section = debug
        ) default int getPharaohsSceptreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_suffering,
            name = ring_of_suffering,
            description = ring_of_suffering,
            section = debug
        ) default int getRingOfSufferingCharges() { return Charges.UNKNOWN; }

            @ConfigItem(
                keyName = ring_of_suffering_status,
                name = ring_of_suffering_status,
                description = ring_of_suffering_status,
                section = debug
            ) default ItemActivity getRingOfSufferingStatus() { return ItemActivity.ACTIVATED; }

        @ConfigItem(
            keyName = sanguinesti_staff,
            name = sanguinesti_staff,
            description = sanguinesti_staff,
            section = debug
        ) default int getSanguinestiStaffCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = skull_sceptre,
            name = skull_sceptre,
            description = skull_sceptre,
            section = debug
        ) default int getSkullSceptreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = soul_bearer,
            name = soul_bearer,
            description = soul_bearer,
            section = debug
        ) default int getSoulBearerCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = trident_of_the_seas,
            name = trident_of_the_seas,
            description = trident_of_the_seas,
            section = debug
        ) default int getTridentOfTheSeasCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = xerics_talisman,
            name = xerics_talisman,
            description = xerics_talisman,
            section = debug
        ) default int getXericsTalismanCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = dragonfire_shield,
            name = dragonfire_shield,
            description = dragonfire_shield,
            section = debug
        ) default int getDragonfireShieldCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = camulet,
            name = camulet,
            description = camulet,
            section = debug
        ) default int getCamuletCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = circlet_of_water,
            name = circlet_of_water,
            description = circlet_of_water,
            section = debug
        ) default int getCircletOfWaterCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = teleport_crystal,
            name = teleport_crystal,
            description = teleport_crystal,
            section = debug
        ) default int getTeleportCrystalCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = bracelet_of_clay,
            name = bracelet_of_clay,
            description = bracelet_of_clay,
            section = debug
        ) default int getBraceletOfClayCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = coffin,
            name = coffin,
            description = coffin,
            section = debug
        ) default int getCoffinCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = log_basket,
            name = log_basket,
            description = log_basket,
            section = debug
        ) default int getLogBasketCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ardougne_cloak,
            name = ardougne_cloak,
            description = ardougne_cloak,
            section = debug
        ) default int getArdougneCloakCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = magic_cape,
            name = magic_cape,
            description = magic_cape,
            section = debug
        ) default int getMagicCapeCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = gem_bag,
            name = gem_bag,
            description = gem_bag,
            section = debug
        ) default int getGemBagCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = seed_box,
            name = seed_box,
            description = seed_box,
            section = debug
        ) default int getSeedBoxCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_helm,
            name = crystal_helm,
            description = crystal_helm,
            section = debug
        ) default int getCrystalHelmCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_body,
            name = crystal_body,
            description = crystal_body,
            section = debug
        ) default int getCrystalBodyCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_legs,
            name = crystal_legs,
            description = crystal_legs,
            section = debug
        ) default int getCrystalLegsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_halberd,
            name = crystal_halberd,
            description = crystal_halberd,
            section = debug
        ) default int getCrystalHalberdCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_shadows,
            name = ring_of_shadows,
            description = ring_of_shadows,
            section = debug
        ) default int getRingOfShadowsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = coal_bag,
            name = coal_bag,
            description = coal_bag,
            section = debug
        ) default int getCoalBagCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = herb_sack,
            name = herb_sack,
            description = herb_sack,
            section = debug
        ) default int getHerbSackCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = escape_crystal_status,
            name = escape_crystal_status,
            description = escape_crystal_status,
            section = debug
        ) default ItemActivity getEscapeCrystalStatus() { return ItemActivity.DEACTIVATED; }

        @ConfigItem(
            keyName = strange_old_lockpick,
            name = strange_old_lockpick,
            description = strange_old_lockpick,
            section = debug
        ) default int getStrangeOldLockCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = desert_amulet,
            name = desert_amulet,
            description = desert_amulet,
            section = debug
        ) default int getDesertAmuletCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = tome_of_fire,
            name = tome_of_fire,
            description = tome_of_fire,
            section = debug
        ) default int getTomeOfFireCharges() { return Charges.UNKNOWN; }
}
