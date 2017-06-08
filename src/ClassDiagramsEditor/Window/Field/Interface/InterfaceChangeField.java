package ClassDiagramsEditor.Window.Field.Interface;


import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Interface.InterfaceField;
import ClassDiagramsEditor.NameVerification;
import ClassDiagramsEditor.ReservedNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceChangeField extends JDialog {
    private InterfaceField field;
    private JTextField nameTextField;
    private JTextField initTextField;
    private JTextField typeTextField;

    public InterfaceChangeField(InterfaceField field, String name, int index, DefaultListModel listModel, Interface element) {
        super(ClassDiagramsEditor.getInstance(), "Change field", true);
        this.field = field;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 190));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        JLabel interfaceName = new JLabel(name);
        interfaceName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel typeLabel = new JLabel("Type");
        JLabel initLabel = new JLabel("Initial value");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(20);
        typeTextField = new JTextField(20);
        initTextField = new JTextField(20);

        nameTextField.setText(field.getName());
        typeTextField.setText(field.getType());
        initTextField.setText(field.getInitializes());

        interfaceName.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);

        typeLabel.setBounds(10, 55, 80, 20);
        typeTextField.setBounds(90, 55, 100, 20);

        initLabel.setBounds(10, 80, 80, 20);
        initTextField.setBounds(90, 80, 100, 20);

        saveButton.setBounds(15, 115, 80, 30);
        cancelButton.setBounds(100, 115, 80, 30);

        add(interfaceName);
        add(nameLabel);
        add(nameTextField);
        add(typeLabel);
        add(typeTextField);
        add(initLabel);
        add(initTextField);
        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String type = typeTextField.getText();
                String init = initTextField.getText();
                if (init.length() == 0) {
                    init = "null";
                }

                boolean checkType = NameVerification.checkWithRegExp(type) && ReservedNames.check(type);
                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);

                if (checkName && checkType) {
                    if (element.getFieldName().contains(name) && !name.equals(field.getName())) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean check = name.equals(field.getName()) && type.equals(field.getType()) && init.equals(field.getInitializes());
                        if (!check) {
                            element.getFieldName().add(name);
                            element.getFieldName().remove(field.getName());
                            ClassDiagramsEditor.getInstance().saveState();
                        } else {
                            dispose();
                            return;
                        }
                        field.setName(name);
                        field.setType(type);

                        field.setInitializes(init);
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
