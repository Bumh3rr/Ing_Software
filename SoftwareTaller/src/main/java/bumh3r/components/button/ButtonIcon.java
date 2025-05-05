package bumh3r.components.button;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

public class ButtonIcon extends JButton {
    private JLabel title;

    public ButtonIcon(String text, String url, Float scale, int border, String colorButton) {
        setLayout(new MigLayout("al center center,insets 0"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "borderWidth:0;" +
                "innerFocusWidth:3;" +
                "focusedBorderColor:fade(" + colorButton + ",70%);" +
                "focusColor:fade(" + colorButton + ",35%);" +
                "background:" + colorButton + ";"
        );

        title = new JLabel(text);
        title.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold 0;" +
                "foreground:#FFF;");
        add(title);
        add(createIcon(url, scale, border));
    }

    private JComponent createIcon(String url, Float scale, int border) {
        FlatSVGIcon svgIcon = new FlatSVGIcon(url, scale).setColorFilter(new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("@accentColor")));

        JLabel label = new JLabel(svgIcon);
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:" + border + "," + border + "," + border + "," + border + ",fade(@accentColor,50%),,999;" +
                "background:fade(@accentColor,20%);");

        return label;
    }

    @Override
    public void setText(String text) {
        title.setText(text);
    }
}
