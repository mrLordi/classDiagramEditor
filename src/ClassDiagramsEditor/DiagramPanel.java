package ClassDiagramsEditor;

import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.Line.*;
import ClassDiagramsEditor.Window.Add.AddClass;
import ClassDiagramsEditor.Enum.Enum;
import ClassDiagramsEditor.Window.Add.AddEnum;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Window.Add.AddInterface;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.util.*;
import java.util.List;

public class DiagramPanel extends JPanel{

    private static DiagramPanel panel;
    private Element from;
    private Element to;
    private List<Element> classList;
    private List<Element> interfaceList;
    private List<Element> enumList;
    private List<Line> lines;
    private List<String> nameList;
    private JPanel buffer;
    private int count = 1;
    private boolean cut;
    private Strategy strategy;

    private DiagramPanel() {
        super();
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem paste = new JMenuItem("Paste");
        popupMenu.add(paste);

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        });
        classList = new LinkedList<>();
        interfaceList = new LinkedList<>();
        enumList = new LinkedList<>();
        lines = new LinkedList<>();
        nameList = new LinkedList<>();
        setLayout(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocus();
                if (e.getButton() == 1) {
                    createElement(e);
                } else if (e.getButton() == 3) {
                    if (buffer == null) {
                        paste.setEnabled(false);
                    } else {
                        paste.setEnabled(true);
                    }
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }

            }
        });

        Action doUndo = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ClassDiagramsEditor.getInstance().undo();
            }
        };
        Action doRedo = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ClassDiagramsEditor.getInstance().redo();
            }
        };
        Action doPaste = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                paste();
            }
        };
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), "doUndo");
        getActionMap().put("doUndo", doUndo);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), "doRedo");
        getActionMap().put("doRedo", doRedo);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "doPaste");
        getActionMap().put("doPaste", doPaste);
    }

    public static DiagramPanel getInstance() {
        if (panel == null) {
            panel = new DiagramPanel();
        }
        return panel;
    }

    public void paste() {
        if (buffer == null) {
            return;
        }
        ClassDiagramsEditor.getInstance().saveState();

        boolean check = false;
        while (!check) {
            check = true;
            if (nameList.contains(buffer.getName())) {
                buffer.setName(buffer.getName() + "_");
                check = false;
            }
        }

        add(buffer);
        nameList.add(buffer.getName());
        revalidate();
        repaint();

        Element temp = null;
        if (buffer instanceof Class) {
            classList.add((Class) buffer);
            if (cut) {
                temp = (Element)((Class) buffer).cloneElement(true);
                cut = false;
                count--;
            } else {
                temp = (Element)((Class) buffer).cloneElement(false);
            }
        } else if (buffer instanceof Interface){
            interfaceList.add((Interface) buffer);
            if (cut) {
                temp = (Element)((Interface) buffer).cloneElement(true);
                cut = false;
                count--;
            } else {
                temp = (Element)((Interface) buffer).cloneElement(false);
            }
        } else if ((buffer instanceof Enum)) {
            enumList.add((Enum) buffer);
            if (cut) {
                temp = (Element)((Enum) buffer).cloneElement(true);
                cut = false;
                count--;
            } else {
                temp = (Element)((Enum) buffer).cloneElement(false);
            }
        }
        String name = temp.getName();
        count++;
        name = name.substring(0, name.length() - 1) + count;
        temp.setName(name);
        buffer = temp;
        ClassDiagramsEditor.getInstance().repaint();
    }

    public Element getFrom() {
        return from;
    }

    public void setFrom(Element from) {
        this.from = from;
    }

    public Element getTo() {
        return to;
    }

    public void setTo(Element to) {
        this.to = to;
    }

    private void createElement(MouseEvent e) {
        if (ClassDiagramsEditor.getInstance().getCreate() != 0) {
            ClassDiagramsEditor.getInstance().saveState();
            if (ClassDiagramsEditor.getInstance().getCreate() == 1) {
                strategy =  new AddClass(e.getX(), e.getY());
                strategy.addElement();
            } else if (ClassDiagramsEditor.getInstance().getCreate() == 2) {
                strategy = new AddInterface(e.getX(), e.getY());
                strategy.addElement();
            } else if (ClassDiagramsEditor.getInstance().getCreate() == 3) {
                strategy = new AddEnum(e.getX(), e.getY());
                strategy.addElement();
            }
            ClassDiagramsEditor.getInstance().setCreate(0);
        }
    }

    public List<Element> getClassList() {
        return classList;
    }

    public List<Element> getInterfaceList() {
        return interfaceList;
    }

    public List<Element> getEnumList() {
        return enumList;
    }

    public void setBuffer(Element buffer, boolean cut) {
        this.buffer = buffer;
        count = 1;
        if (cut) {
            this.cut = true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        for (Line l : lines) {
            l.paintComponent(g);
        }
    }

    public void addLine(Line line) {
        lines.add(line);
        ClassDiagramsEditor.getInstance().repaint();
    }

    public void deleteLine(Line line) {
        lines.remove(line);
        line.deleteLine();
    }

    public Memento saveState() {
        List<List<Element>> allState = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            allState.add(new LinkedList<>());
        }
        for (Element element : classList) {
            allState.get(0).add(((Element) element.deepClone()));
        }
        for (Element element : interfaceList) {
            allState.get(1).add(((Element) element.deepClone()));
        }
        for (Element element : enumList) {
            allState.get(2).add(((Element) element.deepClone()));
        }
        return new Memento(allState);
    }

    private void switchLineType(int lineType, String from, String to) {
        Line line = Line.createLine(from, to, lineType);
        lines.add(line);
    }

    public void restoreState(Memento memento) {
        removeAll();
        lines.clear();

        List<List<Element>> allState = memento.getState();
        classList = allState.get(0);
        interfaceList = allState.get(1);
        enumList = allState.get(2);

        for (Element element : classList) {
            element.updateElement();
            add(element);
        }
        for (Element element : interfaceList) {
            element.updateElement();
            add(element);
        }
        for (Element element : enumList) {
            element.updateElement();
            add(element);
        }

        for (Element element : classList) {
            for (int i = 0; i < element.elements.size(); i++) {
                int lineType = element.linesType.get(i);
                String name = element.elements.get(i);
                switchLineType(lineType, name, element.getName());
            }
        }
        for (Element element : interfaceList) {
            for (int i = 0; i < element.elements.size(); i++) {
                int lineType = element.linesType.get(i);
                String name = element.elements.get(i);
                switchLineType(lineType, name, element.getName());
            }
        }
        for (Element element : enumList) {
            for (int i = 0; i < element.elements.size(); i++) {
                int lineType = element.linesType.get(i);
                String name = element.elements.get(i);
                switchLineType(lineType, name, element.getName());
            }
        }

        ClassDiagramsEditor.getInstance().revalidate();
        ClassDiagramsEditor.getInstance().repaint();
    }

    public List<String> getNameList() {
        return nameList;
    }
}
