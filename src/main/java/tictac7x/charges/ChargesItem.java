package tictac7x.charges;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;

class ChargesItem {
    public final int id;
    @Nonnull public final BufferedImage image;
    @Nonnull public final String tooltip;
    @Nullable public final Color color;
    @Nonnull public final String text;

    public ChargesItem(final int id, @Nonnull final String tooltip, @Nonnull final BufferedImage image, final int count, final boolean percentage) {
        this.id = id;
        this.image = image;
        this.tooltip = tooltip.replaceAll("\\s+\\(?[0-9]*\\)?$", "");
        this.color = count == 0 ? Color.red : null;
        this.text = count + (percentage ? "%" : "");
    }
}
