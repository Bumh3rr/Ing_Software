package bumh3r.system.form;

import bumh3r.components.form.DescriptionForm;
import bumh3r.components.form.TitleForm;
import com.formdev.flatlaf.FlatLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

@Setter
@Getter
public class Form extends JPanel {
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();
    private Runnable eventFormInit;
    private Runnable eventFormOpen;
    private Runnable eventFormRefresh;

    public void formInit() {
    }

    public void formOpen() {
    }

    public void formRefresh() {
    }

    public void installController() {
    }


    public JComponent createHeader(String title, String description, int size) {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 5 10 5 10", "[fill]"));
        panel.add(new TitleForm(title, JLabel.LEFT, (4 - size)));
        panel.add(new DescriptionForm(description));
        return panel;
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

}
