package info.cameronlund.autonplanner.implementations.itz.panels;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.helpers.FieldPositionHelper;
import info.cameronlund.autonplanner.implementations.itz.gameobjects.Cone;
import info.cameronlund.autonplanner.implementations.itz.gameobjects.MobileGoal;
import info.cameronlund.autonplanner.panels.FieldPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ITZFieldPanel extends FieldPanel{

    private BufferedImage fieldImage;
    private Cone[] cones = new Cone[65];
    private MobileGoal[] mogos = new MobileGoal[8];
    //private NearZone nearZone;
    //private FarZone farZone;

    public ITZFieldPanel(String fieldPath, String redMgPath, String blueMgPath, String conePath) {
        // Set up the images we need
        try {
            fieldImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fieldPath));
            MobileGoal.initializeImage(ImageIO.read(getClass().getClassLoader().getResourceAsStream(redMgPath)),
                                       ImageIO.read(getClass().getClassLoader().getResourceAsStream(blueMgPath)));
            Cone.initializeImage(ImageIO.read(getClass().getClassLoader().getResourceAsStream(conePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize cones 1-65
        for (int i = 0; i < 65; i++)
            cones[i] = new Cone(i + 1);

        // initialize mobile goals 1-6
        for (int i = 0; i < 8; i++)
            mogos[i] = new MobileGoal(i + 1, i < 4);

        // To save this classes beauty, use a helper for setting positions
        FieldPositionHelper.setConePositions(cones);
        FieldPositionHelper.setMogoPositions(mogos);

        //nearZone = new NearZone();
        //farZone = new FarZone();
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

        // Draw all the cones
        for (GameObject star : cones)
            star.draw(this, g);

        // Draw all the mogos
        for (GameObject cube : mogos)
            cube.draw(this, g);

        //nearZone.paint(this, g);
        //farZone.paint(this, g);
    }

    @Override
    public void resetField() {
        robot.returnToResting();
        //nearZone.clear();
        //farZone.clear();
        FieldPositionHelper.setConePositions(cones);
        FieldPositionHelper.setMogoPositions(mogos);
    }

    public Cone getStar(int id) {
        return cones[id - 1];
    }

    public MobileGoal getCube(int id) {
        return mogos[id - 1];
    }

}
