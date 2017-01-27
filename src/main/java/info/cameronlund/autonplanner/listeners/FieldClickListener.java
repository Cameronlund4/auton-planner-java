package info.cameronlund.autonplanner.listeners;

import info.cameronlund.autonplanner.panels.FieldPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class FieldClickListener implements MouseListener {
    private int x,y;

    public FieldClickListener(FieldPanel fieldPanel) {
        x = fieldPanel.getX();
        y = fieldPanel.getY();
        fieldPanel.addFieldClickListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX()-x > 687|| e.getY()-y > 687)
            return;
        fieldClicked(e.getX()-x,e.getY()-y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Ignored
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Ignored
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Ignored
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Ignored
    }

    public abstract void fieldClicked(int x, int y);
}
