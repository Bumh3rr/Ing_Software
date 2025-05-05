package bumh3r.components;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import javax.swing.JScrollPane;

public class ScrollPanelPreferences extends JScrollPane {

    public ScrollPanelPreferences(Component view) {
        super(view);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;" +
                "background:lighten($Panel.background,4%);"
        );
        getHorizontalScrollBar().setUnitIncrement(10);
        getVerticalScrollBar().setUnitIncrement(5);
        setColorScroll("@background");
    }

    public ScrollPanelPreferences setColorScroll(String color) {
        getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "width:0;"
        );
        getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "thumbInsets:0,0,0,0;"
                + "thumb:lighten($ScrollBar.thumb,10%);"
                + "pressedThumbColor:$ScrollBar.thumb;"
                + "width:5;"
                + "track:" + color + ";"
        );
        return this;
    }


}
