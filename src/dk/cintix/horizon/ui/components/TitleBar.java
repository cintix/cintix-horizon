package dk.cintix.horizon.ui.components;

import dk.cintix.horizon.ui.HorizonFrame;
import dk.cintix.horizon.ui.states.WindowControls;
import dk.cintix.horizon.ui.states.WindowState;
import dk.cintix.horizon.ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TitleBar extends JComponent {

    private final HorizonFrame frame;

    private final Rectangle closeRect = new Rectangle();
    private final Rectangle hideRect = new Rectangle();
    private final Rectangle maxRect = new Rectangle();
    private final Rectangle menuRect = new Rectangle();

    private String tooltipText;
    private Point lastMouse = new Point();

    private boolean hoverClose;
    private boolean hoverHide;
    private boolean hoverMax;
    private boolean hoverMenu;

    public TitleBar(HorizonFrame frame) {
        this.frame = frame;
        setOpaque(false);

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                lastMouse = e.getPoint();
                updateHover(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverClose = hoverHide = hoverMax = hoverMenu = false;
                tooltipText = null;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getPoint());
            }
        };

        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    private void updateHover(Point p) {
        hoverClose = closeRect.contains(p);
        hoverHide = hideRect.contains(p);
        hoverMax = maxRect.contains(p);
        hoverMenu = menuRect.contains(p);

        tooltipText = null;

        if (hoverClose) {
            tooltipText = "Close";
        } else if (hoverHide) {
            tooltipText = "Minimize";
        } else if (hoverMax) {
            tooltipText = frame.getWindowState() == WindowState.MAXIMIZED
                    ? "Restore"
                    : "Maximize";
        } else if (hoverMenu) {
            tooltipText = "Window menu";
        }

        repaint();
    }

    private void handleClick(Point p) {

        if (closeRect.contains(p)) {
            System.exit(0);
            return;
        }

        if (frame.getControls() == WindowControls.CLOSE_ONLY) {
            return;
        }

        if (hideRect.contains(p)) {
            frame.setState(Frame.ICONIFIED);
            return;
        }

        if (frame.getControls() == WindowControls.CLOSE_HIDE_MAXIMIZE) {

            if (maxRect.contains(p)) {
                frame.toggleMaximize();
                return;
            }

            if (menuRect.contains(p)
                    && frame.getWindowState() == WindowState.MAXIMIZED) {
                showWindowMenu(menuRect.x, menuRect.y + menuRect.height);
            }
        }
    }

    private void showWindowMenu(int x, int y) {
        JPopupMenu menu = frame.getWindowMenu();
        if (menu != null) {
            menu.show(this, x, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean maximized = frame.getWindowState() == WindowState.MAXIMIZED;

        paintBackground(g2, maximized);
        layoutButtons();
        paintTitleArea(g2, maximized);
        paintButtons(g2, maximized);

        if (tooltipText != null) {
            paintTooltip(g2, tooltipText);
        }

        g2.dispose();
    }

    private void paintTooltip(Graphics2D g2, String text) {
        Font font = getFont().deriveFont(Font.PLAIN, 11f);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(text) + 12;
        int h = 20;

        int x = Math.max(6, lastMouse.x - (12 + w));
        int y = Math.min(lastMouse.y + 18, getHeight() - h - 4);

        g2.setColor(new Color(30, 34, 42, 230));
        g2.fillRoundRect(x, y, w, h, 8, 8);

        g2.setColor(new Color(255, 255, 255, 220));
        g2.drawString(text, x + 6, y + 14);
    }

    private void paintBackground(Graphics2D g2, boolean maximized) {
        g2.setColor(maximized ? frame.theme().PANEL_ALT : frame.theme().PANEL);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintTitleArea(Graphics2D g2, boolean maximized) {

        if (!maximized) {
            g2.setColor(frame.theme().TEXT);
            g2.setFont(getFont().deriveFont(Font.PLAIN, 14f));
            g2.drawString("LiveCrew", 16, 30);
            return;
        }

        int pillX = 12;
        int pillY = 8;
        int pillW = 160;
        int pillH = 32;

        g2.setColor(frame.theme().ACCENT);
        g2.fillRoundRect(pillX, pillY, pillW, pillH, 16, 16);

        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 13f));
        g2.drawString("LiveCrew", pillX + 14, pillY + 21);

        if (frame.getControls() == WindowControls.CLOSE_HIDE_MAXIMIZE) {
            paintMenuButton(g2);
        }
    }

    private void layoutButtons() {
        int size = 14;
        int padRight = 14;
        int gap = 10;
        int y = (getHeight() - size) / 2;

        int x = getWidth() - padRight - size;
        closeRect.setBounds(x - 6, y - 6, size + 12, size + 12);
        x -= (size + gap);

        if (frame.getControls() != WindowControls.CLOSE_ONLY) {
            hideRect.setBounds(x - 6, y - 6, size + 12, size + 12);
            x -= (size + gap);
        } else {
            hideRect.setBounds(0, 0, 0, 0);
        }

        if (frame.getControls() == WindowControls.CLOSE_HIDE_MAXIMIZE) {
            maxRect.setBounds(x - 6, y - 6, size + 12, size + 12);
        } else {
            maxRect.setBounds(0, 0, 0, 0);
        }
    }

    private void paintButtons(Graphics2D g2, boolean maximized) {

        paintDotButton(g2, closeRect, new Color(220, 80, 80), hoverClose);

        if (frame.getControls() == WindowControls.CLOSE_ONLY) {
            return;
        }

        paintDotButton(g2, hideRect, new Color(200, 200, 200), hoverHide);

        if (frame.getControls() == WindowControls.CLOSE_HIDE_MAXIMIZE) {
            Color c = maximized
                    ? new Color(120, 200, 140)
                    : new Color(120, 160, 240);
            paintDotButton(g2, maxRect, c, hoverMax);
        }
    }

    private void paintDotButton(Graphics2D g2, Rectangle r, Color base, boolean hover) {
        if (r.width <= 0) {
            return;
        }

        int size = 14;
        int x = r.x + 6;
        int y = r.y + 6;

        g2.setColor(hover ? base.brighter() : base);
        g2.fillOval(x, y, size, size);
    }

    private void paintMenuButton(Graphics2D g2) {

        int x = 12 + 160 + 10;
        int y = 10;
        int w = 34;
        int h = 28;

        menuRect.setBounds(x, y, w, h);

        g2.setColor(hoverMenu
                ? new Color(255, 255, 255, 35)
                : new Color(255, 255, 255, 20));
        g2.fillRoundRect(x, y, w, h, 10, 10);

        g2.setColor(new Color(255, 255, 255, 200));
        int lx = x + 10;
        int ly = y + 9;
        g2.fillRoundRect(lx, ly, 14, 2, 2, 2);
        g2.fillRoundRect(lx, ly + 6, 14, 2, 2, 2);
        g2.fillRoundRect(lx, ly + 12, 14, 2, 2, 2);
    }
}
