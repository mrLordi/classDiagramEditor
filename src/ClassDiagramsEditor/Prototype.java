package ClassDiagramsEditor;

public interface Prototype {
    public Prototype deepClone();
    public Prototype cloneElement(boolean addCopy);
}
