package ClassDiagramsEditor;

public abstract class Properties {
    protected String name;
    protected String type;
    protected int width;

    protected abstract int resize();
    public abstract String toString();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        width = resize();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        width = resize();
    }

    public int getWidth() {
        return width;
    }
}
