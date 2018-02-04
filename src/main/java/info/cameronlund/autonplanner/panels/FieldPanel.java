package info.cameronlund.autonplanner.panels;

import info.cameronlund.autonplanner.actions.ActionManager;
import info.cameronlund.autonplanner.actions.AutonActionWrapper;
import info.cameronlund.autonplanner.actions.DriveAutonAction;
import info.cameronlund.autonplanner.actions.OdomTargetAction;
import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.listeners.FieldClickListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public abstract class FieldPanel extends JPanel {
    protected Robot robot = new Robot();
    private ActionManager manager;
    private ArrayList<GameObject> scored = new ArrayList<>();
    private int[] adjustAngles = new int[] {-270, -180, -90, 0, 90, 180, 270};

    public FieldPanel(int size) {
        // Set up our graphics
        setPreferredSize(new Dimension(size, size));
        new Test(this);
    }

    public FieldPanel() {
        this(687);
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
//                    // Get all the info we need
//                    int x1 = (int) robot.getPosX();
//                    int y1 = (int) robot.getPosY();
//                    System.out.println("X: " + x + " Y: " + y + " X1: " + x1 + " Y1: " + y1);
//
//                    double robotRot = Math.toRadians(robot.getRotation());
//                    System.out.println("Robot rot: " + robotRot + "," + Math.toDegrees(robotRot));
//                    double lineRot = Math.atan((double) (y1 - y) / (double) (x1 - x)) + (Math.PI / 2);
//                    if (x1 > x)
//                        lineRot += Math.PI;
//                    while (Math.abs(lineRot) > Math.PI) {
//                        double lineRotTemp = lineRot;
//                        lineRot = (lineRot - 2*Math.PI);
//                        System.out.println("Math.PI: "+Math.PI+" Input: " + lineRotTemp + " Output: " + lineRot);
//                    }
//                    System.out.println("Line rot: " + lineRot + "," + Math.toDegrees(lineRot));
//                    int distance = (int) (Math.sqrt(Math.pow(y - y1, 2) + Math.pow(x - x1, 2)) / 2) * 12;
//
//                    // Create the turn to the line
//                    AutonActionWrapper turnWrapper = manager.createNewAction();
//                    turnWrapper.setType("Turn");
//                    ((TurnAutonAction) turnWrapper.getAction())
//                            .setAngleDelta((float) Math.toDegrees(lineRot - robotRot));
//                    manager.addAfterSelected(turnWrapper);
//                    manager.setSelected(turnWrapper);
//
//                    if (button == MouseEvent.BUTTON1) {
//                        // Create the drive (166.666666667/2)*12
//                        AutonActionWrapper driveWrapper = manager.createNewAction();
//                        driveWrapper.setType("Drive");
//                        ((DriveAutonAction) driveWrapper.getAction())
//                                .setDistance(distance);
//                        manager.addAfterSelected(driveWrapper);
//                        manager.setSelected(driveWrapper);
//                    }
                    if (button == MouseEvent.BUTTON1) {
                        // Create the drive (166.666666667/2)*12
                        AutonActionWrapper odomWrapper = manager.createNewAction();
                        odomWrapper.setType("Point");
                        int desiredAngle = (int) Math.round((Math.atan((y-robot.getPosY())/(x-robot.getPosX())) * 180 / Math.PI) + 90);
                        int angleDiff = (int) Math.round(robot.getRotation() - desiredAngle);
                        System.out.println(angleDiff);
                        System.out.println(((Math.atan((y-robot.getPosY())/(x-robot.getPosX())) * 180 / Math.PI) + 90));
                        System.out.println(robot.getRotation());
                        if(Math.abs(angleDiff) <= 5) {
                            double dist = Math.sqrt(Math.pow((y-robot.getPosY()), 2) + Math.pow((x-robot.getPosX()), 2));
                            System.out.println("Straightening");
                            int angleRot = -90;
                            // If going right
                            if((desiredAngle%360 > 0 && desiredAngle%360 < 180) ||
                                    desiredAngle%360 < 0 && desiredAngle%360 > -180) {
                                // If going up
                                if((desiredAngle%360 > -90 && desiredAngle%360 < 90) ||
                                        (desiredAngle%360 < -270 && desiredAngle%720 > -540)) {
                                    if(x < robot.getPosX() && y > robot.getPosY())
                                        angleRot = 90;
                                } else { // If going down
                                    if(x < robot.getPosX() && y < robot.getPosY())
                                        angleRot = 90;
                                }
                            } else { // If going left
                                // If going up
                                if((desiredAngle%360 > -90 && desiredAngle%360 < 90) ||
                                        (desiredAngle%360 < -270 && desiredAngle%720 > -540)) {
                                    if(x > robot.getPosX() && y > robot.getPosY())
                                        angleRot = 90;
                                } else { // If going down
                                    if(x > robot.getPosX() && y < robot.getPosY())
                                        angleRot = 90;
                                }
                            }
                            int newX = (int)(robot.getPosX() + Math.round(Math.cos((robot.getRotation() + angleRot) * Math.PI / 180) * dist));
                            int newY = (int) (robot.getPosY() + Math.round(Math.sin((robot.getRotation() + angleRot) * Math.PI / 180) * dist));
                            ((OdomTargetAction) odomWrapper.getAction())
                                    .setX(newX);
                            ((OdomTargetAction) odomWrapper.getAction())
                                    .setY(newY);
                        } else {
                            ((OdomTargetAction) odomWrapper.getAction())
                                    .setX(x);
                            ((OdomTargetAction) odomWrapper.getAction())
                                    .setY(y);
                        }
                        manager.addAfterSelected(odomWrapper);
                        manager.setSelected(odomWrapper);
                    }
                }

            };
        }
    }

    protected ActionManager getManager() {
        return manager;
    }
}
