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
public class FourBarAutonAction extends AutonAction {
    private float angleTarget;
    private JTextField angleField;

    public FourBarAutonAction(AutonActionWrapper wrapper) {
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
    }

    @Override
    public Robot renderWithGraphics(Robot robot, Graphics g) {
        g.setColor(getColor());
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("default", Font.BOLD, 14));
        g2.drawString("L", (int) robot.getPosX() - 6, (int) robot.getPosY() + 5);
        return renderWithoutGraphics(robot);
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        return robot;
    }

    @Override
    public String renderCode(Robot robot) {
        return String.format("setIntakeTarget(%d); // " + getWrapper().getActionName() + "\nwaitForIntake();", (int) angleTarget);
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("4BAR")) {
            System.out.println("Got bad type for " + "4BAR" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        angleTarget = object.get("angleTarget").getAsInt();
        angleField.setText((int) angleTarget+"");
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "4BAR");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("angleTarget", angleTarget);
        return object;
    }
}
