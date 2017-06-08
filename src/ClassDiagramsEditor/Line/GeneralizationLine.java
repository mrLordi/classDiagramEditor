package ClassDiagramsEditor.Line;

import ClassDiagramsEditor.Element;
import java.awt.*;


public class GeneralizationLine extends Line {

    public GeneralizationLine(Element from, Element to) {
        super(from, to);
    }

    @Override
    public void paintBeginning(Graphics g) {

    }

    @Override
    public void paintEnding(Graphics g) {
        if (drawingPoints != null && drawingPoints.size() > 1) {
            Graphics2D g2 = (Graphics2D) g;
            Point startPoint = drawingPoints.get(drawingPoints.size() - 2).getPoint();
            Point endPoint = drawingPoints.get(drawingPoints.size() - 1).getPoint();
            Point[] points = getArrow(startPoint, endPoint, 40, 10);
            Polygon polygon = new Polygon();
            polygon.addPoint(points[1].x, points[1].y);
            polygon.addPoint(points[0].x, points[0].y);
            polygon.addPoint(points[2].x, points[2].y);
            g2.setColor(Color.WHITE);
            g2.fillPolygon(polygon);
            g2.setColor(Color.BLACK);
            g2.drawPolygon(polygon);
        }
    }
}
