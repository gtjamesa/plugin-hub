package tictac7x.charges;

import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxOverlay;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ChargesInfobox extends InfoBox {
    private static final int EQUIPMENT_RING = 12;

    private final Client client;
    private final InfoBoxManager infoboxes;
    private final ItemManager items;
    private final ItemSlot slot;

    @Nullable
    private Integer item_id = null;

    @Nullable
    private BufferedImage item_image = null;

    public ChargesInfobox(final Client client, final InfoBoxManager infoboxes, final ItemManager items, final ItemSlot slot, final ChargesPlugin plugin) {
        super(items.getImage(ItemID.RUNE_2H_SWORD), plugin);
        this.client = client;
        this.infoboxes = infoboxes;
        this.items = items;
        this.slot = slot;
    }

    @Override
    public boolean render() {
        return item_id != null && item_id != -1;
    }

    @Override
    public BufferedImage getImage() {
        return item_image;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + slot;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public Color getTextColor() {
        return null;
    }

    public void onItemContainerChanged(final ItemContainerChanged event) {
        @Nullable
        final ItemContainer item_container = event.getItemContainer();

        @Nullable
        Item item = null;

        if (item_container == null) return;

        switch (slot) {
            case EQUIPMENT_RING:
                if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
                    item = item_container.getItem(EQUIPMENT_RING);
                } else {
                    return;
                }
                break;
        }

        if (item != null) {
            if (item_id == null || item.getId() != item_id) {
                item_id = item.getId();
                item_image = items.getImage(item_id);
            }
        } else {
            item_id = null;
            item_image = null;
        }

        infoboxes.updateInfoBoxImage(this);
    }
}
