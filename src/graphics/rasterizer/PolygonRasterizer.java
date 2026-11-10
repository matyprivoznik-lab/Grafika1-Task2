package graphics.rasterizer;

import model.Line;
import model.Polygon;

public interface PolygonRasterizer {

    void rasterize(Polygon polygon, Line previewLine);

}
