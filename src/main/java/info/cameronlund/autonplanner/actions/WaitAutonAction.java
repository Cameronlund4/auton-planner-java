package info.cameronlund.autonplanner.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WaitAutonAction extends AutonAction {
    private String action = "waitForPID();";
    private ActionListener listener;

    public WaitAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);

        listener = e ->
                action = e.getActionCommand();

        setColor(Color.GREEN);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

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
        return action;
    }
}
