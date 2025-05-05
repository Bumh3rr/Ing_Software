package bumh3r.components.form;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JLabel;

public class TitleForm extends JLabel {

    public TitleForm(String text, int horizontalAlignment,int fontSize) {
        setText(text);
        setHorizontalAlignment(horizontalAlignment);
        updateUI();
        setAlignmentX(LEFT_ALIGNMENT);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +" + fontSize);
    }

}
