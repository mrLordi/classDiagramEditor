package ClassDiagramsEditor.Class;

import ClassDiagramsEditor.*;
import ClassDiagramsEditor.Window.Field.Class.FieldSettings;
import ClassDiagramsEditor.Window.Method.Class.MethodSettings;
import ClassDiagramsEditor.Window.Name.NameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class Class extends Element {

    private MethodSettings methodDialog;
    private NameSettings nameDialog;
    private FieldSettings fieldDialog;

    private List<ClassMethod> methods;
    private List<ClassField> fields;

    private List<String> fieldName;
    private List<String> methodName;

    private boolean isAbstract;
    private boolean isFinal;

    private JPanel fieldsPanel;
    private JPanel methodPanel;


    public Class(String name, int x, int y) {
        super(name, x, y);
        methods = new LinkedList<>();
        fields = new LinkedList<>();
        fieldName = new LinkedList<>();
        methodName = new LinkedList<>();

        color = Color.WHITE;
        fieldsPanel = createPanel(color);
        methodPanel = createPanel(color);
        defaultElement();

        height = 45;
        width = 100;
        isAbstract = false;
        isFinal = false;

        nameLabel = createLabel(this.toString(), color);
        resizeLabel(this.toString(), true);
        nameLabel.setFont(new Font("Default", Font.BOLD, 12));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        nameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    nameDialog.setVisible(true);
                }
            }
        });
        nameLabel.addMouseListener(mousePressedReleased);
        nameLabel.addMouseMotionListener(mouseDragged);

        fieldsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    setFieldDialog();
                }
            }
        });
        fieldsPanel.addMouseListener(mousePressedReleased);
        fieldsPanel.addMouseMotionListener(mouseDragged);

        methodPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    setMethodDialog();
                }
            }
        });
        methodPanel.addMouseListener(mousePressedReleased);
        methodPanel.addMouseMotionListener(mouseDragged);

        addMouseListener(mousePressedReleased);
        addMouseMotionListener(mouseDragged);

        add(BorderLayout.CENTER, fieldsPanel);
        add(BorderLayout.SOUTH, methodPanel);
        add(BorderLayout.NORTH, nameLabel);

        nameDialog = new NameSettings(this);
        updateElement();
    }

    private void setFieldDialog() {
        fieldDialog = new FieldSettings(this);
        fieldDialog.setClassName();
        fieldDialog.setVisible(true);
    }

    private void setMethodDialog() {
        methodDialog = new MethodSettings(this);
        methodDialog.setClassName();
        methodDialog.setVisible(true);
    }

    public void deleteField(int index) {
        int temp = fields.get(index).getWidth();
        fields.remove(index);
        if (temp == maxWidth) {
            maxWidth = 0;
            getMaxWidth();
        }
        if (fields.size() == 0 ) {
            JLabel label = (JLabel)fieldsPanel.getComponent(0);
            label.setText(" ");

        } else {
            fieldsPanel.remove(index);
            height -= 16;
        }
    }

    public void deleteMethod(int index) {
        int temp = methods.get(index).getWidth();
        methods.remove(index);
        if (temp == maxWidth) {
            maxWidth = 0;
            getMaxWidth();
        }
        if (methods.size() == 0) {
            JLabel label = (JLabel)methodPanel.getComponent(0);
            label.setText(" ");

        } else {
            methodPanel.remove(index);
            height -= 16;
        }
    }

    public void addField(ClassField field) {
        JLabel temp = (JLabel)fieldsPanel.getComponent(0);
        if (temp.getText().equals(" ")){
            temp.setText(field.toString());
            resizeField(field, false);
        } else {
            temp = createLabel(field.toString(), color);
            resizeField(field, true);
        }
        fieldsPanel.add(temp);
        fields.add(field);
    }

    public void addMethod(ClassMethod method) {
        JLabel temp = (JLabel)methodPanel.getComponent(0);
        if (temp.getText().equals(" ")){
            temp.setText(method.toString());
            resizeMethod(method, false);
        } else {
            temp = createLabel(method.toString(), color);
            resizeMethod(method, true);
        }
        methodPanel.add(temp);
        methods.add(method);
    }

    @Override
    public void getMaxWidth() {
        for (ClassMethod method : methods) {
            maxWidth = maxWidth < method.getWidth() ? method.getWidth() : maxWidth;
        }
        for (ClassField field : fields) {
            maxWidth = maxWidth < field.getWidth() ? field.getWidth() : maxWidth;
        }

        maxWidth = maxWidth < nameWidth ? nameWidth : maxWidth;
        width = maxWidth + 15;
    }

    @Override
    protected void resizeLabel(String s, boolean check) {
        if (check) {
            height += 16;
        }

        nameWidth = StringSizeInPixels.getLength(s);
        if (s.startsWith("<html><i>")) {
            nameWidth -= StringSizeInPixels.getLength("html><i>");
        }
        if (nameWidth > maxWidth) {
            maxWidth = nameWidth;
        }
        width = nameWidth + 15 > width ? nameWidth + 15 : width;
    }

    @Override
    protected void deleteElement() {
        panel.getClassList().remove(this);
        panel.remove(this);
        panel.revalidate();

        for (int i = lines.size() - 1; i >= 0; i--) {
            lines.get(i).getFrom().getElements().remove(getName());
            lines.get(i).getFrom().getElements().remove(getName());
            panel.deleteLine(lines.get(i));
        }
        panel.getNameList().remove(getName());
        ClassDiagramsEditor.getInstance().repaint();
    }

    @Override
    protected void defaultElement() {
        if (fields.isEmpty()) {
            fieldsPanel.add(createLabel(" ", color));
        }

        if (methods.isEmpty()) {
            methodPanel.add(createLabel(" ", color));
        }
    }

    @Override
    public Prototype cloneElement(boolean addCopy) {
        String name = getName();
        if (addCopy) {
            name += "Copy1";
        }

        Class temp = new Class(name, 0, 0);
        for (ClassField field : fields){
            temp.addField(new ClassField(field.getAccessLevel(), field.getName(), field.isStatic,
                    field.getType(), field.isFinal(), field.getInitializes()));
        }

        for (ClassMethod method : methods) {
            List<Parameters> tempList = new LinkedList<>();
            List<Parameters> list = method.getParameters();
            for (Parameters param : list) {
                tempList.add(new Parameters(param.getName(), param.getType(), param.isFinal()));
            }

            temp.addMethod(new ClassMethod(method.getAccessLevel(), method.getName(), method.isStatic,
                    method.getType(), method.isAbstract(), tempList));
        }

        temp.setAccessLevel(accessLevel);
        temp.setAbstract(isAbstract);
        temp.setFinal(isFinal);
        temp.setX(x);
        temp.setY(y);
        return temp;
    }

    private void resizeMethod(ClassMethod method, boolean check) {
        int methodWidth = method.getWidth();
        width = methodWidth + 20 > width ? methodWidth + 20 : width;
        if (check) {
            height += 16;
        }
        if (methodWidth > maxWidth) {
            maxWidth = methodWidth;
        }
    }

    private void resizeField(ClassField field, boolean check) {
        int fieldWidth = field.getWidth();
        width = fieldWidth + 15 > width ? fieldWidth + 15 : width;
        if (check) {
            height += 16;
        }
        if (fieldWidth > maxWidth) {
            maxWidth = fieldWidth;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (accessLevel == AccessLevel.PUBLIC) {
            stringBuilder.append("+ ");
        } else if (accessLevel == AccessLevel.PRIVATE) {
            stringBuilder.append("- ");
        } else if (accessLevel == AccessLevel.PROTECTED) {
            stringBuilder.append("# ");
        } else {
            stringBuilder.append("~ ");
        }

        if (isAbstract) {
            stringBuilder.insert(0, "<html><i>");
        } else if (isFinal) {
            stringBuilder.append("final ");
        }
        stringBuilder.append(name);

        return stringBuilder.toString();
    }

    public void updateField(int index) {
        JLabel label = (JLabel)fieldsPanel.getComponent(index);
        ClassField field = fields.get(index);
        label.setText(field.toString());
        resizeField(field, false);

        maxWidth = 0;
        getMaxWidth();

        setBounds(x, y, width, height);
    }

    public void updateMethod(int index) {
        JLabel label = (JLabel)methodPanel.getComponent(index);
        ClassMethod method = methods.get(index);
        label.setText(method.toString());
        resizeMethod(method, false);

        maxWidth = 0;
        getMaxWidth();

        setBounds(x, y, width, height);
    }

    public List<ClassField> getFields() {
        return fields;
    }

    public List<ClassMethod> getMethods() {
        return methods;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        updateNameLabel();
    }

    public boolean isFinal() {
        return isFinal;

    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
        updateNameLabel();
    }

    @Override
    protected Element getThis() {
        return this;
    }

    public List<String> getFieldName() {
        return fieldName;
    }

    public List<String> getMethodName() {
        return methodName;
    }
}

