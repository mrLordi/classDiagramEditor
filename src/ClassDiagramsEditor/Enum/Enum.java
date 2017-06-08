package ClassDiagramsEditor.Enum;

import ClassDiagramsEditor.Element;
import ClassDiagramsEditor.Window.Literal.EnumLiteralSettings;
import ClassDiagramsEditor.Window.Name.EnumNameSettings;
import ClassDiagramsEditor.StringSizeInPixels;
import ClassDiagramsEditor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ClassDiagramsEditor.AccessLevel;
import java.util.LinkedList;
import java.util.List;

public class Enum extends Element {
    private EnumLiteralSettings literalDialog;
    private EnumNameSettings nameDialog;
    private List<String> literals;
    private JPanel namePanel;
    private JPanel literalsPanel;

    public Enum(String name, int x, int y) {
        super(name, x, y);

        color = new Color(248,248,255);
        literals = new LinkedList<>();

        literalsPanel = createPanel(color);
        defaultElement();

        height = 40;
        width = 80;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        nameLabel = new JLabel(this.toString());
        JLabel enumLabel = new JLabel("<<enum>>");
        resizeLabel(toString(), true);
        enumLabel.setAlignmentX(CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        enumLabel.setFont(new Font("Default", Font.BOLD, 12));
        nameLabel.setFont(new Font("Default", Font.BOLD, 12));

        namePanel = createPanel(color);
        namePanel.add(enumLabel);
        namePanel.add(nameLabel);

        namePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    nameDialog.setVisible(true);
                }
            }
        });
        namePanel.addMouseListener(mousePressedReleased);
        namePanel.addMouseMotionListener(mouseDragged);

        literalsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    setLiteralDialog();
                }
            }
        });
        literalsPanel.addMouseListener(mousePressedReleased);
        literalsPanel.addMouseMotionListener(mouseDragged);

        addMouseListener(mousePressedReleased);
        addMouseMotionListener(mouseDragged);

        add(BorderLayout.NORTH, namePanel);
        add(BorderLayout.CENTER, literalsPanel);

        nameDialog = new EnumNameSettings(this);
        updateElement();
    }

    private void setLiteralDialog() {
        literalDialog = new EnumLiteralSettings(this);
        literalDialog.setEnumName();
        literalDialog.setVisible(true);
    }

    public void addLiteral(String literal) {
        JLabel temp = (JLabel)literalsPanel.getComponent(0);
        if (temp.getText().equals(" ")){
            temp.setText(literal);
            resizeLiteral(literal, false);
        } else {
            temp = createLabel(literal, color);
            resizeLiteral(literal, true);
        }
        literalsPanel.add(temp);
        literals.add(literal);
    }

    public void deleteLiteral(int index) {
        int temp = StringSizeInPixels.getLength(literals.get(index));
        literals.remove(index);
        if (temp == maxWidth) {
            maxWidth = 0;
            getMaxWidth();
        }
        if (literals.size() == 0 ) {
            JLabel label = (JLabel)literalsPanel.getComponent(0);
            label.setText(" ");

        } else {
            literalsPanel.remove(index);
            height -= 16;
        }
    }

    public void updateLiteral(int index) {
        JLabel label = (JLabel)literalsPanel.getComponent(index);
        String literal = literals.get(index);
        label.setText(literal);
        resizeLiteral(literal, false);

        maxWidth = 0;
        getMaxWidth();

        setBounds(x, y, width, height);
    }

    @Override
    protected void resizeLabel(String s, boolean check) {
        if (check) {
            height += 16;
        }
        nameWidth = StringSizeInPixels.getLength(s);
        if (nameWidth > maxWidth) {
            maxWidth = nameWidth;
        }
        width = nameWidth + 15 > width ? nameWidth + 15 : width;
        if (width < 80) {
            width = 80;
        }
    }

    @Override
    protected void deleteElement() {
        panel.getEnumList().remove(this);
        panel.remove(this);
        for (int i = lines.size() - 1; i >= 0; i--) {
            lines.get(i).getFrom().getElements().remove(getName());
            lines.get(i).getTo().getElements().remove(getName());
            panel.deleteLine(lines.get(i));
        }
        panel.getNameList().remove(getName());
        ClassDiagramsEditor.getInstance().repaint();
    }

    @Override
    protected void defaultElement() {
        if (literals.isEmpty()) {
            literalsPanel.add(createLabel(" ", color));
        }
    }

    @Override
    public Prototype cloneElement(boolean addCopy) {
        String name = getName();
        if (addCopy) {
            name += "Copy1";
        }

        Enum temp = new Enum(name, 0, 0);
        for (String literal : literals){
            temp.addLiteral(literal);
        }

        temp.setAccessLevel(accessLevel);
        temp.setX(x);
        temp.setY(y);
        return temp;
    }

    @Override
    protected Element getThis() {
        return this;
    }

    private void resizeLiteral(String literal, boolean check) {
        int literalWidth = StringSizeInPixels.getLength(literal);
        width = literalWidth + 20 > width ? literalWidth + 20 : width;
        if (check) {
            height += 16;
        }
        if (literalWidth > maxWidth) {
            maxWidth = literalWidth;
        }
    }

    @Override
    public void getMaxWidth() {
        for (String literal : literals) {
            int literalWidth = StringSizeInPixels.getLength(literal);
            maxWidth = maxWidth < literalWidth ? literalWidth : maxWidth;
        }

        maxWidth = maxWidth < nameWidth ? nameWidth : maxWidth;
        width = maxWidth + 15;
        if (width < 80) {
            width = 80;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (accessLevel == AccessLevel.PUBLIC) {
            stringBuilder.append("+ ");
        } else if (accessLevel == AccessLevel.PRIVATE) {
            stringBuilder.append("- ");
        } else if (accessLevel == AccessLevel.PROTECTED) {
            stringBuilder.append("# ");
        } else {
            stringBuilder.append("~ ");
        }

        stringBuilder.append(name);

        return stringBuilder.toString();
    }

    public List<String> getLiterals() {
        return literals;
    }
}
