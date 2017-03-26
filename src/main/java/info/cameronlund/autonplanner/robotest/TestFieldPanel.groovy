package info.cameronlund.autonplanner.robotest

import info.cameronlund.autonplanner.panels.FieldPanel

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class TestFieldPanel extends FieldPanel {
    private BufferedImage fieldImage
    def lines = [new Line(0, 0, 687, 687, 5), new Line(0, 575, 575, 0, 5)]
    def points = [];

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
    }

    @Override
    void resetField() {

    }

    void test(TestRobot r) {
        while (r.getPosY() > 50) {
            points.add(new DPoint(r.posX + r.ghostX, r.posY + r.ghostY));
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
                            r.getGhostX())
                    // Get the b of the line that is perp to l and intersects rPoint
                    def b = (-1 * l.perpSlope * rPoint.x) + (rPoint.y)
                    // Find the point on the line that this perp hits
                    def point = l.getPoint(getIntersectY(l, l.perpSlope, b));
                    if (Math.abs(l.getPoint(point.x).getDistance(rPoint)) > 2)
                        r.moveGhost(Math.atan(l.getPerpSlope()), l.getPoint(point.x).getDistance(rPoint))
                    break
                }
            }
            sleep(100)
        }
    }

    // Returns y value of intersection of two lines
    private int getIntersectY(Line l1, slope2, b2) {
        double ratio = (l1.getSlope() / slope2);
        return ((l1.getyIntercept() - (ratio * b2)) / (1 - ratio));
    }
}

class Line {
    int startX
    int endX
    int startY
    int endY
    int detectionRadius
    double yIntercept
    double slope

    Line(int startX, int startY, int endX, int endY, int detectionRadius) {
        this.startX = startX
        this.endX = endX
        this.startY = startY
        this.endY = endY
        this.detectionRadius = detectionRadius
        slope = (endY - startY) / (endX - startX) // Find slope
        yIntercept = (startY - slope * startX) // Find y-intercept
    }

    void paintLine(Graphics g) {
        Graphics2D g2 = g as Graphics2D
        g2.setColor(Color.white)
        g2.setStroke(new BasicStroke(4))
        g2.drawLine(startX, startY, endX, endY)
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
        x = (y - yIntercept) / slope
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
}