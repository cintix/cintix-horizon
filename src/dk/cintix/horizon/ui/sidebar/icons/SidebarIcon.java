package dk.cintix.horizon.ui.sidebar.icons;

public enum SidebarIcon {

    DASHBOARD("\uE871"),
    USERS("\uE7FB"),
    BOTS("\uE869"),
    CURRENCY("\uE263"),
    MODERATION("\uE8D5"),
    SUBSCRIBERS("\uE7EF"),
    SETTINGS("\uE8B8");

    private final String glyph;

    SidebarIcon(String glyph) {
        this.glyph = glyph;
    }

    public String glyph() {
        return glyph;
    }
}
