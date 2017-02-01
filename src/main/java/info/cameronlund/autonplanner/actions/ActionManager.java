package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.TitleMouseListener;
import info.cameronlund.autonplanner.panels.ActionListPanel;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ActionManager {
    private ActionListPanel panel;
    private List<AutonActionWrapper> actions = new ArrayList<>();
    private AutonActionWrapper selected;
    private JTextField actionNameField;
    private JComboBox<String> actionTypeBox;
    private JFrame frame;
    private JPanel optionsPanel;
    private int i = 0;

    public ActionManager(JFrame frame) {
        this.frame = frame;
        frame.setFocusable(true);
        // Let the user navigate the actions with the arrow keys
        final List<Integer> pressed = new ArrayList<>();
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if (KeyEvent.KEY_PRESSED == e.getID() ||
                            KeyEvent.KEY_RELEASED == e.getID()) { // If we want to listen
                        int currentIndex = actions.indexOf(selected);
                        // Register it
                        if (KeyEvent.KEY_PRESSED == e.getID())
                            pressed.add(e.getKeyCode());
                        else {
                            int i = 0;
                            while (i < pressed.size()) {
                                int key = pressed.get(i);
                                if (key == e.getKeyCode())
                                    pressed.remove((Integer) e.getKeyCode());
                                else
                                    i++;
                            }
                        }

                        if (pressed.contains(61) && pressed.contains(KeyEvent.VK_CONTROL)) { // Ctrl and +
                            AutonActionWrapper action = createNewAction();
                            add(action);
                            setSelected(action);
                        } else if (pressed.contains(45) && pressed.contains(KeyEvent.VK_CONTROL)) { // Ctrl and -
                            if (selected != null) {
                                AutonActionWrapper action = createNewAction();
                                addBeforeSelected(action);
                                setSelected(action);
                            }
                        } else if ((pressed.contains(KeyEvent.VK_BACK_SPACE) || (pressed.contains(KeyEvent.VK_DELETE)))
                                && pressed.contains(KeyEvent.VK_CONTROL)) { // Ctrl and backspace or delete
                            if (selected != null) {
                                int selectedIndex = actions.indexOf(selected);
                                remove(selected);
                                if (actions.size() > 0)
                                    setSelected(actions.get(actions.size() - 1 >= selectedIndex ?
                                            selectedIndex : selectedIndex - 1));
                            }
                        } else
                        if (pressed.contains(KeyEvent.VK_DOWN) && pressed.contains(KeyEvent.VK_CONTROL)) { // Ctrl and down
                            if (currentIndex + 1 <= actions.size() - 1)
                                if (actions.size() == 0)
                                    return false;
                                else
                                    setSelected(actions.get(currentIndex + 1));
                            return false;
                        } else if (pressed.contains(KeyEvent.VK_UP) && pressed.contains(KeyEvent.VK_CONTROL)) { // Ctrl and up
                            if (currentIndex - 1 >= 0) {
                                setSelected(actions.get(currentIndex - 1));
                            } else
                                setSelected(null);
                            return false;
                        }
                    }
                    return false;
                });

        panel = new ActionListPanel((e) -> {
            AutonActionWrapper action = createNewAction();
            add(action);
            setSelected(action);
        }, (e) -> {
            AutonActionWrapper action = createNewAction();
            addBeforeSelected(action);
            setSelected(action);
        });
    }

    public AutonActionWrapper createNewAction() {
        AutonActionWrapper action = new AutonActionWrapper(this, "Unnamed action " + i++);
        action.getTitleGraphic().addMouseListener(new TitleMouseListener(this, action));
        return action;
    }

    public ActionListPanel getActionListPanel() {
        return panel;
    }

    public void setSelected(AutonActionWrapper action) {
        // Deselect old selected
        if (selected != null) {
            selected.setSelected(false);
        }
        // Set selected to new action
        selected = action;
        // If selected isn't null...
        if (selected != null) {
            selected.setSelected(true);
            redrawContent();
        } else {
            clearContent();
        }
        // If the action isn't null
        if (actionNameField != null) {
            actionNameField.requestFocus();
            actionNameField.setEnabled(selected != null);
            actionNameField.setText(selected == null ? "" : selected.getActionName());
        }
        if (actionTypeBox != null) {
            actionTypeBox.setEnabled(selected != null);
            if (selected != null)
                actionTypeBox.setSelectedIndex(selected.getType().ordinal());
        }
        repaint();
    }

    public void clear() {
        panel.resetList();
        actions = new ArrayList<>();
        repaint();
    }

    public void addBeforeSelected(AutonActionWrapper action) {
        if (selected == null)
            return;
        int index = 0;
        Component[] components = panel.getMainList().getComponents();
        for (; index < components.length; index++) {
            if (components[index].getName() != null &&
                    components[index].getName().equals(selected.getTitleGraphic().getName())) {
                break;
            }
        }
        add(action, index, actions.indexOf(selected));
    }

    public void addAfterSelected(AutonActionWrapper action) {
        // TODO Impl
        add(action);
    }

    public void add(AutonActionWrapper action, int graphicIndex, int listIndex) {
        actions.add(listIndex, action);
        JPanel titleGraphic = action.getTitleGraphic();
        if (graphicIndex != -1)
            panel.addMove(titleGraphic, graphicIndex);
        else
            panel.addMove(titleGraphic);
    }

    public void add(AutonActionWrapper action) {
        add(action, -1, actions.size());
    }

    public void remove(AutonActionWrapper action) {
        actions.remove(action);
        panel.removeMove(action.getTitleGraphic());
        if (selected == action)
            setSelected(null);
        repaint();
    }

    public List<AutonActionWrapper> getActions() {
        return actions;
    }

    public JTextField getActionNameField() {
        return actionNameField;
    }

    public void setActionNameField(JTextField actionNameField) {
        this.actionNameField = actionNameField;
        actionNameField.setEnabled(selected != null);
        actionNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Ignored
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (selected != null) {
                    ActionEvent event = new ActionEvent(this, 0, "action");
                    for (ActionListener list : actionNameField.getActionListeners())
                        list.actionPerformed(event);
                }
            }
        });
        actionNameField.addActionListener((e) -> {
            if (selected != null)
                selected.setActionName(actionNameField.getText());
        });
    }

    public void setActionTypeBox(JComboBox<String> actionTypeBox) {
        this.actionTypeBox = actionTypeBox;
        actionTypeBox.setEnabled(selected != null);
        actionTypeBox.addActionListener((e) -> {
            if (selected != null) {
                selected.setType(ActionType.values()[actionTypeBox.getSelectedIndex()]);
            }
        });
    }

    public void clearContent() {
        if (optionsPanel != null) {
            optionsPanel.removeAll();
            optionsPanel.revalidate();
        }
    }

    public void redrawContent() {
        if (selected != null && optionsPanel != null) {
            clearContent();
            optionsPanel.add(selected.getAction().getContent(), BorderLayout.NORTH);
        }
    }

    // Draw all of the actions before the selected
    public void paint(Graphics g, Robot robot) {
        System.out.println("Code: -----------------------------------------------------");
        for (int i = 0; i < actions.indexOf(selected) + 1; i++) {
            System.out.println(actions.get(i).getAction().renderCode());
            robot = actions.get(i).getAction().renderWithGraphics(robot, g);
        }
        System.out.println("-----------------------------------------------------------");
    }

    public JPanel getOptionsPanel() {
        return optionsPanel;
    }

    public void setOptionsPanel(JPanel optionsPanel) {
        this.optionsPanel = optionsPanel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JsonArray toJson() {
        JsonArray actionsArray = new JsonArray();
        for (AutonActionWrapper action : actions)
            actionsArray.add(action.getAction().toJson());
        return actionsArray;
    }

    public void loadJson(JsonArray array) {
        clear();
        for (JsonElement elem : array) {
            // Generate a wrapper for the new action
            JsonObject object = elem.getAsJsonObject();
            System.out.println("--------------------");
            System.out.println(object);
            AutonActionWrapper wrapper = createNewAction();
            wrapper.setActionName(object.get("name").getAsString());
            System.out.println(object.get("type").getAsString());
            // Set the type of the action
            switch (object.get("type").getAsString()) {
                case "CLAW":
                    wrapper.setType(ActionType.CLAW);
                    break;
                case "DRIVE":
                    wrapper.setType(ActionType.DRIVE);
                    break;
                case "LIFT":
                    wrapper.setType(ActionType.LIFT);
                    break;
                case "TURN":
                    wrapper.setType(ActionType.TURN);
                    break;
                case "WAIT":
                    wrapper.setType(ActionType.WAIT);
                    break;
            }
            // Load the data for the action
            wrapper.getAction().loadJson(object);
            // Load the action
            add(wrapper);
            System.out.println("--------------------");
        }
        setSelected(null);
    }

    public void repaint() {
        frame.repaint();
    }
}
