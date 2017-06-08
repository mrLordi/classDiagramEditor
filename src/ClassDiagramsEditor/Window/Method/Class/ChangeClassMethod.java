package ClassDiagramsEditor.Window.Method.Class;

import ClassDiagramsEditor.AccessLevel;
import ClassDiagramsEditor.Class.*;
import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.Window.Method.Class.Parameter.AddParameters;
import ClassDiagramsEditor.Window.Method.Class.Parameter.ChangeParameter;
import ClassDiagramsEditor.*;
import ClassDiagramsEditor.Parameters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

public class ChangeClassMethod extends JDialog {
    private ChangeParameter changeDialog;
    private AddParameters parameterDialog;
    private JTextField nameTextField;
    private JComboBox accessLevelComboBox;
    private JTextField typeTextField;
    private JRadioButton staticRadioButton;
    private JRadioButton abstractRadioButton;
    private JRadioButton noneRadioButton;
    private JList listParameters;

    public ChangeClassMethod(ClassMethod method, int parentIndex, DefaultListModel model, Class element) {
        super(ClassDiagramsEditor.getInstance(), "Change method", true);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(510, 260));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        JLabel nameClassLabel = new JLabel(element.getName());
        nameClassLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel accessLevelLabel = new JLabel("Access level");
        JLabel typeLabel = new JLabel("Type");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Add new");
        JButton deleteButton = new JButton("Delete");
        JButton propertyButton = new JButton("Change");
        JLabel parameters = new JLabel("Parameters:");
        parameters.setHorizontalAlignment(SwingConstants.CENTER);

        List<Parameters> tempList = new LinkedList<>();
        List<Parameters> list = method.getParameters();
        DefaultListModel listModel = new DefaultListModel();
        for (Parameters param : list) {
            listModel.addElement(param.toString());
            tempList.add(new Parameters(param.getName(), param.getType(), param.isFinal()));
        }
        listParameters = new JList(listModel);
        listParameters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listParameters);
        parameterDialog = new AddParameters(element, listModel, list, false, tempList);

        nameTextField = new JTextField(method.getName(), 20);
        String[] access = {"public", "private", "protected", "package"};
        accessLevelComboBox = new JComboBox(access);
        AccessLevel level = method.getAccessLevel();
        if (level == AccessLevel.PUBLIC) {
            accessLevelComboBox.setSelectedIndex(0);
        } else if (level == AccessLevel.PRIVATE) {
            accessLevelComboBox.setSelectedIndex(1);
        } else if (level == AccessLevel.PROTECTED) {
            accessLevelComboBox.setSelectedIndex(2);
        } else {
            accessLevelComboBox.setSelectedIndex(3);
        }
        typeTextField = new JTextField(method.getName(), 20);
        staticRadioButton =  new JRadioButton("Static", method.isStatic());
        abstractRadioButton = new JRadioButton("Abstract", method.isStatic());
        noneRadioButton = new JRadioButton("None");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(staticRadioButton);
        buttonGroup.add(abstractRadioButton);
        buttonGroup.add(noneRadioButton);
        if (!method.isAbstract() && !method.isStatic()) {
            noneRadioButton.setSelected(true);
        }

        nameClassLabel.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);
        accessLevelLabel.setBounds(10, 55, 80, 20);
        accessLevelComboBox.setBounds(90, 55, 100, 20);
        typeLabel.setBounds(10, 80, 80, 20);
        typeTextField.setBounds(90, 80, 100, 20);
        staticRadioButton.setBounds(5, 110, 100, 20);
        abstractRadioButton.setBounds(5, 130, 100, 20);
        noneRadioButton.setBounds(5, 150, 100, 20);
        saveButton.setBounds(170, 190, 80, 30);
        cancelButton.setBounds(260, 190, 80, 30);
        parameters.setBounds(205, 5, 300, 20);
        scrollPane.setBounds(210, 30, 290, 100);
        addButton.setBounds(210, 140, 90, 20);
        propertyButton.setBounds(310, 140, 90, 20);
        deleteButton.setBounds(410, 140, 90, 20);
        deleteButton.setEnabled(false);
        propertyButton.setEnabled(false);

        add(parameters);
        add(scrollPane);
        add(addButton);
        add(propertyButton);
        add(deleteButton);
        add(nameClassLabel);
        add(nameLabel);
        add(nameTextField);
        add(accessLevelLabel);
        add(accessLevelComboBox);
        add(typeLabel);
        add(typeTextField);
        add(staticRadioButton);
        add(abstractRadioButton);
        add(noneRadioButton);
        add(saveButton);
        add(cancelButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                for (Parameters param : list) {
                    listModel.addElement(param.toString());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                for (Parameters param : list) {
                    listModel.addElement(param.toString());
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AccessLevel accessLevel = null;
                if (accessLevelComboBox.getSelectedIndex() == 0) {
                    accessLevel = AccessLevel.PUBLIC;
                } else if (accessLevelComboBox.getSelectedIndex() == 1) {
                    accessLevel = AccessLevel.PRIVATE;
                } else if (accessLevelComboBox.getSelectedIndex() == 2) {
                    accessLevel = AccessLevel.PROTECTED;
                } else {
                    accessLevel = AccessLevel.PACKAGE;
                }

                String name = nameTextField.getText();
                String type = typeTextField.getText();
                boolean st = staticRadioButton.isSelected();
                boolean ab = abstractRadioButton.isSelected();

                boolean checkType = NameVerification.checkWithRegExp(type) && ReservedNames.check(type);
                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);

                if (checkName && checkType) {
                    if (element.getMethods().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean check = name.equals(method.getName()) && type.equals(method.getType()) && st == method.isStatic()
                                && ab == method.isAbstract();
                        if (!check) {
                            ClassDiagramsEditor.getInstance().saveState();
                            element.getMethodName().add(name);
                            element.getMethods().remove(method.getName());
                        }
                        method.setAccessLevel(accessLevel);
                        method.setName(name);
                        method.setType(type);
                        method.setStatic(st);
                        method.setAbstract(ab);
                        method.setParameters(tempList);

                        model.setElementAt(method.toString(), parentIndex);
                        element.updateMethod(parentIndex);
                        element.updateElement();
                        dispose();
                    }
                } else {
                    if (!checkName) {
                        if (name.length() == 0) {
                            JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Enter a name!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Incorrect name!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        if (type.length() == 0) {
                            JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Enter a type!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Incorrect type!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }


            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parameterDialog.setVisible(true);
            }
        });

        listParameters.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!listParameters.isSelectionEmpty()) {
                    deleteButton.setEnabled(true);
                    propertyButton.setEnabled(true);
                } else {
                    deleteButton.setEnabled(false);
                    propertyButton.setEnabled(false);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = listParameters.getSelectedIndex();

                listModel.remove(index);
                tempList.remove(index);
            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = listParameters.getSelectedIndex();
                changeDialog = new ChangeParameter(list.get(index), element, index, listModel, false, tempList, list);
                changeDialog.setVisible(true);
            }
        });
    }
}
