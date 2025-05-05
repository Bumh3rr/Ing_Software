package bumh3r.components.button;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Cursor;
import javax.swing.JButton;
import javax.swing.UIManager;
import static bumh3r.archive.PathResources.Icon.drawer;


public class ButtonAction extends JButton {

    public ButtonAction(String text) {
        this();
        super.setText(text);
    }

    public ButtonAction() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:16;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:null"
        );
    }

    public static class Refresh extends ButtonAction{

        public Refresh() {
            super();
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setIcon(new FlatSVGIcon(drawer + "refresh.svg", 0.35f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Component.accentColor"))));
        }

    }

}
