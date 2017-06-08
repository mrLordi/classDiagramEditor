package ClassDiagramsEditor.Line;

import java.awt.*;

public class StraightLine {
    private int a, b, c;
    public StraightLine(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    public StraightLine(int x1, int y1, int x2, int y2) {
        a = y1 - y2;
        b = x2 - x1;
        c = a * x1 + b * y1;
    }
    public Point intersect(StraightLine line) {
        int numeratorX = c * line.getB() - line.getC() * b;
        int numeratorY = a * line.getC() - line.getA() * c;
        int denominator = a * line.getB() - b * line.getA();

        if (denominator == 0) {
            return new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        return new Point(numeratorX / denominator, numeratorY / denominator);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }
}
