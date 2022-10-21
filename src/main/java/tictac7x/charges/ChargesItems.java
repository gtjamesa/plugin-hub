package tictac7x.charges;

import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChargesItems {
    @Nonnull
    private final ItemManager items;

    @Nonnull
    private final InfoBoxManager infoboxes;

    public ChargesItems(final @Nonnull ItemManager items, final @Nonnull InfoBoxManager infoboxes) {
        this.items = items;
        this.infoboxes = infoboxes;
    }

    public void updateInfoboxes(@Nullable final ItemContainer inventory, @Nullable final ItemContainer equipment, final ChargesInfoBox[] infoboxes_inventory, final ChargesInfoBox[] infoboxes_equipment) {
        final List<Integer> used_items = new ArrayList<>();

        // Equipment items.
        if (equipment != null) {
            for (int i = 0; i < equipment.size(); i++) {
                updateInfoBoxItem(equipment.getItems()[i], inventory, equipment, infoboxes_equipment[i], used_items);
            }
        }

        // Inventory items.
        if (inventory != null) {
            for (int i = 0; i < inventory.size(); i++) {
                updateInfoBoxItem(inventory.getItems()[i], inventory, equipment, infoboxes_inventory[i], used_items);
            }
        }
    }

    private void updateInfoBoxItem(final Item item, final ItemContainer inventory, final ItemContainer equipment, final ChargesInfoBox infobox, final  List<Integer> used_items) {
        // No item or item already shown.
        if (item.getId() == -1 || used_items.contains(item.getId())) {
            infobox.setRender(false);
            return;
        }

        // New item data.
        final ChargesItem charges_item = getInfoBoxItem(item, inventory, equipment);

        // Item not implemented.
        if (charges_item == null) {
            infobox.setRender(false);
            return;
        }

        // Correct new item found.
        used_items.add(item.getId());
        infobox.setImage(charges_item.image);
        infobox.setTooltip(charges_item.tooltip);
        infobox.setText(charges_item.text);
        infobox.setColor(charges_item.color);
        infobox.setRender(true);
        infoboxes.updateInfoBoxImage(infobox);
    }

    private ChargesItem getInfoBoxItem(Item item, final ItemContainer inventory, final ItemContainer equipment) {
        switch (item.getId()) {
            case ItemID.AMULET_OF_GLORY:
            case ItemID.AMULET_OF_GLORY1:
            case ItemID.AMULET_OF_GLORY2:
            case ItemID.AMULET_OF_GLORY3:
            case ItemID.AMULET_OF_GLORY4:
            case ItemID.AMULET_OF_GLORY5:
            case ItemID.AMULET_OF_GLORY6:
                return getAmuletOfGlory(inventory, equipment);

            case ItemID.ENCHANTED_GEM:
            case ItemID.SLAYER_RING_1:
            case ItemID.SLAYER_RING_2:
            case ItemID.SLAYER_RING_3:
            case ItemID.SLAYER_RING_4:
            case ItemID.SLAYER_RING_5:
            case ItemID.SLAYER_RING_6:
            case ItemID.SLAYER_RING_7:
            case ItemID.SLAYER_RING_8:
                return getSlayerRing(inventory, equipment);

            case ItemID.SKILLS_NECKLACE:
            case ItemID.SKILLS_NECKLACE1:
            case ItemID.SKILLS_NECKLACE2:
            case ItemID.SKILLS_NECKLACE3:
            case ItemID.SKILLS_NECKLACE4:
            case ItemID.SKILLS_NECKLACE5:
            case ItemID.SKILLS_NECKLACE6:
                return getSkillsNecklace(inventory, equipment);

            case ItemID.EMERALD_RING:
            case ItemID.RING_OF_DUELING1:
            case ItemID.RING_OF_DUELING2:
            case ItemID.RING_OF_DUELING3:
            case ItemID.RING_OF_DUELING4:
            case ItemID.RING_OF_DUELING5:
            case ItemID.RING_OF_DUELING6:
            case ItemID.RING_OF_DUELING7:
            case ItemID.RING_OF_DUELING8:
                return getRingOfDueling(inventory, equipment);

            case ItemID.KARILS_COIF:
            case ItemID.KARILS_COIF_0:
            case ItemID.KARILS_COIF_25:
            case ItemID.KARILS_COIF_50:
            case ItemID.KARILS_COIF_75:
            case ItemID.KARILS_COIF_100:
                return getKarilsCoif(inventory, equipment);

            case ItemID.KARILS_LEATHERTOP:
            case ItemID.KARILS_LEATHERTOP_0:
            case ItemID.KARILS_LEATHERTOP_25:
            case ItemID.KARILS_LEATHERTOP_50:
            case ItemID.KARILS_LEATHERTOP_75:
            case ItemID.KARILS_LEATHERTOP_100:
                return getKarilsLeathertop(inventory, equipment);

            case ItemID.KARILS_LEATHERSKIRT:
            case ItemID.KARILS_LEATHERSKIRT_0:
            case ItemID.KARILS_LEATHERSKIRT_25:
            case ItemID.KARILS_LEATHERSKIRT_50:
            case ItemID.KARILS_LEATHERSKIRT_75:
            case ItemID.KARILS_LEATHERSKIRT_100:
                return getKarilsLeatherskirt(inventory, equipment);

            case ItemID.KARILS_CROSSBOW:
            case ItemID.KARILS_CROSSBOW_0:
            case ItemID.KARILS_CROSSBOW_25:
            case ItemID.KARILS_CROSSBOW_50:
            case ItemID.KARILS_CROSSBOW_75:
            case ItemID.KARILS_CROSSBOW_100:
                return getKarilsCrossbow(inventory, equipment);

            case ItemID.DHAROKS_HELM:
            case ItemID.DHAROKS_HELM_0:
            case ItemID.DHAROKS_HELM_25:
            case ItemID.DHAROKS_HELM_50:
            case ItemID.DHAROKS_HELM_75:
            case ItemID.DHAROKS_HELM_100:
                return getDharoksHelm(inventory, equipment);

            case ItemID.DHAROKS_PLATEBODY:
            case ItemID.DHAROKS_PLATEBODY_0:
            case ItemID.DHAROKS_PLATEBODY_25:
            case ItemID.DHAROKS_PLATEBODY_50:
            case ItemID.DHAROKS_PLATEBODY_75:
            case ItemID.DHAROKS_PLATEBODY_100:
                return getDharoksPlatebody(inventory, equipment);

            case ItemID.DHAROKS_PLATELEGS:
            case ItemID.DHAROKS_PLATELEGS_0:
            case ItemID.DHAROKS_PLATELEGS_25:
            case ItemID.DHAROKS_PLATELEGS_50:
            case ItemID.DHAROKS_PLATELEGS_75:
            case ItemID.DHAROKS_PLATELEGS_100:
                return getDharoksPlatelegs(inventory, equipment);

            case ItemID.DHAROKS_GREATAXE:
            case ItemID.DHAROKS_GREATAXE_0:
            case ItemID.DHAROKS_GREATAXE_25:
            case ItemID.DHAROKS_GREATAXE_50:
            case ItemID.DHAROKS_GREATAXE_75:
            case ItemID.DHAROKS_GREATAXE_100:
                return getDharoksGreataxe(inventory, equipment);

            case ItemID.GUTHANS_HELM:
            case ItemID.GUTHANS_HELM_0:
            case ItemID.GUTHANS_HELM_25:
            case ItemID.GUTHANS_HELM_50:
            case ItemID.GUTHANS_HELM_75:
            case ItemID.GUTHANS_HELM_100:
                return getGuthansHelm(inventory, equipment);

            case ItemID.GUTHANS_PLATEBODY:
            case ItemID.GUTHANS_PLATEBODY_0:
            case ItemID.GUTHANS_PLATEBODY_25:
            case ItemID.GUTHANS_PLATEBODY_50:
            case ItemID.GUTHANS_PLATEBODY_75:
            case ItemID.GUTHANS_PLATEBODY_100:
                return getGuthansPlatebody(inventory, equipment);

            case ItemID.GUTHANS_CHAINSKIRT:
            case ItemID.GUTHANS_CHAINSKIRT_0:
            case ItemID.GUTHANS_CHAINSKIRT_25:
            case ItemID.GUTHANS_CHAINSKIRT_50:
            case ItemID.GUTHANS_CHAINSKIRT_75:
            case ItemID.GUTHANS_CHAINSKIRT_100:
                return getGuthansChainskirt(inventory, equipment);

            case ItemID.GUTHANS_WARSPEAR:
            case ItemID.GUTHANS_WARSPEAR_0:
            case ItemID.GUTHANS_WARSPEAR_25:
            case ItemID.GUTHANS_WARSPEAR_50:
            case ItemID.GUTHANS_WARSPEAR_75:
            case ItemID.GUTHANS_WARSPEAR_100:
                return getGuthansWarspear(inventory, equipment);

            case ItemID.VERACS_HELM:
            case ItemID.VERACS_HELM_0:
            case ItemID.VERACS_HELM_25:
            case ItemID.VERACS_HELM_50:
            case ItemID.VERACS_HELM_75:
            case ItemID.VERACS_HELM_100:
                return getVeracsHelm(inventory, equipment);

            case ItemID.VERACS_BRASSARD:
            case ItemID.VERACS_BRASSARD_0:
            case ItemID.VERACS_BRASSARD_25:
            case ItemID.VERACS_BRASSARD_50:
            case ItemID.VERACS_BRASSARD_75:
            case ItemID.VERACS_BRASSARD_100:
                return getVeracsBrassard(inventory, equipment);

            case ItemID.VERACS_PLATESKIRT:
            case ItemID.VERACS_PLATESKIRT_0:
            case ItemID.VERACS_PLATESKIRT_25:
            case ItemID.VERACS_PLATESKIRT_50:
            case ItemID.VERACS_PLATESKIRT_75:
            case ItemID.VERACS_PLATESKIRT_100:
                return getVeracsPlateskirt(inventory, equipment);

            case ItemID.VERACS_FLAIL:
            case ItemID.VERACS_FLAIL_0:
            case ItemID.VERACS_FLAIL_25:
            case ItemID.VERACS_FLAIL_50:
            case ItemID.VERACS_FLAIL_75:
            case ItemID.VERACS_FLAIL_100:
                return getVeracsFlail(inventory, equipment);

            case ItemID.AHRIMS_HOOD:
            case ItemID.AHRIMS_HOOD_0:
            case ItemID.AHRIMS_HOOD_25:
            case ItemID.AHRIMS_HOOD_50:
            case ItemID.AHRIMS_HOOD_75:
            case ItemID.AHRIMS_HOOD_100:
                return getAhrimsHood(inventory, equipment);

            case ItemID.AHRIMS_ROBETOP:
            case ItemID.AHRIMS_ROBETOP_0:
            case ItemID.AHRIMS_ROBETOP_25:
            case ItemID.AHRIMS_ROBETOP_50:
            case ItemID.AHRIMS_ROBETOP_75:
            case ItemID.AHRIMS_ROBETOP_100:
                return getAhrimsRobetop(inventory, equipment);

            case ItemID.AHRIMS_ROBESKIRT:
            case ItemID.AHRIMS_ROBESKIRT_0:
            case ItemID.AHRIMS_ROBESKIRT_25:
            case ItemID.AHRIMS_ROBESKIRT_50:
            case ItemID.AHRIMS_ROBESKIRT_75:
            case ItemID.AHRIMS_ROBESKIRT_100:
                return getAhrimsRobeskirt(inventory, equipment);

            case ItemID.AHRIMS_STAFF:
            case ItemID.AHRIMS_STAFF_0:
            case ItemID.AHRIMS_STAFF_25:
            case ItemID.AHRIMS_STAFF_50:
            case ItemID.AHRIMS_STAFF_75:
            case ItemID.AHRIMS_STAFF_100:
                return getAhrimsStaff(inventory, equipment);

            case ItemID.TORAGS_HELM:
            case ItemID.TORAGS_HELM_0:
            case ItemID.TORAGS_HELM_25:
            case ItemID.TORAGS_HELM_50:
            case ItemID.TORAGS_HELM_75:
            case ItemID.TORAGS_HELM_100:
                return getToragsHelm(inventory, equipment);

            case ItemID.TORAGS_PLATEBODY:
            case ItemID.TORAGS_PLATEBODY_0:
            case ItemID.TORAGS_PLATEBODY_25:
            case ItemID.TORAGS_PLATEBODY_50:
            case ItemID.TORAGS_PLATEBODY_75:
            case ItemID.TORAGS_PLATEBODY_100:
                return getToragsPlatebody(inventory, equipment);

            case ItemID.TORAGS_PLATELEGS:
            case ItemID.TORAGS_PLATELEGS_0:
            case ItemID.TORAGS_PLATELEGS_25:
            case ItemID.TORAGS_PLATELEGS_50:
            case ItemID.TORAGS_PLATELEGS_75:
            case ItemID.TORAGS_PLATELEGS_100:
                return getToragsPlatelegs(inventory, equipment);

            case ItemID.TORAGS_HAMMERS:
            case ItemID.TORAGS_HAMMERS_0:
            case ItemID.TORAGS_HAMMERS_25:
            case ItemID.TORAGS_HAMMERS_50:
            case ItemID.TORAGS_HAMMERS_75:
            case ItemID.TORAGS_HAMMERS_100:
                return getToragsHammers(inventory, equipment);
        }

        return null;
    }

    private ChargesItem getRingOfDueling(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.EMERALD_RING,
            items.getItemComposition(ItemID.RING_OF_DUELING1).getName(),
            items.getImage(ItemID.RING_OF_DUELING1),
            inventory.count(ItemID.RING_OF_DUELING1) +
            inventory.count(ItemID.RING_OF_DUELING2) * 2 +
            inventory.count(ItemID.RING_OF_DUELING3) * 3 +
            inventory.count(ItemID.RING_OF_DUELING4) * 4 +
            inventory.count(ItemID.RING_OF_DUELING5) * 5 +
            inventory.count(ItemID.RING_OF_DUELING6) * 6 +
            inventory.count(ItemID.RING_OF_DUELING7) * 7 +
            inventory.count(ItemID.RING_OF_DUELING8) * 8 +
            equipment.count(ItemID.RING_OF_DUELING1) +
            equipment.count(ItemID.RING_OF_DUELING2) * 2 +
            equipment.count(ItemID.RING_OF_DUELING3) * 3 +
            equipment.count(ItemID.RING_OF_DUELING4) * 4 +
            equipment.count(ItemID.RING_OF_DUELING5) * 5 +
            equipment.count(ItemID.RING_OF_DUELING6) * 6 +
            equipment.count(ItemID.RING_OF_DUELING7) * 7 +
            equipment.count(ItemID.RING_OF_DUELING8) * 8,
            false
        );
    }

    private ChargesItem getAmuletOfGlory(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.AMULET_OF_GLORY,
            items.getItemComposition(ItemID.AMULET_OF_GLORY).getName(),
            items.getImage(ItemID.AMULET_OF_GLORY1),
            inventory.count(ItemID.AMULET_OF_GLORY1) +
            inventory.count(ItemID.AMULET_OF_GLORY2) * 2 +
            inventory.count(ItemID.AMULET_OF_GLORY3) * 3 +
            inventory.count(ItemID.AMULET_OF_GLORY4) * 4 +
            inventory.count(ItemID.AMULET_OF_GLORY5) * 5 +
            inventory.count(ItemID.AMULET_OF_GLORY6) * 6 +
            equipment.count(ItemID.AMULET_OF_GLORY1) +
            equipment.count(ItemID.AMULET_OF_GLORY2) * 2 +
            equipment.count(ItemID.AMULET_OF_GLORY3) * 3 +
            equipment.count(ItemID.AMULET_OF_GLORY4) * 4 +
            equipment.count(ItemID.AMULET_OF_GLORY5) * 5 +
            equipment.count(ItemID.AMULET_OF_GLORY6) * 6,
            false
        );
    }

    private ChargesItem getSlayerRing(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.SLAYER_RING_1,
            items.getItemComposition(ItemID.SLAYER_RING_1).getName(),
            items.getImage(ItemID.SLAYER_RING_1),
            inventory.count(ItemID.SLAYER_RING_1) +
            inventory.count(ItemID.SLAYER_RING_2) * 2 +
            inventory.count(ItemID.SLAYER_RING_3) * 3 +
            inventory.count(ItemID.SLAYER_RING_4) * 4 +
            inventory.count(ItemID.SLAYER_RING_5) * 5 +
            inventory.count(ItemID.SLAYER_RING_6) * 6 +
            inventory.count(ItemID.SLAYER_RING_7) * 7 +
            inventory.count(ItemID.SLAYER_RING_8) * 8 +
            equipment.count(ItemID.SLAYER_RING_1) +
            equipment.count(ItemID.SLAYER_RING_2) * 2 +
            equipment.count(ItemID.SLAYER_RING_3) * 3 +
            equipment.count(ItemID.SLAYER_RING_4) * 4 +
            equipment.count(ItemID.SLAYER_RING_5) * 5 +
            equipment.count(ItemID.SLAYER_RING_6) * 6 +
            equipment.count(ItemID.SLAYER_RING_7) * 7 +
            equipment.count(ItemID.SLAYER_RING_8) * 8,
            false
        );
    }

    private ChargesItem getSkillsNecklace(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.SKILLS_NECKLACE,
            items.getItemComposition(ItemID.SKILLS_NECKLACE).getName(),
            items.getImage(ItemID.SKILLS_NECKLACE1),
            inventory.count(ItemID.SKILLS_NECKLACE1) +
            inventory.count(ItemID.SKILLS_NECKLACE2) * 2 +
            inventory.count(ItemID.SKILLS_NECKLACE3) * 3 +
            inventory.count(ItemID.SKILLS_NECKLACE4) * 4 +
            inventory.count(ItemID.SKILLS_NECKLACE5) * 5 +
            inventory.count(ItemID.SKILLS_NECKLACE6) * 6 +
            equipment.count(ItemID.SKILLS_NECKLACE1) +
            equipment.count(ItemID.SKILLS_NECKLACE2) * 2 +
            equipment.count(ItemID.SKILLS_NECKLACE3) * 3 +
            equipment.count(ItemID.SKILLS_NECKLACE4) * 4 +
            equipment.count(ItemID.SKILLS_NECKLACE5) * 5 +
            equipment.count(ItemID.SKILLS_NECKLACE6) * 6,
            false
        );
    }

    private ChargesItem getKarilsCoif(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.KARILS_COIF,
            items.getItemComposition(ItemID.KARILS_COIF).getName(),
            items.getImage(ItemID.KARILS_COIF),
            inventory.count(ItemID.KARILS_COIF_25) * 25 +
            inventory.count(ItemID.KARILS_COIF_50) * 50 +
            inventory.count(ItemID.KARILS_COIF_75) * 75 +
            inventory.count(ItemID.KARILS_COIF_100) * 100 +
            inventory.count(ItemID.KARILS_COIF) * 100 +
            equipment.count(ItemID.KARILS_COIF_25) * 25 +
            equipment.count(ItemID.KARILS_COIF_50) * 50 +
            equipment.count(ItemID.KARILS_COIF_75) * 75 +
            equipment.count(ItemID.KARILS_COIF_100) * 100 +
            equipment.count(ItemID.KARILS_COIF) * 100,
            false
        );
    }

    private ChargesItem getKarilsLeathertop(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.KARILS_LEATHERTOP,
            items.getItemComposition(ItemID.KARILS_LEATHERTOP).getName(),
            items.getImage(ItemID.KARILS_LEATHERTOP),
            inventory.count(ItemID.KARILS_LEATHERTOP_25) * 25 +
            inventory.count(ItemID.KARILS_LEATHERTOP_50) * 50 +
            inventory.count(ItemID.KARILS_LEATHERTOP_75) * 75 +
            inventory.count(ItemID.KARILS_LEATHERTOP_100) * 100 +
            inventory.count(ItemID.KARILS_LEATHERTOP) * 100 +
            equipment.count(ItemID.KARILS_LEATHERTOP_25) * 25 +
            equipment.count(ItemID.KARILS_LEATHERTOP_50) * 50 +
            equipment.count(ItemID.KARILS_LEATHERTOP_75) * 75 +
            equipment.count(ItemID.KARILS_LEATHERTOP_100) * 100 +
            equipment.count(ItemID.KARILS_LEATHERTOP) * 100,
            false
        );
    }

    private ChargesItem getKarilsLeatherskirt(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.KARILS_LEATHERSKIRT,
            items.getItemComposition(ItemID.KARILS_LEATHERSKIRT).getName(),
            items.getImage(ItemID.KARILS_LEATHERSKIRT),
            inventory.count(ItemID.KARILS_LEATHERSKIRT_25) * 25 +
            inventory.count(ItemID.KARILS_LEATHERSKIRT_50) * 50 +
            inventory.count(ItemID.KARILS_LEATHERSKIRT_75) * 75 +
            inventory.count(ItemID.KARILS_LEATHERSKIRT_100) * 100 +
            inventory.count(ItemID.KARILS_LEATHERSKIRT) * 100 +
            equipment.count(ItemID.KARILS_LEATHERSKIRT_25) * 25 +
            equipment.count(ItemID.KARILS_LEATHERSKIRT_50) * 50 +
            equipment.count(ItemID.KARILS_LEATHERSKIRT_75) * 75 +
            equipment.count(ItemID.KARILS_LEATHERSKIRT_100) * 100 +
            equipment.count(ItemID.KARILS_LEATHERSKIRT) * 100,
            false
        );
    }

    private ChargesItem getKarilsCrossbow(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.KARILS_CROSSBOW,
            items.getItemComposition(ItemID.KARILS_CROSSBOW).getName(),
            items.getImage(ItemID.KARILS_CROSSBOW),
            inventory.count(ItemID.KARILS_CROSSBOW_25) * 25 +
            inventory.count(ItemID.KARILS_CROSSBOW_50) * 50 +
            inventory.count(ItemID.KARILS_CROSSBOW_75) * 75 +
            inventory.count(ItemID.KARILS_CROSSBOW_100) * 100 +
            inventory.count(ItemID.KARILS_CROSSBOW) * 100 +
            equipment.count(ItemID.KARILS_CROSSBOW_25) * 25 +
            equipment.count(ItemID.KARILS_CROSSBOW_50) * 50 +
            equipment.count(ItemID.KARILS_CROSSBOW_75) * 75 +
            equipment.count(ItemID.KARILS_CROSSBOW_100) * 100 +
            equipment.count(ItemID.KARILS_CROSSBOW) * 100,
            false
        );
    }

    private ChargesItem getDharoksHelm(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.DHAROKS_HELM,
            items.getItemComposition(ItemID.DHAROKS_HELM).getName(),
            items.getImage(ItemID.DHAROKS_HELM),
            inventory.count(ItemID.DHAROKS_HELM_25) * 25 +
            inventory.count(ItemID.DHAROKS_HELM_50) * 50 +
            inventory.count(ItemID.DHAROKS_HELM_75) * 75 +
            inventory.count(ItemID.DHAROKS_HELM_100) * 100 +
            inventory.count(ItemID.DHAROKS_HELM) * 100 +
            equipment.count(ItemID.DHAROKS_HELM_25) * 25 +
            equipment.count(ItemID.DHAROKS_HELM_50) * 50 +
            equipment.count(ItemID.DHAROKS_HELM_75) * 75 +
            equipment.count(ItemID.DHAROKS_HELM_100) * 100 +
            equipment.count(ItemID.DHAROKS_HELM) * 100,
            false
        );
    }

    private ChargesItem getDharoksPlatebody(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.DHAROKS_PLATEBODY,
            items.getItemComposition(ItemID.DHAROKS_PLATEBODY).getName(),
            items.getImage(ItemID.DHAROKS_PLATEBODY),
            inventory.count(ItemID.DHAROKS_PLATEBODY_25) * 25 +
            inventory.count(ItemID.DHAROKS_PLATEBODY_50) * 50 +
            inventory.count(ItemID.DHAROKS_PLATEBODY_75) * 75 +
            inventory.count(ItemID.DHAROKS_PLATEBODY_100) * 100 +
            inventory.count(ItemID.DHAROKS_PLATEBODY) * 100 +
            equipment.count(ItemID.DHAROKS_PLATEBODY_25) * 25 +
            equipment.count(ItemID.DHAROKS_PLATEBODY_50) * 50 +
            equipment.count(ItemID.DHAROKS_PLATEBODY_75) * 75 +
            equipment.count(ItemID.DHAROKS_PLATEBODY_100) * 100 +
            equipment.count(ItemID.DHAROKS_PLATEBODY) * 100,
            false
        );
    }

    private ChargesItem getDharoksPlatelegs(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.DHAROKS_PLATELEGS,
            items.getItemComposition(ItemID.DHAROKS_PLATELEGS).getName(),
            items.getImage(ItemID.DHAROKS_PLATELEGS),
            inventory.count(ItemID.DHAROKS_PLATELEGS_25) * 25 +
            inventory.count(ItemID.DHAROKS_PLATELEGS_50) * 50 +
            inventory.count(ItemID.DHAROKS_PLATELEGS_75) * 75 +
            inventory.count(ItemID.DHAROKS_PLATELEGS_100) * 100 +
            inventory.count(ItemID.DHAROKS_PLATELEGS) * 100 +
            equipment.count(ItemID.DHAROKS_PLATELEGS_25) * 25 +
            equipment.count(ItemID.DHAROKS_PLATELEGS_50) * 50 +
            equipment.count(ItemID.DHAROKS_PLATELEGS_75) * 75 +
            equipment.count(ItemID.DHAROKS_PLATELEGS_100) * 100 +
            equipment.count(ItemID.DHAROKS_PLATELEGS) * 100,
            false
        );
    }

    private ChargesItem getDharoksGreataxe(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.DHAROKS_GREATAXE,
            items.getItemComposition(ItemID.DHAROKS_GREATAXE).getName(),
            items.getImage(ItemID.DHAROKS_GREATAXE),
            inventory.count(ItemID.DHAROKS_GREATAXE_25) * 25 +
            inventory.count(ItemID.DHAROKS_GREATAXE_50) * 50 +
            inventory.count(ItemID.DHAROKS_GREATAXE_75) * 75 +
            inventory.count(ItemID.DHAROKS_GREATAXE_100) * 100 +
            inventory.count(ItemID.DHAROKS_GREATAXE) * 100 +
            equipment.count(ItemID.DHAROKS_GREATAXE_25) * 25 +
            equipment.count(ItemID.DHAROKS_GREATAXE_50) * 50 +
            equipment.count(ItemID.DHAROKS_GREATAXE_75) * 75 +
            equipment.count(ItemID.DHAROKS_GREATAXE_100) * 100 +
            equipment.count(ItemID.DHAROKS_GREATAXE) * 100,
            false
        );
    }

    private ChargesItem getGuthansHelm(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.GUTHANS_HELM,
            items.getItemComposition(ItemID.GUTHANS_HELM).getName(),
            items.getImage(ItemID.GUTHANS_HELM),
            inventory.count(ItemID.GUTHANS_HELM_25) * 25 +
            inventory.count(ItemID.GUTHANS_HELM_50) * 50 +
            inventory.count(ItemID.GUTHANS_HELM_75) * 75 +
            inventory.count(ItemID.GUTHANS_HELM_100) * 100 +
            inventory.count(ItemID.GUTHANS_HELM) * 100 +
            equipment.count(ItemID.GUTHANS_HELM_25) * 25 +
            equipment.count(ItemID.GUTHANS_HELM_50) * 50 +
            equipment.count(ItemID.GUTHANS_HELM_75) * 75 +
            equipment.count(ItemID.GUTHANS_HELM_100) * 100 +
            equipment.count(ItemID.GUTHANS_HELM) * 100,
            false
        );
    }

    private ChargesItem getGuthansPlatebody(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.GUTHANS_PLATEBODY,
            items.getItemComposition(ItemID.GUTHANS_PLATEBODY).getName(),
            items.getImage(ItemID.GUTHANS_PLATEBODY),
            inventory.count(ItemID.GUTHANS_PLATEBODY_25) * 25 +
            inventory.count(ItemID.GUTHANS_PLATEBODY_50) * 50 +
            inventory.count(ItemID.GUTHANS_PLATEBODY_75) * 75 +
            inventory.count(ItemID.GUTHANS_PLATEBODY_100) * 100 +
            inventory.count(ItemID.GUTHANS_PLATEBODY) * 100 +
            equipment.count(ItemID.GUTHANS_PLATEBODY_25) * 25 +
            equipment.count(ItemID.GUTHANS_PLATEBODY_50) * 50 +
            equipment.count(ItemID.GUTHANS_PLATEBODY_75) * 75 +
            equipment.count(ItemID.GUTHANS_PLATEBODY_100) * 100 +
            equipment.count(ItemID.GUTHANS_PLATEBODY) * 100,
            false
        );
    }

    private ChargesItem getGuthansChainskirt(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.GUTHANS_CHAINSKIRT,
            items.getItemComposition(ItemID.GUTHANS_CHAINSKIRT).getName(),
            items.getImage(ItemID.GUTHANS_CHAINSKIRT),
            inventory.count(ItemID.GUTHANS_CHAINSKIRT_25) * 25 +
            inventory.count(ItemID.GUTHANS_CHAINSKIRT_50) * 50 +
            inventory.count(ItemID.GUTHANS_CHAINSKIRT_75) * 75 +
            inventory.count(ItemID.GUTHANS_CHAINSKIRT_100) * 100 +
            inventory.count(ItemID.GUTHANS_CHAINSKIRT) * 100 +
            equipment.count(ItemID.GUTHANS_CHAINSKIRT_25) * 25 +
            equipment.count(ItemID.GUTHANS_CHAINSKIRT_50) * 50 +
            equipment.count(ItemID.GUTHANS_CHAINSKIRT_75) * 75 +
            equipment.count(ItemID.GUTHANS_CHAINSKIRT_100) * 100 +
            equipment.count(ItemID.GUTHANS_CHAINSKIRT) * 100,
            false
        );
    }

    private ChargesItem getGuthansWarspear(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.GUTHANS_WARSPEAR,
            items.getItemComposition(ItemID.GUTHANS_WARSPEAR).getName(),
            items.getImage(ItemID.GUTHANS_WARSPEAR),
            inventory.count(ItemID.GUTHANS_WARSPEAR_25) * 25 +
            inventory.count(ItemID.GUTHANS_WARSPEAR_50) * 50 +
            inventory.count(ItemID.GUTHANS_WARSPEAR_75) * 75 +
            inventory.count(ItemID.GUTHANS_WARSPEAR_100) * 100 +
            inventory.count(ItemID.GUTHANS_WARSPEAR) * 100 +
            equipment.count(ItemID.GUTHANS_WARSPEAR_25) * 25 +
            equipment.count(ItemID.GUTHANS_WARSPEAR_50) * 50 +
            equipment.count(ItemID.GUTHANS_WARSPEAR_75) * 75 +
            equipment.count(ItemID.GUTHANS_WARSPEAR_100) * 100 +
            equipment.count(ItemID.GUTHANS_WARSPEAR) * 100,
            false
        );
    }

    private ChargesItem getVeracsHelm(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.VERACS_HELM,
            items.getItemComposition(ItemID.VERACS_HELM).getName(),
            items.getImage(ItemID.VERACS_HELM),
            inventory.count(ItemID.VERACS_HELM_25) * 25 +
            inventory.count(ItemID.VERACS_HELM_50) * 50 +
            inventory.count(ItemID.VERACS_HELM_75) * 75 +
            inventory.count(ItemID.VERACS_HELM_100) * 100 +
            inventory.count(ItemID.VERACS_HELM) * 100 +
            equipment.count(ItemID.VERACS_HELM_25) * 25 +
            equipment.count(ItemID.VERACS_HELM_50) * 50 +
            equipment.count(ItemID.VERACS_HELM_75) * 75 +
            equipment.count(ItemID.VERACS_HELM_100) * 100 +
            equipment.count(ItemID.VERACS_HELM) * 100,
            false
        );
    }

    private ChargesItem getVeracsBrassard(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.VERACS_BRASSARD,
            items.getItemComposition(ItemID.VERACS_BRASSARD).getName(),
            items.getImage(ItemID.VERACS_BRASSARD),
            inventory.count(ItemID.VERACS_BRASSARD_25) * 25 +
            inventory.count(ItemID.VERACS_BRASSARD_50) * 50 +
            inventory.count(ItemID.VERACS_BRASSARD_75) * 75 +
            inventory.count(ItemID.VERACS_BRASSARD_100) * 100 +
            inventory.count(ItemID.VERACS_BRASSARD) * 100 +
            equipment.count(ItemID.VERACS_BRASSARD_25) * 25 +
            equipment.count(ItemID.VERACS_BRASSARD_50) * 50 +
            equipment.count(ItemID.VERACS_BRASSARD_75) * 75 +
            equipment.count(ItemID.VERACS_BRASSARD_100) * 100 +
            equipment.count(ItemID.VERACS_BRASSARD) * 100,
            false
        );
    }

    private ChargesItem getVeracsPlateskirt(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.VERACS_PLATESKIRT,
            items.getItemComposition(ItemID.VERACS_PLATESKIRT).getName(),
            items.getImage(ItemID.VERACS_PLATESKIRT),
            inventory.count(ItemID.VERACS_PLATESKIRT_25) * 25 +
            inventory.count(ItemID.VERACS_PLATESKIRT_50) * 50 +
            inventory.count(ItemID.VERACS_PLATESKIRT_75) * 75 +
            inventory.count(ItemID.VERACS_PLATESKIRT_100) * 100 +
            inventory.count(ItemID.VERACS_PLATESKIRT) * 100 +
            equipment.count(ItemID.VERACS_PLATESKIRT_25) * 25 +
            equipment.count(ItemID.VERACS_PLATESKIRT_50) * 50 +
            equipment.count(ItemID.VERACS_PLATESKIRT_75) * 75 +
            equipment.count(ItemID.VERACS_PLATESKIRT_100) * 100 +
            equipment.count(ItemID.VERACS_PLATESKIRT) * 100,
            false
        );
    }

    private ChargesItem getVeracsFlail(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.VERACS_FLAIL,
            items.getItemComposition(ItemID.VERACS_FLAIL).getName(),
            items.getImage(ItemID.VERACS_FLAIL),
            inventory.count(ItemID.VERACS_FLAIL_25) * 25 +
            inventory.count(ItemID.VERACS_FLAIL_50) * 50 +
            inventory.count(ItemID.VERACS_FLAIL_75) * 75 +
            inventory.count(ItemID.VERACS_FLAIL_100) * 100 +
            inventory.count(ItemID.VERACS_FLAIL) * 100 +
            equipment.count(ItemID.VERACS_FLAIL_25) * 25 +
            equipment.count(ItemID.VERACS_FLAIL_50) * 50 +
            equipment.count(ItemID.VERACS_FLAIL_75) * 75 +
            equipment.count(ItemID.VERACS_FLAIL_100) * 100 +
            equipment.count(ItemID.VERACS_FLAIL) * 100,
            false
        );
    }

    private ChargesItem getToragsHelm(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.TORAGS_HELM,
            items.getItemComposition(ItemID.TORAGS_HELM).getName(),
            items.getImage(ItemID.TORAGS_HELM),
            inventory.count(ItemID.TORAGS_HELM_25) * 25 +
            inventory.count(ItemID.TORAGS_HELM_50) * 50 +
            inventory.count(ItemID.TORAGS_HELM_75) * 75 +
            inventory.count(ItemID.TORAGS_HELM_100) * 100 +
            inventory.count(ItemID.TORAGS_HELM) * 100 +
            equipment.count(ItemID.TORAGS_HELM_25) * 25 +
            equipment.count(ItemID.TORAGS_HELM_50) * 50 +
            equipment.count(ItemID.TORAGS_HELM_75) * 75 +
            equipment.count(ItemID.TORAGS_HELM_100) * 100 +
            equipment.count(ItemID.TORAGS_HELM) * 100,
            false
        );
    }

    private ChargesItem getToragsPlatebody(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.TORAGS_PLATEBODY,
            items.getItemComposition(ItemID.TORAGS_PLATEBODY).getName(),
            items.getImage(ItemID.TORAGS_PLATEBODY),
            inventory.count(ItemID.TORAGS_PLATEBODY_25) * 25 +
            inventory.count(ItemID.TORAGS_PLATEBODY_50) * 50 +
            inventory.count(ItemID.TORAGS_PLATEBODY_75) * 75 +
            inventory.count(ItemID.TORAGS_PLATEBODY_100) * 100 +
            inventory.count(ItemID.TORAGS_PLATEBODY) * 100 +
            equipment.count(ItemID.TORAGS_PLATEBODY_25) * 25 +
            equipment.count(ItemID.TORAGS_PLATEBODY_50) * 50 +
            equipment.count(ItemID.TORAGS_PLATEBODY_75) * 75 +
            equipment.count(ItemID.TORAGS_PLATEBODY_100) * 100 +
            equipment.count(ItemID.TORAGS_PLATEBODY) * 100,
            false
        );
    }

    private ChargesItem getToragsPlatelegs(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.TORAGS_PLATELEGS,
            items.getItemComposition(ItemID.TORAGS_PLATELEGS).getName(),
            items.getImage(ItemID.TORAGS_PLATELEGS),
            inventory.count(ItemID.TORAGS_PLATELEGS_25) * 25 +
            inventory.count(ItemID.TORAGS_PLATELEGS_50) * 50 +
            inventory.count(ItemID.TORAGS_PLATELEGS_75) * 75 +
            inventory.count(ItemID.TORAGS_PLATELEGS_100) * 100 +
            inventory.count(ItemID.TORAGS_PLATELEGS) * 100 +
            equipment.count(ItemID.TORAGS_PLATELEGS_25) * 25 +
            equipment.count(ItemID.TORAGS_PLATELEGS_50) * 50 +
            equipment.count(ItemID.TORAGS_PLATELEGS_75) * 75 +
            equipment.count(ItemID.TORAGS_PLATELEGS_100) * 100 +
            equipment.count(ItemID.TORAGS_PLATELEGS) * 100,
            false
        );
    }

    private ChargesItem getToragsHammers(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.TORAGS_HAMMERS,
            items.getItemComposition(ItemID.TORAGS_HAMMERS).getName(),
            items.getImage(ItemID.TORAGS_HAMMERS),
            inventory.count(ItemID.TORAGS_HAMMERS_25) * 25 +
            inventory.count(ItemID.TORAGS_HAMMERS_50) * 50 +
            inventory.count(ItemID.TORAGS_HAMMERS_75) * 75 +
            inventory.count(ItemID.TORAGS_HAMMERS_100) * 100 +
            inventory.count(ItemID.TORAGS_HAMMERS) * 100 +
            equipment.count(ItemID.TORAGS_HAMMERS_25) * 25 +
            equipment.count(ItemID.TORAGS_HAMMERS_50) * 50 +
            equipment.count(ItemID.TORAGS_HAMMERS_75) * 75 +
            equipment.count(ItemID.TORAGS_HAMMERS_100) * 100 +
            equipment.count(ItemID.TORAGS_HAMMERS) * 100,
            false
        );
    }

    private ChargesItem getAhrimsHood(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.AHRIMS_HOOD,
            items.getItemComposition(ItemID.AHRIMS_HOOD).getName(),
            items.getImage(ItemID.AHRIMS_HOOD),
            inventory.count(ItemID.AHRIMS_HOOD_25) * 25 +
            inventory.count(ItemID.AHRIMS_HOOD_50) * 50 +
            inventory.count(ItemID.AHRIMS_HOOD_75) * 75 +
            inventory.count(ItemID.AHRIMS_HOOD_100) * 100 +
            inventory.count(ItemID.AHRIMS_HOOD) * 100 +
            equipment.count(ItemID.AHRIMS_HOOD_25) * 25 +
            equipment.count(ItemID.AHRIMS_HOOD_50) * 50 +
            equipment.count(ItemID.AHRIMS_HOOD_75) * 75 +
            equipment.count(ItemID.AHRIMS_HOOD_100) * 100 +
            equipment.count(ItemID.AHRIMS_HOOD) * 100,
            false
        );
    }

    private ChargesItem getAhrimsRobetop(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.AHRIMS_ROBETOP,
            items.getItemComposition(ItemID.AHRIMS_ROBETOP).getName(),
            items.getImage(ItemID.AHRIMS_ROBETOP),
            inventory.count(ItemID.AHRIMS_ROBETOP_25) * 25 +
            inventory.count(ItemID.AHRIMS_ROBETOP_50) * 50 +
            inventory.count(ItemID.AHRIMS_ROBETOP_75) * 75 +
            inventory.count(ItemID.AHRIMS_ROBETOP_100) * 100 +
            inventory.count(ItemID.AHRIMS_ROBETOP) * 100 +
            equipment.count(ItemID.AHRIMS_ROBETOP_25) * 25 +
            equipment.count(ItemID.AHRIMS_ROBETOP_50) * 50 +
            equipment.count(ItemID.AHRIMS_ROBETOP_75) * 75 +
            equipment.count(ItemID.AHRIMS_ROBETOP_100) * 100 +
            equipment.count(ItemID.AHRIMS_ROBETOP) * 100,
            false
        );
    }

    private ChargesItem getAhrimsRobeskirt(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.AHRIMS_ROBESKIRT,
            items.getItemComposition(ItemID.AHRIMS_ROBESKIRT).getName(),
            items.getImage(ItemID.AHRIMS_ROBESKIRT),
            inventory.count(ItemID.AHRIMS_ROBESKIRT_25) * 25 +
            inventory.count(ItemID.AHRIMS_ROBESKIRT_50) * 50 +
            inventory.count(ItemID.AHRIMS_ROBESKIRT_75) * 75 +
            inventory.count(ItemID.AHRIMS_ROBESKIRT_100) * 100 +
            inventory.count(ItemID.AHRIMS_ROBESKIRT) * 100 +
            equipment.count(ItemID.AHRIMS_ROBESKIRT_25) * 25 +
            equipment.count(ItemID.AHRIMS_ROBESKIRT_50) * 50 +
            equipment.count(ItemID.AHRIMS_ROBESKIRT_75) * 75 +
            equipment.count(ItemID.AHRIMS_ROBESKIRT_100) * 100 +
            equipment.count(ItemID.AHRIMS_ROBESKIRT) * 100,
            false
        );
    }

    private ChargesItem getAhrimsStaff(final ItemContainer inventory, final ItemContainer equipment) {
        return new ChargesItem(
            ItemID.AHRIMS_STAFF,
            items.getItemComposition(ItemID.AHRIMS_STAFF).getName(),
            items.getImage(ItemID.AHRIMS_STAFF),
            inventory.count(ItemID.AHRIMS_STAFF_25) * 25 +
            inventory.count(ItemID.AHRIMS_STAFF_50) * 50 +
            inventory.count(ItemID.AHRIMS_STAFF_75) * 75 +
            inventory.count(ItemID.AHRIMS_STAFF_100) * 100 +
            inventory.count(ItemID.AHRIMS_STAFF) * 100 +
            equipment.count(ItemID.AHRIMS_STAFF_25) * 25 +
            equipment.count(ItemID.AHRIMS_STAFF_50) * 50 +
            equipment.count(ItemID.AHRIMS_STAFF_75) * 75 +
            equipment.count(ItemID.AHRIMS_STAFF_100) * 100 +
            equipment.count(ItemID.AHRIMS_STAFF) * 100,
            false
        );
    }
}
