package graphics.rasterizer;

import graphics.Raster;
import model.Line;

public class TrivialLineRasterizer implements LineRasterizer {

    private final Raster raster;

    public TrivialLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(
                line.getPoint1().getX(),
                line.getPoint1().getY(),
                line.getPoint2().getX(),
                line.getPoint2().getY(),
                line.getColor()
        );
    }

    private void rasterize(int x1, int y1, int x2, int y2, int color) {
        // TODO: Dokončit implementaci

        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

        // TODO: X2 = X1- vertikální čára

        // TODO: X2 < X1 je nunté prohodit X2 a X1

        // TODO: Pokud je (y2 - y1) > (x2- x1) - jdeme po y
        for (int x = x1; x <= x2; x++) {
            float y = k * x + q;
            raster.setPixel(x, Math.round(y), color);
        }

    }

}
