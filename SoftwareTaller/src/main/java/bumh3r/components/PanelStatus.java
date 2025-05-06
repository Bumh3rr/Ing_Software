package bumh3r.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.ColorFunctions;
import java.awt.Color;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelStatus extends JPanel {
    private JLabel icon, text;

    public PanelStatus() {
        setOpaque(false);
        setLayout(new MigLayout("wrap 3,fillx,insets 0", "0[]0[][]3", "fill"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;"
                + "[light]border:4,8,4,8,#E2E2E2;"
                + "[dark]border:4,8,4,8,#b0b0b0;"
                + "arc:18;"
        );
        icon = new JLabel();
        icon.setBorder(BorderFactory.createEmptyBorder());

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setBackground(Color.decode("#E2E2E2"));
        separator.setBorder(BorderFactory.createEmptyBorder());

        text = new JLabel();
        text.setBorder(BorderFactory.createEmptyBorder());
        text.putClientProperty(FlatClientProperties.STYLE,"font:bold;");

        add(icon, "grow 0,al center");
        add(separator, "growy,gapx 0 0");
        add(text);
    }

    public void setValue(boolean isEnable, String str) {
        Color fade;
        if (isEnable) {
            fade = ColorFunctions.lighten(Color.decode("#1aad2c"), 0.05f);
        } else {
            fade = ColorFunctions.lighten(Color.decode("#ff2600"), 0.1f);
        }
        SwingUtilities.invokeLater(() -> {
            text.setText(str);
            text.setForeground(fade);

            icon.setIcon(new FlatSVGIcon(modal + (isEnable ? "ic_done.svg" : "ic_fail.svg"), 0.65f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter((x) -> fade)));
            icon.putClientProperty(FlatClientProperties.STYLE, ""
                    + (isEnable ? "background:fade(#1aad2c,10%);" : "background:fade(#ff2600,10%);"));
        });
    }
}
