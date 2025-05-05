package bumh3r.components.label;

import bumh3r.fonts.FontPublicaSans;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatLabel;
import javax.swing.BorderFactory;

public class LabelPublicaSans extends FlatLabel {
    private Float size;
    private String styles;
    private FontPublicaSans.FontType type;

    public LabelPublicaSans(String text) {
        this.size = 13f;
        this.type = FontPublicaSans.FontType.BOLD_BLACK;
        this.styles = "";
        setText(text);
        setFont(FontPublicaSans.getInstance().getFont(this.type, this.size));
    }

    public LabelPublicaSans size(Float size) {
        this.size = size;
        setFont(FontPublicaSans.getInstance().getFont(this.type, this.size));
        return this;
    }

    public LabelPublicaSans type(FontPublicaSans.FontType type) {
        this.type = type;
        setFont(FontPublicaSans.getInstance().getFont(this.type, this.size));
        return this;
    }

    public LabelPublicaSans style(String styles) {
        this.styles = styles;
        putClientProperty(FlatClientProperties.STYLE, this.styles);
        return this;
    }

    public LabelPublicaSans emptyBorder() {
        setBorder(BorderFactory.createEmptyBorder());
        return this;
    }

}
