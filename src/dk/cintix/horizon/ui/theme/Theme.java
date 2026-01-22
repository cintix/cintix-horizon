package dk.cintix.horizon.ui.theme;

import java.awt.Color;

public class Theme {

    public final Color BG;
    public final Color PANEL;
    public final Color PANEL_ALT;
    public final Color ACCENT;
    public final Color TEXT;
    public final Color SIDEBAR_BG;
    public final Color SIDEBAR_HOVER;
    public final Color SIDEBAR_ACTIVE;

    public Theme(
            Color BG,
            Color PANEL,
            Color PANEL_ALT,
            Color ACCENT,
            Color TEXT,
            Color SIDEBAR_BG,
            Color SIDEBAR_HOVER,
            Color SIDEBAR_ACTIVE
    ) {
        this.BG = BG;
        this.PANEL = PANEL;
        this.PANEL_ALT = PANEL_ALT;
        this.ACCENT = ACCENT;
        this.TEXT = TEXT;
        this.SIDEBAR_BG = SIDEBAR_BG;
        this.SIDEBAR_HOVER = SIDEBAR_HOVER;
        this.SIDEBAR_ACTIVE = SIDEBAR_ACTIVE;
    }

    public static final Theme DEFAULT = new Theme(
            new Color(18, 20, 26),           // BG
            new Color(26, 29, 36),           // PANEL
            new Color(126, 129, 136),        // PANEL_ALT
            new Color(90, 130, 255),         // ACCENT
            new Color(220, 225, 235),        // TEXT
            new Color(52, 55, 62),            // SIDEBAR_BG
            new Color(255, 255, 255, 12),    // SIDEBAR_HOVER
            new Color(255, 255, 255, 22)     // SIDEBAR_ACTIVE
    );
}
