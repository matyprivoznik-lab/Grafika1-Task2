package view;

import graphics.Raster;
import graphics.RasterImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Canvas extends JPanel {

    private final RasterImage raster;

    private static final int BACKGROUND_COLOR = new Color(0x2F2F2F).getRGB();

    public Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new RasterImage(width, height);
        clear();
    }

    public void clear() {
        raster.clear(BACKGROUND_COLOR);
    }

    public Raster getRaster() {
        return raster;
    }

    public int getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        raster.present(graphics);
    }

}
