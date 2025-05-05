package bumh3r.view.panel;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

public class PanelAddUsuario extends Panel {
    public static final String ID = PanelAddCliente.class.getName();
    private JTextArea description;
    private InputText usuario,contraseña,confirmContraseña;
    private FlatComboBox type;
    private FlatComboBox permisos;
    private ButtonDefault buttonAdd;

    public PanelAddUsuario() {
        initComponents();
        init();
    }

    private void initComponents() {
        usuario = new InputText("Ingrese el username", 100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        contraseña = new InputText("Ingrese la contraseña", 100);
        confirmContraseña = new InputText("Confirme la contraseña", 100);
        type = new FlatComboBox();
        permisos = new FlatComboBox();
        type.addItem("Seleccione el Empleado");
        permisos.addItem("Seleccione los Permisos");

        buttonAdd = new ButtonDefault("Agregar");
        description = new JTextArea("En este apartado podrás registrar los usuarios en el sistema, asegurate de ingresar los datos correctos.");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBorder(BorderFactory.createEmptyBorder());
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "background:null");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 250:450", "fill,grow"));
        add(description);

        add(createdSubTitle("Datos del Usuario Nuevo"), "grow 0,gapy 5 5,al center");
        add(createdGramatical("Usuario"), "gapy 10");
        add(usuario);
        add(createdGramatical("Contraseña"));
        add(contraseña);
        add(createdGramatical("Confirmar Contraseña"));
        add(confirmContraseña);
        add(createdGramatical("Empleado"));
        add(type);

        add(buttonAdd, "grow 0,gapy 15,al trail");
    }


    private JComponent createdGramatical(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "font:12;"
        );
        return label;
    }

    private JComponent createdSubTitle(String str) {
        return new LabelPublicaSans(str).size(15f).type(FontPublicaSans.FontType.BOLD_BLACK);
    }
}
