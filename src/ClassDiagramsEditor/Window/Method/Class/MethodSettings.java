package ClassDiagramsEditor.Window.Method.Class;

import ClassDiagramsEditor.Class.*;
import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MethodSettings extends JDialog {
    private ChangeClassMethod changeDialog;
    private AddClassMethod addDialog;
    private Class element;
    private JList methodsList;
    private JLabel className;

    public MethodSettings(Class element) {
        super(ClassDiagramsEditor.getInstance(), "Method setting", true);
        this.element = element;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(310, 200));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        className = new JLabel(element.getName());
        className.setHorizontalAlignment(SwingConstants.CENTER);
        JButton addButton = new JButton("Add new");
        JButton deleteButton = new JButton("Delete");
        JButton propertyButton = new JButton("Change");

        DefaultListModel listModel = new DefaultListModel();
        List<ClassMethod> list = element.getMethods();
        for (ClassMethod method : list) {
            listModel.addElement(method.toString());
        }
        methodsList = new JList(listModel);
        methodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        addDialog = new AddClassMethod(element, listModel);

        JScrollPane scrollPane = new JScrollPane(methodsList);

        className.setBounds(5, 5, 300, 20);
        scrollPane.setBounds(10, 30, 290, 100);
        addButton.setBounds(10, 140, 90, 20);
        propertyButton.setBounds(110, 140, 90, 20);
        deleteButton.setBounds(210, 140, 90, 20);
        deleteButton.setEnabled(false);
        propertyButton.setEnabled(false);

        add(className);
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
                addDialog.setClassName();
                addDialog.setVisible(true);
                element.updateElement();
            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = methodsList.getSelectedIndex();
                changeDialog = new ChangeClassMethod(element.getMethods().get(index), index, listModel, element);
                changeDialog.setVisible(true);
                element.updateElement();
            }
        });
    }

    public void setClassName() {
        className.setText(element.getName());
    }
}
