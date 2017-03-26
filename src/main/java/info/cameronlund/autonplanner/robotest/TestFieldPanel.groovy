package info.cameronlund.autonplanner.robotest

import info.cameronlund.autonplanner.panels.FieldPanel

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class TestFieldPanel extends FieldPanel {
    private BufferedImage fieldImage
    def lines = [new Line(0, 0, 687, 687, 1), new Line(0, 575, 575, 0, 1)]
    def points = [];
    def ypoints = [];
    def ppoints = [];

    TestFieldPanel() {
        super.robot = new TestRobot(lines)
        // Set up the images we need
        try {
            fieldImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("test_field.png"))
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    @Override
    void paintField(Graphics g) {
        g.drawImage(fieldImage, 0, 0, this)
        lines.each { it.paintLine(g) }
        g.setColor(Color.blue)
        points.each { int x = it.x; int y = it.y; g.drawOval(x, y, 3, 3); }
        g.setColor(Color.yellow)
        ypoints.each { int x = it.x; int y = it.y; g.drawOval(x, y, 3, 3); }
        g.setColor(Color.magenta)
        ppoints.each { int x = it.x; int y = it.y; g.drawOval(x, y, 3, 3); }

    }

    @Override
    void resetField() {

    }

    void test(TestRobot r) {
        while (true) {
            while (r.getPosY() > 50) {
                // Move robot 1 pixel
                r.movePixels(1)
                repaint()
                for (int i = 0; i < 3; i++) {
                    if (r.isOnLine(i)) {
                        // Get the line we are on
                        Line l = r.getOnLine(i)
                        // Get the line sensor that hit's robot offset
                        def sPoint = r.getLineSensors().get(i)
                        // Get the pos of that line sensor on the ghost
                        def rPoint = new DPoint(sPoint.x + r.getPosX() + r.getGhostX(), sPoint.y + r.getPosY() +
                                r.getGhostY())
                        // Get the b of the line that is perp to l and intersects rPoint
                        double b = (-1 * l.perpSlope * rPoint.x) + (rPoint.y)
                        // Find the point on the line that this perp hits
                        def point = l.getPointY(getIntersectY(l, l.perpSlope, b))
                        if (Math.abs(point.getDistance(rPoint)) > 1) {
                            r.ghostX = point.x - r.getPosX() - sPoint.x;
                            r.ghostY = point.y - r.getPosY() - sPoint.y
                        }
                        break
                    }
                }
                sleep(20)
            }
            sleep(1000);
            r.returnToResting();
        }
    }

    // Returns y value of intersection of two lines
    private double getIntersectY(Line l1, slope2, b2) {
        double ratio = (l1.getSlope() / slope2);
        return ((l1.getyIntercept() - (ratio * b2)) / (1 - ratio));
    }

    double getAngle(DPoint one, DPoint two) {
        double angle = Math.atan2(one.y - two.y, one.x - two.x);

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}

class Line {
    double startX
    double endX
    double startY
    double endY
    double detectionRadius
    double yIntercept
    double slope

    Line(double startX, double startY, double endX, double endY, double detectionRadius) {
        this.startX = startX
        this.endX = endX
        this.startY = startY
        this.endY = endY
        this.detectionRadius = detectionRadius
        slope = (double) (endY - startY) / (double) (endX - startX) // Find slope
        yIntercept = (double) (startY - slope * (double) startX) // Find y-intercept
    }

    void paintLine(Graphics g) {
        Graphics2D g2 = g as Graphics2D
        g2.setColor(Color.white)
        g2.setStroke(new BasicStroke(4))
        g2.drawLine(startX as int, startY as int, endX as int, endY as int)
    }

    double getSlope() {
        return slope
    }

    double getPerpSlope() {
        return 1 / slope * -1
    }

    DPoint getPoint(double x) {
        double y = slope * x + yIntercept
        return new DPoint(x, y)
    }

    DPoint getPointY(double y) {
        double x = (y - yIntercept) / slope as double
        return new DPoint(x, y)
    }
}

class DPoint {
    public final double x, y

    DPoint(double x, double y) {
        this.x = x
        this.y = y
    }

    double deltaX(DPoint point) {
        return x - point.x
    }

    double deltaY(DPoint point) {
        return y - point.y
    }

    double getDistance(DPoint point) {
        return Math.sqrt(Math.pow((y - point.y), 2) + Math.pow((x - point.x), 2))
    }

    String toString() {
        return "[X: " + x + " Y: " + y + "]";
    }
}