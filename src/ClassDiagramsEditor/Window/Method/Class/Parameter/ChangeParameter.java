package ClassDiagramsEditor.Window.Method.Class.Parameter;

import ClassDiagramsEditor.*;
import ClassDiagramsEditor.Class.Class;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChangeParameter extends JDialog {
    private JTextField nameTextField;
    private JTextField typeTextField;
    private JCheckBox finalCheckBox;

    public ChangeParameter(Parameters parameter, Class parent, int index, DefaultListModel model, boolean add, List<Parameters> tempList, List<Parameters> list) {
        super(ClassDiagramsEditor.getInstance(), "Change parameter", true);

        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 180));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        JLabel nameClassLabel = new JLabel(parent.getName());
        nameClassLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel typeLabel = new JLabel("Type");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(parameter.getName());
        typeTextField = new JTextField( parameter.getType(), 20);
        finalCheckBox = new JCheckBox("Final", parameter.isFinal());

        nameClassLabel.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);
        typeLabel.setBounds(10, 55, 80, 20);
        typeTextField.setBounds(90, 55, 100, 20);
        finalCheckBox.setBounds(5, 85, 100, 20);
        saveButton.setBounds(15, 110, 80, 30);
        cancelButton.setBounds(100, 110, 80, 30);

        add(nameClassLabel);
        add(nameLabel);
        add(nameTextField);
        add(typeLabel);
        add(typeTextField);
        add(finalCheckBox);
        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String type = typeTextField.getText();
                boolean fn = finalCheckBox.isSelected();

                boolean checkType = NameVerification.checkWithRegExp(type) && ReservedNames.check(type);
                boolean checkName = NameVerification.checkWithRegExp(name)&& ReservedNames.check(name);
                boolean check = false;
                if (checkName && checkType) {
                    for (Parameters param : list) {
                        if (param.getName().equals(name)) {
                            check = true;
                        }
                    }
                    if (check) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        check = name.equals(parameter.getName()) && type.equals(parameter.getType())&& fn == parameter.isFinal();
                        if (!check) {
                            ClassDiagramsEditor.getInstance().saveState();
                        } else {
                            dispose();
                            return;
                        }
                        if (!add) {
                            Parameters temp = new Parameters(name, type, finalCheckBox.isSelected());
                            tempList.add(temp);
                            model.setElementAt(temp.toString(), index);
                        } else {
                            parameter.setName(name);
                            parameter.setType(type);
                            parameter.setFinal(finalCheckBox.isSelected());
                            model.setElementAt(parameter.toString(), index);
                        }

                        parent.updateElement();
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
