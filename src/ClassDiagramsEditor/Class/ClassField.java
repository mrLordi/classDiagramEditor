package ClassDiagramsEditor.Class;

import ClassDiagramsEditor.*;

public class ClassField extends ClassProperties {
    private boolean isFinal;
    private String initializes;

    public ClassField(AccessLevel accessLevel, String name, boolean isStatic, String type, boolean isFinal, String initializes) {
        this.accessLevel = accessLevel;
        this.name = name;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
        this.type = type;
        this.initializes = initializes;
        width = resize();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (isStatic) {
            stringBuilder.append("<html><u>");
        }

        if (accessLevel == AccessLevel.PUBLIC) {
            stringBuilder.append("+ ");
        } else if (accessLevel == AccessLevel.PRIVATE) {
            stringBuilder.append("- ");
        } else if (accessLevel == AccessLevel.PROTECTED) {
            stringBuilder.append("# ");
        } else {
            stringBuilder.append("~ ");
        }
        stringBuilder.append(name + " : " );

        if (isFinal) {
            stringBuilder.append("final ");
        }
        stringBuilder.append(type);
        if (initializes != null && initializes.length() != 0) {
            stringBuilder.append(" = " + initializes.toString());
        }

        return stringBuilder.toString();
    }

    @Override
    protected int resize() {
        int fieldWidth = StringSizeInPixels.getLength(toString());
        String str = null;
        if (isStatic()) {
            str = new String("<html><u>");
        }
        if(str != null) {
            fieldWidth -= StringSizeInPixels.getLength(str);
        }
        return fieldWidth;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
        width = resize();
    }

    public String getInitializes() {
        return initializes;
    }

    public void setInitializes(String initializes) {
        this.initializes = initializes;
        width = resize();
    }
}
