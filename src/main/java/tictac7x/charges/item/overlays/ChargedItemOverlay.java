package tictac7x.charges.item.overlays;

import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
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
import java.util.Optional;

public class ChargedItemOverlay extends WidgetItemOverlay {
    private final Client client;
    private final TooltipManager tooltipManager;
    private final ItemManager itemManager;
    private final ConfigManager configManager;
    private final ChargesImprovedConfig config;
    private final ChargedItemBase[] chargedItems;

    public ChargedItemOverlay(
        final Client client,
        final TooltipManager tooltipManager,
        final ItemManager itemManager,
        final ConfigManager configManager,
        final ChargesImprovedConfig config,
        final ChargedItemBase[] chargedItems
    ) {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.itemManager = itemManager;
        this.configManager = configManager;
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
        Optional<ChargedItemBase> chargedItem = Optional.empty();
        Optional<TriggerItem> triggerItem = Optional.empty();

        // Find correct charged item.
        chargedItemFinder: for (final ChargedItemBase chargedItemBase : chargedItems) {
            for (final TriggerItem chargedItemTriggerItem : chargedItemBase.items) {
                if (chargedItemTriggerItem.itemId == itemId) {
                    chargedItem = Optional.of(chargedItemBase);
                    triggerItem = Optional.of(chargedItemTriggerItem);
                    break chargedItemFinder;
                }
            }
        }

        // Invalid item.
        if (!chargedItem.isPresent() || !triggerItem.isPresent()) return;

        if (
            // Item overlay disabled.
            !isChargedItemOverlayEnabled(chargedItem.get()) ||

            // Infinity charges hidden.
            !config.showUnlimited() && chargedItem.get().getCharges().equals("∞") ||
            !config.showUnlimited() && triggerItem.get().fixedCharges.isPresent() && triggerItem.get().fixedCharges.get().equals(Charges.UNLIMITED) ||

            // Hide overlays in bank.
            !config.showBankOverlays() && isBankWidget(widgetItem)
        ) return;

        // Get default charges from charged item.
        String charges = chargedItem.get().getCharges();
        Color color = chargedItem.get().getTextColor();

        // Override charges and color for fixed items.
        for (final TriggerItem item : chargedItem.get().items) {
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
        if (isBankWidget(widgetItem) && !chargedItem.get().getCharges().equals("?")) {
            charges_component.setColor(config.getColorDefault());
        }

        charges_component.render(graphics);

        // Charged item with storage
        renderTooltip(chargedItem.get(), widgetItem);
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

    private boolean isChargedItemOverlayEnabled(final ChargedItemBase chargedItem) {
        final Optional<String> visible = Optional.ofNullable(configManager.getConfiguration(ChargesImprovedConfig.group, chargedItem.configKey + "_overlay"));

        if (visible.isPresent() && visible.get().equals("false")) {
            return false;
        }

        return true;
    }
}
