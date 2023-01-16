package tictac7x.charges;

import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;
import tictac7x.charges.triggers.TriggerItem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class ChargedItemsOverlay extends WidgetItemOverlay {
    private final ChargedItemInfoBox[] infoboxes_charged_items;

    public ChargedItemsOverlay(final ChargedItemInfoBox[] infoboxes_charged_items) {
        this.infoboxes_charged_items = infoboxes_charged_items;
        showOnInventory();
        showOnEquipment();
        showOnBank();
    }

    @Override
    public void renderItemOverlay(final Graphics2D graphics, final int item_id, final WidgetItem item_widget) {
        for (final ChargedItemInfoBox infobox : infoboxes_charged_items) {
            if (infobox.triggers_items == null) continue;
            boolean matches = false;

            for (final TriggerItem trigger_item : infobox.triggers_items) {
                if (trigger_item.item_id == item_id) {
                    matches = true;
                    break;
                }
            }

            if (matches) {
                final String charges = getCharges(infobox);
                graphics.setFont(FontManager.getRunescapeSmallFont());

                final Rectangle bounds = item_widget.getCanvasBounds();
                final TextComponent charges_component = new TextComponent();

                charges_component.setPosition(new Point(bounds.x, (int) bounds.getMaxY()));
                charges_component.setColor(charges.equals("?") ? Color.orange : charges.equals("0") ? Color.red : Color.white);
                charges_component.setText(charges);
                charges_component.render(graphics);

                return;
            }
        }
    }

    private String getCharges(final ChargedItemInfoBox infobox) {
        return infobox.getCharges() == -1 ? "?" : String.valueOf(infobox.getCharges());
    }
}
