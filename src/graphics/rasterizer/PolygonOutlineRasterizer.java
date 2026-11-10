package graphics.rasterizer;

import model.Line;
import model.Polygon;

import java.util.ArrayList;
import java.util.List;

public final class PolygonOutlineRasterizer implements PolygonRasterizer {

    private final LineRasterizer lineRasterizer;

    public PolygonOutlineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    @Override
    public void rasterize(Polygon polygon, Line previewLine) {
        List<Line> lines = new ArrayList<>(polygon.getLines());
        if (previewLine != null) {
            lines.add(previewLine);
        }

        rasterizeLines(lines);
        if (lines.size() > 1) {
            Line last = lines.getLast();
            Line first = lines.getFirst();
            lineRasterizer.rasterize(new Line(last.getPoint2(), first.getPoint1(), last.getColor()));
        }
    }

    private void rasterizeLines(List<Line> lines) {
        for (Line line : lines) {
            lineRasterizer.rasterize(line);
        }
    }

}
