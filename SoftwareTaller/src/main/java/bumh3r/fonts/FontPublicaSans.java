package bumh3r.fonts;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.Cleanup;
import static bumh3r.archive.PathResources.Font.url;

public class FontPublicaSans {

    private static FontPublicaSans instance;
    private Map<FontType, Font> fonts;

    public static void closeSession() {
        instance = null;
    }

    public static FontPublicaSans getInstance() {
        if (instance == null) {
            instance = new FontPublicaSans();
        }
        return instance;
    }

    public FontPublicaSans() {
        fonts = new HashMap<>();
    }


    public Font getFont(FontType type, float size) {
        if (fonts.containsKey(type)) {
            return fonts.get(type).deriveFont(size);
        }

        try {
            @Cleanup
            InputStream inputImagen = FontPublicaSans.class.getClassLoader().getResourceAsStream(url + type.getValue());
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputImagen);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            fonts.put(type, font);
            return fonts.get(type).deriveFont(size);

        } catch (Exception e) {
            return null;
        }
    }

    public enum FontType {
        MEDIUM("PublicaSansRound-Rg.otf"),
        BOLD("PublicaSansRound-Md.otf"),
        BOLD_BLACK("public-sans.bold.otf"),
        BOLD_BLACK_ITALIC("PublicaSansRound-XBdIt.otf");
        private final String path;

        FontType(String path) {
            this.path = path;
        }
        public String getValue() {
            return path;
        }
    }

}
