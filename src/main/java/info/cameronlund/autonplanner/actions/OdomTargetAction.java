package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class OdomTargetAction extends AutonAction {
    private int x;
    private int y;
    private JTextField yField;
    private JTextField xField;

    public OdomTargetAction(AutonActionWrapper wrapper) {
        super(wrapper);
        setColor(Color.YELLOW);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel label1 = new JLabel("\u2022 X: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label1, gbc);

        xField = new JTextField();
        xField.setText(x + "");
        xField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        xField.setPreferredSize(new Dimension(75, 25));
        xField.setMaximumSize(new Dimension(75, 25));
        xField.addActionListener(e -> {
            try {
                x = Integer.parseInt(xField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                xField.setText((int) x + "");
            }
        });
        xField.addFocusListener(new ActionFocusListener(xField));
        getSaveStateListener().addComponent(xField);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(xField, gbc);
        setContent(content);

        JLabel label2 = new JLabel("\u2022 Y: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(label2, gbc);

        yField = new JTextField();
        yField.setText(y + "");
        yField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        yField.setPreferredSize(new Dimension(75, 25));
        yField.setMaximumSize(new Dimension(75, 25));
        yField.addActionListener(e -> {
            try {
                y = Integer.parseInt(yField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                yField.setText(y + "");
            }
        });
        yField.addFocusListener(new ActionFocusListener(yField));
        getSaveStateListener().addComponent(yField);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(yField, gbc);
        setContent(content);
    }

    @Override
    public info.cameronlund.autonplanner.robot.Robot renderWithGraphics(info.cameronlund.autonplanner.robot.Robot robot, Graphics g) {
        // Draw the line between current and old pos
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x, y, (int) robot.getPosX(), (int) robot.getPosY());
        g2.setColor(Color.BLACK);
        g2.fillOval((int) robot.getPosX() - 4, (int) robot.getPosY() - 4, 8, 8);
        // Get the old stuff
        double rotation = robot.getRotation();
        int xOld = (int) robot.getPosX();
        int yOld = (int) robot.getPosY();
        // Move the robot where it should be
        robot = renderWithoutGraphics(robot);
        // Now draw the turn
        g.setColor(getColor());
        double angleDelta = robot.getRotation()-rotation;
        g.drawArc(xOld - 10, yOld - 10, 20, 20, (int) (0 - rotation) - 90, ((angleDelta < 0) ? 1 : -1) * (180 -
                (int) ((angleDelta < 0) ? angleDelta : -1 * angleDelta)));
        // Return the robot
        return robot;
    }

    @Override
    public info.cameronlund.autonplanner.robot.Robot renderWithoutGraphics(Robot robot) {
        int xOld = (int) robot.getPosX();
        int yOld = (int) robot.getPosY();
        robot.setPosition(x,y);
        double robotRot = Math.toRadians(robot.getRotation());
        // System.out.println("Robot rot: " + robotRot + "," + Math.toDegrees(robotRot));
        double lineRot = Math.atan((double) (yOld - y) / (double) (xOld - x)) + (Math.PI / 2);
        if (xOld > x)
            lineRot += Math.PI;
        while (Math.abs(lineRot) > Math.PI) {
            double lineRotTemp = lineRot;
            lineRot = (lineRot - 2*Math.PI);
            System.out.println("Math.PI: "+Math.PI+" Input: " + lineRotTemp + " Output: " + lineRot);
        }
        robot.addRotation((float) Math.toDegrees(lineRot - robotRot));
        return robot;
    }

    @Override
    public String renderCode(info.cameronlund.autonplanner.robot.Robot robot) {
        // TODO Convert ticks to mm
        // X and Y intentionally swapped, because how odom works and how cheaps coords work

        //              Pix to Ticks * Ticks to Inches * Inches to Millimeters
        double convPixToMM =  6  *  ((4 * Math.PI) / 360)    *   25.4;
        return String.format("autoDriveToPoint(%d,%d, false, true); // " + getWrapper().getActionName(), Math.round(x * convPixToMM), Math.round(y * convPixToMM));
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("POINT")) {
            System.out.println("Got bad type for " + "POINT" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        x = object.get("x").getAsInt();
        xField.setText(x+"");
        y = object.get("y").getAsInt();
        yField.setText(y+"");
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "POINT");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("x", x);
        object.addProperty("y", y);
        return object;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        xField.setText(x+"");
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        yField.setText(y+"");
        this.y = y;
    }

}