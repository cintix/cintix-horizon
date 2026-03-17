package dk.cintix.horizon.ui.components;

import java.util.Objects;
import javax.swing.JComponent;

/**
 *
 * @author cintix
 */
public final class Tab {

    private final String label;
    private final JComponent content;

    public Tab(String label, JComponent content) {
        this.label = Objects.requireNonNull(label, "label");
        this.content = Objects.requireNonNull(content, "content");
    }

    public String getLabel() {
        return label;
    }

    public JComponent getContent() {
        return content;
    }
}