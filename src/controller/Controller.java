package controller;

import graphics.filler.RecursiveSeedFiller;
import graphics.filler.ScanlineFiller;
import graphics.filler.SeedFiller;
import graphics.rasterizer.LineRasterizer;
import graphics.rasterizer.PolygonOutlineRasterizer;
import graphics.rasterizer.PolygonRasterizer;
import graphics.rasterizer.XiaolinWuLineRasterizer;
import model.Line;
import model.Point;
import model.Polygon;
import view.Canvas;
import view.gui.Toolbar;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;

import static java.awt.event.KeyEvent.VK_C;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static javax.swing.KeyStroke.getKeyStroke;

public class Controller {

    private final Canvas canvas;
    private final Toolbar toolbar;


    private Line previewLine;
    private Line previewLineClip;
    private final Polygon polygon;
    private final Polygon clipPolygon;
    private final PolygonRasterizer polygonRasterizer;
    private final LineRasterizer lineRasterizer;
    private List<Line> cohenSutherlandResult;

    private Point seedFillPoint;
    private boolean seedFillMode;
    private boolean modeClipPolygon;
    private final SeedFiller seedFiller;
    private final ScanlineFiller scanlineFiller;

    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();
    private static final int FILL_COLOR = new Color(0x58BFA9).getRGB();
    private static final int CLIP_LINE_COLOR = Color.YELLOW.getRGB();

    private static boolean isShiftPressed = false;
    private boolean isCohenSutherland;

    public Controller(Canvas canvas, Toolbar toolbar) {
        this.canvas = canvas;
        this.toolbar = toolbar;
        this.polygon = new Polygon();
        this.clipPolygon = new Polygon();
        this.scanlineFiller = new ScanlineFiller(canvas.getRaster());
        this.seedFiller = new RecursiveSeedFiller(canvas.getRaster());
        this.lineRasterizer = new XiaolinWuLineRasterizer(canvas.getRaster());
        this.polygonRasterizer = new PolygonOutlineRasterizer(lineRasterizer);
    }

