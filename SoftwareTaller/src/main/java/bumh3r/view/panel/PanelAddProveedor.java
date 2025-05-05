package bumh3r.view.panel;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

public class PanelAddProveedor extends Panel {
    public static final String ID = PanelAddEmployee.class.getName();
    private JTextArea description;
    private InputText firstname,address, email;
    private InputTextPhone phone;
    private ButtonDefault buttonAdd;

    public PanelAddProveedor() {
        initComponents();
        init();
    }

    private void initComponents() {
        phone = new InputTextPhone();
        firstname = new InputText("Ingrese Nombre", 100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        address = new InputText("Ingrese la Dirección", 100).setIcon(PathResources.Icon.modal + "ic_municipality.svg");
        email = new InputText("Ingrese Correo", 100).setIcon(PathResources.Icon.modal + "ic_email.svg");
        buttonAdd = new ButtonDefault("Agregar");
        description = new JTextArea("En el apartado de proveedores puedes agregar tus proveedores. Para agregar un nuevo proveedor, llena los campos solicitados y da clic en el botón de agregar.");
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

        add(createdSubTitle("Datos Personales"), "grow 0,gapy 5 5,al center");
        add(createdGramatical("Nombre Completo"), "gapy 10");
        add(firstname);
        add(createdGramatical("Correo"));
        add(email);
        add(createdGramatical("Teléfono"));
        add(phone);
        add(createdGramatical("Dirección"));
        add(address);
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
