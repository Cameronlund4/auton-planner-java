package info.cameronlund.autonplanner.robotest

public class SensorAlignCode extends RobotCode {

    int ghostX = 0;
    int ghostY = 0;

    SensorAlignCode(ProsTestRobot robot) {
        super(robot)
        ghostX = robot.getPosX();
        ghostY = robot.getPosY();
    }

    @Override
    void run() {
        while (true) {
            println r().encoderGet(r().getEncoderBL());
            sleep(200)
        }
    }
}