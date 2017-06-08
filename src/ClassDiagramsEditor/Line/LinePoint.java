package ClassDiagramsEditor.Line;

import ClassDiagramsEditor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LinePoint extends JLabel implements MouseListener, MouseMotionListener, FocusListener{
    private int x, y;
    private boolean active;
    private int centerX, centerY;
    private int mouseX, mouseY;
    private Element panel;
    private Line line;

    public LinePoint(int x1, int y1, Line line) {
        super();
        this.line = line;
        centerX = x1;
        centerY = y1;
        setBounds(x1 - 5, y1 - 5, 11, 11);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if ((e.getKeyCode() == KeyEvent.VK_DELETE)) {
                    ClassDiagramsEditor.getInstance().saveState();
                    DiagramPanel.getInstance().deleteLine(line);
                }
            }
        });
    }

    public LinePoint(int x, int y, Line line, Element panel1) {
        this(x, y, line);
        this.panel = panel1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (active) {
            g.setColor(Color.BLUE);
            g.drawOval(0, 0, 10, 10);
        }
    }

    @Override
    public void setLocation(int x, int y) {
        centerX = x + 5;
        centerY = y + 5;
        super.setLocation(x, y);
    }

    private void putOnBorder() {
        StraightLine[] lines = new StraightLine[]{
                new StraightLine(1, 0, panel.getX()),
                new StraightLine(1, 0, panel.getX() + panel.getWidth()),
                new StraightLine(0, 1, panel.getY()),
                new StraightLine(0, 1, panel.getY() + panel.getHeight())
        };
        int centerX = (2 * panel.getX() + panel.getWidth()) / 2;
        int centerY = (2 * panel.getY() + panel.getHeight()) / 2;
        if (centerX == getX() && centerY == getY()) {
            setLocation(panel.getX() + panel.getWidth() - 5, centerY - 5);
            return;
        }
        StraightLine fromCenter = new StraightLine(centerX, centerY, getX() + 5, getY() + 5);
        Point[] intersectPoints = new Point[4];
        for (int i = 0; i < 4; i++) {
            intersectPoints[i] = fromCenter.intersect(lines[i]);
        }
        Point closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            double distance = Math.hypot(intersectPoints[i].getX() - getX(), intersectPoints[i].getY() - getY());
            if (intersectPoints[i].x >= panel.getX() &&
                    intersectPoints[i].x <= panel.getX() + panel.getWidth() &&
                    intersectPoints[i].y >= panel.getY() &&
                    intersectPoints[i].y <= panel.getY() + panel.getHeight() &&
                    distance < closestDistance) {
                closestDistance = distance;
                closestPoint = intersectPoints[i];
            }
        }
        setLocation(closestPoint.x - 5, closestPoint.y - 5);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Point getPoint() {
        return new Point(centerX, centerY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        line.setActive(true);
    }

    @Override
    public void focusLost(FocusEvent e) {
        line.setActive(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (active) {
            if (e.getButton() == 3) {
                line.removePoint(this);
            } else if (e.getButton() == 1){
                line.addPointNear(this);
            }
        }
        else {
            requestFocus();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (active) {
            x = getX();
            y = getY();
            mouseX = e.getXOnScreen();
            mouseY = e.getYOnScreen();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (panel != null) {
//            Component component = getParent().getComponentAt(centerX, centerY);
//            panel =  component;
            putOnBorder();
            ClassDiagramsEditor.getInstance().repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (active) {
            setLocation(x + e.getXOnScreen() - mouseX, y + e.getYOnScreen() - mouseY);
            Point location = getLocation();
            if (location.x < 0) {
                location.x = 0;
            }
            if (location.y < 0) {
                location.y = 0;
            }
            setLocation(location.x, location.y);
            ClassDiagramsEditor.getInstance().repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

