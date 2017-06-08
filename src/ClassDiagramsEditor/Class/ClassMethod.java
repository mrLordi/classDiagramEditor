package ClassDiagramsEditor.Class;

import ClassDiagramsEditor.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClassMethod extends ClassProperties {
    private boolean isAbstract;
    private List<Parameters> parameters;

    public ClassMethod(AccessLevel accessLevel, String name, boolean isStatic, String type, boolean isAbstract, List<Parameters> parameters) {
        this.accessLevel = accessLevel;
        this.name = name;
        this.isAbstract = isAbstract;
        this.isStatic = isStatic;
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

        if (isStatic) {
            stringBuilder.append("<html><u>");
        } else if(isAbstract) {
            stringBuilder.append("<html><i>");
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

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        if (isAbstract) {
            setStatic(false);
        }
        width = resize();
    }

    @Override
    protected int resize() {
        int methodWidth = StringSizeInPixels.getLength(toString());
        String str = null;
        if (isAbstract()) {
            str = new String("<html><i>");
        } else if (isStatic()) {
            str = new String("<html><u>");
        }

        if(str != null) {
            methodWidth -= StringSizeInPixels.getLength(str);
        }

        return methodWidth;
    }
}
