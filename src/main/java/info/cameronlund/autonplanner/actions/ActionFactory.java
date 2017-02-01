package info.cameronlund.autonplanner.actions;

public abstract class ActionFactory {
    public abstract AutonAction createAction(String type, AutonActionWrapper wrapper);
}
