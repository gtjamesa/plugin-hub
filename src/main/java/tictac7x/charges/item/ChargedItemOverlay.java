package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesImprovedPlugin;
import tictac7x.charges.store.Charges;
import tictac7x.charges.item.triggers.TriggerItem;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class ChargedItemOverlay extends WidgetItemOverlay {
    private final Client client;
    private final ChargesImprovedConfig config;
    private final ChargedItem[] charged_items;

    public ChargedItemOverlay(final Client client, final ChargesImprovedConfig config, final ChargedItem[] charged_items) {
        this.client = client;
        this.config = config;
        this.charged_items = charged_items;
        showOnInventory();
        showOnEquipment();
        showOnInterfaces(84);
        showOnBank();
    }

    private boolean isBankWidget(final WidgetItem item_widget) {
        return
            item_widget.getWidget().getParentId() == 786442 ||
            item_widget.getWidget().getParentId() == 786443 ||
            item_widget.getWidget().getParentId() == 786444 ||
            item_widget.getWidget().getParentId() == 786445
        ;
    }


    @Override
    public void renderItemOverlay(final Graphics2D graphics, final int item_id, final WidgetItem item_widget) {
        if (!config.showItemOverlays()) return;

        for (final ChargedItem charged_item : charged_items) {
            if (
                config.getHiddenItemOverlays().contains(charged_item.infobox_id) ||
                charged_item.getCharges() == Charges.UNLIMITED ||
                !config.showBankOverlays() && isBankWidget(item_widget)
            ) continue;

            TriggerItem trigger_item_to_use = null;
            for (final TriggerItem trigger_item : charged_item.triggersItems) {
                if (trigger_item.item_id == item_id && !trigger_item.hide_overlay) {
                    trigger_item_to_use = trigger_item;
                    break;
                }
            }
            if (trigger_item_to_use == null) continue;

            // Charges from infobox.
            String charges = ChargesImprovedPlugin.getChargesMinified(charged_item.getCharges());

            graphics.setFont(FontManager.getRunescapeSmallFont());

            // Charges from name (override the infobox).
            if (trigger_item_to_use.fixed_charges != null) {
                charges = ChargesImprovedPlugin.getChargesMinified(trigger_item_to_use.fixed_charges);
            }

            final Rectangle bounds = item_widget.getCanvasBounds();
            final TextComponent charges_component = new TextComponent();

            charges_component.setPosition(new Point(bounds.x, (int) bounds.getMaxY()));
            charges_component.setText(charges);

            if (charges.equals("?")) {
                charges_component.setColor(config.getColorUnknown());
            } else if (
                !isBankWidget(item_widget) && (charged_item.needsToBeEquipped() && !charged_item.inEquipment()) ||
                charges.equals("0") && !charged_item.zero_charges_is_positive ||
                charged_item.negative_full_charges != null && charged_item.getCharges() == charged_item.negative_full_charges ||
                charged_item.isDeactivated()
            ) {
                charges_component.setColor(config.getColorEmpty());
            } else {
                charges_component.setColor(config.getColorDefault());
            }

            charges_component.render(graphics);
            return;
        }
    }
}
