package info.cameronlund.autonplanner.robotest

import info.cameronlund.autonplanner.robot.Robot

import java.awt.*
import java.util.List

class TestRobot extends Robot {
    double ghostX
    double ghostY
    List lines
    List lineSensors = [new DPoint(-37, -37), new DPoint(-37, 37),
                        new DPoint(37, -37), new DPoint(37, 37)]

    TestRobot(List lines) {
        this.lines = lines
        setResting(400, 515)
        returnToResting()
        def rand = new Random()
        ghostX = rand.nextInt(100) - 50
        ghostY = rand.nextInt(100) - 50
    }

    void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g

        // Draw actual position
        g2.setColor(Color.WHITE)
        Rectangle bot = new Rectangle((int) posX - 41, (int) posY - 41, 82, 82)
        Rectangle posMarkerL = new Rectangle((int) posX - 41, (int) posY - 41, 10, 10)
        Rectangle posMarkerR = new Rectangle((int) posX + 31, (int) posY - 41, 10, 10)

        if (rotation != 0.0)
            g2.rotate(Math.toRadians(getRotation()), posX, posY) // Rotate on the robots center
        g2.draw(bot)
        g2.fill(posMarkerL)
        g2.fill(posMarkerR)
        // Draw the line sensors
        g2.setColor(isOnLine(0) ? Color.GREEN : Color.RED)
        g2.drawOval((int) posX - 40, (int) posY - 40, 5, 5)
        g2.setColor(isOnLine(1) ? Color.GREEN : Color.RED)
        g2.drawOval((int) posX - 40, (int) posY + 35, 5, 5)
        g2.setColor(isOnLine(2) ? Color.GREEN : Color.RED)
        g2.drawOval((int) posX + 35, (int) posY - 40, 5, 5)
        g2.setColor(isOnLine(3) ? Color.GREEN : Color.RED)
        g2.drawOval((int) posX + 35, (int) posY + 35, 5, 5)
        g2.rotate(Math.toRadians(-1 * getRotation()), posX, posY) // Rotate on the robots center

        // Draw ghost position
        g2.setColor(Color.YELLOW)
        bot = new Rectangle((int) (getPosX() + ghostX - 41), (int) (getPosY() + ghostY - 41), 82, 82)
        posMarkerL = new Rectangle((int) (getPosX() + ghostX - 41), (int) (getPosY() + ghostY - 41), 10, 10)
        posMarkerR = new Rectangle((int) (getPosX() + ghostX + 31), (int) (getPosY() + ghostY - 41), 10, 10)

        if (rotation != 0.0)
            g2.rotate(Math.toRadians(getRotation()), getPosX() + ghostX, getPosY() + ghostY)
        // Rotate on the robots center
        g2.draw(bot)
        g2.fill(posMarkerL)
        g2.fill(posMarkerR)
        g2.rotate(Math.toRadians(-1 * getRotation()), getPosX() + ghostX, getPosY() + ghostY)
        // Rotate on the robots center
    }

    boolean isOnLine(int i) {
        def sensor = lineSensors.get(i) as DPoint
        boolean isOnLine = false
        for (def l : getLines()) {
            def li = l as Line
            double y = li.getPoint(getPosX() + sensor.x).y
            if (Math.abs(y - (sensor.y + getPosY())) <= li.getDetectionRadius()) isOnLine = true
        }
        return isOnLine
    }

    Line getOnLine(int i) {
        def sensor = lineSensors.get(i) as DPoint
        for (def l : getLines()) {
            def li = l as Line
            double y = li.getPoint(getPosX() + sensor.x).y
            if (Math.abs(y - (sensor.y + getPosY())) <= li.getDetectionRadius()) return li
        }
        return null
    }

    void moveGhost(angle, distance) {
        ghostX += Math.cos(angle) * distance; //  - (Math.PI / 2)
        ghostY += Math.sin(angle) * distance;
    }
}