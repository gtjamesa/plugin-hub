package tictac7x.charges;

import java.awt.*;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;
import tictac7x.charges.triggers.TriggerItem;

public class ChargedItemsOverlay extends WidgetItemOverlay {
    private final ChargedItemInfoBox[] infoboxes_charged_items;

    public ChargedItemsOverlay(final ChargedItemInfoBox[] infoboxes_charged_items) {
        this.infoboxes_charged_items = infoboxes_charged_items;
        showOnInventory();
        showOnEquipment();
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
                graphics.setFont(FontManager.getRunescapeSmallFont());

                final Rectangle bounds = item_widget.getCanvasBounds();
                final TextComponent charges = new TextComponent();

                charges.setPosition(new Point(bounds.x, bounds.y + 10));
                charges.setColor(Color.white);
                charges.setText(String.valueOf(infobox.charges));
                charges.render(graphics);

                return;
            }
        }
    }
}
