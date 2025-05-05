package bumh3r.components.button;

import bumh3r.archive.PathResources;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Cursor;
import javax.swing.JButton;
import javax.swing.UIManager;

public class ButtonInfoIcon extends JButton {

    public ButtonInfoIcon() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:2,2,2,2;"
        );
        setIcon(new FlatSVGIcon(PathResources.Icon.modal + "ic_info.svg", 0.65f)
                .setColorFilter(new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Label.disabledForeground"))));
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

}
