package info.cameronlund.autonplanner.actions;


import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class TurnAutonAction extends AutonAction {
    private float angleDelta = 0;
    private int speed = 0;
    private JTextField angleField;
    private JTextField speedField;
    private String action = "action1";
    private JRadioButton gyro;
    private JRadioButton encoder;
    private ActionListener listener;

    public TurnAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);
        setColor(Color.BLUE);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel("\u2022 Degrees: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label, gbc);

        angleField = new JTextField();
        angleField.setText((int) angleDelta + "");
        angleField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        angleField.setPreferredSize(new Dimension(75, 25));
        angleField.setMaximumSize(new Dimension(75, 25));
        angleField.addActionListener(e -> {
            try {
                angleDelta = Integer.parseInt(angleField.getText());
                wrapper.getManager().getFrame().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                angleField.setText((int) angleDelta + "");
            }
        });
        angleField.addFocusListener(new ActionFocusListener(angleField));
        getSaveStateListener().addComponent(angleField);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(angleField, gbc);

        JLabel typeLabel = new JLabel("\u2022 Type: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(typeLabel, gbc);

        listener = e -> action = e.getActionCommand();
        ButtonGroup group = new ButtonGroup();

        gyro = createRadioButton("Gyro turn","action1");
        gyro.setSelected(true);
        group.add(gyro);
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(gyro, gbc);

        JLabel label2 = new JLabel("          \u2022 Speed: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
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
                wrapper.getManager().getFrame().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                speedField.setText(speed + "");
            }
        });
        speedField.addFocusListener(new ActionFocusListener(speedField));
        getSaveStateListener().addComponent(speedField);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(speedField, gbc);

        encoder = createRadioButton("Encoder turn","action2");
        group.add(encoder);
        gbc.gridx = 0;
        gbc.gridy = 4;
        content.add(encoder, gbc);

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
        int x = robot.getPosX();
        int y = robot.getPosY();
        float rotation = robot.getRotation();
        robot = renderWithoutGraphics(robot);
        g.setColor(getColor());
        g.drawArc(x - 10, y - 10, 20, 20, (int) (0 - rotation) - 90, ((angleDelta < 0) ? 1 : -1) * (180 -
                (int) ((angleDelta < 0) ? angleDelta : -1 * angleDelta)));
        return robot;
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        robot.addRotation(angleDelta);
        return robot;
    }

    @Override
    public String renderCode() {
        // ((sqrt((driveWidthHoles*0.5)^2 + (driveHeightHoles*0.5)^2)*pi)/((wheelSize)pi))*360

        // Total drive ticks/point turn rotation: ((sqrt((30*0.5)^2 + (28*0.5)^2)*pi)/(4pi))*360
        // Ticks/degree point turn rotation ((sqrt((30*0.5)^2 + (28*0.5)^2)*pi)/(4pi))
        switch (action) {
            case "action2":
                return String.format("pidDrivePoint(%d * sideMult); // " + getWrapper().getActionName(), (int) (angleDelta * 5.21566151f));
            case "action1":
                return String.format("turnToAngle(%d * sideMult, %d * sideMult); // " + getWrapper().getActionName(), ((int) angleDelta * -10), speed); // TODO Implement gyro turn
            default:
                return "// !----- Failed to generate turn code here -----!";
        }
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("TURN")) {
            System.out.println("Got bad type for " + "TURN" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        angleDelta = object.get("angleDelta").getAsInt();
        angleField.setText((int) angleDelta+"");
        speed = object.get("speed").getAsInt();
        speedField.setText(speed+"");
        action = object.get("action").getAsString();
        switch (action) {
            case "action1":
                gyro.setSelected(true);
                break;
            case "action2":
                encoder.setSelected(true);
                break;
        }
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "TURN");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("angleDelta", angleDelta);
        object.addProperty("action",action);
        object.addProperty("speed",speed);
        return object;
    }

    public void setAngleDelta(float angleDelta) {
        this.angleDelta = angleDelta;
        angleField.setText((int)angleDelta+"");
    }
}
