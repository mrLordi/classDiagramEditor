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
import java.util.*;
import java.util.List;

public class AddClassMethod extends JDialog {
    private ChangeParameter changeDialog;
    private AddParameters parameterDialog;
    private Class element;
    private JTextField nameTextField;
    private JComboBox accessLevelComboBox;
    private JTextField typeTextField;
    private JRadioButton staticRadioButton;
    private JRadioButton abstractRadioButton;
    private JRadioButton noneRadioButton;
    private JList listParameters;
    private JLabel className;

    public AddClassMethod(Class element, DefaultListModel model) {
        super(ClassDiagramsEditor.getInstance(), "Add method", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(510, 260));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        className = new JLabel(element.getName());
        className.setHorizontalAlignment(SwingConstants.CENTER);

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

        DefaultListModel listModel = new DefaultListModel();
        List<Parameters> list = new LinkedList<>();
        listParameters = new JList(listModel);
        listParameters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listParameters);
        parameterDialog = new AddParameters(element, listModel, list, true, null);

        nameTextField = new JTextField(20);
        String[] access = {"public", "private", "protected", "package"};
        accessLevelComboBox = new JComboBox(access);
        typeTextField = new JTextField(20);
        staticRadioButton =  new JRadioButton("Static");
        abstractRadioButton = new JRadioButton("Abstract");
        noneRadioButton = new JRadioButton("None");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(staticRadioButton);
        buttonGroup.add(abstractRadioButton);
        buttonGroup.add(noneRadioButton);

        noneRadioButton.setSelected(true);

        className.setBounds(5, 5, 190, 20);
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
        add(className);
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

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init(listModel, list);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int level = accessLevelComboBox.getSelectedIndex();
                AccessLevel accessLevel = null;
                if (level == 0) {
                    accessLevel = AccessLevel.PUBLIC;
                } else if (level == 1) {
                    accessLevel = AccessLevel.PRIVATE;
                } else if (level == 2) {
                    accessLevel = AccessLevel.PROTECTED;
                } else {
                    accessLevel = AccessLevel.PACKAGE;
                }

                String name = nameTextField.getText();
                String type = typeTextField.getText();

                boolean checkType = NameVerification.checkWithRegExp(type) && ReservedNames.check(type);
                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);

                if (checkName && checkType) {
                    if (element.getMethodName().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ClassDiagramsEditor.getInstance().saveState();
                        ClassMethod classMethod = new ClassMethod(accessLevel, name, staticRadioButton.isSelected(), type,
                                abstractRadioButton.isSelected(), list);

                        model.addElement(classMethod.toString());
                        element.getMethodName().add(name);
                        element.addMethod(classMethod);
                        element.updateElement();
                        init(listModel, list);
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
                    element.updateElement();
            }
        });

        listParameters.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!listParameters.isSelectionEmpty()){
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
                int inedex = listParameters.getSelectedIndex();
                int response = JOptionPane.showConfirmDialog(ClassDiagramsEditor.getInstance(), "Are you sure?");

                if (response == JOptionPane.YES_OPTION) {
                    listModel.remove(inedex);
                    list.remove(inedex);
                    element.updateElement();
                }

            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = listParameters.getSelectedIndex();
                changeDialog = new ChangeParameter(list.get(index), element, index, listModel, true, null, list);
                changeDialog.setVisible(true);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                init(listModel, list);
            }
        });
    }

    private void init(DefaultListModel model, List<Parameters> list) {
        model.clear();
        list.clear();
        nameTextField.setText("");
        accessLevelComboBox.setSelectedIndex(0);
        typeTextField.setText("");
        noneRadioButton.setSelected(true);
        dispose();
    }

    public void setClassName() {
        className.setText(element.getName());
    }

}
