package bumh3r.components.input;

import bumh3r.components.PopupSystemClipboard;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.UIManager;

public class InputText extends FlatTextField {

    private final int LIMITE_DEFAULT = 45;
    private int limite;
    private String placeHolder;
    private String isEnabledShowClean = "showClearButton:true;";
    private String isEnabledIcon = "";

    public InputText(String placeHolder, int limite) {
        this(limite);
        this.placeHolder = placeHolder;
        this.setPlaceholderText(this.placeHolder);
    }

    public InputText(int limite) {
        this();
        this.limite = limite;
        setDocument(new LimitTextDocument(this.limite));
    }

    public InputText(String text) {
        this();
        this.setPlaceholderText(text);
    }

    public InputText() {
        setDocument(new LimitTextDocument(LIMITE_DEFAULT));
        setComponentPopupMenu(new PopupSystemClipboard(this));
        setStyles();
        setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.MEDIUM, 13f));
    }

    public InputText setIcon(String url) {
        return createIcon(url, 0.35f);
    }

    public InputText setIcon(String url, Float scale) {
        return createIcon(url, scale);
    }

    private InputText createIcon(String url, Float scale) {
        putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon(url, scale));
        addFocusColorIcon(url, scale);
        isEnabledIcon = "iconTextGap:10;";
        setStyles();
        return this;
    }

    public InputText setShowClean(Boolean bool) {
        this.isEnabledShowClean = bool ? "showClearButton:true;" : "";
        setStyles();
        return this;
    }

    public void setStyles() {
        putClientProperty(FlatClientProperties.STYLE,
                isEnabledIcon + isEnabledShowClean);
    }

    public void addFocusColorIcon(String url, Float scale) {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                        new FlatSVGIcon(url, scale).setColorFilter(new FlatSVGIcon.ColorFilter((x) -> UIManager.getColor("Component.accentColor"))));
            }

            @Override
            public void focusLost(FocusEvent e) {
                putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon(url, scale));
            }
        });
    }

}
