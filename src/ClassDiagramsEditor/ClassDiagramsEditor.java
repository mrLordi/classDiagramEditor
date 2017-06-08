package ClassDiagramsEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ClassDiagramsEditor extends JFrame{
    private static ClassDiagramsEditor frame;
    private int create = 0;
    private boolean newLine = false;
    private int lineType = 0;
    private Stack<Memento> undo;
    private Stack<Memento> redo;
    private boolean from;
    private DiagramPanel panel;

    private ClassDiagramsEditor() {
        super("Class diagrams editor");
        undo = new Stack<>();
        redo = new Stack<>();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(Color.WHITE);
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = DiagramPanel.getInstance();
        getContentPane().setBackground(Color.WHITE);
        add(panel);

        JToolBar elementToolBar = new JToolBar("new element", SwingConstants.VERTICAL);
        elementToolBar.setFloatable(false);

        JButton addClassButton = new JButton(new ImageIcon(".//resources//с.png"));
        addClassButton.setBackground(Color.WHITE);
        addClassButton.setMaximumSize(new Dimension(55, 55));
        JButton addInterfaceButton = new JButton(new ImageIcon(".//resources//i.png"));
        addInterfaceButton.setBackground(Color.WHITE);
        addInterfaceButton.setMaximumSize(new Dimension(55, 55));
        JButton addEnumButton = new JButton(new ImageIcon(".//resources//e.png"));
        addEnumButton.setBackground(Color.WHITE);
        addEnumButton.setMaximumSize(new Dimension(55, 55));

        elementToolBar.add(addClassButton);
        elementToolBar.add(addInterfaceButton);
        elementToolBar.add(addEnumButton);
        add(elementToolBar, BorderLayout.EAST);

        JButton aggregationLine = new JButton(new ImageIcon(".//resources//aggregation.png"));
        aggregationLine.setBackground(Color.WHITE);
        aggregationLine.setMaximumSize(new Dimension(60, 30));
        JButton associationLine = new JButton(new ImageIcon(".//resources//association.png"));
        associationLine.setBackground(Color.WHITE);
        associationLine.setMaximumSize(new Dimension(60, 30));
        JButton compositionLine = new JButton(new ImageIcon(".//resources//composition.png"));
        compositionLine.setBackground(Color.WHITE);
        compositionLine.setMaximumSize(new Dimension(60, 30));
        JButton dependencyLine = new JButton(new ImageIcon(".//resources//dependency.png"));
        dependencyLine.setBackground(Color.WHITE);
        dependencyLine.setMaximumSize(new Dimension(60, 30));
        JButton generalizationLine = new JButton(new ImageIcon(".//resources//generalization.png"));
        generalizationLine.setBackground(Color.WHITE);
        generalizationLine.setMaximumSize(new Dimension(60, 30));
        JButton realizationLine = new JButton(new ImageIcon(".//resources//realization.png"));
        realizationLine.setBackground(Color.WHITE);
        realizationLine.setMaximumSize(new Dimension(60, 30));
        JButton uniAggregationLine = new JButton(new ImageIcon(".//resources//uniAggregation.png"));
        uniAggregationLine.setBackground(Color.WHITE);
        uniAggregationLine.setMaximumSize(new Dimension(60, 30));
        JButton uniAssociationLine = new JButton(new ImageIcon(".//resources//uniAssociation.png"));
        uniAssociationLine.setBackground(Color.WHITE);
        uniAssociationLine.setMaximumSize(new Dimension(60, 30));
        JButton uniCompositionLine = new JButton(new ImageIcon(".//resources//uniComposition.png"));
        uniCompositionLine.setBackground(Color.WHITE);
        uniCompositionLine.setMaximumSize(new Dimension(60, 30));

        JToolBar lineToolBar = new JToolBar("new line", SwingConstants.VERTICAL);
        lineToolBar.setFloatable(false);
        lineToolBar.add(aggregationLine);
        lineToolBar.add(uniAggregationLine);
        lineToolBar.add(compositionLine);
        lineToolBar.add(uniCompositionLine);
        lineToolBar.add(associationLine);
        lineToolBar.add(uniAssociationLine);
        lineToolBar.add(dependencyLine);
        lineToolBar.add(generalizationLine);
        lineToolBar.add(realizationLine);
        add(lineToolBar, BorderLayout.WEST);

        aggregationLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 1;
                newLine = true;
                from = false;
            }
        });
        uniAggregationLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 2;
                newLine = true;
                from = false;
            }
        });
        compositionLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 3;
                newLine = true;
                from = false;
            }
        });
        uniCompositionLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 4;
                newLine = true;
                from = false;
            }
        });
        associationLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 5;
                newLine = true;
                from = false;
            }
        });
        uniAssociationLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 6;
                newLine = true;
                from = false;
            }
        });
        dependencyLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 7;
                newLine = true;
                from = false;
            }
        });
        generalizationLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 8;
                newLine = true;
                from = false;
            }
        });
        realizationLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lineType = 9;
                newLine = true;
                from = false;
            }
        });

        addClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create = 1;
                newLine = false;
                from = false;
            }
        });
        addInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create = 2;
                newLine = false;
                from = false;
            }
        });
        addEnumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create = 3;
                newLine = false;
                from = false;
            }
        });

        setVisible(true);
    }

    public static ClassDiagramsEditor getInstance() {
        if (frame == null) {
            frame = new ClassDiagramsEditor();
        }
        return frame;
    }

    public int getLineType() {
        return lineType;
    }

    public boolean isNewLine() {
        return newLine;
    }

    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
    }

    public int getCreate() {
        return create;
    }

    public void setCreate(int create) {
        this.create = create;
    }

    public void saveState() {
        if (!redo.isEmpty()) {
            redo.clear();
        }
        undo.push(panel.saveState());
    }

    public void undo() {
        if (!undo.isEmpty()) {
            redo.push(panel.saveState());
            panel.restoreState(undo.pop());
        }
    }

    public void redo() {
        if (!redo.isEmpty()) {
            undo.push(panel.saveState());
            panel.restoreState(redo.pop());
        }
    }

    public boolean isFrom() {
        return from;
    }

    public void setFrom(boolean from) {
        this.from = from;
    }
}
