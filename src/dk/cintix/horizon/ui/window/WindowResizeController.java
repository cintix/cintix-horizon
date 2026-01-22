package dk.cintix.horizon.ui.window;

import dk.cintix.horizon.ui.HorizonFrame;
import dk.cintix.horizon.ui.states.ResizeRegion;
import dk.cintix.horizon.ui.states.WindowState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowResizeController extends MouseAdapter {

    private static final int BORDER = 6;

    private final HorizonFrame frame;

    private ResizeRegion region = ResizeRegion.NONE;
    private Point startMouse;
    private Rectangle startBounds;

    public WindowResizeController(HorizonFrame frame, JComponent root) {
        this.frame = frame;
        root.addMouseListener(this);
        root.addMouseMotionListener(this);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (frame.getWindowState() == WindowState.MAXIMIZED) {
            frame.setCursor(Cursor.getDefaultCursor());
            return;
        }

        region = detectRegion(e.getPoint());
        frame.setCursor(cursorFor(region));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (region == ResizeRegion.NONE) return;

        startMouse = e.getLocationOnScreen();
        startBounds = frame.getBounds();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (region == ResizeRegion.NONE) return;
        if (frame.getWindowState() == WindowState.MAXIMIZED) return;

        Point p = e.getLocationOnScreen();
        int dx = p.x - startMouse.x;
        int dy = p.y - startMouse.y;

        Rectangle r = new Rectangle(startBounds);

        switch (region) {
            case E  -> r.width += dx;
            case S  -> r.height += dy;
            case SE -> { r.width += dx; r.height += dy; }

            case W  -> { r.x += dx; r.width -= dx; }
            case N  -> { r.y += dy; r.height -= dy; }

            case NW -> {
                r.x += dx; r.width -= dx;
                r.y += dy; r.height -= dy;
            }

            case NE -> {
                r.width += dx;
                r.y += dy; r.height -= dy;
            }

            case SW -> {
                r.x += dx; r.width -= dx;
                r.height += dy;
            }
        }

        applyBounds(r);
    }

    private void applyBounds(Rectangle r) {
        int minW = 600;
        int minH = 400;

        if (r.width < minW || r.height < minH) return;

        frame.setBounds(r);
        frame.applyShape();
    }

    private ResizeRegion detectRegion(Point p) {
        int w = frame.getWidth();
        int h = frame.getHeight();

        boolean left   = p.x <= BORDER;
        boolean right  = p.x >= w - BORDER;
        boolean top    = p.y <= BORDER;
        boolean bottom = p.y >= h - BORDER;

        if (top && left)     return ResizeRegion.NW;
        if (top && right)    return ResizeRegion.NE;
        if (bottom && left)  return ResizeRegion.SW;
        if (bottom && right) return ResizeRegion.SE;

        if (top)    return ResizeRegion.N;
        if (bottom) return ResizeRegion.S;
        if (left)   return ResizeRegion.W;
        if (right)  return ResizeRegion.E;

        return ResizeRegion.NONE;
    }

    private Cursor cursorFor(ResizeRegion r) {
        return switch (r) {
            case N  -> Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            case S  -> Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            case E  -> Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
            case W  -> Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
            case NE -> Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
            case NW -> Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
            case SE -> Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
            case SW -> Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
            default -> Cursor.getDefaultCursor();
        };
    }
}
