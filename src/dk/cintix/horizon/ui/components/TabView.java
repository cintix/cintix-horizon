package dk.cintix.horizon.ui.components;

import dk.cintix.horizon.ui.theme.Theme;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public final class TabView extends JComponent {

    private static final int HEADER_HEIGHT = 36;
    private static final int TAB_PADDING_X = 16;

    private final Theme theme;
    private final List<Tab> tabs = new ArrayList<>();

    private int activeIndex = 0;
    private int hoverIndex = -1;

    public TabView(Theme theme) {
        this.theme = Objects.requireNonNull(theme, "theme");
        setLayout(null);
        setOpaque(false);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = findTabIndexAt(e.getX(), e.getY());
                if (index != hoverIndex) {
                    hoverIndex = index;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverIndex != -1) {
                    hoverIndex = -1;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int index = findTabIndexAt(e.getX(), e.getY());
                if (index >= 0 && index < tabs.size() && index != activeIndex) {
                    setActiveIndex(index);
                }
            }
        };

        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    /* ===========================
       Public API
       =========================== */

    public TabView tab(String label, JComponent content) {
        addTab(label, content);
        return this;
    }

    public void addTab(String label, JComponent content) {
        Tab tab = new Tab(label, content);
        tabs.add(tab);

        content.setVisible(tabs.size() == 1);
        add(content);

        revalidate();
        repaint();
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int index) {
        if (index < 0 || index >= tabs.size()) {
            return;
        }

        if (activeIndex == index) {
            return;
        }

        tabs.get(activeIndex).getContent().setVisible(false);
        activeIndex = index;
        tabs.get(activeIndex).getContent().setVisible(true);

        revalidate();
        repaint();
    }

    /* ===========================
       Layout
       =========================== */

    @Override
    public void doLayout() {
        int width = getWidth();
        int height = getHeight() - HEADER_HEIGHT;

        if (activeIndex >= 0 && activeIndex < tabs.size()) {
            JComponent content = tabs.get(activeIndex).getContent();
            content.setBounds(0, HEADER_HEIGHT, width, height);
        }
    }

    /* ===========================
       Painting
       =========================== */

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            paintHeader(g2);
        } finally {
            g2.dispose();
        }
    }

    private void paintHeader(Graphics2D g2) {
        g2.setColor(theme.BG);
        g2.fillRect(0, 0, getWidth(), HEADER_HEIGHT);

        FontMetrics fm = g2.getFontMetrics();
        int x = 0;

        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            int textWidth = fm.stringWidth(tab.getLabel());
            int tabWidth = textWidth + TAB_PADDING_X * 2;

            boolean active = i == activeIndex;
            boolean hover = i == hoverIndex;

            if (active) {
                g2.setColor(theme.SIDEBAR_ACTIVE);
                g2.fillRect(x, HEADER_HEIGHT - 3, tabWidth, 3);
            } else if (hover) {
                g2.setColor(theme.SIDEBAR_HOVER);
                g2.fillRect(x, 0, tabWidth, HEADER_HEIGHT);
            }

            g2.setColor(active ? theme.TEXT : theme.TEXT_SECONDARY);
            int textX = x + TAB_PADDING_X;
            int textY = (HEADER_HEIGHT + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(tab.getLabel(), textX, textY);

            x += tabWidth;
        }
    }

    /* ===========================
       Hit detection
       =========================== */

    private int findTabIndexAt(int mouseX, int mouseY) {
        if (mouseY > HEADER_HEIGHT) {
            return -1;
        }

        FontMetrics fm = getFontMetrics(getFont());
        int x = 0;

        for (int i = 0; i < tabs.size(); i++) {
            int textWidth = fm.stringWidth(tabs.get(i).getLabel());
            int tabWidth = textWidth + TAB_PADDING_X * 2;

            if (mouseX >= x && mouseX <= x + tabWidth) {
                return i;
            }
            x += tabWidth;
        }

        return -1;
    }
}
