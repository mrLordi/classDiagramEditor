package ClassDiagramsEditor.Window.Field.Class;

import ClassDiagramsEditor.AccessLevel;
import ClassDiagramsEditor.Class.*;
import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeClassField extends JDialog {
    private ClassField field;
    private JTextField nameTextField;
    private JComboBox accessLevelComboBox;
    private JTextField initTextField;
    private JTextField typeTextField;
    private JCheckBox staticCheckBox;
    private JCheckBox finaCheckBox;

    public ChangeClassField(ClassField field, String name, int index, DefaultListModel listModel, Class element) {
        super(ClassDiagramsEditor.getInstance(), "Change field", true);
        this.field = field;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 250));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        JLabel nameClassLabel = new JLabel(name);
        nameClassLabel.setHorizontalAlignment(SwingConstants.CENTER);

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

        nameTextField.setText(field.getName());
        AccessLevel level = field.getAccessLevel();
        if (level == AccessLevel.PUBLIC) {
            accessLevelComboBox.setSelectedIndex(0);
        } else if (level == AccessLevel.PRIVATE) {
            accessLevelComboBox.setSelectedIndex(1);
        } else if (level == AccessLevel.PROTECTED) {
            accessLevelComboBox.setSelectedIndex(2);
        } else {
            accessLevelComboBox.setSelectedIndex(3);
        }
        typeTextField.setText(field.getType());
        initTextField.setText(field.getInitializes());
        staticCheckBox.setSelected(field.isStatic());
        finaCheckBox.setSelected(field.isFinal());

        nameClassLabel.setBounds(5, 5, 190, 20);
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

        add(nameClassLabel);
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
                String init = initTextField.getText();
                boolean st = staticCheckBox.isSelected();
                boolean fn = finaCheckBox.isSelected();

                boolean checkType = NameVerification.checkWithRegExp(type) && ReservedNames.check(type);
                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);

                if (checkName && checkType) {
                    if (element.getFieldName().contains(name) && !name.equals(field.getName())) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean check = name.equals(field.getName()) && type.equals(field.getType()) && init.equals(field.getInitializes())
                                        && st == field.isStatic() && fn == field.isFinal();
                        if (!check) {
                            ClassDiagramsEditor.getInstance().saveState();
                            element.getFieldName().add(name);
                            element.getFieldName().remove(field.getName());
                        } else {
                            dispose();
                            return;
                        }

                        field.setType(type);
                        field.setName(name);
                        field.setAccessLevel(accessLevel);
                        field.setInitializes(init);
                        field.setStatic(st);
                        field.setFinal(fn);

                        listModel.setElementAt(field.toString(), index);
                        element.updateField(index);
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

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }
}
