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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InterfaceAddField extends JDialog{
    private Interface element;
    private JTextField nameTextField;
    private JTextField initTextField;
    private JTextField typeTextField;
    private JLabel interfaceNameLabel;

    public InterfaceAddField(Interface element, DefaultListModel listModele) {
        super(ClassDiagramsEditor.getInstance(), "Add new field", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 190));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        interfaceNameLabel = new JLabel(element.getName());
        interfaceNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel typeLabel = new JLabel("Type");
        JLabel initLabel = new JLabel("Initial value");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        nameTextField = new JTextField(20);
        typeTextField = new JTextField(20);
        initTextField = new JTextField(20);

        interfaceNameLabel.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);

        typeLabel.setBounds(10, 55, 80, 20);
        typeTextField.setBounds(90, 55, 100, 20);

        initLabel.setBounds(10, 80, 80, 20);
        initTextField.setBounds(90, 80, 100, 20);

        saveButton.setBounds(15, 115, 80, 30);
        cancelButton.setBounds(100, 115, 80, 30);

        add(interfaceNameLabel);
        add(nameLabel);
        add(nameTextField);
        add(typeLabel);
        add(typeTextField);
        add(initLabel);
        add(initTextField);
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

                        String init = initTextField.getText();
                        if (init.length() == 0) {
                            init = "null";
                        }
                        InterfaceField field = new InterfaceField(name, type, init);

                        listModele.addElement(field.toString());
                        element.getFieldName().add(name);
                        element.addField(field);
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
        typeTextField.setText("");
        initTextField.setText("");
        dispose();
    }

    public void setInterfaceName() {
        interfaceNameLabel.setText(element.getName());
    }
}
