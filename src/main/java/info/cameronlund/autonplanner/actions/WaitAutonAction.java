package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.Robot;
import java.awt.event.ActionListener;

public class WaitAutonAction extends AutonAction {
    private String action = "waitForPID();";
    private ActionListener listener;
    private int millis;
    private JRadioButton pidButton;
    private JRadioButton liftButton;
    private JRadioButton clawButton;
    private JRadioButton delayButton;
    private JTextField millisField;

    public WaitAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);

        setColor(Color.GREEN);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        millisField = new JTextField();
        millisField.setText(millis + "");
        millisField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        millisField.setPreferredSize(new Dimension(75, 25));
        millisField.setMaximumSize(new Dimension(75, 25));
        millisField.addActionListener(e -> {
            try {
                millis = Integer.parseInt(millisField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                millisField.setText(millis + "");
            }
        });
        millisField.addFocusListener(new ActionFocusListener(millisField));
        getSaveStateListener().addComponent(millisField);
        millisField.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(millisField, gbc);

        listener = e -> {
            action = e.getActionCommand();
            millisField.setEnabled("delay".equals(action));
            wrapper.getManager().repaint();
        };

        JLabel label = new JLabel("\u2022 Wait for: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label, gbc);

        ButtonGroup group = new ButtonGroup();

        gbc.insets = new Insets(5, 15, 5, 5);
        pidButton = createRadioButton("PID Loop", "waitForPID();");
        pidButton.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(pidButton, gbc);
        group.add(pidButton);

        liftButton = createRadioButton("Lift movement", "waitForLift();");
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(liftButton, gbc);
        group.add(liftButton);

        clawButton = createRadioButton("Claw movement", "waitForClaw();");
        gbc.gridx = 0;
        gbc.gridy = 3;
        content.add(clawButton, gbc);
        group.add(clawButton);

        delayButton = createRadioButton("Millis wait", "delay");
        gbc.gridx = 0;
        gbc.gridy = 4;
        content.add(delayButton, gbc);
        group.add(delayButton);

        setContent(content);
    }

    private JRadioButton createRadioButton(String text, String action) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setActionCommand(action);
        radioButton.setFocusPainted(true);
        radioButton.addActionListener(listener);
        return radioButton;
    }

    @Override
    public info.cameronlund.autonplanner.robot.Robot renderWithGraphics(info.cameronlund.autonplanner.robot.Robot robot, Graphics g) {
        g.setColor(getColor());
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("default", Font.BOLD, 14));
        g2.drawString("W", robot.getPosX() - 6, robot.getPosY() + 5);
        return renderWithoutGraphics(robot);
    }

    @Override
    public info.cameronlund.autonplanner.robot.Robot renderWithoutGraphics(info.cameronlund.autonplanner.robot.Robot robot) {
        return robot;
    }

    @Override
    public String renderCode(info.cameronlund.autonplanner.robot.Robot robot) {
        return (!action.equals("delay") ? action : String.format("wait1MSec(%d);", millis))
                + " // " + getWrapper().getActionName();
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("Wait")) {
            System.out.println("Got bad type for " + "Wait" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        millis = object.get("millis").getAsInt();
        millisField.setText(millis+"");
        action = object.get("action").getAsString();
        millisField.setEnabled("delay".equals(action));
        switch (action) {
            case "waitForPID();":
                pidButton.setSelected(true);
                break;
            case "waitForLift();":
                liftButton.setSelected(true);
                break;
            case "waitForClaw();":
                clawButton.setSelected(true);
                break;
            case "delay":
                delayButton.setSelected(true);
                break;
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "Wait");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("millis", millis);
        object.addProperty("action", action);
        return object;
    }
}
