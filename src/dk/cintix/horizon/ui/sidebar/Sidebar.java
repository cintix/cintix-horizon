package dk.cintix.horizon.ui.sidebar;

import dk.cintix.horizon.ui.HorizonFrame;
import dk.cintix.horizon.ui.sidebar.icons.SidebarIcon;
import dk.cintix.horizon.ui.fonts.IconFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends JComponent {

    private static final int WIDTH = 64;
    private static final int ITEM_HEIGHT = 56;

    private final List<SidebarItem> items = new ArrayList<>();
    private final HorizonFrame frame;

    public Sidebar(HorizonFrame frame) {
        this.frame = frame;
        setOpaque(true);
        setBackground(frame.theme().SIDEBAR_BG);

        // demo items – kan flyttes ud senere
        items.add(new SidebarItem("dashboard", SidebarIcon.DASHBOARD, "Dashboard"));
        items.add(new SidebarItem("users", SidebarIcon.USERS, "Users"));
        items.add(new SidebarItem("bots", SidebarIcon.BOTS, "Bots"));
        items.add(new SidebarItem("currency", SidebarIcon.CURRENCY, "Currency"));
        items.add(new SidebarItem("moderation", SidebarIcon.MODERATION, "Moderation"));
        items.add(new SidebarItem("settings", SidebarIcon.SETTINGS, "Settings"));

        items.get(0).setActive(true);

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                updateHover(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                clearHover();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getPoint());
            }
        };

        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    public void addItem(SidebarItem item) {
        items.add(item);
        revalidate();
        repaint();
    }

    public void removeItem(SidebarItem item) {
        items.remove(item);
        revalidate();
        repaint();
    }

    public void clearItems() {
        items.clear();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, ITEM_HEIGHT * items.size());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        paintItems(g2);

        g2.dispose();
    }

    private void paintItems(Graphics2D g2) {
        int y = 0;

        for (SidebarItem item : items) {

            // background highlight
            if (item.isActive()) {
                g2.setColor(frame.theme().SIDEBAR_ACTIVE);
                g2.fillRect(0, y, getWidth(), ITEM_HEIGHT);
            } else if (item.isHover()) {
                g2.setColor(frame.theme().SIDEBAR_HOVER);
                g2.fillRect(0, y, getWidth(), ITEM_HEIGHT);
            }

            // icon
            g2.setFont(IconFont.ICON_24);
            g2.setColor(item.isActive()
                    ? frame.theme().ACCENT
                    : frame.theme().TEXT);

            FontMetrics fm = g2.getFontMetrics();
            String glyph = item.getIcon().glyph();

            int x = (getWidth() - fm.stringWidth(glyph)) / 2;
            int iy = y + (ITEM_HEIGHT + fm.getAscent() - fm.getDescent()) / 2;

            g2.drawString(glyph, x, iy);

            y += ITEM_HEIGHT;
        }
    }

    // --- interaction ---

    private void updateHover(Point p) {
        int index = p.y / ITEM_HEIGHT;

        for (int i = 0; i < items.size(); i++) {
            items.get(i).setHover(i == index);
        }
        repaint();
    }

    private void clearHover() {
        for (SidebarItem item : items) {
            item.setHover(false);
        }
        repaint();
    }

    private void handleClick(Point p) {
        int index = p.y / ITEM_HEIGHT;
        if (index < 0 || index >= items.size()) return;
        SidebarItem sidebarItem = items.get(index);

        setActive(sidebarItem);
        onItemSelected(sidebarItem.getId());
        sidebarItem.fireSelect();
    }

    private void setActive(SidebarItem active) {
        for (SidebarItem item : items) {
            item.setActive(item == active);
        }
        repaint();
    }

    // --- callback (stub) ---

    protected void onItemSelected(String id) {
        // TODO: hook navigation later
        System.out.println("Sidebar selected: " + id);
    }
}
