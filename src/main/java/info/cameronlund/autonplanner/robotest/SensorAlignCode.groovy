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
            def dist = (r().encoderGet(r().getEncoderBL())/360)*(4*Math.PI)*687/144;
            ghostX += (dist * Math.cos(robot.analogRead(ProsTestRobot.SENSOR.GYROSCOPE)))///360*4*Math.PI;
            ghostY += (dist * Math.sin(robot.analogRead(ProsTestRobot.SENSOR.GYROSCOPE)))///360*4*Math.PI;
            println new DPoint(ghostX, ghostY).toString() + " | "+new DPoint(robot.getPosX(),robot.getPosY()).toString();
            r().encoderReset(r().getEncoderBL())
            sleep(200)
        }
    }
}