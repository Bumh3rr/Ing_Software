package bumh3r.components.input;

import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class InputArea extends FlatTextArea {

    public InputArea() {
        setDocument(new LimitTextDocument(250));
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    public JComponent createdInput() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 3","[grow]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);"
                + "border:5,15,5,15");
        panel.add(this, "grow,push");
        return panel;
    }
}
