package bumh3r.components.button;

import bumh3r.fonts.FontPublicaSans;
import com.formdev.flatlaf.extras.components.FlatButton;
import java.awt.event.ActionListener;
import lombok.NonNull;

public class ButtonAccentBase extends FlatButton {
    private String accentColor = "@accentBaseColor";
    private String stylesModel = "";

    public ButtonAccentBase(String text, @NonNull String accentColor) {
        this(text);
        this.accentColor = accentColor;
        this.stylesModel = generatedStyles(this.accentColor);
        setStyle(this.stylesModel);
    }
    public ButtonAccentBase(String text) {
        this();
        super.setText(text);
    }
    public ButtonAccentBase() {
        setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD,13.2f));
        this.stylesModel = generatedStyles(this.accentColor);
        setStyle(this.stylesModel);
    }

    public ButtonAccentBase addStyles(@NonNull String styles){
        stylesModel += styles;
        setStyle(stylesModel);
        return this;
    }

    private String generatedStyles(String color){
        return "background:"+color+";"
                + "foreground:#FFF;"
                + "margin: 6,10,6,10;"
                + "focusedBackground:saturate("+color+",5%);"
                + "focusedBorderColor:fade("+color+",10%);"
                + "hoverBorderColor:null;"
                + "focusColor:fade("+color+",35%);";
    }



}
