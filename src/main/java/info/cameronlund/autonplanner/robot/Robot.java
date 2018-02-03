package info.cameronlund.autonplanner.robot;

import info.cameronlund.autonplanner.gameobjects.GameObject;
import info.cameronlund.autonplanner.zones.ScoringZone;

import java.awt.*;

public class Robot {
    private double rotation = 45;
    private int restingX = 155;
    private int restingY = 530;
    private int restingRotation = 0;
    private double posX = restingX;
    private double posY = restingY;
    private RobotInventory inventory = new RobotInventory(this);

    public void setResting(int restingX, int restingY) {
        this.restingX = restingX;
        this.restingY = restingY;
    }

    public void setRestingReturn(int restingX, int restingY) {
        setResting(restingX, restingY);
        returnToResting();
    }

    public void returnToResting() {
        posX = restingX;
        posY = restingY;
        rotation = restingRotation;
    }

    public void setPosition(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }


    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void addRotation(double rotation) {
        setRotation(this.rotation + rotation);
    }

    public void subtractRotation(double rotation) {
        setRotation(this.rotation - rotation);
    }

    public void rotateGyro(int ticks, boolean right) {
        rotation += right ? ticks / 10 : ticks / 10 * -1;
    }

    public void movePixels(double pixelDistance) {
        // - (Math.PI / 2) is to make 0 up
        posX += Math.cos(Math.toRadians(rotation) - (Math.PI / 2)) * pixelDistance;
        posY += Math.sin(Math.toRadians(rotation) - (Math.PI / 2)) * pixelDistance;
    }

    public void moveTicks(int ticks) {
        // Pixels = (ticks/12)*2, it's ticks/12 cause I did the math for pixels in inches, not feet (Whoops)
        movePixels((int) ((double) ticks / 12) * 2); // This is direct drive (encoder axle same speed as wheel axle)
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);

        Rectangle bot = new Rectangle((int) posX - 41, (int) posY - 41, 82, 82);
        Rectangle posMarkerL = new Rectangle((int) posX - 41, (int) posY - 41, 10, 10);
        Rectangle posMarkerR = new Rectangle((int) posX + 31, (int) posY - 41, 10, 10);

        if (rotation != 0.0)
            g2.rotate(Math.toRadians(getRotation()), posX, posY); // Rotate on the robots center
        g2.draw(bot);
        g2.fill(posMarkerL);
        g2.fill(posMarkerR);
        inventory.paint(null, g);
        g2.rotate(Math.toRadians(-1 * (getRotation())), posX, posY); // Rotate on the robots center
    }

    public int getRestingRotation() {
        return restingRotation;
    }

    public void setRestingRotation(int restingRotation) {
        this.restingRotation = restingRotation;
    }

    public RobotInventory getInventory() {
        return inventory;
    }

    public void pickup(GameObject object) {
        inventory.scoreGameObject(object);
    }

    public void score(ScoringZone zone) {
        inventory.transferObjects(zone);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getRestingX() {
        return restingX;
    }

    public int getRestingY() {
        return restingY;
    }
}
