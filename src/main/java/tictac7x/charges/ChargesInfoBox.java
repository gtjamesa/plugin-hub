package tictac7x.charges;

import net.runelite.client.plugins.Plugin;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChargesInfoBox extends net.runelite.client.ui.overlay.infobox.InfoBox {
    private final String id;
    private String text;
    private Color color;
    private boolean render;

    public ChargesInfoBox(final String id, final Plugin plugin) {
        super(null, plugin);
        this.id = id;
    }

    @Override
    public String getName() {
        return super.getName() + "_" + id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getTooltip() {
        return super.getTooltip() + "_" + id;
    }

    @Override
    public Color getTextColor() {
        return color;
    }

    @Override
    public boolean render() {
        return render;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public void setRender(final boolean render) {
        this.render = render;
    }
}


