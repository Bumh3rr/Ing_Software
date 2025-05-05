package bumh3r.model.other;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import lombok.Cleanup;

public class Theme {

    private static final String URL = "theme.txt";
    private static File ARCHIVO;
    private static Theme instance;

    public static Theme getInstance() {
        if (instance == null) {
            instance = new Theme();
        }
        return instance;
    }

    public Theme initTheme() {
        if (ARCHIVO == null) {
            ARCHIVO = new File(URL);
        }
        return this;
    }

    public static void ThemeLight() {
        SwingUtilities.invokeLater(FlatMacLightLaf::setup);
    }

    public static void ThemeDark() {
        SwingUtilities.invokeLater(FlatMacDarkLaf::setup);
    }

    public Boolean readTheme() throws IOException {
        @Cleanup
        DataInputStream in = new DataInputStream(new FileInputStream(ARCHIVO));
        if (in.available() > 0) {
            return in.readBoolean();
        }
        return false;
    }

    public void writeTheme(Boolean mode) throws IOException {
        @Cleanup
        DataOutputStream out = new DataOutputStream(new FileOutputStream(ARCHIVO));
        out.writeBoolean(mode);
        out.flush();
    }

    public void resetArchivo() {
        try {
            ARCHIVO.delete();
            ARCHIVO.createNewFile();
            writeTheme(Boolean.FALSE);
            ThemeLight();
        } catch (IOException ex) {
            Logger.getLogger(Theme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void changeMode(boolean dark) {
        if (FlatLaf.isLafDark() != dark) {
            if (!dark) {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatIntelliJLaf.setup();
                    FlatLaf.updateUI();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            } else {
                EventQueue.invokeLater(() -> {
                    FlatAnimatedLafChange.showSnapshot();
                    FlatMacDarkLaf.setup();
                    FlatLaf.updateUI();
                    FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            }
        }
    }

}
