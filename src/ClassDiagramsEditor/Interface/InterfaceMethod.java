package ClassDiagramsEditor.Interface;

import ClassDiagramsEditor.Parameters;
import ClassDiagramsEditor.Properties;
import ClassDiagramsEditor.StringSizeInPixels;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InterfaceMethod extends Properties {
    private List<Parameters> parameters;

    public InterfaceMethod(String name, String type, List<Parameters> parameters) {
        this.name = name;
        this.type = type;
        this.parameters = new LinkedList<>();
        for (Parameters param : parameters) {
            this.parameters.add(new Parameters(param.getName(), param.getType(), param.isFinal()));
        }
        width = resize();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("+ ");
        stringBuilder.append(name + "( ");
        if (parameters != null) {
            Iterator<Parameters> iterator = parameters.iterator();
            while (iterator.hasNext()) {
                Parameters param = iterator.next();
                stringBuilder.append(param.toString());
                if (iterator.hasNext()) {
                    stringBuilder.append(", ");
                }
            }
        }
        stringBuilder.append(") : " + type);

        return stringBuilder.toString();
    }

    public List<Parameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameters> parameterses) {
        parameters = parameterses;
        width = resize();
    }

    @Override
    protected int resize() {
        int methodWidth = StringSizeInPixels.getLength(toString());
        return methodWidth;
    }
}
