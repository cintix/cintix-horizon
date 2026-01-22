package dk.cintix.horizon.ui.sidebar;

import dk.cintix.horizon.ui.sidebar.icons.SidebarIcon;

public final class SidebarItem {

    private final String id;
    private final SidebarIcon icon;
    private final String tooltip;

    private boolean active;
    private boolean hover;

    private Runnable onSelect;

    public SidebarItem(String id, SidebarIcon icon, String tooltip) {
        this.id = id;
        this.icon = icon;
        this.tooltip = tooltip;
    }

    // --- identity ---

    public void setOnSelect(Runnable runnable) {
        this.onSelect = runnable;
    }

    public void fireSelect() {
        if (onSelect != null) onSelect.run();
    }

    public String getId() {
        return id;
    }

    public SidebarIcon getIcon() {
        return icon;
    }

    public String getTooltip() {
        return tooltip;
    }

    // --- state (styres af Sidebar) ---

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }
}
