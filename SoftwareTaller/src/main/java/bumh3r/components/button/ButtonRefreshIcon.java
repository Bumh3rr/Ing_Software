package bumh3r.components.button;

import bumh3r.archive.PathResources;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Cursor;
import javax.swing.JButton;

public class ButtonRefreshIcon extends JButton {

    public ButtonRefreshIcon(float scale) {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:5,5,5,5;" +
                "background: null;" +
                "arc:16;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;"
        );
        setIcon(new FlatSVGIcon(PathResources.Icon.home + "ic_update.svg", scale));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
