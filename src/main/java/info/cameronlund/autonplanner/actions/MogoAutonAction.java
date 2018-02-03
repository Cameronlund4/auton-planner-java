package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Name: Cameron Lund
 * Date: 1/23/2017
 * JDK: 1.8.0_101
 * Project: x
 */
public class MogoAutonAction extends AutonAction {
    private int speed = 0;
    private int time = 0;
    private JTextField speedField;
    private JTextField timeField;

    public MogoAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel("\u2022 Speed: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label, gbc);

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
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(speedField, gbc);
        setContent(content);

        JLabel label2 = new JLabel("\u2022 Time: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(label2, gbc);

        timeField = new JTextField();
        timeField.setText(time + "");
        timeField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        timeField.setPreferredSize(new Dimension(75, 25));
        timeField.setMaximumSize(new Dimension(75, 25));
        timeField.addActionListener(e -> {
            try {
                time = Integer.parseInt(timeField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                timeField.setText(time + "");
            }
        });
        timeField.addFocusListener(new ActionFocusListener(timeField));
        getSaveStateListener().addComponent(timeField);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(timeField, gbc);
        setContent(content);
    }

    @Override
    public Robot renderWithGraphics(Robot robot, Graphics g) {
        int x = (int) robot.getPosX();
        int y = (int) robot.getPosY();
        robot = renderWithoutGraphics(robot);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x, y, (int) robot.getPosX(), (int) robot.getPosY());
        g2.setColor(Color.BLACK);
        g2.fillOval((int) robot.getPosX() - 4, (int) robot.getPosY() - 4, 8, 8);
        return robot;
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        return robot;
    }

    @Override
    public String renderCode(Robot robot) {
        return String.format("// " + getWrapper().getActionName() + "\nmotorSet(MOTOR_MOGO, %d); " +
                "\ndelay(%d);\nmotorSet(MOTOR_MOGO, 0);", speed, time);
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("MOGO")) {
            System.out.println("Got bad type for " + "MOGO" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        speed = object.get("speed").getAsInt();
        time = object.get("time").getAsInt();
        speedField.setText(speed + "");
        timeField.setText(time + "");
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "MOGO");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("speed", speed);
        object.addProperty("time", time);
        // TODO Implement
        return object;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        speedField.setText(speed+"");
    }

    public void setTime(int time) {
        this.time = time;
        timeField.setText(time + "");
    }
}
