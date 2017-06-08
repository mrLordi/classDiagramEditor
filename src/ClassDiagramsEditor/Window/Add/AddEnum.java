package ClassDiagramsEditor.Window.Add;


import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.DiagramPanel;
import ClassDiagramsEditor.Enum.*;
import ClassDiagramsEditor.Enum.Enum;
import ClassDiagramsEditor.Strategy;
import ClassDiagramsEditor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEnum extends JDialog implements Strategy{
    private JTextField nameTextField;

    public AddEnum(int x, int y) {
        super(ClassDiagramsEditor.getInstance(), "New enum", true);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 120));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());
        DiagramPanel panel = DiagramPanel.getInstance();

        JLabel nameLabel = new JLabel("Name");
        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(20);

        nameLabel.setBounds(10, 10, 80, 20);
        nameTextField.setBounds(90, 10, 100, 20);
        createButton.setBounds(15, 45, 80, 30);
        cancelButton.setBounds(100, 45, 80, 30);

        add(nameLabel);
        add(nameTextField);
        add(createButton);
        add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = nameTextField.getText();
                if (NameVerification.checkWithRegExp(name) && ReservedNames.check(name)) {
                    if (panel.getNameList().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Enum temp = new Enum(name, x - 1, y - 1);
                        panel.add(temp);
                        panel.getEnumList().add(temp);
                        panel.getNameList().add(name);
                        ClassDiagramsEditor.getInstance().revalidate();
                        ClassDiagramsEditor.getInstance().repaint();
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
    }

    @Override
    public void addElement() {
        this.setVisible(true);
    }
}

