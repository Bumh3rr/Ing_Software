package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.TableSimple;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.Pago;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion_Dispositivo;
import bumh3r.system.panel.Panel;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelDetallesVenta extends Panel {
    public static final String ID = PanelDetallesVenta.class.getName();
    private LabelForDescription description;
    private TableSimple<Reparacion_Dispositivo> reparacionTable;
    private TableSimple<Refaccion> refaccionTable;
    private TableSimple<Pago> pagoTable;
    private JButton button_guardarCambios, button_cancelarVenta,button_agregarPago;
    private InputText id, nombre, telefono_movil, telefono_fijo;
    private FlatTextArea inputObservaciones;

    public PanelDetallesVenta() {
        initComponents();
        init();
    }

    private void initComponents() {
        description = new LabelForDescription("En este apartado podrás seleccionar las reparaciones, agregar refacciones y registrar el pago para completar la venta.");
        button_guardarCambios = new ButtonDefault("Guardar Cambios");
        button_cancelarVenta = new ButtonAccentBase("Cancelar Venta", "#ff4013");
        button_agregarPago = new ButtonAccentBase("Agregar Pago","#ffb71b");
        reparacionTable = new TableSimple<>(new String[]{"Tipo Reparación", "Reparación", "Precio", "Abono"});
        refaccionTable = new TableSimple<>(new String[]{"Nombre", "Unidades", "Precio Unidad", "SubTotal"});
        pagoTable = new TableSimple<>(new String[]{"Método de Pago", "Monto", "Fecha"});

        id = getInstance();
        nombre = getInstance();
        telefono_movil = getInstance();
        telefono_fijo = getInstance();

        inputObservaciones = new FlatTextArea();
        inputObservaciones.setDocument(new LimitTextDocument(250));
        inputObservaciones.setLineWrap(true);
        inputObservaciones.setWrapStyleWord(true);
        inputObservaciones.setEnabled(false);

        id.setText("123456");
        nombre.setText("example");
        telefono_movil.setText("123-456-7890");
        telefono_fijo.setText("123-456-7890");
        inputObservaciones.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod");
        button_agregarPago.addActionListener((x)->{
            ModalDialog.pushModal(
                    CustomModal.builder()
                            .component(new PanelAddPago())
                            .title("Agregar Pago")
                            .buttonClose(false)
                            .icon(modal + "ic_pay.svg")
                            .rollback(()-> ModalDialog.popModel(ID))
                            .ID(ID)
                            .build(),
                    ID
            );
        });
    }

    private InputText getInstance() {
        InputText input = new InputText("campo vació", 300) {
            @Override
            public void setText(String t) {
                if (t == null || t.isEmpty()) {
                    t = "";
                }
                super.setText(t);
            }
        };
        input.setEditable(false);
        return input;
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n n n,w 500:650", "[fill,grow]"));
        add(description);
        add(createdSubTitle("Cliente", 15f), "grow 0,al center");
        add(extracted(), "gapx 70 70");
        add(new JSeparator(), "gapx 20 20");

        add(createdSubTitle("Reparaciones", 15f), "grow 0,al center");
        add(reparacionTable, "h 150!");
        add(new JSeparator(), "gapx 20 20");

        add(createdSubTitle("Refacciones", 15f), "grow 0,al center");
        add(refaccionTable, "h 150!");
        add(new JSeparator(), "gapx 20 20");

        add(createdSubTitle("Pagos", 15f), "grow 0,al center");
        add(button_agregarPago, "grow 0,al trail");
        add(pagoTable, "h 150!");
        add(new JSeparator(), "gapx 20 20");

        add(button_cancelarVenta, "grow 0,al lead,split 2");
        add(button_guardarCambios, "grow 0,al trail");
    }

    private JComponent extracted() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 1 n 1 n", "center,fill,grow"));
        panel.add(Panel.createdGramaticalP("ID"));
        panel.add(createdGramaticalP("Nombre"));
        panel.add(id, "grow");
        panel.add(nombre, "grow");
        panel.add(createdGramaticalP("Teléfono Móvil"));
        panel.add(createdGramaticalP("Teléfono Fijo"));
        panel.add(telefono_movil, "grow");
        panel.add(telefono_fijo, "grow");

        panel.add(createdGramaticalP("Dirección"), "span,grow 0");
        panel.add(createInputObservaciones(), "span,grow");
        return panel;
    }

    private JComponent createInputObservaciones() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 1"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        inputObservaciones.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        panel.add(inputObservaciones, "grow,push");
        return panel;
    }

}
