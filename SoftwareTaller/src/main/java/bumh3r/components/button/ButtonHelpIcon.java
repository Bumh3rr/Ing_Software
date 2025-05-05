package bumh3r.components.button;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatHelpButtonIcon;
import java.awt.Cursor;
import javax.swing.JButton;

public class ButtonHelpIcon extends JButton {


    public ButtonHelpIcon(){
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,4,0,0;"
        );
        setIcon(new FlatHelpButtonIcon());
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
