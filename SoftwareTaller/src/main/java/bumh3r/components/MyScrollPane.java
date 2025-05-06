package bumh3r.components;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import javax.swing.JScrollPane;

public class MyScrollPane extends JScrollPane {
    public MyScrollPane(Component view) {
        super(view);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;");
        getHorizontalScrollBar().setUnitIncrement(25);
        getVerticalScrollBar().setUnitIncrement(25);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setColorScroll("@background");
    }

    public void setColorScroll(String color) {
        getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;"
                + "thumbInsets:0,0,0,0;"
                + "thumb:lighten($ScrollBar.thumb,10%);"
                + "pressedThumbColor:$ScrollBar.thumb;"
                + "width:9;"
                + "track:" + color + ";"
        );
    }
}

