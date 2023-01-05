package tictac7x.charges;

import java.awt.*;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

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
            boolean matches = false;
            for (final int item_id_to_render : infobox.item_ids_to_render) {
                if (item_id_to_render == item_id) {
                    matches = true;
                    break;
                }
            }

            if (matches) {
                graphics.setFont(FontManager.getRunescapeSmallFont());

                final Rectangle bounds = item_widget.getCanvasBounds();
                final TextComponent charges = new TextComponent();

                charges.setPosition(new Point(bounds.x, bounds.y + 10));
                charges.setColor(Color.white);
                charges.setText(infobox.getText());
                charges.render(graphics);

                return;
            }
        }
    }
}
