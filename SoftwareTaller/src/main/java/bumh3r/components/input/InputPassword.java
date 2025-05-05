package bumh3r.components.input;

import bumh3r.components.PopupSystemClipboard;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatPasswordField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.UIManager;

public class InputPassword extends FlatPasswordField {

    private final int LIMITE_DEFAULT = 45;
    private int limite;
    private String placeHolder;
    private String isEnabledShowReveal = "showRevealButton:true;";
    private String isEnabledIcon = "";

    public InputPassword(String placeHolder, int limite) {
        this(limite);
        this.placeHolder = placeHolder;
        this.setPlaceholderText(this.placeHolder);
    }

    public InputPassword(int limite) {
        this();
        this.limite = limite;
        setDocument(new LimitTextDocument(this.limite));
    }

    public InputPassword() {
        setDocument(new LimitTextDocument(LIMITE_DEFAULT));
        setComponentPopupMenu(new PopupSystemClipboard(this));
        setStyles();
        setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.MEDIUM, 13f));
    }

    public InputPassword setIcon(String url) {
        return createIcon(url, 0.35f);
    }

    public InputPassword setIcon(String url, Float scale) {
        return createIcon(url, scale);
    }

    private InputPassword createIcon(String url, Float scale) {
        putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon(url, scale));
        addFocusColorIcon(url, scale);
        isEnabledIcon = "iconTextGap:10;";
        setStyles();
        return this;
    }

    public InputPassword setShowClean(Boolean bool) {
        this.isEnabledShowReveal = bool ? "showClearButton:true;" : "";
        setStyles();
        return this;
    }

    public void setStyles() {
        putClientProperty(FlatClientProperties.STYLE,
                isEnabledIcon + isEnabledShowReveal);
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
