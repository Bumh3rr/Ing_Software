package bumh3r.view.panel;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.controller.ControladorCliente;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.New.TipoEmpleado;
import bumh3r.model.other.EstadosMx;
import bumh3r.request.ClienteRequest;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class PanelAddCliente extends Panel {
    public static final String ID = PanelAddCliente.class.getName();
    private JTextArea description;
    private InputText name, address;
    private InputTextPhone phone_movil, phone_fijo;
    private ButtonDefault buttonAdd;

    public PanelAddCliente() {
        initComponents();
        init();
    }

    private void initComponents() {
        phone_movil = new InputTextPhone();
        phone_fijo = new InputTextPhone();
        name = new InputText("Ingrese Nombre completo", 100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        address = new InputText("Ingrese la Dirección", 100).setIcon(PathResources.Icon.modal + "ic_municipality.svg");

        buttonAdd = new ButtonDefault("Agregar");
        description = new JTextArea("En este apartado podrás registrar los clientes en el sistema, asegurate de ingresar los datos correctos.");
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
        add(createdGramaticalP("Nombre Completo"), "gapy 10");
        add(name);
        add(createdGramaticalP("Teléfono 1"));
        add(phone_movil);
        add(createdGramaticalP("Teléfono 2"));
        add(phone_fijo);
        add(createdGramaticalP("Dirección"));
        add(address);
        add(buttonAdd, "grow 0,gapy 15,al trail");
    }

    private JComponent createdSubTitle(String str) {
        return new LabelPublicaSans(str).size(15f).type(FontPublicaSans.FontType.BOLD_BLACK);
    }

    public ClienteRequest getValue() {
        String nombreValue = !this.name.getText().isEmpty() ? this.name.getText().strip() : null;
        String telefono_movil = this.phone_movil.getValue() != null ? this.phone_movil.getValue().toString() : null;
        String telefono_fijo = this.phone_fijo.getValue() != null ? this.phone_fijo.getValue().toString() : null;
        String direccionValue = !this.address.getText().isEmpty() ? this.address.getText().strip() : null;
        return new ClienteRequest(nombreValue, telefono_movil, telefono_fijo, direccionValue);
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            this.name.setText("");
            this.phone_movil.setValue(null);
            this.phone_fijo.setValue(null);
            this.address.setText("");
        });
    }


    public void installEventAddCustomer(Runnable runnable) {
        buttonAdd.addActionListener((e) -> runnable.run());
    }
}
