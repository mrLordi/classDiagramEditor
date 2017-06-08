package ClassDiagramsEditor;

import java.awt.*;

public class StringSizeInPixels {
    private final static Font font = new Font("Default", Font.PLAIN, 12);
    private final static FontMetrics fontMetrics = DiagramPanel.getInstance().getGraphics().getFontMetrics(font);

    public static int getLength(String str) {
        return fontMetrics.stringWidth(str);
    }
}
