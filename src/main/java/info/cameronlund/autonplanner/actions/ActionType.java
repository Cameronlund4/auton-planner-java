package info.cameronlund.autonplanner.actions;

/**
 * Name: Cameron Lund
 * Date: 1/19/2017
 * JDK: 1.8.0_101
 * Project: None
 */
public enum ActionType {
    DRIVE, TURN, LIFT, CLAW, WAIT;

    private static String[] types = {"Drive", "Turn", "Lift", "Claw", "Wait"};

    public static String[] getTypesList() {
        return types;
    }
}
