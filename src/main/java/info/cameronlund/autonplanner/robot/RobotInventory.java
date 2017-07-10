package info.cameronlund.autonplanner.robot;

import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.implementations.itz.gameobjects.Cone;
import info.cameronlund.autonplanner.implementations.itz.gameobjects.MobileGoal;
import info.cameronlund.autonplanner.zones.ScoringZone;

import java.awt.*;
import java.awt.image.ImageObserver;

public class RobotInventory extends ScoringZone {
    private Robot robot;

    public RobotInventory(Robot robot) {
        super("Robot");
        this.robot = robot;
    }

    @Override
    public void paint(ImageObserver observer, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        int coneCount = 0;
        int mogoCount = 0;

        for (GameObject object : getScoredObjects()) {
            if (object instanceof MobileGoal) {
                mogoCount += 1;
                // TODO Something like this:
                //coneCount += ((MobileGoal) object).getConeCount();
            }
            if (object instanceof Cone)
                coneCount++;
        }
        int posX = (int) robot.getPosX();
        int posY = (int) robot.getPosY();

        g2.setFont(new Font("default", Font.PLAIN, 12));
        g2.drawString("Mogos: " + coneCount, posX - 38, posY + 26);
        g2.drawString("Cones: " + mogoCount, posX - 38, posY + 38);
    }
}
