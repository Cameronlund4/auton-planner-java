package info.cameronlund.autonplanner.robotest;

public abstract class RobotCode implements Runnable {
    private ProsTestRobot robot;

    public RobotCode(ProsTestRobot robot) {
        this.robot = robot;
    }

    public ProsTestRobot r() {
        return getRobot();
    }

    public ProsTestRobot getRobot() {
        return robot;
    }
}
