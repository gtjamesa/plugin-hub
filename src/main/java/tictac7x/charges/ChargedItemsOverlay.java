package tictac7x.charges;

import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;
import tictac7x.charges.triggers.TriggerItem;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChargedItemsOverlay extends WidgetItemOverlay {
    private final ItemManager items;
    private final ChargesImprovedConfig config;
    private final ChargedItemInfoBox[] infoboxes_charged_items;
    private final Pattern charges_in_name = Pattern.compile(".*?\\(?(?<charges>\\d+)\\)?");

    public ChargedItemsOverlay(final ItemManager items, final ChargesImprovedConfig config, final ChargedItemInfoBox[] infoboxes_charged_items) {
        this.items = items;
        this.config = config;
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
                // Charges from infobox.
                String charges = ChargesImprovedPlugin.getChargesMinified(infobox.getCharges());
                graphics.setFont(FontManager.getRunescapeSmallFont());

                // Charges from name (override the infobox).
                final String name = items.getItemComposition(item_id).getName();
                final Matcher matcher = charges_in_name.matcher(name);
                if (matcher.find()) {
                    charges = ChargesImprovedPlugin.getChargesMinified(Integer.parseInt(matcher.group("charges")));
                }

                final Rectangle bounds = item_widget.getCanvasBounds();
                final TextComponent charges_component = new TextComponent();

                charges_component.setPosition(new Point(bounds.x, (int) bounds.getMaxY()));
                charges_component.setColor(charges.equals("?") ? config.getColorUnknown() : charges.equals("0") ? config.getColorEmpty() : config.getColorDefault());
                charges_component.setText(charges);
                charges_component.render(graphics);

                return;
            }
        }
    }
}
