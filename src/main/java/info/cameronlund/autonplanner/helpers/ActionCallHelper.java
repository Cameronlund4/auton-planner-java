package info.cameronlund.autonplanner.helpers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionCallHelper {
    public void callActions(JMenuItem item) {
        ActionEvent event = new ActionEvent(this, 0, "call");
            for (ActionListener list : item.getActionListeners())
                list.actionPerformed(event);
    }
}
