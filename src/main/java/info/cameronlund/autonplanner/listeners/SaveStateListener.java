package info.cameronlund.autonplanner.listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class SaveStateListener {
    private List<JTextField> components = new ArrayList<>();

    public void addComponent(JTextField component) {
        components.add(component);
    }

    public void callActions() {
        ActionEvent event = new ActionEvent(this, 0, "saveState");
        for (JTextField component : components)
            for (ActionListener list : component.getActionListeners())
                list.actionPerformed(event);
    }
}
