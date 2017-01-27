package info.cameronlund.autonplanner.robot;

import info.cameronlund.autonplanner.gameobjects.Cube;
import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.gameobjects.Star;
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
        int starCount = 0;
        int cubeCount = 0;

        for (GameObject object : getScoredObjects()) {
            if (object instanceof Star)
                starCount += 1;
            if (object instanceof Cube)
                cubeCount += 1;
        }
        int posX = robot.getPosX();
        int posY = robot.getPosY();

        g2.setFont(new Font("default",Font.PLAIN,12));
        g2.drawString("Stars: "+starCount,posX-38,posY+26);
        g2.drawString("Cubes: "+cubeCount,posX-38,posY+38);
    }
}
