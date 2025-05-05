package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.system.form.Form;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class FormInicio extends Form {

    private JPanel statsPanel;
    private ButtonDefault buttonNotes;

    public FormInicio() {
        initComponents();
        init();
    }

    private void initComponents() {
        statsPanel = new JPanel(new MigLayout("wrap 3, insets 5, gap 15", "[grow,fill][grow,fill][grow,fill]", "[]"));
        statsPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,2%);"
                + "[dark]background:lighten(@background,2%)");

        buttonNotes = new ButtonDefault("Ver Menu");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]"));
        add(createHeader("Inicio", "Bienvenido al software de gestión de celulares", 1));
        add(buttonNotes, "grow,gapy 10");

    }

}