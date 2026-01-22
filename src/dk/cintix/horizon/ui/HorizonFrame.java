
package dk.cintix.horizon.ui;

import dk.cintix.horizon.ui.components.TitleBar;
import dk.cintix.horizon.ui.sidebar.Sidebar;
import dk.cintix.horizon.ui.states.WindowControls;
import dk.cintix.horizon.ui.states.WindowState;
import dk.cintix.horizon.ui.theme.Theme;
import dk.cintix.horizon.ui.theme.ThemeProvider;
import dk.cintix.horizon.ui.window.WindowDragController;
import dk.cintix.horizon.ui.window.WindowResizeController;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class HorizonFrame extends JFrame {

    private final WindowControls controls;
    private final Sidebar sidebar = new Sidebar(this);
    private final TitleBar titleBar = new TitleBar(this);
    private WindowState state = WindowState.NORMAL;

    private final int corner = 18;
    private JPopupMenu windowMenu;
    private ThemeProvider themeProvider = () -> Theme.DEFAULT;

    public void setThemeProvider(ThemeProvider provider) {
        this.themeProvider = provider != null
                ? provider
                : () -> Theme.DEFAULT;

        repaint();
    }

    public Theme theme() {
        return themeProvider.getTheme();
    }

    public void setWindowMenu(JPopupMenu menu) {
        this.windowMenu = menu;
    }

    public JPopupMenu getWindowMenu() {
        return windowMenu;
    }

    public HorizonFrame(WindowControls controls) {
        this.controls = controls;

        setTitle("LiveCrew");
        setUndecorated(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(null);
        root.setBackground(theme().BG);
        setContentPane(root);

        titleBar.setBounds(0, 0, getWidth(), 48);
        root.add(titleBar);

        sidebar.setBounds(0, 48, 64, getHeight() - 48);
        root.add(sidebar);

        new WindowDragController(this, titleBar);
        new WindowResizeController(this, root);

        applyShape();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                layoutUI();
                applyShape();
            }
        });

        setVisible(true);
    }

    public WindowControls getControls() {
        return controls;
    }

    public WindowState getWindowState() {
        return state;
    }

    public void toggleMaximize() {
        if (controls != WindowControls.CLOSE_HIDE_MAXIMIZE) return;

        if (state == WindowState.NORMAL) {
            state = WindowState.MAXIMIZED;
            setExtendedState(MAXIMIZED_BOTH);
        } else {
            state = WindowState.NORMAL;
            setExtendedState(NORMAL);
        }
        applyShape();
        layoutUI();
        repaint();
    }

    private void layoutUI() {
        int titleH = 48;
        int sidebarW = 64;

        titleBar.setBounds(0, 0, getWidth(), titleH);
        sidebar.setBounds(0, titleH, sidebarW, getHeight() - titleH);
    }

    public void applyShape() {
        if (state == WindowState.MAXIMIZED) {
            setShape(null);
        } else {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), corner, corner));
        }
    }
}
