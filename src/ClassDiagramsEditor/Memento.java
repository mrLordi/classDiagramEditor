package ClassDiagramsEditor;

import java.util.List;

public class Memento {
    private final List<List<Element>> state;
    public Memento(List<List<Element>> state) {
        this.state = state;
    }
    public List<List<Element>> getState() {
        return state;
    }
}
