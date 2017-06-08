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
import java.util.List;

public class InterfaceChangeMethod extends JDialog {
    private InterfaceChangeParameter changeDialog;
    private InterfaceAddParameter parameterDialog;
    private JTextField nameTextField;
    private JTextField typeTextField;
    private JList listParameters;

    public InterfaceChangeMethod(JFrame frame, InterfaceMethod method, int parentIndex, DefaultListModel model, Interface element) {
        super(frame, "Change method", true);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(510, 260));
        setLocationRelativeTo(frame);

        JLabel nameInterfaceLabel = new JLabel(element.getName());
        nameInterfaceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JLabel typeLabel = new JLabel("Type");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Add new");
        JButton deleteButton = new JButton("Delete");
        JButton propertyButton = new JButton("Change");
        JLabel parameters = new JLabel("Parameters:");
        parameters.setHorizontalAlignment(SwingConstants.CENTER);

        List<Parameters> tempList = new LinkedList<>();
        List<Parameters> list = method.getParameters();
        DefaultListModel listModel = new DefaultListModel();
        for (Parameters param : list) {
            listModel.addElement(param.toString());
            tempList.add(new Parameters(param.getName(), param.getType(), param.isFinal()));
        }
        listParameters = new JList(listModel);

        listParameters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listParameters);
        parameterDialog = new InterfaceAddParameter(element, listModel, list, false, tempList);

        nameTextField = new JTextField(method.getName(), 20);
        typeTextField = new JTextField(method.getType(), 20);

        nameInterfaceLabel.setBounds(5, 5, 190, 20);
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
        add(nameInterfaceLabel);
        add(nameLabel);
        add(nameTextField);
        add(typeLabel);
        add(typeTextField);
        add(saveButton);
        add(cancelButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                for (Parameters param : list) {
                    listModel.addElement(param.toString());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                for (Parameters param : list) {
                    listModel.addElement(param.toString());
                }
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
                    if (element.getMethods().contains(name)) {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "The name must be unique!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean check = name.equals(method.getName()) && type.equals(method.getType());
                        if (!check) {
                            ClassDiagramsEditor.getInstance().saveState();
                            element.getMethodName().add(name);
                            element.getMethods().remove(method.getName());
                        }
                        method.setName(name);
                        method.setType(type);
                        method.setParameters(tempList);

                        model.setElementAt(method.toString(), parentIndex);
                        element.updateMethod(parentIndex);
                        element.updateElement();
                        dispose();
                    }
                } else {
                    if (!checkName) {
                        if (name.length() == 0) {
                            JOptionPane.showMessageDialog(frame, "Enter a name!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Incorrect name!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        if (type.length() == 0) {
                            JOptionPane.showMessageDialog(frame, "Enter a type!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Incorrect type!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parameterDialog.setVisible(true);
            }
        });

        listParameters.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!listParameters.isSelectionEmpty()) {
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

                listModel.remove(index);
                tempList.remove(index);
            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = listParameters.getSelectedIndex();
                changeDialog = new InterfaceChangeParameter(list.get(index), element, index, listModel, false, tempList, list);

                changeDialog.setVisible(true);
            }
        });
    }
}
