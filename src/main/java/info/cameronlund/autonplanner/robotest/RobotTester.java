package info.cameronlund.autonplanner.robotest;

import info.cameronlund.autonplanner.actions.ActionManager;
import info.cameronlund.autonplanner.panels.FieldPanel;

import javax.swing.*;
import java.awt.*;

public class RobotTester {
    private int actionId = 0;
    private FieldPanel fieldPanel;

    RobotTester() {
        // Main frame for the project
        JFrame frame = new JFrame("[2616E] Robot Tester");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(921, 966));
        frame.setResizable(false);
        frame.setMinimumSize(frame.getPreferredSize());
        BorderLayout layout = (BorderLayout) frame.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1393, 715));
        contentPanel.setMinimumSize(contentPanel.getPreferredSize());
        contentPanel.setLayout(new BorderLayout());
        layout = (BorderLayout) contentPanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        fieldPanel = new TestFieldPanel();
        fieldPanel.setManager(new ActionManager(frame));

        // Main panel containing field and list of moves
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        layout = (BorderLayout) mainPanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        // Menu creation ------------
        JMenu exampleMenu = new JMenu("Example");

        JMenuItem exampleMenuItem = new JMenuItem("Sub Example");
        exampleMenuItem.setActionCommand("Sub Example");
        exampleMenuItem.addActionListener(l -> {
            System.out.println("You have tested the example.");
        });
        exampleMenu.add(exampleMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(exampleMenu);
        // End menu creation ---------

        // Add our content to the frame
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Display frame
        frame.pack();
        frame.setVisible(true);

        new Thread(new SensorAlignCode((ProsTestRobot) fieldPanel.getRobot())).start();
        ((TestFieldPanel) fieldPanel).test((ProsTestRobot) fieldPanel.getRobot());
    }
}
