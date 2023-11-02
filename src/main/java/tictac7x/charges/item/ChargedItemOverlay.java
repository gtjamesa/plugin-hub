package tictac7x.charges.item;

import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesImprovedPlugin;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.item.triggers.TriggerItem;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class ChargedItemOverlay extends WidgetItemOverlay {
    private final Client client;
    private final TooltipManager tooltipManager;
    private final ItemManager itemManager;
    private final ChargesImprovedConfig config;
    private final ChargedItem[] charged_items;

    public ChargedItemOverlay(
        final Client client,
        final TooltipManager tooltipManager,
        final ItemManager itemManager,
        final ChargesImprovedConfig config,
        final ChargedItem[] charged_items
    ) {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.itemManager = itemManager;
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
    public void renderItemOverlay(final Graphics2D graphics, final int itemId, final WidgetItem widgetItem) {
        if (!config.showItemOverlays()) return;

        for (final ChargedItem charged_item : charged_items) {
            if (
                config.getHiddenItemOverlays().contains(charged_item.infobox_id) ||
                charged_item.getCharges() == Charges.UNLIMITED ||
                !config.showBankOverlays() && isBankWidget(widgetItem)
            ) continue;

            TriggerItem trigger_item_to_use = null;
            for (final TriggerItem trigger_item : charged_item.triggersItems) {
                if (trigger_item.item_id == itemId && !trigger_item.hide_overlay) {
                    trigger_item_to_use = trigger_item;
                    break;
                }
            }
            if (trigger_item_to_use == null || trigger_item_to_use.fixed_charges != null && trigger_item_to_use.fixed_charges == Charges.UNLIMITED) continue;

            // Charges from infobox.
            String charges = ChargesImprovedPlugin.getChargesMinified(charged_item.getCharges());

            graphics.setFont(FontManager.getRunescapeSmallFont());

            // Charges from name (override the infobox).
            if (trigger_item_to_use.fixed_charges != null) {
                charges = ChargesImprovedPlugin.getChargesMinified(trigger_item_to_use.fixed_charges);
            }

            final Rectangle bounds = widgetItem.getCanvasBounds();
            final TextComponent charges_component = new TextComponent();

            charges_component.setPosition(new Point(bounds.x, (int) bounds.getMaxY()));
            charges_component.setText(charges);

            // Set color.
            charges_component.setColor(charged_item.getTextColor());
            if (isBankWidget(widgetItem) && charged_item.getCharges() != Charges.UNKNOWN) {
                charges_component.setColor(config.getColorDefault());
            }

            charges_component.render(graphics);

            // Charged item with storage
            renderTooltip(charged_item, widgetItem);

            return;
        }
    }

    private void renderTooltip(final ChargedItem chargedItem, final WidgetItem widgetItem) {
        // Config, not storage item, empty storage checks.
        if (
            !config.showStorageTooltips() ||
            !(chargedItem instanceof ChargedItemWithStorage) ||
            chargedItem.getCharges() == 0
        ) return;

        // Mouse position check.
        final net.runelite.api.Point mousePosition = client.getMouseCanvasPosition();
        if (!widgetItem.getCanvasBounds().contains(mousePosition.getX(), mousePosition.getY())) return;

        String tooltip = "";
        for (final StorageItem storageItem : ((ChargedItemWithStorage) chargedItem).getStorage()) {
            if (storageItem.getQuantity() > 0) {
                tooltip += itemManager.getItemComposition(storageItem.itemId).getName() + ": ";
                tooltip += ColorUtil.wrapWithColorTag(String.valueOf(storageItem.getQuantity()), JagexColors.MENU_TARGET) + "</br>";
            }
        }
        tooltip = tooltip.replaceAll("</br>$", "");

        if (!tooltip.isEmpty()) {
            tooltipManager.addFront(new Tooltip(tooltip));
        }
    }
}
