package ClassDiagramsEditor.Window.Method.Interface;

import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Interface.InterfaceMethod;
import ClassDiagramsEditor.Window.Method.Interface.InterfaceAddMethod;
import ClassDiagramsEditor.Window.Method.Interface.InterfaceChangeMethod;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by 1 on 08.05.2016.
 */
public class InterfaceMethodSettings extends JDialog {
    private InterfaceChangeMethod changeDialog;
    private InterfaceAddMethod addDialog;
    private Interface element;
    private JList methodsList;
    private JLabel interfaceName;

    public InterfaceMethodSettings(Interface element) {
        super(ClassDiagramsEditor.getInstance(), "Method setting", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(310, 200));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        interfaceName = new JLabel(element.getName());
        interfaceName.setHorizontalAlignment(SwingConstants.CENTER);
        JButton addButton = new JButton("Add new");
        JButton deleteButton = new JButton("Delete");
        JButton propertyButton = new JButton("Change");

        DefaultListModel listModel = new DefaultListModel();
        List<InterfaceMethod> list = element.getMethods();
        for (InterfaceMethod method : list) {
            listModel.addElement(method.toString());
        }
        methodsList = new JList(listModel);
        methodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addDialog = new InterfaceAddMethod(element, listModel);

        JScrollPane scrollPane = new JScrollPane(methodsList);

        interfaceName.setBounds(5, 5, 300, 20);
        scrollPane.setBounds(10, 30, 290, 100);
        addButton.setBounds(10, 140, 90, 20);
        propertyButton.setBounds(110, 140, 90, 20);
        deleteButton.setBounds(210, 140, 90, 20);
        deleteButton.setEnabled(false);
        propertyButton.setEnabled(false);

        add(interfaceName);
        add(scrollPane);
        add(addButton);
        add(propertyButton);
        add(deleteButton);

        methodsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!methodsList.isSelectionEmpty()) {
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
                int inedex = methodsList.getSelectedIndex();
                int response = JOptionPane.showConfirmDialog(ClassDiagramsEditor.getInstance(), "Are you sure?");

                if (response == JOptionPane.YES_OPTION) {
                    listModel.remove(inedex);
                    element.deleteMethod(inedex);
                    element.updateElement();
                }

            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDialog.setInterfaceName();
                addDialog.setVisible(true);
                element.updateElement();
            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = methodsList.getSelectedIndex();
                changeDialog = new InterfaceChangeMethod(ClassDiagramsEditor.getInstance(), element.getMethods().get(index), index, listModel, element);
                changeDialog.setVisible(true);
                element.updateElement();
            }
        });
    }

    public void setInterfaceName() {
        interfaceName.setText(element.getName());
    }
}
