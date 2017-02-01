package info.cameronlund.autonplanner;

import info.cameronlund.autonplanner.actions.*;
import info.cameronlund.autonplanner.panels.FieldPanel;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        String[] types = new String[]{
                "Drive", "Turn", "Lift", "Claw", "Wait"
        };

        ArrayList<String> typesList = new ArrayList<>();
        Collections.addAll(typesList, types);

        new AutonPlanner(new ActionManager(typesList, new ActionFactory() {
            @Override
            public AutonAction createAction(String type, AutonActionWrapper wrapper) {
                switch (type) {
                    case "Drive":
                        return new DriveAutonAction(wrapper);
                    case "Turn":
                        return new TurnAutonAction(wrapper);
                    case "Wait":
                        return new WaitAutonAction(wrapper);
                    case "Lift":
                        return new LiftAutonAction(wrapper);
                    case "Claw":
                        return new ClawAutonAction(wrapper);
                    default:
                        return new DriveAutonAction(wrapper);
                }
            }
        }),
                new FieldPanel("scale_field(2-1).png",
                        "cube.png", "star.png"));
    }
}
