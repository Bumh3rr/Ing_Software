package bumh3r.view.panel;

import bumh3r.components.input.InputArea;
import bumh3r.components.table.TableSimple;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.DetalleVenta;
import bumh3r.model.Pago;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import bumh3r.model.Venta;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.miginfocom.swing.MigLayout;

public class PanelDetallesVenta extends Panel {
    public static final String ID = PanelDetallesVenta.class.getName();
    private LabelForDescription description;
    private TableSimple<Reparacion> reparacionTable;
    private TableSimple<Refaccion> refaccionTable;
    private TableSimple<Pago> pagoTable;
    private JButton button_cancelarVenta, button_agregarPago;
    private InputText id, nombre, telefono_movil, telefono_fijo;
    private InputArea direccion;
    private Function<Reparacion, Object[]> objReparacion = reparacion -> new Object[]{
            reparacion.getCategoria().getNombre(),
            reparacion.getReparacion(),
            String.format("%s %s", reparacion.getEmpleado().getNombre(), reparacion.getEmpleado().getApellido()),
            String.format("$%.2f", reparacion.getPrecio()),
            String.format("$%.2f", reparacion.getAbono())
    };

    private Function<Refaccion, Object[]> objRefaccion = refaccion -> new Object[]{
            refaccion.getCategoria().getNombre(),
            refaccion.getNombre(),
            String.format("$%.2f", refaccion.getPrecio_venta()),
            refaccion.getProveedor().toString()
    };

    private Function<Pago, Object[]> objPago = pago -> new Object[]{
            pago.getMetodoPago().getNombre(),
            String.format("$%.2f", pago.getMonto()),
            DateFull.getDateOnly(pago.getFecha()),
    };

    public void installEventShowPanelAddPago(Runnable runnable) {
        button_agregarPago.addActionListener(e -> runnable.run());
    }

    public void installEventCancelarVenta(Runnable runnable) {
        button_cancelarVenta.addActionListener(e -> runnable.run());
    }

    public PanelDetallesVenta() {
        initComponents();
        init();
    }

    private void initComponents() {
        description = new LabelForDescription("En este apartado podrás seleccionar las reparaciones, agregar refacciones y registrar el pago para completar la venta.");
        button_cancelarVenta = new ButtonAccentBase("Cancelar Venta", "#ff4013");
        button_agregarPago = new ButtonAccentBase("Agregar Pago", "#ffb71b");
        reparacionTable = new TableSimple<>(new String[]{"Categoría", "Reparación", "Técnico Encargado", "Precio", "Abono"});
        reparacionTable.installParentScroll(this);
        refaccionTable = new TableSimple<>(new String[]{"Categoría", "Nombre", "Precio", "Proveedor"});
        refaccionTable.installParentScroll(this);
        pagoTable = new TableSimple<>(new String[]{"Método de Pago", "Monto", "Fecha"});
        pagoTable.installParentScroll(this);

        id = getInstance();
        nombre = getInstance();
        telefono_movil = getInstance();
        telefono_fijo = getInstance();
        direccion = new InputArea();
        direccion.setEnabled(false);
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
        add(button_cancelarVenta, "span,grow 0,al trail");
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
        panel.add(direccion.createdInput(), "span,grow");
        return panel;
    }

    public void setData(Venta venta) {
        id.setText(String.valueOf(venta.getId()));
        nombre.setText(venta.getNota().getCliente().getNombre());
        telefono_movil.setText(venta.getNota().getCliente().getTelefono_movil());
        telefono_fijo.setText(venta.getNota().getCliente().getTelefono_fijo());
        direccion.setText(venta.getNota().getCliente().getDireccion());

        List<Reparacion> reparaciones = new ArrayList<>();
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getReparacion() != null) {
                reparaciones.add(detalle.getReparacion());
                break;
            }
        }

        List<Refaccion> refacciones = new ArrayList<>();
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getRefaccion() != null) {
                refacciones.add(detalle.getRefaccion());
                break;
            }
        }
        if (!refacciones.isEmpty())
            refaccionTable.addAll(refacciones, objRefaccion);

        if (!reparaciones.isEmpty())
            reparacionTable.addAll(reparaciones, objReparacion);

        pagoTable.addAll(venta.getPagos(), objPago);
    }

}
