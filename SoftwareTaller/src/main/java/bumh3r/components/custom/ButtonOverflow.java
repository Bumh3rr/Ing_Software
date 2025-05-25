package bumh3r.components.custom;

import bumh3r.utils.fonts.FontPublicaSans;
import bumh3r.system.form.Form;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatPopupMenu;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import net.miginfocom.swing.MigLayout;

import static bumh3r.utils.PathResources.Icon.home;

public class ButtonOverflow extends JToggleButton {
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();
    private JLabel icon;
    private JPopupMenu popup;
    private ButtonOption[] optionList;

    public ButtonOverflow(ButtonOption... optionList) {
        this.optionList = optionList;
        init();
    }

    private void init() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        addActionListener(e -> showPopup());
        setLayout(new MigLayout("fill,insets 0", "[center]", "[center]"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "margin:2,4,2,4;" +
                "borderWidth:0;" +
                "innerFocusWidth:0;" +
                "focusedBorderColor:null;" +
                "focusColor:null;" +
                "selectedBackground:darken(@background,5%);" +
                "[light]background:darken($Panel.background,1%);" +
                "[dark]background:lighten($Panel.background,1%);"
        );
        icon = new JLabel(new FlatSVGIcon(home + "ic_overflowMenu.svg"));
        add(icon, "grow 0");
    }

    private void showPopup() {
        if (popup == null) {
            popup = new FlatPopupMenu();
            popup.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    formCheckUI();
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    formCheckUI();
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    closePop();
                    formCheckUI();
                }
            });
            popup.putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]background:#f9f7f6;" +
                    "[dark]background:lighten(@background,2%);"
            );
            popup.setBorder(BorderFactory.createEmptyBorder());
            popup.add(createdButtons());
        }
        if (this.isSelected()) {
            popup.show(this, (-186 + this.getWidth()), this.getHeight());
        }
    }

    public void formCheckUI() {
        SwingUtilities.invokeLater(() -> {
            if (oldTheme != UIManager.getLookAndFeel()) {
                try {
                    oldTheme = UIManager.getLookAndFeel();
                    SwingUtilities.updateComponentTreeUI(this);
                    UIManager.setLookAndFeel(oldTheme);
                    FlatLaf.updateUI();
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void closePop() {
        if (popup != null) {
            popup.setVisible(false);
            setSelected(false);
        }
    }

    public JComponent createdButtons() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 2 4,width 180", "[fill]", "[]0[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:99;" +
                "background:null;");
        List.of(this.optionList).forEach((button) -> panel.add(button, "gapy 1 1"));
        return panel;
    }

    public static class ButtonOption extends JButton {

        public ButtonOption(String text, ActionListener event) {
            this(text, null, null, event);
        }

        public ButtonOption(String text, String color_hex, String icon, ActionListener event) {
            super(text);
            setLayout(new MigLayout("height 20,insets 3 3", "[]12[]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc:22;" +
                    "borderWidth:0;" +
                    "innerFocusWidth:0;" +
                    "focusedBorderColor:null;" +
                    "focusColor:null;" +
                    "[light]hoverBackground:darken(@background,3%);" +
                    "[dark]hoverBackground:lighten(@background,1%);" +
                    "pressedBackground:lighten(@background,2%);" +
                    "background:lighten(@background,4%);"
            );

            if (icon != null && color_hex != null) {
                setIcon(new FlatSVGIcon(icon, 0.65f)
                        .setColorFilter(new FlatSVGIcon.ColorFilter((x) -> UIManager.getColor(color_hex))));
                setForeground(Color.decode(color_hex));
            }
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD, 12.5f));
            addActionListener(event);
        }
    }

}
