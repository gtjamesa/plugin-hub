package tictac7x.charges;

import net.runelite.client.config.*;
import tictac7x.charges.store.Charges;
import tictac7x.charges.store.ItemActivity;

import java.awt.Color;

import static tictac7x.charges.ChargesImprovedConfig.group;

@ConfigGroup(group)
public interface ChargesImprovedConfig extends Config {
    String group = "tictac7x-charges";
    String version = "version";
    String storage = "storage";
    String date = "date";
    String debug_ids = "debug_ids";

    String barrows_set = "barrows_set";
    String ogre_bellows = "ogre_bellows";
    String ring_of_slayer = "slayer_ring";
    String forestry_kit = "forestry_kit";
    String necklace_of_passage = "necklage_of_passage";
    String necklace_of_phoenix = "necklage_of_phoenix";
    String fungicide_spray = "fungicide_spray";
    String tackle_box = "tackle_box";
    String waterskin = "waterskin";

    String arclight = "arclight";
    String ash_sanctifier = "ash_sanctifier";
    String ash_sanctifier_status = "ash_sanctifier_status";
    String bonecrusher = "bone_crusher";
    String bone_crusher_status = "bone_crusher_status";
    String kharedsts_memoirs = "kharedsts_memoirs";
    String bottomless_compost_bucket = "bottomless_compost_bucket";
    String bottomless_compost_bucket_type = "bottomless_compost_bucket_type";
    String bracelet_of_slaughter = "bracelet_of_slaughter";
    String bryophytas_staff = "bryophytas_staff";
    String ring_of_celestial = "celestial_ring";
    String ring_of_the_elements = "ring_of_elements";
    String escape_crystal = "escape_crystal";
    String escape_crystal_status = "escape_crystal_status";
    String chronicle = "chronicle";
    String crystal_shield = "crystal_shield";
    String crystal_bow = "crystal_bow";
    String crystal_halberd = "crystal_halberd";
    String bracelet_of_expeditious = "expeditious_bracelet";
    String falador_shield = "falador_shield";
    String fish_barrel = "fish_barrel";
    String gricollers_can = "gricollers_can";
    String ibans_staff = "ibans_staff";
    String pharaohs_sceptre = "pharaohs_sceptre";
    String ring_of_suffering = "ring_of_suffering";
    String ring_of_suffering_status = "ring_of_suffering_status";
    String sanguinesti_staff = "sanguinesti_staff";
    String skull_sceptre = "skull_sceptre";
    String enchanted_lyre = "enchanted_lyre";
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
    String tome_of_water = "tome_of_water";
    String necklace_of_dodgy = "dodgy_necklace";
    String kandarin_headgear = "kandarin_headgear";
    String fremennik_sea_boots = "fremennik_sea_boots";
    String jar_generator = "jar_generator";
    String ring_of_explorers = "explorers_ring";
    String plank_sack = "plank_sack";
    String slayer_staff_e = "slayer_staff_e";
    String warped_sceptre = "warped_sceptre";
    String crystal_saw = "crystal_saw";

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
            keyName = "storage_tooltips",
            name = "Show storage tooltips",
            description = "Show tooltips for items with storage",
            position = 2,
            section = general
        ) default boolean showStorageTooltips() { return true; }

        @ConfigItem(
            keyName = "menu_replacements",
            name = "Unify menu entries",
            description = "Replace obscure menu entries like \"Reminisce\" and \"Divine\" with \"Teleport\" and \"Check\" and show detailed herb patches names.",
            position = 3,
            section = general
        ) default boolean useCommonMenuEntries() { return true; }

        @ConfigItem(
            keyName = "hide_destroy",
            name = "Hide destroy menu entries",
            description = "Hide destroy menu entry from items that make no sense to destroy",
            position = 4,
            section = general
        ) default boolean hideDestroy() { return true; }

        @ConfigItem(
            keyName = "show_unlimited_charges",
            name = "Show unlimited charges",
            description = "Show infinity symbol for items with unlimited charges",
            position = 5,
            section = general
        ) default boolean showUnlimited() { return true; }

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
        description = "Choose for which charged items infobox is visible",
        position = 3,
        closedByDefault = true
    ) String infoboxes = "infoboxes";

        @ConfigItem(
            keyName = barrows_set + "_infobox",
            name = "Barrows set",
            description = "",
            section = infoboxes
        ) default boolean barrowsInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_body + "_infobox",
            name = "Crystal body",
            description = "",
            section = infoboxes
        ) default boolean crystalBodyInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_helm + "_infobox",
            name = "Crystal helm",
            description = "",
            section = infoboxes
        ) default boolean crystalHelmInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_legs + "_infobox",
            name = "Crystal legs",
            description = "",
            section = infoboxes
        ) default boolean crystalLegsInfobox() { return true; }

        @ConfigItem(
            keyName = fremennik_sea_boots + "_infobox",
            name = "Fremennik sea boots",
            description = "",
            section = infoboxes
        ) default boolean fremennikSeaBootsInfobox() { return true; }

        @ConfigItem(
            keyName = ardougne_cloak + "_infobox",
            name = "Ardougne cloak",
            description = "",
            section = infoboxes
        ) default boolean ardougneCloakInfobox() { return true; }

        @ConfigItem(
            keyName = coffin + "_infobox",
            name = "Coffin",
            description = "",
            section = infoboxes
        ) default boolean coffinInfobox() { return true; }

        @ConfigItem(
            keyName = forestry_kit + "_infobox",
            name = "Forestry kit",
            description = "",
            section = infoboxes
        ) default boolean forestryKitInfobox() { return true; }

        @ConfigItem(
            keyName = magic_cape + "_infobox",
            name = "Magic cape",
            description = "",
            section = infoboxes
        ) default boolean magicCapeInfobox() { return true; }

        @ConfigItem(
            keyName = circlet_of_water + "_infobox",
            name = "Circlet of water",
            description = "",
            section = infoboxes
        ) default boolean circletOfWaterInfobox() { return true; }

        @ConfigItem(
            keyName = kandarin_headgear + "_infobox",
            name = "Kandarin Headgear",
            description = "",
            section = infoboxes
        ) default boolean kandarinHeadgearInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_clay + "_infobox",
            name = "Bracelet of clay",
            description = "",
            section = infoboxes
        ) default boolean braceletOfClayInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_expeditious + "_infobox",
            name = "Expeditious bracelet",
            description = "",
            section = infoboxes
        ) default boolean expeditiousBraceletInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_flamtaer + "_infobox",
            name = "Flamtaer bracelet",
            description = "",
            section = infoboxes
        ) default boolean flamtaerBraceletInfobox() { return true; }

        @ConfigItem(
            keyName = bracelet_of_slaughter + "_infobox",
            name = "Bracelet of slaughter",
            description = "",
            section = infoboxes
        ) default boolean braceletOfSlaughterInfobox() { return true; }

        @ConfigItem(
            keyName = camulet + "_infobox",
            name = "Camulet",
            description = "",
            section = infoboxes
        ) default boolean camuletInfobox() { return true; }

        @ConfigItem(
            keyName = desert_amulet + "_infobox",
            name = "Desert amulet",
            description = "",
            section = infoboxes
        ) default boolean desertAmuletInfobox() { return true; }

        @ConfigItem(
            keyName = escape_crystal + "_infobox",
            name = "Escape crystal",
            description = "",
            section = infoboxes
        ) default boolean escapeCrystalInfobox() { return true; }

        @ConfigItem(
            keyName = necklace_of_dodgy + "_infobox",
            name = "Dodgy necklace",
            description = "",
            section = infoboxes
        ) default boolean dodgyNecklaceInfobox() { return true; }

        @ConfigItem(
            keyName = necklace_of_passage + "_infobox",
            name = "Necklace of passage",
            description = "",
            section = infoboxes
        ) default boolean necklaceOfPassageInfobox() { return true; }

        @ConfigItem(
            keyName = necklace_of_phoenix + "_infobox",
            name = "Phoenix necklace",
            description = "",
            section = infoboxes
        ) default boolean phoenixNecklaceInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_celestial + "_infobox",
            name = "Celestial ring",
            description = "",
            section = infoboxes
        ) default boolean celestialRingInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_the_elements + "_infobox",
            name = "Ring of the elements",
            description = "",
            section = infoboxes
        ) default boolean ringOfTheElementsInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_explorers + "_infobox",
            name = "Explorer's ring",
            description = "",
            section = infoboxes
        ) default boolean explorersRingInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_recoil + "_infobox",
            name = "Ring of recoil",
            description = "",
            section = infoboxes
        ) default boolean ringOfRecoilInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_shadows + "_infobox",
            name = "Ring of shadows",
            description = "",
            section = infoboxes
        ) default boolean ringOfShadowsInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_slayer + "_infobox",
            name = "Slayer ring",
            description = "",
            section = infoboxes
        ) default boolean slayerRingInfobox() { return true; }

        @ConfigItem(
            keyName = ring_of_suffering + "_infobox",
            name = "Ring of suffering",
            description = "",
            section = infoboxes
        ) default boolean ringOfSufferingInfobox() { return true; }

        @ConfigItem(
            keyName = xerics_talisman + "_infobox",
            name = "Xeric's talisman",
            description = "",
            section = infoboxes
        ) default boolean xericsTalismanInfobox() { return true; }

        @ConfigItem(
            keyName = chronicle + "_infobox",
            name = "Chronicle",
            description = "",
            section = infoboxes
        ) default boolean chronicleInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_shield + "_infobox",
            name = "Crystal shield",
            description = "",
            section = infoboxes
        ) default boolean crystalShieldInfobox() { return true; }

        @ConfigItem(
            keyName = dragonfire_shield + "_infobox",
            name = "Dragonfire shield",
            description = "",
            section = infoboxes
        ) default boolean dragonfireShieldInfobox() { return true; }

        @ConfigItem(
            keyName = falador_shield + "_infobox",
            name = "Falador shield",
            description = "",
            section = infoboxes
        ) default boolean faladorShieldInfobox() { return true; }

        @ConfigItem(
            keyName = kharedsts_memoirs + "_infobox",
            name = "Kharedst's memoirs",
            description = "",
            section = infoboxes
        ) default boolean kharedstsMemoirsInfobox() { return true; }

        @ConfigItem(
            keyName = tome_of_fire + "_infobox",
            name = "Tome of fire",
            description = "",
            section = infoboxes
        ) default boolean tomeOfFireInfobox() { return true; }

        @ConfigItem(
            keyName = tome_of_water + "_infobox",
            name = "Tome of water",
            description = "",
            section = infoboxes
        ) default boolean tomeOfWaterInfobox() { return true; }

        @ConfigItem(
            keyName = ash_sanctifier + "_infobox",
            name = "Ash sanctifier",
            description = "",
            section = infoboxes
        ) default boolean ashSanctifierInfobox() { return true; }

        @ConfigItem(
            keyName = bonecrusher + "_infobox",
            name = "Bonecrusher",
            description = "",
            section = infoboxes
        ) default boolean bonecrusherInfobox() { return true; }

        @ConfigItem(
            keyName = bottomless_compost_bucket + "_infobox",
            name = "Bottomless compost bucket",
            description = "",
            section = infoboxes
        ) default boolean bottomlessCompostBucketInfobox() { return true; }

        @ConfigItem(
            keyName = coal_bag + "_infobox",
            name = "Coal bag",
            description = "",
            section = infoboxes
        ) default boolean coalBagInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_saw + "_infobox",
            name = "Crystal saw",
            description = "",
            section = infoboxes
        ) default boolean crystalSawInfobox() { return true; }

        @ConfigItem(
            keyName = fish_barrel + "_infobox",
            name = "Fish barrel",
            description = "",
            section = infoboxes
        ) default boolean fishBarrelInfobox() { return true; }

        @ConfigItem(
            keyName = fungicide_spray + "_infobox",
            name = "Fungicide spray",
            description = "",
            section = infoboxes
        ) default boolean fungicideSprayInfobox() { return true; }

        @ConfigItem(
            keyName = gem_bag + "_infobox",
            name = "Gem bag",
            description = "",
            section = infoboxes
        ) default boolean gemBagInfobox() { return true; }

        @ConfigItem(
            keyName = gricollers_can + "_infobox",
            name = "Gricoller's can",
            description = "",
            section = infoboxes
        ) default boolean gricollersCanInfobox() { return true; }

        @ConfigItem(
            keyName = herb_sack + "_infobox",
            name = "Herb sack",
            description = "",
            section = infoboxes
        ) default boolean herbSackInfobox() { return true; }

        @ConfigItem(
            keyName = jar_generator + "_infobox",
            name = "Jar generator",
            description = "",
            section = infoboxes
        ) default boolean jarGeneratorInfobox() { return true; }

        @ConfigItem(
            keyName = log_basket + "_infobox",
            name = "Log basket",
            description = "",
            section = infoboxes
        ) default boolean logBasketInfobox() { return true; }

        @ConfigItem(
            keyName = ogre_bellows + "_infobox",
            name = "Ogre bellows",
            description = "",
            section = infoboxes
        ) default boolean ogreBellowsInfobox() { return true; }

        @ConfigItem(
            keyName = plank_sack + "_infobox",
            name = "Plank sack",
            description = "",
            section = infoboxes
        ) default boolean plankSackInfobox() { return true; }

        @ConfigItem(
            keyName = seed_box + "_infobox",
            name = "Seed box",
            description = "",
            section = infoboxes
        ) default boolean seedBoxInfobox() { return true; }

        @ConfigItem(
            keyName = soul_bearer + "_infobox",
            name = "Soul bearer",
            description = "",
            section = infoboxes
        ) default boolean soulBearerInfobox() { return true; }

        @ConfigItem(
            keyName = strange_old_lockpick + "_infobox",
            name = "Strange old lockpick",
            description = "",
            section = infoboxes
        ) default boolean strangeOldLockpickInfobox() { return true; }

        @ConfigItem(
            keyName = tackle_box + "_infobox",
            name = "Tackle box",
            description = "",
            section = infoboxes
        ) default boolean tackleBoxInfobox() { return true; }

        @ConfigItem(
            keyName = teleport_crystal + "_infobox",
            name = "Teleport crystal",
            description = "",
            section = infoboxes
        ) default boolean teleportCrystalInfobox() { return true; }

        @ConfigItem(
            keyName = waterskin + "_infobox",
            name = "Waterskin",
            description = "",
            section = infoboxes
        ) default boolean waterskinInfobox() { return true; }

        @ConfigItem(
            keyName = arclight + "_infobox",
            name = "Arclight",
            description = "",
            section = infoboxes
        ) default boolean arclightInfobox() { return true; }

        @ConfigItem(
            keyName = bryophytas_staff + "_infobox",
            name = "Bryophyta's staff",
            description = "",
            section = infoboxes
        ) default boolean bryophytasStaffInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_bow + "_infobox",
            name = "Crystal bow",
            description = "",
            section = infoboxes
        ) default boolean crystalBowInfobox() { return true; }

        @ConfigItem(
            keyName = crystal_halberd + "_infobox",
            name = "Crystal halberd",
            description = "",
            section = infoboxes
        ) default boolean crystalHalberdInfobox() { return true; }

        @ConfigItem(
            keyName = enchanted_lyre + "_infobox",
            name = "Enchanged Lyre",
            description = "",
            section = infoboxes
        ) default boolean enchantedLyreInfobox() { return true; }

        @ConfigItem(
            keyName = ibans_staff + "_infobox",
            name = "Iban's staff",
            description = "",
            section = infoboxes
        ) default boolean ibansStaffInfobox() { return true; }

        @ConfigItem(
            keyName = pharaohs_sceptre + "_infobox",
            name = "Pharaoh's sceptre",
            description = "",
            section = infoboxes
        ) default boolean pharaohsSceptreInfobox() { return true; }

        @ConfigItem(
            keyName = sanguinesti_staff + "_infobox",
            name = "Sanguinesti staff",
            description = "",
            section = infoboxes
        ) default boolean sanguinestiStaffInfobox() { return true; }

        @ConfigItem(
            keyName = skull_sceptre + "_infobox",
            name = "Skull sceptre",
            description = "",
            section = infoboxes
        ) default boolean skullSceptreInfobox() { return true; }

        @ConfigItem(
            keyName = slayer_staff_e + "_infobox",
            name = "Slayer staff (e)",
            description = "",
            section = infoboxes
        ) default boolean slayerStaffEInfobox() { return true; }

        @ConfigItem(
            keyName = toxic_staff_of_the_dead + "_infobox",
            name = "Toxic staff of the dead",
            description = "",
            section = infoboxes
        ) default boolean toxicStaffOfTheDeadInfobox() { return true; }

        @ConfigItem(
            keyName = trident_of_the_seas + "_infobox",
            name = "Trident of the seas",
            description = "",
            section = infoboxes
        ) default boolean tridentOfTheSeasInfobox() { return true; }

        @ConfigItem(
            keyName = warped_sceptre + "_infobox",
            name = "Warped sceptre",
            description = "",
            section = infoboxes
        ) default boolean warpedSceptreInfobox() { return true; }


    @ConfigSection(
        name = "Overlays",
        description = "Choose for which charged items number is shown next to it",
        position = 4,
        closedByDefault = true
    ) String overlays = "overlays";

    @ConfigSection(
        name = "Debug",
        description = "Values of charges for all items under the hood",
        position = 99,
        closedByDefault = true
    ) String debug = "debug";

        @ConfigItem(
            keyName = version,
            name = version,
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
            keyName = storage,
            name = storage,
            description = "All player items to check for daily resets",
            section = debug,
            position = 3
        ) default String getStorage() { return ""; }

        @ConfigItem(
            keyName = debug_ids,
            name = "Debug IDs",
            description = "Shows animation and graphics ids within ingame messages to add support for new items",
            section = debug,
            position = 4
        ) default boolean showDebugIds() { return false; }

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
            keyName = bonecrusher,
            name = bonecrusher,
            description = bonecrusher,
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
            keyName = ring_of_celestial,
            name = ring_of_celestial,
            description = ring_of_celestial,
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
            keyName = bracelet_of_expeditious,
            name = bracelet_of_expeditious,
            description = bracelet_of_expeditious,
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
            keyName = log_basket + "_storage",
            name = log_basket + "_storage",
            description = log_basket + "_storage",
            section = debug
        ) default String getLogBasketStorage() { return ""; }

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
            keyName = gem_bag + "_storage",
            name = gem_bag + "_storage",
            description = gem_bag + "_storage",
            section = debug
        ) default String getGemBagStorageCharges() { return ""; }

        @ConfigItem(
            keyName = seed_box,
            name = seed_box,
            description = seed_box,
            section = debug
        ) default int getSeedBoxCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = seed_box + "_storage",
            name = seed_box + "_storage",
            description = seed_box + "_storage",
            section = debug
        ) default String getSeedBoxStorage() { return ""; }

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

        @ConfigItem(
            keyName = necklace_of_dodgy,
            name = necklace_of_dodgy,
            description = necklace_of_dodgy,
            section = debug
        ) default int getDodgyNecklaceCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = kandarin_headgear,
            name = kandarin_headgear,
            description = kandarin_headgear,
            section = debug
        ) default int getKandarinHeadgearCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = fremennik_sea_boots,
            name = fremennik_sea_boots,
            description = fremennik_sea_boots,
            section = debug
        ) default int getFremennikSeaBootsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = jar_generator,
            name = jar_generator,
            description = jar_generator,
            section = debug
        ) default int getJarGeneratorCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_explorers + "_storage",
            name = ring_of_explorers + "_storage",
            description = ring_of_explorers + "_storage",
            section = debug
        ) default String getExplorersRingCharges() { return ""; }

        @ConfigItem(
            keyName = enchanted_lyre,
            name = enchanted_lyre,
            description = enchanted_lyre,
            section = debug
        ) default int getEnchantedLyreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = ring_of_the_elements,
            name = ring_of_the_elements,
            description = ring_of_the_elements,
            section = debug
        ) default int getRingOfElementsCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = plank_sack,
            name = plank_sack,
            description = plank_sack,
            section = debug
        ) default int getPlankSackCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = slayer_staff_e,
            name = slayer_staff_e,
            description = slayer_staff_e,
            section = debug
        ) default int getSlayerStaffECharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = warped_sceptre,
            name = warped_sceptre,
            description = warped_sceptre,
            section = debug
        ) default int getWarpedSceptreCharges() { return Charges.UNKNOWN; }

        @ConfigItem(
            keyName = crystal_saw,
            name = crystal_saw,
            description = crystal_saw,
            section = debug
        ) default int getCrystalSawCharges() { return Charges.UNKNOWN; }
}
