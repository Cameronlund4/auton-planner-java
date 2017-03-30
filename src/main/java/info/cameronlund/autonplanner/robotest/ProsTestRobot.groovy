package info.cameronlund.autonplanner.robotest

public class ProsTestRobot extends TestRobot {
    private final Random rand = new Random();
    private static final GYRO_ERROR = 8;
    private static final ENCODER_ERROR = 20;
    private double encoderRight;
    private double encoderLeft;

    ProsTestRobot(List lines) {
        super(lines)
    }

    // Simulate gyroscope
    public int analogRead(SENSOR sensor) {
        switch (sensor) {
            case SENSOR.GYROSCOPE:
                return rand.nextInt(GYRO_ERROR) - (0.5 * GYRO_ERROR) + rotation;
            default:
                0;
        }
    }

    // Simulate encoders
    public void movePixels(double distance) {
        super.movePixels(distance);
        //int ticks = distance / 2 * 12;
        double ticks = (distance / ((4 * Math.PI) * 25.4))*360
        encoderRight += /*(rand.nextInt(ENCODER_ERROR) as double) - (0.5 * ENCODER_ERROR) + */ticks;
        encoderLeft += /*(rand.nextInt(ENCODER_ERROR) as double) - (0.5 * ENCODER_ERROR) + */ticks;
        moveGhost(ghostRot, distance + new Random().nextInt(ENCODER_ERROR) - (0.5 * ENCODER_ERROR))
    }

    public int encoderGet(int encoder) {
        switch (encoder) {
            case 0:
                return encoderLeft;
            case 1:
                return encoderRight;
            default:
                return 0;
        }
    }

    public void encoderReset(int encoder) {
        switch (encoder) {
            case 0:
                encoderLeft = 0;
                break;
            case 1:
                encoderRight = 0;
                break;
        }
    }

    private Thread drivingThread;
    private int speed;

    public void drive(int speedLeft, speedRight) {

        int pixelDistance = (((((speed / 127) * 100) / 60 / 1000) * 20) * 4 * Math.PI) * 25.4
        // - (Math.PI / 2) is to make 0 up
        posX += Math.cos(Math.toRadians(rotation) - (Math.PI / 2)) * pixelDistance;
        posY += Math.sin(Math.toRadians(rotation) - (Math.PI / 2)) * pixelDistance;
    }

    public void drive(int _speed, boolean thread) {
        this.speed = _speed;
        if (thread) {
            if (speed == 0) {
                if (!drivingThread) {
                    drivingThread.interrupt();
                    drivingThread == null;
                }
            } else if (!drivingThread)
                drivingThread = new Thread() {
                    public void run() {
                        while (!isInterrupted()) {
                            // At x rpm, rotations every 20ms is (x/60/1000)*20
                            // With 4 inch wheels distance traveled a rotation is 4pi
                            // Distance traveled in 20ms is ((rpm/60/1000)*20)*4pi
                            // RPM is (speed/127)*100
                            // Inches->mm = inches * 25.4
                            movePixels((((((speed / 127) * 100) / 60 / 1000) * 20) * 4 * Math.PI) * 25.4)
                            sleep(20);
                        }
                    }
                }
        } else {
            movePixels((((((speed / 127) * 100) / 60 / 1000) * 20) * 4 * Math.PI) * 25.4)
        }
    }

    public int getEncoderBL() {
        return 0;
    }

    public int getEncoderBR() {
        return 1;
    }

    public enum SENSOR {
        GYROSCOPE
    }

    public int getPixX() {
        return posX / 4;
    }

    public int getPixY() {
        return posY / 4;
    }
}