package ClassDiagramsEditor.Interface;

import ClassDiagramsEditor.AccessLevel;
import ClassDiagramsEditor.Element;
import ClassDiagramsEditor.Parameters;
import ClassDiagramsEditor.Window.Field.Interface.InterfaceFieldSettings;
import ClassDiagramsEditor.Window.Method.Interface.InterfaceMethodSettings;
import ClassDiagramsEditor.Window.Name.InterfaceNameSettings;
import ClassDiagramsEditor.StringSizeInPixels;
import ClassDiagramsEditor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class Interface extends Element {
    private InterfaceMethodSettings methodDialog;
    private InterfaceFieldSettings fieldDialog;
    private InterfaceNameSettings nameDialog;

    private List<InterfaceMethod> methods;
    private List<InterfaceField> fields;

    private List<String> fieldName;
    private List<String> methodName;

    private JPanel namePanel;
    private JPanel fieldsPanel;
    private JPanel methodPanel;

    public Interface(String name, int x, int y) {
        super(name, x, y);

        color = new Color(255,255,240);
        methods = new LinkedList<>();
        fields = new LinkedList<>();
        fieldName = new LinkedList<>();
        methodName = new LinkedList<>();

        fieldsPanel = createPanel(color);
        methodPanel = createPanel(color);
        defaultElement();

        height = 60;
        width = 100;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        nameLabel = new JLabel(this.toString());
        JLabel interfaceLabel = new JLabel("<<interface>>");
        resizeLabel(toString(), true);
        interfaceLabel.setAlignmentX(CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        interfaceLabel.setFont(new Font("Default", Font.BOLD, 12));
        nameLabel.setFont(new Font("Default", Font.BOLD, 12));

        namePanel = createPanel(color);
        namePanel.add(interfaceLabel);
        namePanel.add(nameLabel);

        namePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    nameDialog.setVisible(true);
                }
            }
        });
        namePanel.addMouseListener(mousePressedReleased);
        namePanel.addMouseMotionListener(mouseDragged);

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
        add(BorderLayout.NORTH, namePanel);

        nameDialog = new InterfaceNameSettings(this);

        updateElement();
    }

    private void setFieldDialog() {
        fieldDialog = new InterfaceFieldSettings(this);
        fieldDialog.setInterfaceName();
        fieldDialog.setVisible(true);
    }

    private void setMethodDialog() {
        methodDialog = new InterfaceMethodSettings(this);
        methodDialog.setInterfaceName();
        methodDialog.setVisible(true);
    }

    public void addField(InterfaceField field) {
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

    public void addMethod(InterfaceMethod method) {
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

    public void updateField(int index) {
        JLabel label = (JLabel)fieldsPanel.getComponent(index);
        InterfaceField field = fields.get(index);
        label.setText(field.toString());
        resizeField(field, false);

        maxWidth = 0;
        getMaxWidth();

        setBounds(x, y, width, height);
    }

    public void updateMethod(int index) {
        JLabel label = (JLabel)methodPanel.getComponent(index);
        InterfaceMethod method = methods.get(index);
        label.setText(method.toString());
        resizeMethod(method, false);

        maxWidth = 0;
        getMaxWidth();

        setBounds(x, y, width, height);
    }

    private void resizeMethod(InterfaceMethod method, boolean check) {
        int methodWidth = method.getWidth();
        width = methodWidth + 20 > width ? methodWidth + 20 : width;
        if (check) {
            height += 16;
        }
        if (methodWidth > maxWidth) {
            maxWidth = methodWidth;
        }
    }

    private void resizeField(InterfaceField field, boolean check) {
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
    protected void resizeLabel(String s, boolean check) {
        if (check) {
            height += 16;
        }
        nameWidth = StringSizeInPixels.getLength(s);
        if (nameWidth > maxWidth) {
            maxWidth = nameWidth;
        }
        width = nameWidth + 15 > width ? nameWidth + 15 : width;
        if (width < 100) {
            width = 100;
        }
    }

    @Override
    protected void deleteElement() {
        panel.getInterfaceList().remove(this);
        panel.remove(this);
        for (int i = lines.size() - 1; i >= 0; i--) {
            lines.get(i).getFrom().getElements().remove(getName());
            lines.get(i).getTo().getElements().remove(getName());
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

        Interface temp = new Interface(name, 0, 0);
        for (InterfaceField field : fields){
            temp.addField(new InterfaceField(field.getName(), field.getType(), field.getInitializes() ));
        }

        for (InterfaceMethod method : methods) {
            List<Parameters> tempList = new LinkedList<>();
            List<Parameters> list = method.getParameters();
            for (Parameters param : list) {
                tempList.add(new Parameters(param.getName(), param.getType(), param.isFinal()));
            }

            temp.addMethod(new InterfaceMethod(method.getName(), method.getType(), tempList));
        }

        temp.setAccessLevel(accessLevel);
        temp.setX(x);
        temp.setY(y);
        return temp;
    }

    @Override
    protected Element getThis() {
        return this;
    }

    @Override
    public void getMaxWidth() {
        for (InterfaceMethod method : methods) {
            maxWidth = maxWidth < method.getWidth() ? method.getWidth() : maxWidth;
        }
        for (InterfaceField field : fields) {
            maxWidth = maxWidth < field.getWidth() ? field.getWidth() : maxWidth;
        }

        maxWidth = maxWidth < nameWidth ? nameWidth : maxWidth;
        width = maxWidth + 15;
        if (width < 100) {
            width = 100;
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

        stringBuilder.append(name);

        return stringBuilder.toString();
    }

    public List<InterfaceMethod> getMethods() {
        return methods;
    }

    public List<InterfaceField> getFields() {
        return fields;
    }

    public List<String> getMethodName() {
        return methodName;
    }

    public List<String> getFieldName() {
        return fieldName;
    }

}
