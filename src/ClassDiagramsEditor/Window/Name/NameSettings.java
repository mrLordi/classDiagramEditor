package ClassDiagramsEditor.Window.Name;

import ClassDiagramsEditor.AccessLevel;
import ClassDiagramsEditor.Class.*;
import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NameSettings extends JDialog {
    private Class element;
    private JTextField nameTextField;
    private JComboBox accessLevelComboBox;
    private JRadioButton finalRadioButton;
    private JRadioButton abstractRadioButton;
    private JRadioButton noneRadioButton;

    public NameSettings(Class element) {
        super(ClassDiagramsEditor.getInstance(), "Class name setting", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(200, 180));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        JLabel nameLabel = new JLabel("Class name");
        JLabel accessLevelLabel = new JLabel("Access level");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");


        String[] access = {"public", "private", "protected", "package"};
        accessLevelComboBox = new JComboBox(access);

        nameTextField = new JTextField(20);
        finalRadioButton = new JRadioButton("final");
        abstractRadioButton = new JRadioButton("abstract");
        noneRadioButton = new JRadioButton("none");

        nameLabel.setBounds(10, 10, 80, 20);
        nameTextField.setBounds(90, 10, 100, 20);

        accessLevelLabel.setBounds(10, 35, 80, 20);
        accessLevelComboBox.setBounds(90, 35, 100, 20);

        finalRadioButton.setBounds(5, 60, 100, 20);
        abstractRadioButton.setBounds(5, 75, 100, 20);
        noneRadioButton.setBounds(5, 90, 100, 20);

        saveButton.setBounds(15, 115, 80, 30);
        cancelButton.setBounds(100, 115, 80, 30);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(finalRadioButton);
        buttonGroup.add(abstractRadioButton);
        buttonGroup.add(noneRadioButton);

        init();

        add(nameLabel);
        add(nameTextField);
        add(accessLevelLabel);
        add(accessLevelComboBox);
        add(finalRadioButton);
        add(abstractRadioButton);
        add(noneRadioButton);
        add(saveButton);
        add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
                dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                boolean fi = finalRadioButton.isSelected();
                boolean ab = abstractRadioButton.isSelected();
                int level = accessLevelComboBox.getSelectedIndex();
                AccessLevel accessLevel = null;
                if (level == 0) {
                    accessLevel = AccessLevel.PUBLIC;
                } else if (level == 1) {
                    accessLevel = AccessLevel.PRIVATE;
                } else if (level == 2) {
                    accessLevel = AccessLevel.PROTECTED;
                } else {
                    accessLevel = AccessLevel.PACKAGE;;
                }

                if (NameVerification.checkWithRegExp(name) && ReservedNames.check(name)) {
                    if (element.getPanel().getNameList().contains(name) && !name.equals(element.getName())) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean check = name.equals(element.getName()) && fi == element.isFinal() && ab == element.isAbstract()&&
                                accessLevel == element.getAccessLevel();
                        if (!check) {
                            ClassDiagramsEditor.getInstance().saveState();
                            element.getPanel().getNameList().add(name);
                            element.getPanel().getNameList().remove(element.getName());
                        } else {
                            init();
                            dispose();
                        }
                        if (noneRadioButton.isSelected()) {
                            element.setFinal(false);
                            element.setAbstract(false);
                        } else if (abstractRadioButton.isSelected()) {
                            element.setAbstract(true);
                        } else if (finalRadioButton.isSelected()) {
                            element.setFinal(true);
                        }

                        element.setAccessLevel(accessLevel);
                        element.setName(name);
                        element.updateNameLabel();
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

    private void init() {
        nameTextField.setText(element.getName());
        finalRadioButton.setSelected(element.isFinal());
        abstractRadioButton.setSelected(element.isAbstract());

        if (!element.isFinal() && !element.isAbstract()) {
            noneRadioButton.setSelected(true);
        }

        AccessLevel level = element.getAccessLevel();
        if (level == AccessLevel.PUBLIC) {
            accessLevelComboBox.setSelectedIndex(0);
        } else if (level == AccessLevel.PRIVATE) {
            accessLevelComboBox.setSelectedIndex(1);
        } else if (level == AccessLevel.PROTECTED) {
            accessLevelComboBox.setSelectedIndex(2);
        } else {
            accessLevelComboBox.setSelectedIndex(3);
        }

    }
}
