package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class RasterImage implements Raster {

    private final BufferedImage image;

    public RasterImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void clear(Integer value) {
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(value));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.dispose();
    }

    public void present(Graphics device) {
        device.drawImage(image, 0, 0, null);
    }

    @Override
    public Integer getPixel(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return image.getRGB(x, y);
    }

    @Override
    public void setPixel(int x, int y, Integer value) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            image.setRGB(x, y, value);
        }
    }

}
