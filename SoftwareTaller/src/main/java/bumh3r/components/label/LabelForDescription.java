package bumh3r.components.label;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class LabelForDescription extends JTextArea{

    public LabelForDescription(String str) {
        super(str);
        setWrapStyleWord(true);
        setLineWrap(true);
        setEditable(false);
        setBorder(BorderFactory.createEmptyBorder());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "background:null");
    }
}
