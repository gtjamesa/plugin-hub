package tictac7x.charges.item.overlays;

import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import tictac7x.charges.ChargesImprovedConfig;
import tictac7x.charges.ChargesImprovedPlugin;
import tictac7x.charges.item.ChargedItemBase;
import tictac7x.charges.store.Charges;

import java.awt.Color;

public class ChargedItemInfobox extends InfoBox {
    private final ChargedItemBase chargedItem;
    private final ItemManager items;
    private final InfoBoxManager infoboxes;
    private final ChargesImprovedConfig config;

    private int itemId;
    private String tooltip = "";

    public ChargedItemInfobox(
        final ChargedItemBase chargedItem,
        final ItemManager items,
        final InfoBoxManager infoboxes,
        final ClientThread clientThread,
        final ChargesImprovedConfig config,
        final ChargesImprovedPlugin plugin
    ) {
        super(items.getImage(chargedItem.itemId), plugin);
        this.chargedItem = chargedItem;
        this.items = items;
        this.infoboxes = infoboxes;
        this.config = config;
        this.itemId = chargedItem.itemId;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + chargedItem.itemId;
    }

    @Override
    public String getText() {
        return chargedItem.getCharges();
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }

    @Override
    public Color getTextColor() {
        return chargedItem.getTextColor();
    }

    @Override
    public boolean render() {
        updateInfobox();

        return (
            config.showInfoboxes() &&
            !config.getHiddenInfoboxes().contains(chargedItem.itemKey) &&
            !chargedItem.getCharges().equals("∞") &&
            (chargedItem.inInventory() || chargedItem.isEquipped())
        );
    }

    private void updateInfobox() {
        if (itemId != chargedItem.itemId) {
            // Update infobox id to keep track of correct item.
            itemId = chargedItem.itemId;

            // Update infobox image.
            setImage(items.getImage(itemId));
            infoboxes.updateInfoBoxImage(this);
        }

        // Update tooltip.
        tooltip =
            chargedItem.getItemName() +
            (chargedItem.needsToBeEquipped() && !chargedItem.isEquipped() ? " (needs to be equipped)" : "");
    }
}


