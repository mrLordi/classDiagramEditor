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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EnumAddLiteral extends JDialog {
    private Enum element;
    private JTextField nameTextField;
    private JLabel enumNameLabel;

    public EnumAddLiteral(Enum element, DefaultListModel listModele) {
        super(ClassDiagramsEditor.getInstance(), "Add new field", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 130));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        enumNameLabel = new JLabel(element.getName());
        enumNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(20);

        enumNameLabel.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);

        saveButton.setBounds(15, 60, 80, 30);
        cancelButton.setBounds(100, 60, 80, 30);

        add(enumNameLabel);
        add(nameLabel);
        add(nameTextField);
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
                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);

                if (checkName) {
                    if (element.getLiterals().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ClassDiagramsEditor.getInstance().saveState();
                        listModele.addElement(name);
                        element.addLiteral(name);
                        init();
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
                init();
            }
        });
    }

    private void init() {
        nameTextField.setText("");
        dispose();
    }

    public void setEnumName() {
        enumNameLabel.setText(element.getName());
    }
}

