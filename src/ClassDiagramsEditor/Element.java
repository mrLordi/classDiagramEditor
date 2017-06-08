package ClassDiagramsEditor;

import ClassDiagramsEditor.Class.Class;
import ClassDiagramsEditor.Enum.Enum;
import ClassDiagramsEditor.Interface.Interface;
import ClassDiagramsEditor.Line.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public abstract class Element extends JPanel implements FocusListener, Prototype{
    protected DiagramPanel panel;

    protected List<String> elements;
    protected List<Line> lines;
    protected List<Integer> linesType;

    protected AccessLevel accessLevel;
    protected String name;
    protected JLabel nameLabel;

    protected boolean move;

    protected int nameWidth;
    protected int maxWidth = 0;
    protected int height;
    protected int width;
    protected int x;
    protected int y;
    protected int mouseX;
    protected int mouseY;

    protected MouseAdapter mousePressedReleased = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getButton() == 3) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            requestFocus();

            if (ClassDiagramsEditor.getInstance().isNewLine()) {
                if (!ClassDiagramsEditor.getInstance().isFrom()) {
                    panel.setFrom((Element)getThis().cloneElement(false));
                    ClassDiagramsEditor.getInstance().setFrom(true);
                } else {
                    panel.setTo((Element)getThis().cloneElement(false));
                    ClassDiagramsEditor.getInstance().setFrom(true);
                    ClassDiagramsEditor.getInstance().setNewLine(false);

                    int lineType = ClassDiagramsEditor.getInstance().getLineType();
                    boolean check = true;
                    if (lineType == 1) {
                        if (panel.getFrom() instanceof Enum) {
                            check = false;
                        }
                    } else if (lineType == 2) {
                        if (panel.getFrom() instanceof Enum) {
                            check = false;
                        }
                    } else if (lineType == 3) {
                        if (panel.getFrom() instanceof Enum) {
                            check = false;
                        }
                    } else if (lineType == 4) {
                        if (panel.getFrom() instanceof Enum) {
                            check = false;
                        }
                    } else if (lineType == 8) {
                        if (!(panel.getFrom() instanceof Class && panel.getTo() instanceof Class) &&
                                !(panel.getFrom() instanceof Interface && panel.getTo() instanceof Interface)) {
                            check = false;
                        }
                    } else if (lineType == 9) {
                        if (!(panel.getFrom() instanceof Class && panel.getTo() instanceof Interface)) {
                            check = false;
                        }
                    }
                    if (check) {
                        ClassDiagramsEditor.getInstance().saveState();
                        Line line = Line.createLine(panel.getFrom().getName(), panel.getTo().getName(), lineType);
                        panel.addLine(line);
                        elements.add(panel.getFrom().getName());
                        linesType.add(lineType);
                    } else {
                        JOptionPane.showMessageDialog(ClassDiagramsEditor.getInstance(), "Incorrect connection!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {

                if (e.getButton() == 2 ) {
                    ClassDiagramsEditor.getInstance().saveState();
                    move = true;
                    x = getX();
                    y = getY();
                    mouseX = e.getXOnScreen();
                    mouseY = e.getYOnScreen();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (e.getButton() == 2) {
                x = getX();
                y = getY();
                move = false;
            }
        }
    };
        protected MouseAdapter mouseDragged = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (move) {
                setLocation(x - mouseX + e.getXOnScreen(), y - mouseY + e.getYOnScreen());
                Point location = getLocation();
                if (location.x < 0) {
                    location.x = 0;
                }
                if (location.y < 0) {
                    location.y = 0;
                }
                setLocation(location);
                for (Line l : lines) {
                    l.elementMoved(getThis(), e);
                }
                ClassDiagramsEditor.getInstance().repaint();
            }
        }
    };

    protected JPopupMenu popupMenu;
    protected Color color;

    public Element(String name, int x, int y) {
        super();
        this.panel = DiagramPanel.getInstance();
        this.x = x;
        this.y = y;
        this.name = name;
        lines = new LinkedList<>();
        elements = new LinkedList<>();
        linesType = new LinkedList<>();

        accessLevel = AccessLevel.PACKAGE;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        popupMenu = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem cut = new JMenuItem("Cut");
        popupMenu.add(delete);
        popupMenu.add(copy);
        popupMenu.add(cut);

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassDiagramsEditor.getInstance().saveState();
                deleteElement();
                popupMenu.setVisible(false);
            }
        });
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy();
            }
        });
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cut();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if ((e.getKeyCode() == KeyEvent.VK_DELETE)) {
                    ClassDiagramsEditor.getInstance().saveState();
                    deleteElement();
                }
                if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    copy();
                }

                if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    cut();
                }
            }
        });
        addFocusListener(this);
    }

    protected abstract void getMaxWidth();
    protected abstract void resizeLabel(String s, boolean check);
    protected abstract void deleteElement();
    protected abstract void defaultElement();
    public abstract Prototype cloneElement(boolean addCopy);
    protected abstract Element getThis();
    public abstract String toString();

    private void cut() {
        ClassDiagramsEditor.getInstance().saveState();

        Element temp = (Element)cloneElement(false);
        temp.updateElement();
        temp.setX(0);
        temp.setY(0);
        panel.setBuffer(temp, true);
        deleteElement();
    }

    private void copy() {
        Element temp = (Element)cloneElement(true);
        temp.setX(0);
        temp.setY(0);
        temp.updateElement();
        panel.setBuffer(temp, false);
    }

    public void deleteLine(Line line) {
        if (lines.contains(line)) {
            lines.remove(line);
        }
    }

    protected JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Default", Font.PLAIN, 12));
        label.setOpaque(true);
        label.setBackground(color);
        return label;
    }

    protected JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setBackground(color);
        return panel;
    }

    public void updateElement() {
        revalidate();
        repaint();
        maxWidth = 0;
        getMaxWidth();
        setBounds(x, y, width, height);
        ClassDiagramsEditor.getInstance().repaint();
    }

    public void updateNameLabel() {
        int temp = nameWidth + 15;
        resizeLabel(toString(), false);
        if (temp == width && temp != nameWidth) {
            maxWidth = 0;
            getMaxWidth();
        }
        nameLabel.setText(toString());
        setBounds(x, y, width, height);
        ClassDiagramsEditor.getInstance().repaint();
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        updateNameLabel();
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        updateNameLabel();
    }

    @Override
    public void focusGained(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    @Override
    public void focusLost(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    @Override
    public Prototype deepClone() {
        Element element = (Element)cloneElement(false);
        for (int i = 0; i < elements.size(); i++) {
            element.elements.add(elements.get(i));
            element.linesType.add(linesType.get(i));
        }

        element.updateElement();
        return element;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public DiagramPanel getPanel() {
        return panel;
    }

    public List<String> getElements() {
        return elements;
    }

}
