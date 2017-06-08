package ClassDiagramsEditor.Window.Method.Interface;

import ClassDiagramsEditor.*;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Interface.InterfaceMethod;
import ClassDiagramsEditor.Window.Method.Interface.Parameters.InterfaceAddParameter;
import ClassDiagramsEditor.Window.Method.Interface.Parameters.InterfaceChangeParameter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class InterfaceAddMethod extends JDialog {
    private InterfaceChangeParameter changeDialog;
    private InterfaceAddParameter parameterDialog;
    private Interface element;
    private JTextField nameTextField;
    private JTextField typeTextField;
    private JList listParameters;
    private JLabel interfaceName;

    public InterfaceAddMethod(Interface element, DefaultListModel model) {
        super(ClassDiagramsEditor.getInstance(), "Add method", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(510, 260));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        interfaceName = new JLabel(element.getName());
        interfaceName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel typeLabel = new JLabel("Type");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Add new");
        JButton deleteButton = new JButton("Delete");
        JButton propertyButton = new JButton("Change");
        JLabel parameters = new JLabel("Parameters:");
        parameters.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultListModel listModel = new DefaultListModel();
        java.util.List<Parameters> list = new LinkedList<>();
        listParameters = new JList(listModel);
        listParameters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listParameters);

        parameterDialog = new InterfaceAddParameter(element, listModel, list, true, null);

        nameTextField = new JTextField(20);
        typeTextField = new JTextField(20);

        interfaceName.setBounds(5, 5, 190, 20);
        nameLabel.setBounds(10, 30, 80, 20);
        nameTextField.setBounds(90, 30, 100, 20);
        typeLabel.setBounds(10, 55, 80, 20);
        typeTextField.setBounds(90, 55, 100, 20);
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
        add(interfaceName);
        add(nameLabel);
        add(nameTextField);
        add(typeLabel);
        add(typeTextField);
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
                String name = nameTextField.getText();
                String type = typeTextField.getText();

                boolean checkType = NameVerification.checkWithRegExp(type) && ReservedNames.check(type);
                boolean checkName = NameVerification.checkWithRegExp(name) && ReservedNames.check(name);

                if (checkName && checkType) {
                    if (element.getMethodName().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ClassDiagramsEditor.getInstance().saveState();
                        InterfaceMethod method = new InterfaceMethod(name, type, list);

                        model.addElement(method.toString());
                        element.addMethod(method);
                        element.getMethodName().add(name);
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
                int index = listParameters.getSelectedIndex();
                int response = JOptionPane.showConfirmDialog(ClassDiagramsEditor.getInstance(), "Are you sure?");

                if (response == JOptionPane.YES_OPTION) {
                    listModel.remove(index);
                    list.remove(index);
                    element.updateElement();
                }

            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = listParameters.getSelectedIndex();
                changeDialog = new InterfaceChangeParameter(list.get(index), element, index, listModel, true, null, list);

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

    private void init(DefaultListModel model, java.util.List<Parameters> list) {
        model.clear();
        list.clear();
        nameTextField.setText("");
        typeTextField.setText("");
        dispose();
    }

    public void setInterfaceName() {
        interfaceName.setText(element.getName());
    }

}
