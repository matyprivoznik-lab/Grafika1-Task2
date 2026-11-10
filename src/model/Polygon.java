package model;

import java.util.ArrayList;
import java.util.List;

public final class Polygon {

    private final List<Line> lines;

    public Polygon() {
        lines = new ArrayList<>();
    }

    public void clear() {
        lines.clear();
    }

    public Point getLastPoint() {
        return lines.isEmpty() ? null : lines.getLast().getPoint2();
    }

    public List<Line> getLines() {
        return List.copyOf(lines);
    }

    public void addLine(Line line) {
        lines.add(line);
    }

}
