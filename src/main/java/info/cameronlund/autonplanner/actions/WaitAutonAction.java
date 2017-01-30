package info.cameronlund.autonplanner.actions;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class WaitAutonAction extends AutonAction {
    private String action = "waitForPID();";
    private ActionListener listener;
    private int time;

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

        JTextField millisField = new JTextField();
        millisField.setText(time + "");
        millisField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        millisField.setPreferredSize(new Dimension(75, 25));
        millisField.setMaximumSize(new Dimension(75, 25));
        millisField.addActionListener(e -> {
            try {
                time = Integer.parseInt(millisField.getText());
                wrapper.getManager().getFrame().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                millisField.setText(time + "");
            }
        });
        millisField.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(millisField, gbc);

        listener = e -> {
            action = e.getActionCommand();
            millisField.setEnabled("delay".equals(action));
        };

        JLabel label = new JLabel("\u2022 Wait for: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label, gbc);

        ButtonGroup group = new ButtonGroup();

        gbc.insets = new Insets(5, 15, 5, 5);
        JRadioButton pidButton = createRadioButton("PID Loop", "waitForPID();");
        pidButton.setSelected(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(pidButton, gbc);
        group.add(pidButton);

        JRadioButton liftButton = createRadioButton("Lift movement", "waitForLift();");
        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(liftButton, gbc);
        group.add(liftButton);

        JRadioButton clawButton = createRadioButton("Claw movement", "waitForClaw();");
        gbc.gridx = 0;
        gbc.gridy = 3;
        content.add(clawButton, gbc);
        group.add(clawButton);

        JRadioButton delayButton = createRadioButton("Millis wait", "delay");
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
    public String renderCode() {
        return (!action.equals("delay") ? action : String.format("wait1MSec(%d);", time))
                + " // " + getWrapper().getActionName();
    }
}
