package ClassDiagramsEditor.Window.Field.Interface;

import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Interface.InterfaceField;
import ClassDiagramsEditor.Window.Field.Interface.InterfaceAddField;
import ClassDiagramsEditor.Window.Field.Interface.InterfaceChangeField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfaceFieldSettings extends JDialog {
    private InterfaceAddField addDialog;
    private InterfaceChangeField changeDialog;
    private Interface element;
    private JList fields;
    private JLabel interfaceName;

    public InterfaceFieldSettings(Interface element) {
        super(ClassDiagramsEditor.getInstance(), "Interface field setting", true);
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
        List<InterfaceField> list = element.getFields();
        for (InterfaceField field : list) {
            listModel.addElement(field.toString());
        }
        fields = new JList(listModel);
        fields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addDialog = new InterfaceAddField(element, listModel);

        JScrollPane scrollPane = new JScrollPane(fields);

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

        fields.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!fields.isSelectionEmpty()){
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
                int inedex = fields.getSelectedIndex();
                int response = JOptionPane.showConfirmDialog(ClassDiagramsEditor.getInstance(), "Are you sure?");

                if (response == JOptionPane.YES_OPTION) {
                    listModel.remove(inedex);
                    element.deleteField(inedex);
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
                int index = fields.getSelectedIndex();
                changeDialog = new InterfaceChangeField(element.getFields().get(index), element.getName(), index, listModel, element);
                changeDialog.setVisible(true);
            }
        });

    }
    public void setInterfaceName() {
        interfaceName.setText(element.getName());
    }
}
