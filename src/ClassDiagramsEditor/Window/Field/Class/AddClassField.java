package ClassDiagramsEditor.Window.Field.Class;

import ClassDiagramsEditor.AccessLevel;
import ClassDiagramsEditor.Class.*;
import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.NameVerification;
import ClassDiagramsEditor.ReservedNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AddClassField extends JDialog {
    private Class element;
    private JTextField nameTextField;
    private JComboBox accessLevelComboBox;
    private JTextField initTextField;
    private JTextField typeTextField;
    private JCheckBox staticCheckBox;
    private JCheckBox finaCheckBox;
    private JLabel className;

    public AddClassField(Class element, DefaultListModel listModel) {
        super(ClassDiagramsEditor.getInstance(), "Add new field", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 250));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        className = new JLabel(element.getName());
        className.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel accessLevelLabel = new JLabel("Access level");
        JLabel typeLabel = new JLabel("Type");
        JLabel initLabel = new JLabel("Initial value");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(20);
        String[] access = {"public", "private", "protected", "package"};
        accessLevelComboBox = new JComboBox(access);
        typeTextField = new JTextField(20);
        staticCheckBox = new JCheckBox("Static");
        finaCheckBox = new JCheckBox("Final");
        initTextField = new JTextField(20);

        className.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);

        accessLevelLabel.setBounds(10, 55, 80, 20);
        accessLevelComboBox.setBounds(90, 55, 100, 20);

        typeLabel.setBounds(10, 80, 80, 20);
        typeTextField.setBounds(90, 80, 100, 20);

        initLabel.setBounds(10, 105, 80, 20);
        initTextField.setBounds(90, 105, 100, 20);

        staticCheckBox.setBounds(5, 135, 100, 20);
        finaCheckBox.setBounds(5, 160, 100, 20);

        saveButton.setBounds(15, 185, 80, 30);
        cancelButton.setBounds(100, 185, 80, 30);

        add(className);
        add(nameLabel);
        add(nameTextField);
        add(accessLevelLabel);
        add(accessLevelComboBox);
        add(typeLabel);
        add(typeTextField);
        add(initLabel);
        add(initTextField);
        add(staticCheckBox);
        add(finaCheckBox);
        add(saveButton);
        add(cancelButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                init();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String type = typeTextField.getText();

                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);
                boolean checkType = ReservedNames.check(type) && NameVerification.checkWithRegExp(type);
                if (checkName && checkType) {
                    if (element.getFieldName().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ClassDiagramsEditor.getInstance().saveState();
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

                        String init = initTextField.getText();
                        if (init.length() == 0) {
                            init = null;
                        }
                        ClassField classField = new ClassField(accessLevel, name, staticCheckBox.isSelected(), type,
                                finaCheckBox.isSelected(), init);
                        element.getFieldName().add(name);
                        listModel.addElement(classField.toString());
                        element.addField(classField);
                        init();
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

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });
    }

    private void init() {
        nameTextField.setText("");
        accessLevelComboBox.setSelectedIndex(0);
        typeTextField.setText("");
        staticCheckBox.setSelected(false);
        finaCheckBox.setSelected(false);
        initTextField.setText("");
        dispose();
    }

    public void setClassName() {
        className.setText(element.getName());
    }

}
