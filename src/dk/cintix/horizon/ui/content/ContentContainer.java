package dk.cintix.horizon.ui.content;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContentContainer extends JComponent {

    private final Map<String, ContentView> views = new LinkedHashMap<>();
    private ContentView active;

    public ContentContainer() {
        setLayout(null);
        setOpaque(false);
    }

    public void register(ContentView view) {
        views.put(view.getId(), view);
    }

    public void show(String id) {
        ContentView next = views.get(id);
        if (next == null) {
            return;
        }

        if (active != null) {
            remove(active.getComponent());
        }

        active = next;
        JComponent c = active.getComponent();
        add(c);
        layoutView();
        repaint();
    }

    @Override
    public void doLayout() {
        layoutView();
    }

    private void layoutView() {
        if (active == null) {
            return;
        }

        JComponent c = active.getComponent();
        c.setBounds(0, 0, getWidth(), getHeight());
    }
}
