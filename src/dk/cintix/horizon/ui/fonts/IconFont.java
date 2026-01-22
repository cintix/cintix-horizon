package dk.cintix.horizon.ui.fonts;

import java.awt.Font;
import java.io.InputStream;

public final class IconFont {

    public static final Font ICON_20;
    public static final Font ICON_24;
    public static final Font ICON_28;

    static {
        try (InputStream inputStream = IconFont.class.getResourceAsStream("MaterialIcons-Regular.ttf")) {
            if (inputStream == null ) {
                throw new IllegalStateException("Icon font resource not found");
            }

            Font base = Font.createFont(Font.TRUETYPE_FONT,inputStream);

            ICON_20 = base.deriveFont(20f);
            ICON_24 = base.deriveFont(24f);
            ICON_28 = base.deriveFont(28f);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load icon font", e);
        }
    }

    private IconFont() {
        // utility class
    }
}
