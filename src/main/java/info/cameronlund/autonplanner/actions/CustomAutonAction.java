package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Name: Cameron Lund
 * Date: 1/23/2017
 * JDK: 1.8.0_101
 * Project: x
 */
public class CustomAutonAction extends AutonAction {
    private String code = "// Add your code here";
    private JTextArea codeField;

    public CustomAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel("\u2022 Code: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label, gbc);

        codeField = new JTextArea();
        codeField.setText(code);
        codeField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        codeField.setPreferredSize(new Dimension(375, 475));
        codeField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                try {
                    code = codeField.getText();
                    wrapper.getManager().repaint();
                } catch (NumberFormatException ignored) {
                    ignored.printStackTrace();
                    codeField.setText(code);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    code = codeField.getText();
                    wrapper.getManager().repaint();
                } catch (NumberFormatException ignored) {
                    ignored.printStackTrace();
                    codeField.setText(code);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(codeField, gbc);
        setContent(content);

        JButton updateButton = new JButton("Update");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(updateButton, gbc);
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
    public String renderCode(info.cameronlund.autonplanner.robot.Robot robot) {
        return "// " + getWrapper().getActionName() + "\n" + code;
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("CUSTOM")) {
            System.out.println("Got bad type for " + "CUSTOM" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        code = object.get("code").getAsString();
        codeField.setText(code);
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "CUSTOM");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("code", code);
        // TODO Implement
        return object;
    }

    public void setCode(String code) {
        this.code = code;
        codeField.setText(code);
    }
}
