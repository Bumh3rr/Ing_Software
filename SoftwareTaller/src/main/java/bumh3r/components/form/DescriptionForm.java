package bumh3r.components.form;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

public class DescriptionForm extends JTextArea {
    public DescriptionForm(String text) {
        super(text);
        putClientProperty(FlatClientProperties.STYLE,
                       "background:null;"
                        + "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%)");
        setEditable(false);
        setBorder(BorderFactory.createEmptyBorder());
    }
}
