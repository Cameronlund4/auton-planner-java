package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Name: Cameron Lund
 * Date: 1/27/2017
 * JDK: 1.8.0_101
 * Project: x
 */
public class LiftAutonAction extends AutonAction {
    private int speed;
    private float angleTarget;
    private JTextField speedField;
    private JTextField angleField;

    public LiftAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);
        setColor(Color.YELLOW);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel label1 = new JLabel("\u2022 Target angle: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label1, gbc);

        angleField = new JTextField();
        angleField.setText((int) angleTarget + "");
        angleField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        angleField.setPreferredSize(new Dimension(75, 25));
        angleField.setMaximumSize(new Dimension(75, 25));
        angleField.addActionListener(e -> {
            try {
                angleTarget = Integer.parseInt(angleField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                angleField.setText((int) angleTarget + "");
            }
        });
        angleField.addFocusListener(new ActionFocusListener(angleField));
        getSaveStateListener().addComponent(angleField);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(angleField, gbc);
        setContent(content);

        JLabel label2 = new JLabel("\u2022 Speed: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(label2, gbc);

        speedField = new JTextField();
        speedField.setText(speed + "");
        speedField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        speedField.setPreferredSize(new Dimension(75, 25));
        speedField.setMaximumSize(new Dimension(75, 25));
        speedField.addActionListener(e -> {
            try {
                speed = Integer.parseInt(speedField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                speedField.setText(speed + "");
            }
        });
        speedField.addFocusListener(new ActionFocusListener(speedField));
        getSaveStateListener().addComponent(speedField);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(speedField, gbc);
        setContent(content);
    }

    @Override
    public Robot renderWithGraphics(Robot robot, Graphics g) {
        g.setColor(getColor());
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("default", Font.BOLD, 14));
        g2.drawString("L", robot.getPosX() - 6, robot.getPosY() + 5);
        return renderWithoutGraphics(robot);
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        return robot;
    }

    @Override
    public String renderCode() {
        return String.format("setLift(%d,%d); // " + getWrapper().getActionName(), (int) angleTarget, speed);
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("Lift")) {
            System.out.println("Got bad type for " + "Lift" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        angleTarget = object.get("angleTarget").getAsInt();
        angleField.setText((int) angleTarget+"");
        speed = object.get("speed").getAsInt();
        speedField.setText(speed+"");
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "Lift");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("speed", speed);
        object.addProperty("angleTarget", angleTarget);
        return object;
    }
}
