package ClassDiagramsEditor;

public class Parameters {
    private String name;
    private String type;
    private boolean isFinal;

    public Parameters (String name, String type, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    @Override
    public String toString() {
        String s = getName() + " : ";
        if (isFinal()){
            s += "final ";
        }
        s += getType();
        return s;
    }
}
