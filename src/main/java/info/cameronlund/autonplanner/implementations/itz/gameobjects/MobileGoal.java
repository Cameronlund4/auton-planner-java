package info.cameronlund.autonplanner.implementations.itz.gameobjects;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

// TODO Fix variables, draw the right color for the right goal type
public class MobileGoal extends GameObject {
    private static BufferedImage mgImageBlue;
    private static BufferedImage mgImageRed;

    private static int mgImageWidth;
    private static int mgImageHeight;

    private boolean color;
    private Font cubeFont = new Font("default", Font.BOLD, 16);

    // true = blue; false = red
    public MobileGoal(int type_id, boolean color) {
        super("Mogo", type_id);
        this.color = color;
    }

    public static void initializeImage(BufferedImage _cubePathBlue, BufferedImage _cubePathRed) {
        mgImageBlue = _cubePathBlue;
        mgImageRed = _cubePathRed;

        mgImageWidth = mgImageBlue.getWidth();
        mgImageHeight = mgImageBlue.getHeight();
    }

    public static BufferedImage getMgImageBlue() {
        return mgImageBlue;
    }

    public static BufferedImage getMgImageRed() {
        return mgImageRed;
    }

    public static int getMgImageWidth() {
        return mgImageWidth;
    }

    public static int getMgImageHeight() {
        return mgImageHeight;
    }

    @Override
    public void draw(ImageObserver observer, Graphics g, int centerX, int centerY) {
        if (!isOnField() || (isSkillsOnly() && !AutonPlanner.isSkill()))
            return;
        g.drawImage(color ? mgImageRed : mgImageBlue, centerX - (mgImageWidth / 2),
                centerY - (mgImageHeight / 2), observer);
        int x = centerX - (getTypeId() > 9 ? 10 : 5);
        int y = centerY + 23;
        g.setFont(cubeFont);
        g.setColor(Color.WHITE);
        // Outline cause stars don't contrast well and cubes should match em
        g.drawString(getTypeId() + "", x - 1, y - 1);
        g.drawString(getTypeId() + "", x - 1, y + 1);
        g.drawString(getTypeId() + "", x + 1, y - 1);
        g.drawString(getTypeId() + "", x + 1, y + 1);
        // Now draw the actual number
        g.setColor(Color.BLACK);
        g.drawString(getTypeId() + "", x, y);
    }
}
