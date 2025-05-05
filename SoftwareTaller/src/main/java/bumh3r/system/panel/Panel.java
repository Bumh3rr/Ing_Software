package bumh3r.system.panel;

import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.system.form.Form;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Panel extends JPanel {
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public void panelInit() {
    }

    public void panelRefresh() {
    }

    public void panelOpen() {
    }

    public void installController() {
    }

    public void panelCheckUI() {
        SwingUtilities.invokeLater(() -> {
            if (oldTheme != UIManager.getLookAndFeel()) {
                try {
                    oldTheme = UIManager.getLookAndFeel();
                    SwingUtilities.updateComponentTreeUI(this);
                    UIManager.setLookAndFeel(oldTheme);
                    FlatLaf.updateUI();
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public static JComponent createdSubTitle(String str, float size) {
        LabelPublicaSans type = new LabelPublicaSans(str).size(size).type(FontPublicaSans.FontType.BOLD_BLACK);
        type.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,15%);"
                + "[dark]foreground:darken(@foreground,15%);");
        return type;
    }

    public static JComponent createdGramaticalP(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "font:12;"
        );
        return label;
    }

}
