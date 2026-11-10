package graphics.filler;

import graphics.Raster;
import model.Line;
import model.Point;
import model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ScanlineFiller implements PolygonFiller {

    private final Raster raster;

    public ScanlineFiller(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void fill(Polygon polygon, int fillColor) {
        List<Line> edges = new ArrayList<Line>(polygon.getLines());
        if (edges.size() < 2) {
            return;
        }

        // a) Uzavření polygonu spojením posledního a prvního bodu
        Line last = edges.getLast();
        Line first = edges.getFirst();
        edges.add(new Line(last.getPoint2(), first.getPoint1(), last.getColor()));

        // b) Zajištění správné orientace hran (A -> B) a určení rozsahu
        int maxY = 0;
        int minY = raster.getHeight();
        for (int i = 0; i < edges.size(); i++) {
            Line edge = edges.get(i);
            Point p1 = edge.getPoint1();
            Point p2 = edge.getPoint2();
            if (p1.getY() > p2.getY()) {
                Point temp = p1;
                p1 = p2;
                p2 = temp;
                edges.set(i, new Line(p1, p2, edge.getColor()));
            }
            minY = Math.min(minY, p1.getY());
            maxY = Math.max(maxY, p2.getY());
        }

        // c) Výpočet průsečíků hran se středem každého řádku
        List<Double> intersections = new ArrayList<>();
        for(int y = Math.max(0, minY); y < Math.min(raster.getHeight(), maxY); y++){
            intersections.clear();
            double scanY = y + 0.5;
            // Výpočet průsečíků
            for (Line edge : edges) {
                Point p1 = edge.getPoint1();
                Point p2 = edge.getPoint2();
                // Odstranění rovných čar + spodního bodu
                if (y < p1.getY() || y >= p2.getY()) {
                    continue;
                }
                // Dopočítáme jednotlivé souřadnice (průsečíky) x
                double x = p1.getX() + (scanY - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
                intersections.add(x);
            }

            // Seřazení průsečíků a vyplnění pixelů mezi nimi
            intersections.sort(null);
            // Projdeme jednotlivé průsečíky (vždy po dvouch)
            for (int i = 0; i + 1 < intersections.size(); i = i + 2) {
                // Dopočítáme (zaokrouhlíme) x souřadnici odkud máme jít
                int from = Math.max(0, (int) Math.ceil(intersections.get(i) - 0.5));
                // Dopočítáme (zaokrouhlíme) x souřadnici kam máme jít
                int to = Math.min(raster.getWidth(), (int) Math.ceil(intersections.get(i + 1) - 0.5));
                // Projdeme od from do to a obarvíme pixely
                for (int x = from; x < to; x++) {
                    raster.setPixel(x, y, fillColor);
                }
            }
        }

    }

}
