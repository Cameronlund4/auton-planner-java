package info.cameronlund.autonplanner.panels;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.gameobjects.Cube;
import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.gameobjects.Star;
import info.cameronlund.autonplanner.helpers.FieldPositionHelper;
import info.cameronlund.autonplanner.zones.FarZone;
import info.cameronlund.autonplanner.zones.NearZone;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StarstruckFieldPanel extends FieldPanel {
    private BufferedImage fieldImage;
    private Star[] stars = new Star[24];
    private Cube[] cubes = new Cube[4];
    private NearZone nearZone;
    private FarZone farZone;

    public StarstruckFieldPanel(String fieldPath, String cubePath, String starPath) {
        // Set up the images we need
        try {
            fieldImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fieldPath));
            Cube.initializeImage(ImageIO.read(getClass().getClassLoader().getResourceAsStream(cubePath)));
            Star.initializeImage(ImageIO.read(getClass().getClassLoader().getResourceAsStream(starPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize stars 1-24
        for (int i = 0; i < 24; i++)
            stars[i] = new Star(i + 1);

        // initialize cubes 1-4
        for (int i = 0; i < 4; i++)
            cubes[i] = new Cube(i + 1);

        // To save this classes beauty, use a helper for setting positions
        FieldPositionHelper.setStarPositions(stars);
        FieldPositionHelper.setCubePositions(cubes);

        nearZone = new NearZone();
        farZone = new FarZone();
    }

    @Override
    public void paintField(Graphics g) {
        // If preload should be loaded, load it
        if (AutonPlanner.isPreloadLoaded())
            loadToRobot(getStar(1));

        // Set the robot starting stuff
        getRobot().setRotation(AutonPlanner.getStartingRotation());
        // TODO: Render all the actions

        // Ensure the field image exists
        if (fieldImage == null)
            return;
        // Draw the field
        g.drawImage(fieldImage, 0, 0, this);

        // Draw whatever actions we want
        getManager().paint(g, getRobot());

        // Draw all the stars
        for (GameObject star : stars)
            star.draw(this, g);

        // Draw all the cubes
        for (GameObject cube : cubes)
            cube.draw(this, g);

        nearZone.paint(this, g);
        farZone.paint(this, g);
    }

    @Override
    public void resetField() {
        robot.returnToResting();
        nearZone.clear();
        farZone.clear();
        FieldPositionHelper.setStarPositions(stars);
        FieldPositionHelper.setCubePositions(cubes);
    }

    public Star getStar(int id) {
        return stars[id - 1];
    }

    public Cube getCube(int id) {
        return cubes[id - 1];
    }
}
