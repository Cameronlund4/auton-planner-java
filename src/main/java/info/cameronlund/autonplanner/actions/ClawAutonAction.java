package info.cameronlund.autonplanner.actions;


import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClawAutonAction extends AutonAction {
    private JTextField angleField;
    private String action = "action1";
    private JRadioButton openClaw;
    private JRadioButton closeClaw;
    private ActionListener listener;

    public ClawAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);
        setColor(Color.BLUE);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel typeLabel = new JLabel("\u2022 Type: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(typeLabel, gbc);

        listener = e -> {
            action = e.getActionCommand();
            wrapper.getManager().repaint();
        };
        ButtonGroup group = new ButtonGroup();

        openClaw = createRadioButton("Open Claw", "action1");
        openClaw.setSelected(true);
        group.add(openClaw);
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(openClaw, gbc);

        closeClaw = createRadioButton("Close Claw", "action2");
        group.add(closeClaw);
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(closeClaw, gbc);

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
    public Robot renderWithGraphics(Robot robot, Graphics g) {
        return robot;
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        return robot;
    }

    @Override
    public String renderCode(Robot robot) {
        // ((sqrt((driveWidthHoles*0.5)^2 + (driveHeightHoles*0.5)^2)*pi)/((wheelSize)pi))*360

        // Total drive ticks/point turn rotation: ((sqrt((30*0.5)^2 + (28*0.5)^2)*pi)/(4pi))*360
        // Ticks/degree point turn rotation ((sqrt((30*0.5)^2 + (28*0.5)^2)*pi)/(4pi))
        switch (action) {
            case "action2":
                return String.format("closeClaw(); // " + getWrapper().getActionName() + "\nwaitForClaw();");
            case "action1":
                return String.format("openClaw(); // " + getWrapper().getActionName() + "\nwaitForClaw();");
            default:
                return "// !----- Failed to generate turn code here -----!";
        }
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("CLAW")) {
            System.out.println("Got bad type for " + "CLAW" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        action = object.get("action").getAsString();
        switch (action) {
            case "action1":
                openClaw.setSelected(true);
                break;
            case "action2":
                closeClaw.setSelected(true);
                break;
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "CLAW");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("action", action);
        return object;
    }
}
