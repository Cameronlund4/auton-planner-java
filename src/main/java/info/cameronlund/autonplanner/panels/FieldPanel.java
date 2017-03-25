package info.cameronlund.autonplanner.panels;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.actions.*;
import info.cameronlund.autonplanner.gameobjects.Cube;
import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.gameobjects.Star;
import info.cameronlund.autonplanner.helpers.FieldPositionHelper;
import info.cameronlund.autonplanner.listeners.FieldClickListener;
import info.cameronlund.autonplanner.robot.Robot;
import info.cameronlund.autonplanner.zones.FarZone;
import info.cameronlund.autonplanner.zones.NearZone;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public abstract class FieldPanel extends JPanel {
    protected Robot robot = new Robot();
    private ActionManager manager;
    private ArrayList<GameObject> scored = new ArrayList<>();

    public FieldPanel() {
        // Set up our graphics
        setPreferredSize(new Dimension(687, 687));
        new Test(this);
    }

    // Essentially an alias, improves readability
    public void addFieldClickListener(FieldClickListener listener) {
        addMouseListener(listener);
    }

    public void paintComponent(Graphics g) {
        reset(); // Reset everything
        paintField(g);
        robot.paint(g);
    }

    public abstract void paintField(Graphics g);

    private void reset() {
        robot.getInventory().clear();
        scored = new ArrayList<>();
        resetField();
    }

    public abstract void resetField();

    public void loadToRobot(GameObject object) {
        robot.getInventory().scoreGameObject(object);
        object.setOnField(false);
    }

    public Robot getRobot() {
        return robot;
    }

    public void setManager(ActionManager manager) {
        this.manager = manager;
    }

    public class Test {
        Test(FieldPanel panel) {
            new FieldClickListener(panel) {
                @Override
                public void fieldClicked(int x, int y, int button) {
                    // Get all the info we need
                    int x1 = robot.getPosX();
                    int y1 = robot.getPosY();
                    System.out.println("X: " + x + " Y: " + y + " X1: " + x1 + " Y1: " + y1);

                    double robotRot = Math.toRadians(robot.getRotation());
                    System.out.println("Robot rot: " + robotRot + "," + Math.toDegrees(robotRot));
                    double lineRot = Math.atan((double) (y1 - y) / (double) (x1 - x)) + (Math.PI / 2);
                    if (x1 > x)
                        lineRot += Math.PI;
                    while (Math.abs(lineRot) > Math.PI) {
                        double lineRotTemp = lineRot;
                        lineRot = (lineRot - 2*Math.PI);
                        System.out.println("Math.PI: "+Math.PI+" Input: " + lineRotTemp + " Output: " + lineRot);
                    }
                    System.out.println("Line rot: " + lineRot + "," + Math.toDegrees(lineRot));
                    int distance = (int) (Math.sqrt(Math.pow(y - y1, 2) + Math.pow(x - x1, 2)) / 2) * 24;

                    // Create the turn to the line
                    AutonActionWrapper turnWrapper = manager.createNewAction();
                    turnWrapper.setType(ActionType.TURN);
                    ((TurnAutonAction) turnWrapper.getAction())
                            .setAngleDelta((float) Math.toDegrees(lineRot - robotRot));
                    manager.addAfterSelected(turnWrapper);
                    manager.setSelected(turnWrapper);

                    if (button == MouseEvent.BUTTON1) {
                        // Create the drive (166.666666667/2)*12
                        AutonActionWrapper driveWrapper = manager.createNewAction();
                        driveWrapper.setType(ActionType.DRIVE);
                        ((DriveAutonAction) driveWrapper.getAction())
                                .setDistance(distance);
                        manager.addAfterSelected(driveWrapper);
                        manager.setSelected(driveWrapper);
                    }
                }

            };
        }
    }

    protected ActionManager getManager() {
        return manager;
    }
}
