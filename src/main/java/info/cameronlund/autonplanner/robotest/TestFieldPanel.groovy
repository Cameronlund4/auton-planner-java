package info.cameronlund.autonplanner.robotest

import info.cameronlund.autonplanner.panels.FieldPanel

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class TestFieldPanel extends FieldPanel {
    private BufferedImage fieldImage
    def lines = [new Line(0, 0, 687, 687, 5), new Line(0, 575, 575, 0, 5)]

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
    }

    @Override
    void resetField() {

    }

    void test(TestRobot r) {
        while (r.getPosY() > 50) {
            // Move robot 1 pixel
            r.movePixels(1);
            repaint();
            for (int i = 0; i < 3; i++) {
                if (r.isOnLine(i)) {
                    Line l = r.getOnLine(i);
                    def sPoint = r.getLineSensors().get(i);
                    def rPoint = new DPoint(sPoint.x + r.getPosX(), sPoint.y + r.getPosY())
                    r.moveGhost(Math.atan(l.getPerpSlope()), l.getPoint(rPoint.x).getDistance(rPoint));
                    break;
                }
            }
            sleep(100)
        }
    }
}

class Line {
    int startX
    int endX
    int startY
    int endY
    int detectionRadius
    double yIntercept
    double slope;

    Line(int startX, int startY, int endX, int endY, int detectionRadius) {
        this.startX = startX
        this.endX = endX
        this.startY = startY
        this.endY = endY
        this.detectionRadius = detectionRadius
        slope = (endY - startY) / (endX - startX); // Find slope
        yIntercept = (startY - slope * startX); // Find y-intercept
    }

    void paintLine(Graphics g) {
        Graphics2D g2 = g as Graphics2D
        g2.setColor(Color.white)
        g2.setStroke(new BasicStroke(4))
        g2.drawLine(startX, startY, endX, endY)
    }

    double getSlope() {
        return slope;
    }

    double getPerpSlope() {
        return 1 / slope * -1;
    }

    DPoint getPoint(double x) {
        double y = slope * x + yIntercept;
        return new DPoint(x, y);
    }
}

public class DPoint {
    public final double x, y;

    DPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double deltaX(DPoint point) {
        return x - point.x;
    }

    double deltaY(DPoint point) {
        return y - point.y
    }

    double getDistance(DPoint point) {
        return Math.sqrt(Math.pow((y - point.y), 2) + Math.pow((x - point.x), 2));
    }
}