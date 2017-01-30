package info.cameronlund.autonplanner.listeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ActionFocusListener implements FocusListener {
    private JTextField field;

    public ActionFocusListener(JTextField field) {
        this.field = field;
    }

    @Override
    public void focusGained(FocusEvent e) {
        // Ignored
    }

    @Override
    public void focusLost(FocusEvent e) {
        ActionEvent event = new ActionEvent(this, 0, "action");
        for (ActionListener list : field.getActionListeners())
            list.actionPerformed(event);
    }
}
