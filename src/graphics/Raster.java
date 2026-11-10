package graphics;

import java.awt.*;

public interface Raster {

    int getWidth();
    int getHeight();
    Integer getPixel(int x, int y);

    void clear(Integer value);
    void present(Graphics graphics);
    void setPixel(int x, int y, Integer value);

}
