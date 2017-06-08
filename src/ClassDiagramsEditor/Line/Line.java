package ClassDiagramsEditor.Line;

import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.DiagramPanel;
import ClassDiagramsEditor.Element;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.*;

public abstract class Line {
    private Element from, to;
    private Point fromPoint, toPoint;
    protected java.util.List<LinePoint> drawingPoints;
    private boolean active;

    public Line() {
        fromPoint = new Point();
        toPoint = new Point();
        drawingPoints = new LinkedList<>();
    }

    public Line(Element from, Element to) {
        this();
        this.from = from;
        this.to = to;
        if (from == to) {
            from.addLine(this);
        } else {
            from.addLine(this);
            to.addLine(this);
        }
        findEndPoints();
        registerPoints();
    }

    private void registerPoints() {
        for (LinePoint point : drawingPoints) {
            DiagramPanel.getInstance().add(point);
        }
    }

    public void findEndPoints() {
        if (from == to) {
            fromPoint.x = (int) (Math.random() * (from.getWidth() + 1) + from.getX());
            fromPoint.y = from.getY() + from.getHeight();
            toPoint.x = to.getX() + to.getWidth();
            toPoint.y = (int) (Math.random() * (to.getHeight() + 1) + to.getY());
            drawingPoints.add(new LinePoint(fromPoint.x, fromPoint.y, this, from));
            drawingPoints.add(new LinePoint(fromPoint.x, fromPoint.y + 20, this, null));
            drawingPoints.add(new LinePoint(toPoint.x + 20, fromPoint.y + 20, this, null));
            drawingPoints.add(new LinePoint(toPoint.x + 20, toPoint.y, this, null));
            drawingPoints.add(new LinePoint(toPoint.x, toPoint.y, this, to));
        }
        else {
            boolean intersectX = (from.getX() <= to.getX() &&
                    to.getX() <= from.getX() + from.getWidth()) ||
                    (from.getX() <= to.getX() + to.getWidth() &&
                            to.getX() + to.getWidth() <= from.getX() + from.getWidth());
            boolean intersectY = (from.getY() <= to.getY() &&
                    to.getY() <= from.getY() + from.getHeight()) ||
                    (from.getY() <= to.getY() + to.getHeight() &&
                            to.getY() + to.getHeight() <= from.getY() + from.getHeight());
            if (intersectX && intersectY) {
                fromPoint.setLocation(-5, -5);
                toPoint.setLocation(-5, -5);
            } else {
                Element rightest = (from.getX() + from.getWidth() > to.getX() + to.getWidth()) ? from : to;
                Element highest = from.getY() < to.getY() ? from : to;
                if (intersectX || (!intersectX && !intersectY)) {
                    fromPoint.x = (int) (Math.random() * (from.getWidth() + 1) + from.getX());
                    toPoint.x = (int) (Math.random() * (to.getWidth() + 1) + to.getX());
                    if (from == highest) {
                        fromPoint.y = from.getY() + from.getHeight();
                        toPoint.y = to.getY();
                    } else {
                        fromPoint.y = from.getY();
                        toPoint.y = to.getY() + to.getHeight();
                    }
                } else if (intersectY) {
                    fromPoint.y = (int) (Math.random() * (from.getHeight() + 1) + from.getY());
                    toPoint.y = (int) (Math.random() * (to.getHeight() + 1) + to.getY());
                    if (from == rightest) {
                        fromPoint.x = from.getX();
                        toPoint.x = to.getX() + to.getWidth();
                    } else {
                        fromPoint.x = from.getX() + from.getWidth();
                        toPoint.x = to.getX();
                    }
                }
                drawingPoints.add(new LinePoint(fromPoint.x, fromPoint.y, this, from));
                drawingPoints.add(new LinePoint(toPoint.x, toPoint.y, this, to));
            }
        }
    }

    protected void drawLine(Graphics2D g) {
        for (int i = 1; i < drawingPoints.size(); i++) {
            Line2D line2D = new Line2D.Double(drawingPoints.get(i - 1).getPoint(), drawingPoints.get(i).getPoint());
            g.draw(line2D);
        }
    }

    protected Stroke getStroke(Graphics g) {
        return ((Graphics2D) g).getStroke();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(active ? Color.RED : Color.BLACK);
        Stroke defaultStroke = g2.getStroke();
        g2.setStroke(getStroke(g));
        drawLine(g2);
        g2.setStroke(defaultStroke);
        paintBeginning(g);
        paintEnding(g);
        g2.dispose();
    }

    public void elementMoved(Element element, MouseEvent e) {
        if (drawingPoints != null && drawingPoints.size() >= 2) {
            if (from == to) {
                drawingPoints.get(0).mouseReleased(e);
                drawingPoints.get(drawingPoints.size() - 1).mouseReleased(e);
            } else if (element == from) {
                drawingPoints.get(0).mouseReleased(e);
            } else if (element == to) {
                drawingPoints.get(drawingPoints.size() - 1).mouseReleased(e);
            }
        }
    }

