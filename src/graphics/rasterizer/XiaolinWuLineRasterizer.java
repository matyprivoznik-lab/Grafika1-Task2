package graphics.rasterizer;

import graphics.Raster;
import model.Line;

public final class XiaolinWuLineRasterizer implements LineRasterizer {

    private final Raster raster;

    public XiaolinWuLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        double x0 = line.getPoint1().getX(), y0 = line.getPoint1().getY();
        double x1 = line.getPoint2().getX(), y1 = line.getPoint2().getY();

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        double a0 = steep ? y0 : x0, b0 = steep ? x0 : y0;
        double a1 = steep ? y1 : x1, b1 = steep ? x1 : y1;
        if (a0 > a1) {
            double t = a0; a0 = a1; a1 = t;
            t = b0; b0 = b1; b1 = t;
        }

        double gradient = a1 == a0 ? 0 : (b1 - b0) / (a1 - a0);
        for (int a = (int) Math.round(a0); a <= Math.round(a1); a++) {
            double b = b0 + gradient * (a - a0);
            int bi = (int) Math.floor(b);
            double f = b - bi;
            plot(steep, a, bi, 1 - f, line.getColor());
            plot(steep, a, bi + 1, f, line.getColor());
        }
    }

    private void plot(boolean steep, int x, int y, double coverage, int color) {
        int px = steep ? y : x;
        int py = steep ? x : y;
        if (coverage <= 0 || px < 0 || px >= raster.getWidth() || py < 0 || py >= raster.getHeight()) {
            return;
        }

        int bg = raster.getPixel(px, py);
        double alpha = coverage * (color >>> 24) / 255.0;
        raster.setPixel(
            px, py,
            0xff000000
                | blend(color >> 16, bg >> 16, alpha) << 16
                | blend(color >> 8, bg >> 8, alpha) << 8
                | blend(color, bg, alpha)
        );
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    private int blend(int fg, int bg, double alpha) {
        return (int) Math.round((fg & 0xff) * alpha + (bg & 0xff) * (1 - alpha));
    }

}
