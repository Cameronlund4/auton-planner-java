package info.cameronlund.autonplanner.zones;

import info.cameronlund.autonplanner.gameobjects.Cube;
import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.gameobjects.Star;

import java.awt.*;
import java.awt.image.ImageObserver;

public class NearZone extends ScoringZone {

    public NearZone() {
        super("Near zone");
    }

    @Override
    public void paint(ImageObserver observer, Graphics g) {
        int startingY = 246;
        int starCount = 0;
        int cubeCount = 0;
        for (GameObject object : getScoredObjects()) {
            if (object instanceof Star)
                starCount += 1;
            if (object instanceof Cube)
                cubeCount += 1;
        }

        g.setColor(Color.white);

        // Draw cube count and score
        g.drawImage(Cube.getCubeImage(), 303 - (Cube.getCubeImageWidth() / 2),
                startingY - 4 - (Cube.getCubeImageHeight()), observer);
        g.drawString("Scored: " + cubeCount, 343, (startingY - (Cube.getCubeImageHeight() / 2)) - 10);
        g.drawString("  (" + cubeCount * 2 + " pts)", 343, (startingY - 4 - (Cube.getCubeImageHeight() / 2)) + 10);

        // Draw star count and score
        g.drawImage(Star.getStarImage(), 303 - (Star.getStarImageWidth() / 2),
                startingY + 4, observer);
        g.drawString("Scored: " + starCount, 343, (startingY + (Cube.getCubeImageHeight() / 2)));
        g.drawString("  (" + starCount + " pts)", 343, (startingY + (Cube.getCubeImageHeight() / 2)) + 20);

        // Draw pretty ] shape
        Graphics2D g2 = (Graphics2D) g;
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g2.drawLine(420, (startingY - (Cube.getCubeImageHeight() / 2)) - 25, 425,
                (startingY - (Cube.getCubeImageHeight() / 2)) - 25);
        g2.drawLine(425, (startingY - (Cube.getCubeImageHeight() / 2)) - 25, 425,
                (startingY + (Cube.getCubeImageHeight() / 2)) + 25);
        g2.drawLine(420, (startingY + (Cube.getCubeImageHeight() / 2)) + 25, 425,
                (startingY + (Cube.getCubeImageHeight() / 2)) + 25);

        // Draw total points count
        g.drawString("(" + ((starCount) + (cubeCount * 2)) + " total pts)", 430, (startingY + (Cube.getCubeImageHeight() / 2)) + 22);
    }
}
