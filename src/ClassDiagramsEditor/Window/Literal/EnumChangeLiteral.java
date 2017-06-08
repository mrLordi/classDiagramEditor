package ClassDiagramsEditor.Window.Literal;

import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.Enum.*;
import ClassDiagramsEditor.Enum.Enum;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Interface.InterfaceField;
import ClassDiagramsEditor.NameVerification;
import ClassDiagramsEditor.ReservedNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnumChangeLiteral extends JDialog {
    private String literal;
    private JTextField nameTextField;

    public EnumChangeLiteral(String lit, String name, int index, DefaultListModel listModel, Enum element) {
        super(ClassDiagramsEditor.getInstance(), "Change field", true);
        literal = lit;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 130));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        JLabel enumName = new JLabel(name);
        enumName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(20);

        nameTextField.setText(literal);

        enumName.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);

        saveButton.setBounds(15, 60, 80, 30);
        cancelButton.setBounds(100, 60, 80, 30);

        add(enumName);
        add(nameLabel);
        add(nameTextField);
        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                boolean checkName = NameVerification.checkWithRegExp(name)&& ReservedNames.check(name);
                if (checkName) {
                    if (element.getLiterals().contains(name) && !name.equals(literal)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (!name.equals(literal)) {
                            ClassDiagramsEditor.getInstance().saveState();
                            element.getLiterals().add(name);
                            element.getLiterals().remove(literal);
                        } else {
                            dispose();
                            return;
                        }
                        literal = name;
                        element.getLiterals().set(index, literal);

                        listModel.setElementAt(literal, index);
                        element.updateLiteral(index);
                        dispose();
                    }
                } else {
                    if (name.length() == 0) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Enter a name!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Incorrect name!", "ERROR", JOptionPane.ERROR_MESSAGE);
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

