package ClassDiagramsEditor.Class;

import ClassDiagramsEditor.*;

public abstract class ClassProperties extends Properties{
    protected AccessLevel accessLevel;
    protected boolean isStatic;

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        width = resize();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        width = resize();
    }
}