    public void removePoint(LinePoint linePoint) {
        int index = drawingPoints.indexOf(linePoint);
        if ((index == 0 || index == drawingPoints.size() - 1) && active){
            return;
        }
        if (index > 0 && index != drawingPoints.size()) {
            drawingPoints.remove(index);
            DiagramPanel.getInstance().remove(linePoint);
            drawingPoints.get(0).requestFocus();
            ClassDiagramsEditor.getInstance().repaint();
        }
    }

    public void addPointNear(LinePoint linePoint) {
        int index = drawingPoints.indexOf(linePoint);
        if (index == -1) {
            return;
        }
        if (index == 0) {
            addPointBetween(linePoint, drawingPoints.get(index + 1));
        }
        else if (index == drawingPoints.size() - 1) {
            addPointBetween(drawingPoints.get(index - 1), linePoint);
        }
        else {
            LinePoint left = drawingPoints.get(index - 1);
            LinePoint right = drawingPoints.get(index + 1);
            double leftDistance = Math.hypot(
                    left.getCenterX() - linePoint.getCenterX(),
                    left.getCenterY() - linePoint.getCenterY()
            );
            double rightDistance = Math.hypot(
                    right.getCenterX() - linePoint.getCenterX(),
                    right.getCenterY() - linePoint.getCenterY()
            );
            if (rightDistance > leftDistance) {
                addPointBetween(linePoint, right);
            }
            else {
                addPointBetween(left, linePoint);
            }
        }
        ClassDiagramsEditor.getInstance().repaint();
    }

    private void addPointBetween(LinePoint left, LinePoint right) {
        LinePoint newPoint = new LinePoint(
                (left.getCenterX() + right.getCenterX()) / 2,
                (left.getCenterY() + right.getCenterY()) / 2,
                this, null
        );
        newPoint.setActive(true);
        drawingPoints.add(
                drawingPoints.indexOf(left) + 1,
                newPoint
        );
        DiagramPanel.getInstance().add(newPoint);
        DiagramPanel.getInstance().revalidate();
        ClassDiagramsEditor.getInstance().repaint();
    }

    public void setActive(boolean active) {
        this.active = active;
        for (LinePoint lp : drawingPoints) {
            lp.setActive(active);
        }
        ClassDiagramsEditor.getInstance().repaint();
    }

    public void deleteLine() {
        setActive(false);
        for(LinePoint linePoint : drawingPoints) {
            DiagramPanel.getInstance().remove(linePoint);
        }
        from.deleteLine(this);
        to.deleteLine(this);
    }

    public Element getFrom() {
        return from;
    }

    public Element getTo() {
        return to;
    }

    protected Point[] getArrow(Point startPoint, Point endPoint, int angle, int size) {
        double phi = Math.toRadians(angle);
        double dy = endPoint.y - startPoint.y;
        double dx = endPoint.x - startPoint.x;
        double theta = Math.atan2(dy, dx);
        Point[] res = new Point[4];
        res[0] = new Point(endPoint.x, endPoint.y);
        res[1] = new Point(
                (int) (endPoint.x - size * Math.cos(theta + phi)),
                (int) (endPoint.y - size * Math.sin(theta + phi))
        );
        res[2] = new Point(
                (int) (endPoint.x - size * Math.cos(theta - phi)),
                (int) (endPoint.y - size * Math.sin(theta - phi))
        );
        res[3] = new Point(
                (int)(endPoint.x - 2 * size * Math.sin(phi) * Math.cos(theta)),
                (int)(endPoint.y - 2 * size * Math.cos(phi) * Math.sin(theta))
        );
        return res;
    }

    public static Line createLine(String from, String to, int lineType) {
        switch (lineType) {
            case 1: return new AggregationLine(search(from), search(to));
            case 2: return new UniAggregationLine(search(from), search(to));
            case 3: return new CompositionLine(search(from), search(to));
            case 4: return new UniCompositionLine(search(from), search(to));
            case 5: return new AssociationLine(search(from), search(to));
            case 6: return new UniAssociationLine(search(from), search(to));
            case 7: return new DependencyLine(search(from), search(to));
            case 8: return new GeneralizationLine(search(from), search(to));
            case 9: return new RealizationLine(search(from), search(to));
        }
        return null;
    }

    private static Element search(String name){
        DiagramPanel panel= DiagramPanel.getInstance();
        for (Element element : panel.getClassList()) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        for (Element element : panel.getInterfaceList()) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        for (Element element : panel.getEnumList()) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    public abstract void paintBeginning(Graphics g);

    public abstract void paintEnding(Graphics g);
}
