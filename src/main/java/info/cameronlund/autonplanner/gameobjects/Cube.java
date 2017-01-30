package info.cameronlund.autonplanner.gameobjects;

import info.cameronlund.autonplanner.AutonPlanner;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Cube extends GameObject {
    private static BufferedImage cubeImage;

    private static int cubeImageWidth;
    private static int cubeImageHeight;
    private Font cubeFont = new Font("default", Font.BOLD, 16);

    public Cube(int type_id) {
        super("Cube", type_id);
    }

    public static void initializeImage(BufferedImage _cubePath) {
        cubeImage = _cubePath;
        cubeImageWidth = cubeImage.getWidth();
        cubeImageHeight = cubeImage.getHeight();
    }

    public static BufferedImage getCubeImage() {
        return cubeImage;
    }

    public static int getCubeImageWidth() {
        return cubeImageWidth;
    }

    public static int getCubeImageHeight() {
        return cubeImageHeight;
    }

    @Override
    public void draw(ImageObserver observer, Graphics g, int centerX, int centerY) {
        if (!isOnField() || (isSkillsOnly() && !AutonPlanner.isSkill()))
            return;
        g.drawImage(cubeImage, centerX - (cubeImageWidth / 2), centerY - (cubeImageHeight / 2), observer);
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
