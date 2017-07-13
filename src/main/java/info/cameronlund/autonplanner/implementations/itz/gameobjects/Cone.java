package info.cameronlund.autonplanner.implementations.itz.gameobjects;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Cone extends GameObject {
    private static BufferedImage coneImage;

    private static int coneImageWidth;
    private static int coneImageHeight;
    private Font coneFont = new Font("default", Font.BOLD, 16);

    public Cone(int type_id) {
        super("Cone", type_id);
    }

    public static void initializeImage(BufferedImage _conePath) {
        coneImage = _conePath;
        coneImageWidth = coneImage.getWidth();
        coneImageHeight = coneImage.getHeight();
    }

    public static BufferedImage getConeImage() {
        return coneImage;
    }

    public static int getConeImageWidth() {
        return coneImageWidth;
    }

    public static int getConeImageHeight() {
        return coneImageHeight;
    }

    @Override
    public void draw(ImageObserver observer, Graphics g, int centerX, int centerY) {
        if (!isOnField() || (isSkillsOnly() && !AutonPlanner.isSkill()))
            return;
        g.drawImage(coneImage, centerX - (coneImageWidth / 2), centerY - (coneImageHeight / 2), observer);
        int x = centerX - (getTypeId() > 9 ? 10 : 5);
        int y = centerY + 23;
        int bottomDistance = 687 - y;
        if (bottomDistance < 5)
            y = 687 - 5;
        g.setFont(coneFont);
        g.setColor(Color.WHITE);
        // Outline cause cones (probably) don't contrast well (I mean I haven't checked, but lets be honest it is vex yellow)
        g.drawString(getTypeId() + "", x - 1, y - 1);
        g.drawString(getTypeId() + "", x - 1, y + 1);
        g.drawString(getTypeId() + "", x + 1, y - 1);
        g.drawString(getTypeId() + "", x + 1, y + 1);
        // Now draw the actual number
        g.setColor(Color.BLACK);
        g.drawString(getTypeId() + "", x, y);
    }
}
