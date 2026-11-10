package model;

public final class Line {

    private final int color;
    private final Point point1;
    private final Point point2;

    public Line(Point point1, Point point2, int color) {
        this.color = color;
        this.point1 = point1;
        this.point2 = point2;
    }

    public int getColor() {
        return color;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

}
