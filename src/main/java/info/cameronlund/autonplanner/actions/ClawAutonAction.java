package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Name: Cameron Lund
 * Date: 1/27/2017
 * JDK: 1.8.0_101
 * Project: x
 */
public class ClawAutonAction extends AutonAction {
    private float angleTarget = 0f;
    private int speed;
    private int millis;
    private String action = "action1";
    private ActionListener listener;

    public ClawAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);

        setColor(Color.RED);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        ButtonGroup group = new ButtonGroup();

        // Target set
        JLabel targetLabel = new JLabel("       \u2022 Target: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(targetLabel, gbc);

        JLabel speedLabel = new JLabel("\u2022 Speed: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(speedLabel, gbc);

        JLabel millisLabel = new JLabel("       \u2022 Millis: ");
        gbc.gridx = 0;
        gbc.gridy = 4;
        content.add(millisLabel, gbc);

        JTextField speedField = new JTextField();
        speedField.setText(speed + "");
        speedField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        speedField.setPreferredSize(new Dimension(75, 25));
        speedField.setMaximumSize(new Dimension(75, 25));
        speedField.addActionListener(e -> {
            try {
                speed = Integer.parseInt(speedField.getText());
                wrapper.getManager().getFrame().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                speedField.setText(speed + "");
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(speedField, gbc);

        JTextField targetField = new JTextField();
        targetField.setText((int) angleTarget + "");
        targetField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        targetField.setPreferredSize(new Dimension(75, 25));
        targetField.setMaximumSize(new Dimension(75, 25));
        targetField.addActionListener(e -> {
            try {
                angleTarget = Integer.parseInt(targetField.getText());
                wrapper.getManager().getFrame().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                targetField.setText((int) angleTarget + "");
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(targetField, gbc);

        JTextField millisField = new JTextField();
        millisField.setEnabled(false);
        millisField.setText(millis + "");
        millisField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        millisField.setPreferredSize(new Dimension(75, 25));
        millisField.setMaximumSize(new Dimension(75, 25));
        millisField.addActionListener(e -> {
            try {
                millis = Integer.parseInt(millisField.getText());
                wrapper.getManager().getFrame().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                millisField.setText(millis + "");
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(millisField, gbc);

        listener = e -> {
            action = e.getActionCommand();
            switch (action) {
                case "action1":
                    millisField.setEnabled(false);
                    targetField.setEnabled(true);
                    break;
                case "action2":
                    millisField.setEnabled(true);
                    targetField.setEnabled(false);
                    break;
            }
        };

        // Target set
        gbc.insets = new Insets(5, 5, 5, 5);
        JRadioButton pidButton = createRadioButton("Set claw", "action1");
        pidButton.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(pidButton, gbc);
        group.add(pidButton);

        // Close set
        JRadioButton liftButton = createRadioButton("Close claw", "action2");
        gbc.gridx = 0;
        gbc.gridy = 3;
        content.add(liftButton, gbc);
        group.add(liftButton);

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
        g.setColor(getColor());
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("default", Font.BOLD, 14));
        g2.drawString("C", robot.getPosX() - 6, robot.getPosY() + 5);
        return renderWithoutGraphics(robot);
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        return robot;
    }

    @Override
    public String renderCode() {
        switch (action) {
            case "action1":
                return String.format("setClaw(%d,%d); // " + getWrapper().getActionName(), (int) angleTarget, speed);
            case "action2":
                return String.format("clawClose(%d,%d); // " + getWrapper().getActionName(), millis, speed);
            default:
                return "// !----- Failed to generate claw code here -----!";
        }
    }

    @Override
    public void loadJson(JsonObject object) {
        // TODO Implement
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "CLAW");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("angleTarget", angleTarget);
        object.addProperty("speed", speed);
        object.addProperty("millis", millis);
        object.addProperty("action", action);
        return object;
    }
}
