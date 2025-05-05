package bumh3r.components.button;

import com.formdev.flatlaf.extras.components.FlatButton;

public class ButtonDefault extends FlatButton {
    private String stylesModel = "foreground:#FFF;";

    public ButtonDefault(String text) {
        this();
        super.setText(text);
    }

    public ButtonDefault() {
        setStyle(stylesModel);
    }

    public ButtonDefault addStyles(String styles) {
        stylesModel += styles;
        setStyle(stylesModel);
        return this;
    }

    @Override
    public boolean isDefaultButton() {
        return true;
    }
}

