package ClassDiagramsEditor.Window.Field.Class;

import ClassDiagramsEditor.Class.*;
import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.Window.Field.Class.AddClassField;
import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.Window.Field.Class.ChangeClassField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FieldSettings extends JDialog {
    private AddClassField addDialog;
    private ChangeClassField changeDialog;
    private Class element;
    private JList fields;
    private JLabel className;

    public FieldSettings(Class element) {

        super(ClassDiagramsEditor.getInstance(), "Class field setting", true);
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
        List<ClassField> list = element.getFields();
        for (ClassField field : list) {
            listModel.addElement(field.toString());
        }
        fields = new JList(listModel);
        fields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addDialog = new AddClassField(element, listModel);

        JScrollPane scrollPane = new JScrollPane(fields);

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
                int index = fields.getSelectedIndex();
                int response = JOptionPane.showConfirmDialog(ClassDiagramsEditor.getInstance(), "Are you sure?");

                if (response == JOptionPane.YES_OPTION) {
                    listModel.remove(index);
                    element.deleteField(index);
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
                int index = fields.getSelectedIndex();
                changeDialog = new ChangeClassField(element.getFields().get(index), element.getName(), index, listModel, element);
                changeDialog.setVisible(true);
            }
        });

    }

    public void setClassName() {
        className.setText(element.getName());
    }
}
