package info.cameronlund.autonplanner.listeners;

import info.cameronlund.autonplanner.actions.ActionManager;
import info.cameronlund.autonplanner.actions.AutonActionWrapper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TitleMouseListener implements MouseListener {
    private AutonActionWrapper action;
    private ActionManager manager;

    public TitleMouseListener(ActionManager manager, AutonActionWrapper action) {
        this.action = action;
        this.manager = manager;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        manager.setSelected(action.isSelected() ? null : action);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!action.isSelected())
            action.getTitleGraphic().setBackground(Color.lightGray);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!action.isSelected())
            action.getTitleGraphic().setBackground(action.getDefaultBg());
    }
}