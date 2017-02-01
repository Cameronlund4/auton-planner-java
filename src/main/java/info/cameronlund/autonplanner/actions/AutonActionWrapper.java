package info.cameronlund.autonplanner.actions;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class AutonActionWrapper {
    private static int nextId = 0;
    private GridBagConstraints gc = new GridBagConstraints();
    private Insets insets = new Insets(0, 5, 0, 0);
    private Color defaultBg;
    private ActionManager manager;
    private JPanel titleGraphic;
    private JLabel titleLabel;
    private JLabel subTitleLabel;
    private String actionName;
    private boolean selected = false;
    private String type;
    private int id;
    private AutonAction action;

    public AutonActionWrapper(ActionManager manager, String actionName) {
        this.manager = manager;
        type = manager.getActionTypes().get(0);
        id = nextId++;

        // Generate title label
        titleLabel = new JLabel();
        JPanel titleWrapper = new JPanel();
        titleWrapper.setLayout(new BorderLayout());
        titleWrapper.setPreferredSize(new Dimension(200, 14));
        titleWrapper.setMinimumSize(new Dimension(200, 14));
        titleWrapper.add(titleLabel, BorderLayout.CENTER);

        // Generate sub title
        subTitleLabel = new JLabel("Test");
        Font subTitleFont = new Font("default", Font.PLAIN, 10);
        subTitleLabel.setFont(subTitleFont);
        JPanel subTitleWrapper = new JPanel();
        subTitleWrapper.setLayout(new BorderLayout());
        subTitleWrapper.setPreferredSize(new Dimension(200, 14));
        subTitleWrapper.setMinimumSize(new Dimension(200, 14));
        subTitleWrapper.add(subTitleLabel, BorderLayout.CENTER);
        resetSubtitleText();

        // Generate remove button
        JButton removeButton = new JButton("X");
        removeButton.setOpaque(false);
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.setContentAreaFilled(false);
        removeButton.setForeground(Color.RED);
        removeButton.addActionListener((e) -> {
            manager.remove(this);
        });

        // Generate title graphic
        titleGraphic = new JPanel() {
            @Override
            public void setBackground(Color cg) {
                super.setBackground(cg);
                titleWrapper.setBackground(cg);
                subTitleWrapper.setBackground(cg);
            }
        };
        titleGraphic.setPreferredSize(new Dimension(300, 40));
        titleGraphic.setLayout(new GridBagLayout());
        titleGraphic.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        titleGraphic.setName("action_title_graphic" + getId());
        defaultBg = titleGraphic.getBackground();

        // Add title label
        cleanGC();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.WEST;
        titleGraphic.add(titleWrapper, gc);
        setActionName(actionName);

        // Add title sublabel
        cleanGC();
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(0, 5, 5, 0);
        gc.anchor = GridBagConstraints.WEST;
        titleGraphic.add(subTitleWrapper, gc);

        // Add delete move button
        cleanGC();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.EAST;
        titleGraphic.add(removeButton, gc);

        action = new DriveAutonAction(this);
    }

    public boolean equals(Object o) {
        return o instanceof AutonActionWrapper && ((AutonActionWrapper) o).getId() == getId();
    }

    private void cleanGC() {
        gc = new GridBagConstraints();
        gc.insets = insets;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
        titleLabel.setText(actionName);
    }

    public JPanel getTitleGraphic() {
        return titleGraphic;
    }

    public JPanel getOptionsGraphic() {
        return action.getContent();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (selected)
            titleGraphic.setBackground(Color.gray);
        else {
            titleGraphic.setBackground(defaultBg);
            if (action != null)
                action.saveState();
        }
        this.selected = selected;
    }

    private void resetSubtitleText() {
        // Make type start with capital and rest lower
        String typeString = type.toLowerCase();
        typeString = typeString.replace(typeString.charAt(0), Character.toUpperCase(typeString.charAt(0)));

        // Change our text
        subTitleLabel.setText(typeString);
    }

    public Color getDefaultBg() {
        return defaultBg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (action != null && this.type.equals(type)) // We already are this type, ignore
            return;
        this.type = type;
        resetSubtitleText();
        action = manager.createAction(type, this);
        manager.redrawContent();
        manager.getFrame().repaint();
    }

    public int getId() {
        return id;
    }

    public AutonAction getAction() {
        return action;
    }

    public ActionManager getManager() {
        return manager;
    }
}
