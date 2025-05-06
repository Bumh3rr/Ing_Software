package bumh3r.system.preferences;

import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.ActionListener;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Preferences<T> extends JPanel{
    private T identifier;
    private String idModal;
    private ActionListener[] events;
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public Preferences(T identifier, String idModal,ActionListener... events) {
        this.identifier = identifier;
        this.idModal = idModal;
        this.events = events;
        putClientProperty(FlatClientProperties.STYLE,
                "[light]background:lighten($Panel.background,4%);" +
                        "[dark]background:null;"
        );
    }

    public String title() {
        return "Preferences";
    }

    public void initPreference() {

    }

    public void openPreference() {

    }

    public JComponent createdGramaticalP(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "font:12;"
        );
        return label;
    }

    public JComponent createdSubtitles(String title) {
        return new LabelPublicaSans(title).size(15f).type(FontPublicaSans.FontType.BOLD_BLACK);
    }

    public void installEvents() {

    }
}
