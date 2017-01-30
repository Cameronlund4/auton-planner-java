package info.cameronlund.autonplanner.panels;

import info.cameronlund.autonplanner.ui.CustomScrollbarUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ActionListPanel extends JPanel {
    private JPanel mainList;
    private JScrollPane scroll;
    private int listPosition = 0;

    public ActionListPanel(ActionListener buttonListener, ActionListener buttonEndListener) {
        setLayout(new BorderLayout());

        resetList();

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BorderLayout());

        JButton add = new JButton("Add to End");
        add.setFocusPainted(false);
        add.setBorder(new MatteBorder(1, 1, 0, 1, Color.GRAY));
        add.setPreferredSize(new Dimension(150, 30));
        add.setBackground(Color.lightGray);
        add.addActionListener(buttonListener);

        JButton addBefore = new JButton("Add Before");
        addBefore.setFocusPainted(false);
        addBefore.setBorder(new MatteBorder(1, 1, 0, 1, Color.GRAY));
        addBefore.setPreferredSize(new Dimension(150, 30));
        addBefore.setBackground(Color.lightGray);
        addBefore.addActionListener(buttonEndListener);

        buttonContainer.add(add, BorderLayout.WEST);
        buttonContainer.add(addBefore, BorderLayout.EAST);
        add(buttonContainer, BorderLayout.SOUTH);

        JLabel label = new JLabel("Auton Actions", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.lightGray);
        label.setBorder(new MatteBorder(1, 1, 0, 1, Color.GRAY));
        add(label, BorderLayout.NORTH);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 200);
    }

    public void resetList() {
        listPosition = 0;
        if (mainList != null)
            remove(mainList);
        if (scroll != null)
            remove(scroll);
        mainList = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        JPanel temp = new JPanel();
        mainList.add(temp, gbc);

        scroll = new JScrollPane(mainList);
        scroll.setBorder(new MatteBorder(1, 1, 0, 1, Color.GRAY));
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().setUI(new CustomScrollbarUI());
        scroll.getVerticalScrollBar().setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));
        add(scroll);
    }

    public JPanel getMainList() {
        return mainList;
    }

    // SHOULD NOT be called to add to the end
    public void addMove(Component comp, int index) {
        listPosition++; // WILL CAUSE ISSUES if the index increments listPosition
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainList.add(comp, gbc, index);
        revalidate();
        repaint();
    }

    public void addMove(Component comp) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainList.add(comp, gbc, listPosition++);
        revalidate();
        repaint();
    }

    public void removeMove(Component comp) {
        mainList.remove(comp);
        listPosition -= 1;
        revalidate();
        repaint();
    }
}
