package info.cameronlund.autonplanner.implementations.starstruck.gameobjects;

import info.cameronlund.autonplanner.AutonPlanner;
import info.cameronlund.autonplanner.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Star extends GameObject {
    private static BufferedImage starImage;
    private static int starImageWidth;
    private static int starImageHeight;
    private Font starFont = new Font("default", Font.BOLD, 16);

    public Star(int type_id) {
        super("Star", type_id);
    }

    public static void initializeImage(BufferedImage _starImage) {
        starImage = _starImage;
        starImageWidth = starImage.getWidth();
        starImageHeight = starImage.getHeight();
    }

    public static BufferedImage getStarImage() {
        return starImage;
    }

    public static int getStarImageWidth() {
        return starImageWidth;
    }

    public static int getStarImageHeight() {
        return starImageHeight;
    }

    @Override
    public void draw(ImageObserver observer, Graphics g, int centerX, int centerY) {
        if (!isOnField() || (isSkillsOnly() && !AutonPlanner.isSkill()))
            return;
        g.drawImage(starImage, centerX - (starImageWidth / 2), centerY - (starImageHeight / 2), observer);
        int x = centerX - (getTypeId() > 9 ? 9 : 4);
        int y = centerY + 8;
        g.setFont(starFont);
        g.setColor(Color.BLUE);
        // Outline cause stars don't contrast well
        g.drawString(getTypeId() + "", x - 1, y - 1);
        g.drawString(getTypeId() + "", x - 1, y + 1);
        g.drawString(getTypeId() + "", x + 1, y - 1);
        g.drawString(getTypeId() + "", x + 1, y + 1);
        // Now draw the actual number
        g.setColor(Color.WHITE);
        g.drawString(getTypeId() + "", x, y);
    }
}
