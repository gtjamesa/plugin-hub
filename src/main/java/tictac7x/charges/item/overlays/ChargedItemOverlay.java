package tictac7x.charges.item.overlays;

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
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.item.ChargedItemWithStorage;
import tictac7x.charges.item.storage.StorageItem;
import tictac7x.charges.store.Charges;
import tictac7x.charges.item.triggers.TriggerItem;

import java.awt.*;

public class ChargedItemOverlay extends WidgetItemOverlay {
    private final Client client;
    private final TooltipManager tooltipManager;
    private final ItemManager itemManager;
    private final ChargesImprovedConfig config;
    private final ChargedItemBase[] chargedItems;

    public ChargedItemOverlay(
        final Client client,
        final TooltipManager tooltipManager,
        final ItemManager itemManager,
        final ChargesImprovedConfig config,
        final ChargedItemBase[] chargedItems
    ) {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.itemManager = itemManager;
        this.config = config;
        this.chargedItems = chargedItems;
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

        for (final ChargedItemBase chargedItem : chargedItems) {
            boolean validItem = false;
            for (final TriggerItem triggerItem : chargedItem.items) {
                if (triggerItem.itemId == itemId) {
                    validItem = true;
                    break;
                }
            }
            if (!validItem) continue;

            if (
                // Item overlay hidden.
                config.getHiddenItemOverlays().contains(chargedItem.itemKey) ||

                // Infinity charges hidden.
                !config.showUnlimited() && chargedItem.getCharges().equals("∞") ||

                // Hide overlays in bank.
                !config.showBankOverlays() && isBankWidget(widgetItem)
            ) continue;

            // Get default charges from charged item.
            String charges = chargedItem.getCharges();
            Color color = chargedItem.getTextColor();

            // Override charges and color for fixed items.
            for (final TriggerItem item : chargedItem.items) {
                if (item.itemId == itemId && item.fixedCharges.isPresent()) {
                    charges = item.fixedCharges.get() == Charges.UNLIMITED
                        ? "∞"
                        : String.valueOf(item.fixedCharges.get());
                    color = item.fixedCharges.get() == 0 ? config.getColorEmpty() : config.getColorDefault();
                }
            }

            graphics.setFont(FontManager.getRunescapeSmallFont());

            final Rectangle bounds = widgetItem.getCanvasBounds();
            final TextComponent charges_component = new TextComponent();

            charges_component.setPosition(new Point(bounds.x, (int) bounds.getMaxY()));
            charges_component.setText(charges);

            // Set color.
            charges_component.setColor(color);

            // Override for bank items.
            if (isBankWidget(widgetItem) && !chargedItem.getCharges().equals("?")) {
                charges_component.setColor(config.getColorDefault());
            }

            charges_component.render(graphics);

            // Charged item with storage
            renderTooltip(chargedItem, widgetItem);

            return;
        }
    }

    private void renderTooltip(final ChargedItemBase chargedItem, final WidgetItem widgetItem) {
        // Config, not storage item, empty storage checks.
        if (
            !config.showStorageTooltips() ||
            !(chargedItem instanceof ChargedItemWithStorage) ||
            chargedItem.getCharges().equals("0")
        ) return;

        // Mouse position check.
        final net.runelite.api.Point mousePosition = client.getMouseCanvasPosition();
        if (!widgetItem.getCanvasBounds().contains(mousePosition.getX(), mousePosition.getY())) return;

        String tooltip = "";
        for (final StorageItem storageItem : ((ChargedItemWithStorage) chargedItem).getStorage()) {
            if (storageItem.getQuantity() > 0) {
                tooltip += (storageItem.displayName.isPresent() ? storageItem.displayName.get() : itemManager.getItemComposition(storageItem.itemId).getName()) + ": ";
                tooltip += ColorUtil.wrapWithColorTag(String.valueOf(storageItem.getQuantity()), JagexColors.MENU_TARGET) + "</br>";
            }
        }
        tooltip = tooltip.replaceAll("</br>$", "");

        if (!tooltip.isEmpty()) {
            tooltipManager.addFront(new Tooltip(tooltip));
        }
    }
}
