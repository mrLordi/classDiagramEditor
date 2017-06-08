package ClassDiagramsEditor.Window.Literal;

import ClassDiagramsEditor.ClassDiagramsEditor;
import ClassDiagramsEditor.Enum.Enum;
import ClassDiagramsEditor.Window.Literal.EnumAddLiteral;
import ClassDiagramsEditor.Window.Literal.EnumChangeLiteral;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnumLiteralSettings extends JDialog {
    private EnumAddLiteral addDialog;
    private EnumChangeLiteral changeDialog;
    private Enum parent;
    private JList literals;
    private JLabel enumName;

    public EnumLiteralSettings(Enum parent) {
        super(ClassDiagramsEditor.getInstance(), "Literal setting", true);
        this.parent = parent;
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(310, 200));
        setLocationRelativeTo(ClassDiagramsEditor.getInstance());

        enumName = new JLabel(parent.getName());
        enumName.setHorizontalAlignment(SwingConstants.CENTER);
        JButton addButton = new JButton("Add new");
        JButton deleteButton = new JButton("Delete");
        JButton propertyButton = new JButton("Change");

        DefaultListModel listModel = new DefaultListModel();
        java.util.List<String> list = parent.getLiterals();
        for (String liteal : list) {
            listModel.addElement(liteal);
        }
        literals = new JList(listModel);
        literals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addDialog = new EnumAddLiteral(parent, listModel);

        JScrollPane scrollPane = new JScrollPane(literals);

        enumName.setBounds(5, 5, 300, 20);
        scrollPane.setBounds(10, 30, 290, 100);
        addButton.setBounds(10, 140, 90, 20);
        propertyButton.setBounds(110, 140, 90, 20);
        deleteButton.setBounds(210, 140, 90, 20);
        deleteButton.setEnabled(false);
        propertyButton.setEnabled(false);

        add(enumName);
        add(scrollPane);
        add(addButton);
        add(propertyButton);
        add(deleteButton);

        literals.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!literals.isSelectionEmpty()){
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
                int inedex = literals.getSelectedIndex();
                int response = JOptionPane.showConfirmDialog(ClassDiagramsEditor.getInstance(), "Are you sure?");

                if (response == JOptionPane.YES_OPTION) {
                    listModel.remove(inedex);
                    parent.deleteLiteral(inedex);
                    parent.updateElement();
                }

            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDialog.setEnumName();
                addDialog.setVisible(true);
                parent.updateElement();
            }
        });

        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int inedex = literals.getSelectedIndex();
                changeDialog = new EnumChangeLiteral(parent.getLiterals().get(inedex), parent.getName(),
                        inedex, listModel, parent);
                changeDialog.setVisible(true);
            }
        });

    }
    public void setEnumName() {
        enumName.setText(parent.getName());
    }
}

