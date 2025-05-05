package bumh3r.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ContainerCards extends JScrollPane {

    public ContainerCards(JPanel panelCards) {
        super.setViewportView(panelCards);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%);");
        setBorder(BorderFactory.createEmptyBorder());
        getHorizontalScrollBar().setUnitIncrement(10);
        getVerticalScrollBar().setUnitIncrement(10);
        getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,0,0,0;"
                + "width:5;");
        getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,0,0,0;"
                + "width:5;");
    }
}