    public void init() {
        toolbar.addClearListener(_ -> clearDrawing());
        toolbar.addSeedFillListener(_ -> toggleSeedFillMode());
        toolbar.addClipListener(_ -> clipPolygonButton());
        toolbar.addCohenSutherlandListener(_ -> cohenSutherlandButton());
        canvas.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(getKeyStroke(VK_C, 0), "clearCanvas");
        canvas.getActionMap().put("clearCanvas", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                clearDrawing();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (seedFillMode) {
                        fillAt(event);
                    } else {
                        startLine(event);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                endLine(event);
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (modeClipPolygon) {
                    if (previewLineClip != null) {
                        previewLineClip = new Line(previewLineClip.getPoint1(), getPoint(event, previewLineClip), PREVIEW_COLOR);
                        render();
                    }
                } else {
                    if (previewLine != null) {
                        previewLine = new Line(previewLine.getPoint1(), getPoint(event, previewLine), PREVIEW_COLOR);
                        render();
                    }
                }
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = false;
                }
            }
        });
    }



    private void render() {
        canvas.clear();
        if (cohenSutherlandResult != null) {
            polygonRasterizer.rasterize(clipPolygon, null);
            for (Line line : cohenSutherlandResult) {
                lineRasterizer.rasterize(line);
            }
            canvas.repaint();
            return;
        }
        polygonRasterizer.rasterize(polygon, previewLine);
        polygonRasterizer.rasterize(clipPolygon, previewLineClip);
        if (seedFillPoint != null) {
            //seedFiller.fill(seedFillPoint.getX(), seedFillPoint.getY(), Color.YELLOW.getRGB(), FILL_COLOR);
            scanlineFiller.fill(polygon, FILL_COLOR);
        }
        canvas.repaint();
    }

    private void clearDrawing() {
        polygon.clear();
        previewLine = null;
        previewLineClip = null;
        clipPolygon.clear();
        cohenSutherlandResult = null;
        seedFillPoint = null;
        seedFillMode = false;
        toolbar.setSeedFillMode(false);
        toolbar.setClipButton(false);
        modeClipPolygon = false;
        isCohenSutherland = false;
        isShiftPressed = false;
        canvas.clear();
        render();
    }

    private void fillAt(MouseEvent event) {
        Point seed = new Point(event.getX(), event.getY());

        if (!isInside(polygon, seed)) {
            return;
        }
        seedFillPoint = seed;
        render();
    }
    private boolean isInside(Polygon polygon, Point point) {
        boolean inside = false;

        for (Line line : polygon.getLines()) {
            Point a = line.getPoint1();
            Point b = line.getPoint2();

            if ((a.getY() > point.getY()) != (b.getY() > point.getY())
                    && point.getX() < (double) (b.getX() - a.getX())
                    * (point.getY() - a.getY()) / (b.getY() - a.getY()) + a.getX()) {
                inside = !inside;
            }
        }

        return inside;
    }

    private Point getPoint(MouseEvent event, Line previewLine) {
        int x = event.getX();
        int y = event.getY();

        if (!isShiftPressed || previewLine == null) {
            return new Point(x, y);
        }
        if (modeClipPolygon) {
            return shiftIsPressed(x, y, previewLine);
        } else {
            return shiftIsPressed(x, y, previewLine);
        }
    }

    private Point shiftIsPressed(int x, int y, Line previewLine) {
        Point startPoint = previewLine.getPoint1();
        int startX = startPoint.getX();
        int startY = startPoint.getY();

        double dx = x - startX;
        double dy = y - startY;
        double length = Math.hypot(dx, dy);

        if (length == 0) {
            return new Point(startX, startY);
        }

        double angle = Math.atan2(dy, dx);
        double step = Math.PI / 4; // 45 stupňů
        double snappedAngle = Math.round(angle / step) * step;

        x = startX + (int) Math.round(length * Math.cos(snappedAngle));
        y = startY + (int) Math.round(length * Math.sin(snappedAngle));

        return new Point(x, y);
    }

    private void startLine(MouseEvent event) {
        cohenSutherlandResult = null;
        if (!modeClipPolygon) {
            Point startPoint = polygon.getLastPoint();
            if (startPoint == null) {
                startPoint = getPoint(event, previewLine);
            }
            previewLine = new Line(startPoint, startPoint, PREVIEW_COLOR);
            render();
        }
        else {
            Point startPoint = clipPolygon.getLastPoint();
            if (startPoint == null) {
                startPoint = getPoint(event, previewLineClip);
            }
            previewLineClip = new Line(startPoint, startPoint, PREVIEW_COLOR);
            render();
        }
    }

    private void ClipPolygon(Polygon polygon, Polygon clipPolygon) {
        int n;
        if (isCohenSutherland) { n=3; } else { n=5; }
        if (polygon.getLines().isEmpty() || clipPolygon.getLines().size() < n) {
            return;
        }

        List<Point> clipVertices = new ArrayList<>();
        clipVertices.add(clipPolygon.getLines().get(0).getPoint1());
        for (Line line : clipPolygon.getLines()) {
            clipVertices.add(line.getPoint2());
        }

        Point first = clipVertices.get(0);
        Point last = clipVertices.get(clipVertices.size() - 1);
        if (first.getX() == last.getX() && first.getY() == last.getY()) {
            clipVertices.remove(clipVertices.size() - 1);
        }

        if (clipVertices.size() < n) {
            return;
        }

        double signedArea = 0;
        for (int i = 0; i < clipVertices.size(); i++) {
            Point current = clipVertices.get(i);
            Point next = clipVertices.get((i + 1) % clipVertices.size());
            signedArea += (double) current.getX() * next.getY()
                    - (double) next.getX() * current.getY();
        }

        if (signedArea == 0) {
            return;
        }
        double orientation = Math.signum(signedArea);

        List<Point> output = new ArrayList<>();
        output.add(polygon.getLines().get(0).getPoint1());
        for (Line line : polygon.getLines()) {
            output.add(line.getPoint2());
        }

        first = output.get(0);
        last = output.get(output.size() - 1);
        if (first.getX() == last.getX() && first.getY() == last.getY()) {
            output.remove(output.size() - 1);
        }

        for (int i = 0; i < clipVertices.size() && !output.isEmpty(); i++) {
            Point edgeStart = clipVertices.get(i);
            Point edgeEnd = clipVertices.get((i + 1) % clipVertices.size());

            List<Point> input = output;
            output = new ArrayList<>();
            Point previous = input.get(input.size() - 1);

            for (Point current : input) {
                double previousSide =
                        ((double) edgeEnd.getX() - edgeStart.getX())
                                * ((double) previous.getY() - edgeStart.getY())
                                - ((double) edgeEnd.getY() - edgeStart.getY())
                                * ((double) previous.getX() - edgeStart.getX());

                double currentSide =
                        ((double) edgeEnd.getX() - edgeStart.getX())
                                * ((double) current.getY() - edgeStart.getY())
                                - ((double) edgeEnd.getY() - edgeStart.getY())
                                * ((double) current.getX() - edgeStart.getX());

                boolean previousInside = orientation * previousSide >= 0;
                boolean currentInside = orientation * currentSide >= 0;

                if (currentInside != previousInside) {
                    double segmentX = (double) current.getX() - previous.getX();
                    double segmentY = (double) current.getY() - previous.getY();
                    double edgeX = (double) edgeEnd.getX() - edgeStart.getX();
                    double edgeY = (double) edgeEnd.getY() - edgeStart.getY();

                    double denominator = segmentX * edgeY - segmentY * edgeX;
                    double t = (((double) edgeStart.getX() - previous.getX()) * edgeY
                            - ((double) edgeStart.getY() - previous.getY()) * edgeX)
                            / denominator;

                    output.add(new Point(
                            (int) Math.round(previous.getX() + t * segmentX),
                            (int) Math.round(previous.getY() + t * segmentY)
                    ));
                }

                if (currentInside) {
                    output.add(current);
                }
                previous = current;
            }
        }

        int color = polygon.getLines().get(0).getColor();
        polygon.clear();
        for (int i = 0; i < output.size(); i++) {
            Point start = output.get(i);
            Point end = output.get((i + 1) % output.size());
            polygon.addLine(new Line(start, end, color));
        }
        isCohenSutherland = false;
    }

    private void endLine(MouseEvent event) {
        if(modeClipPolygon) {
            if (event.getButton() != MouseEvent.BUTTON1 || previewLineClip == null) {
                return;
            }
            clipPolygon.addLine(new Line(previewLineClip.getPoint1(), getPoint(event, previewLineClip), CLIP_LINE_COLOR));
            previewLineClip = null;
            render();
        }
        else {
            if (event.getButton() != MouseEvent.BUTTON1 || previewLine == null) {
                return;
            }
            polygon.addLine(new Line(previewLine.getPoint1(), getPoint(event, previewLine), LINE_COLOR));
            previewLine = null;
            render();
        }
    }

    private void cohenSutherlandButton() {
        if (modeClipPolygon) {
        clipPolygon.clear();
        clipPolygon.addLine(new Line(new Point(250, 150), new Point(550, 150), CLIP_LINE_COLOR));
        clipPolygon.addLine(new Line(new Point(550, 150), new Point(550, 450), CLIP_LINE_COLOR));
        clipPolygon.addLine(new Line(new Point(550, 450), new Point(250, 450), CLIP_LINE_COLOR));
        clipPolygon.addLine(new Line(new Point(250, 450), new Point(250, 150), CLIP_LINE_COLOR));
        render();
        isCohenSutherland = true;
        ClipPolygon(polygon, clipPolygon);
        render();
        }
    }




    //------------------------------Tlačítka---------------------------------
    private void clipPolygonButton() {

        if (modeClipPolygon) {
            modeClipPolygon = false;
            ClipPolygon(polygon, clipPolygon);
            clipPolygon.clear();
            render();
        } else {
            cohenSutherlandResult = null;
            modeClipPolygon = true;
        }
        toolbar.setClipButton(modeClipPolygon);
    }

    private void toggleSeedFillMode() {
        seedFillMode = !seedFillMode;
        toolbar.setSeedFillMode(seedFillMode);
    }
}
