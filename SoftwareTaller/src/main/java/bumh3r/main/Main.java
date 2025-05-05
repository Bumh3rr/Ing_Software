package bumh3r.main;

import bumh3r.fonts.FontPublicaSans;
import bumh3r.notifications.Notify;
import bumh3r.system.form.FormsManager;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import raven.modal.ModalDialog;
import raven.modal.option.BorderOption;
import raven.popup.GlassPanePopup;

public class Main extends JFrame {
    public Main() {
        super("FlipFlopTool");
        init();
    }

    private void init() {
        Notify.install(this);
        GlassPanePopup.install(this);
        PoolThreads.getInstance().execute(() -> FormsManager.getInstance().initFrame(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1240, 750));
//        setLocationRelativeTo(null);

        System.setProperty("flatlaf.animation", "true");

        ModalDialog.getDefaultOption()
                .setOpacity(0.4f)
                .getBorderOption()
                .setShadow(BorderOption.Shadow.DOUBLE_EXTRA_LARGE);

        if (SystemInfo.isMacOS) {
            System.setProperty("apple.awt.application.appearance", "system");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "Flif Flop");
            System.setProperty("apple.awt.application.appearance", "system");
            getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
            getRootPane().putClientProperty("apple.awt.fullscreenable", true);
        } else if (SystemInfo.isWindows) {
            System.setProperty("Flatlaf.useWindowDecorations", "true");
            System.setProperty("JRootPane.useWindowDecorations", "true");
            System.setProperty("TitlePane.useWindowDecorations", "true");
        }

        if (SystemInfo.isMacFullWindowContentSupported) {
            //getRootPane().putClientProperty("apple.awt.fullWindowContent", true);
            getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        }
    }

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("theme");
        FlatMacDarkLaf.setup();
        UIManager.put("defaultFont", FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.MEDIUM, 13f));
        EventQueue.invokeLater(() -> new Main().setVisible(true));
    }
}
