package ClassDiagramsEditor.Line;
import ClassDiagramsEditor.*;

import java.awt.*;


public class UniAssociationLine extends Line {

    public UniAssociationLine(Element from, Element to) {
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
            g2.drawLine(points[1].x, points[1].y, points[0].x, points[0].y);
            g2.drawLine(points[2].x, points[2].y, points[0].x, points[0].y);
        }
    }
}
