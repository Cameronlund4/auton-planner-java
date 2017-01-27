package info.cameronlund.autonplanner.panels;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.actions.ActionManager;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class FieldPanel extends JPanel {
    private HashMap<ArrayList<Point>, Color> lines = new HashMap<>();
    private BufferedImage fieldImage;
    private Star[] stars = new Star[24];
    private Cube[] cubes = new Cube[4];
    private NearZone nearZone;
    private FarZone farZone;
    private Robot robot = new Robot();
    private ActionManager manager;
    private ArrayList<GameObject> scored = new ArrayList<>();

    public FieldPanel(String fieldPath, String cubePath, String starPath) {
        // Set up our graphics
        setPreferredSize(new Dimension(687, 687));
        new Test(this);

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

    public Star getStar(int id) {
        return stars[id - 1];
    }

    public Cube getCube(int id) {
        return cubes[id - 1];
    }

    // Essentially an alias, improves readability
    public void addFieldClickListener(FieldClickListener listener) {
        addMouseListener(listener);
    }

    public void paintComponent(Graphics g) {
        reset(); // Reset everything

        // If preload should be loaded, load it
        if (AutonPlanner.isPreloadLoaded())
            loadToRobot(getStar(1));

        // Set the robot starting stuff
        robot.setRotation(AutonPlanner.getStartingRotation());
        // TODO: Render all the actions

        // Ensure the field image exists
        if (fieldImage == null)
            return;
        // Draw the field
        g.drawImage(fieldImage, 0, 0, this);

        // Draw whatever actions we want
        manager.paint(g, robot);

        // Draw all the stars
        for (GameObject star : stars)
            star.draw(this, g);

        // Draw all the cubes
        for (GameObject cube : cubes)
            cube.draw(this, g);

        nearZone.paint(this, g);
        farZone.paint(this, g);

        robot.paint(g);

        for (Map.Entry<ArrayList<Point>, Color> set : lines.entrySet()) {
            g.setColor(set.getValue());
            g.drawLine((int) set.getKey().get(0).getX(), (int) set.getKey().get(0).getY(),
                    (int) set.getKey().get(1).getX(), (int) set.getKey().get(1).getY());
        }
    }

    public void reset() {
        robot.returnToResting();
        robot.getInventory().clear();
        scored = new ArrayList<>();
        nearZone.clear();
        farZone.clear();
        FieldPositionHelper.setStarPositions(stars);
        FieldPositionHelper.setCubePositions(cubes);
    }

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
        private Point pos1;
        private final Random r = new Random();

        public Test(FieldPanel panel) {
            new FieldClickListener(panel) {
                @Override
                public void fieldClicked(int x, int y) {
                    if (pos1 == null) {
                        pos1 = new Point(x, y);
                    } else {
                        ArrayList<Point> points = new ArrayList<>();
                        points.add(pos1);
                        pos1 = new Point(x, y);
                        points.add(pos1);
                        lines.put(points, Color.RED);
                        repaint();
                    }
                }
            };
        }
    }
}
