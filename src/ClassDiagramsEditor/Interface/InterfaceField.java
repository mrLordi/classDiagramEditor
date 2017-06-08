package ClassDiagramsEditor.Interface;

import ClassDiagramsEditor.Properties;
import ClassDiagramsEditor.StringSizeInPixels;

public class InterfaceField extends Properties {
    private String initializes;

    public InterfaceField(String name, String type, String initializes) {
        this.name = name;
        this.type = type;
        this.initializes = initializes;
        width = resize();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("+ ");
        stringBuilder.append(name + " : " );
        stringBuilder.append(type);
        stringBuilder.append(" = " + initializes);

        return stringBuilder.toString();
    }

    @Override
    protected int resize() {
        int fieldWidth = StringSizeInPixels.getLength(toString());
        return fieldWidth;
    }

    public String getInitializes() {
        return initializes;
    }

    public void setInitializes(String initializes) {
        this.initializes = initializes;
        width = resize();
    }
}
