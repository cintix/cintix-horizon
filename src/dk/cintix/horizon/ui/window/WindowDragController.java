package dk.cintix.horizon.ui.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowDragController extends MouseAdapter {

    private final JFrame frame;
    private Point click;

    public WindowDragController(JFrame frame, JComponent dragArea) {
        this.frame = frame;
        dragArea.addMouseListener(this);
        dragArea.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            return;
        }
        Point p = e.getLocationOnScreen();
        frame.setLocation(p.x - click.x, p.y - click.y);
    }
}
