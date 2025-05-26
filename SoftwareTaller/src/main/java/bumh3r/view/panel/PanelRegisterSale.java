package bumh3r.view.panel;

import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.table.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.form.DescriptionForm;
import bumh3r.model.MetodoPago;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import bumh3r.request.PagoRequest;
import bumh3r.request.VentaRequest;
import bumh3r.system.panel.Panel;
import bumh3r.utils.thread.PoolThreads;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;

public class PanelRegisterSale extends Panel {
    public static final String ID = PanelRegisterSale.class.getName();
    private DescriptionForm description;
    private Table<Reparacion> reparacionTable;
    private Table<Refaccion> refaccionTable;
    private JButton buttonSelectReparation, buttonSelectRepair, buttonGenerateSale;
    private FlatComboBox<MetodoPago> comboBoxMethodPago;
    private InputFormattedDecimal inputDescuento, inputMonto;
    private JLabel precioTotal, montoAbonado, subTotal, descuentoLabel;
    private final Function<Reparacion, Object[]> dataMapperReparacion = usuarioMapper -> new Object[]{
            usuarioMapper.getCategoria(),
            usuarioMapper.getReparacion(),
            usuarioMapper.getPrecio(),
            usuarioMapper.getAbono()
    };

    private final Function<Refaccion, Object[]> dataMapperRefaccion = usuarioMapper -> new Object[]{
            usuarioMapper.getCategoria(),
            usuarioMapper.getNombre(),
            usuarioMapper.getPrecio_venta(),
            usuarioMapper.getProveedor().getNombre(),
    };

    public void installEventShowSelectReparacion(Runnable event) {
        buttonSelectReparation.addActionListener(e -> event.run());
    }

    public void installEventShowSelectRefaccion(Runnable event) {
        buttonSelectRepair.addActionListener(e -> event.run());
    }

    public void installEventButtonGenerarVenta(Runnable event) {
        buttonGenerateSale.addActionListener(e -> event.run());
    }

    public void installEventEliminarReparacion(Function<Reparacion, Void> event) {
        reparacionTable.setRowClickListener(event);
    }

    public void installEventEliminarRefaccion(Function<Refaccion, Void> event) {
        refaccionTable.setRowClickListener(event);
    }

    public PanelRegisterSale() {
        initComponents();
        init();
    }

    private void initComponents() {
        description = new DescriptionForm("En este apartado podrás seleccionar las reparaciones, agregar refacciones y registrar el pago para completar la venta.");
        buttonSelectReparation = new ButtonAccentBase("Seleccionar Reparaciones", "#ff8307");
        buttonSelectRepair = new ButtonAccentBase("Seleccionar Refacciones", "#ff4013");
        buttonGenerateSale = new ButtonDefault("Generar Venta");
        reparacionTable = new Table<>(new String[]{"Categoría", "Reparación", "Precio", "Abono"}, dataMapperReparacion);
        reparacionTable.installParentScroll(this);
        reparacionTable.setNameAccion("Eliminar");
        refaccionTable = new Table<>(new String[]{"Categoría", "Nombre", "Precio", "Proveedor"}, dataMapperRefaccion);
        refaccionTable.installParentScroll(this);
        refaccionTable.setNameAccion("Eliminar");
        comboBoxMethodPago = new FlatComboBox<>();
        MetodoPago.addItemsMetodoPago(comboBoxMethodPago);
        inputDescuento = new InputFormattedDecimal(0);
        inputMonto = new InputFormattedDecimal(0);
        precioTotal = new JLabel("$0.00");
        montoAbonado = new JLabel("$0.00");
        subTotal = new JLabel("$0.00");
        descuentoLabel = new JLabel("$0.00");

        inputDescuento.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        Float value = inputDescuento.getValue() == null ? 0.0f : Float.parseFloat(inputDescuento.getValue().toString());
                        setPresupuesto(value);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        Float value = inputDescuento.getValue() == null ? 0.0f : Float.parseFloat(inputDescuento.getValue().toString());
                        setPresupuesto(value);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        Float value = inputDescuento.getValue() == null ? 0.0f : Float.parseFloat(inputDescuento.getValue().toString());
                        setPresupuesto(value);
                    }
                }
        );
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n n n", "[fill,grow]"));
        add(description);
        add(createdSubTitle("Reparaciones", 15f), "grow 0,al center");
        add(buttonSelectReparation, "grow 0,al trail");
        add(reparacionTable, "h 150:180");
        add(new JSeparator(), "gapx 20 20");

        add(createdSubTitle("Refacciones", 15f), "grow 0,al center");
        add(buttonSelectRepair, "grow 0,al trail");
        add(refaccionTable, "h 150:180");
        add(new JSeparator(), "gapx 20 20");

        add(createdSubTitle("Pago", 15f), "grow 0,al center");
        add(extracted(), "gapx 20 20");
        add(created(), "gapx 20 20,gapy 10 10");

        add(buttonGenerateSale, "grow 0,al trail");
    }

    private JComponent created() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 1 n 1 n", "[fill,grow][fill,grow]"));
        panel.add(createdGramaticalP("Método de Pago"), "span,grow 0");
        panel.add(comboBoxMethodPago, "span,grow 0");
        panel.add(createdGramaticalP("Descuento"));
        panel.add(createdGramaticalP("Monto"));
        panel.add(inputDescuento);
        panel.add(inputMonto);
        return panel;
    }

    private JComponent extracted() {
        JPanel panel = new JPanel(new MigLayout("wrap 4,fillx,insets 1 n 1 n", "center"));
        panel.add(createdGramaticalP("Precio Total"), "grow 0");
        panel.add(createdGramaticalP("Descuento"), "grow 0");
        panel.add(createdGramaticalP("Monto Abonado"), "grow 0");
        panel.add(createdGramaticalP("SubTotal"), "grow 0");
        panel.add(precioTotal);
        panel.add(descuentoLabel);
        panel.add(montoAbonado);
        panel.add(subTotal);
        return panel;
    }

    public void addOneReparacion(Reparacion reparacion) {
        reparacionTable.addOne(reparacion);
    }

    public void addOneRefaccion(Refaccion refaccion) {
        refaccionTable.addOne(refaccion);
    }

    public void removeOneReparacion(Reparacion reparacion) {
        reparacionTable.getDataList().remove(reparacion);
        reparacionTable.update();
    }

    public void removeOneRefaccion(Refaccion refaccion) {
        refaccionTable.getDataList().remove(refaccion);
        refaccionTable.update();
    }

    public List<Reparacion> getListReparacion() {
        return reparacionTable.getDataList();
    }

    public List<Refaccion> getListRefaccion() {
        return refaccionTable.getDataList();
    }

    public void setPresupuesto() {
        PoolThreads.getInstance().execute(() -> {
            float total = 0.0f;
            for (Reparacion reparacion : reparacionTable.getDataList()) {
                total += reparacion.getPrecio();
            }
            for (Refaccion refaccion : refaccionTable.getDataList()) {
                total += refaccion.getPrecio_venta();
            }

            float abono = 0.0f;
            for (Reparacion reparacion : reparacionTable.getDataList()) {
                abono += reparacion.getAbono();
            }
            float subTotalValue = total - abono;
            precioTotal.setText(String.format("$%.2f", total));
            montoAbonado.setText(String.format("$%.2f", abono));
            subTotal.setText(String.format("$%.2f", subTotalValue));
            descuentoLabel.setText(String.format("$%.2f", 0.0f));
            SwingUtilities.invokeLater(() -> inputDescuento.setLimit(subTotalValue));
        });
    }

    public void setPresupuesto(float descuento) {
        PoolThreads.getInstance().execute(() -> {
            float total = 0.0f;
            for (Reparacion reparacion : reparacionTable.getDataList()) {
                total += reparacion.getPrecio();
            }
            for (Refaccion refaccion : refaccionTable.getDataList()) {
                total += refaccion.getPrecio_venta();
            }

            float abono = 0.0f;
            for (Reparacion reparacion : reparacionTable.getDataList()) {
                abono += reparacion.getAbono();
            }
            float subTotalValue = total - abono - descuento;

            precioTotal.setText(String.format("$%.2f", total));
            descuentoLabel.setText(String.format("$%.2f", descuento));
            montoAbonado.setText(String.format("$%.2f", abono));
            subTotal.setText(String.format("$%.2f", subTotalValue));
            SwingUtilities.invokeLater(() -> inputMonto.setLimit(subTotalValue));
        });
    }

    public VentaRequest getValue() {
        List<Reparacion> reparaciones = reparacionTable.getDataList();
        List<Refaccion> refacciones = refaccionTable.getDataList();
        float total = 0.0f;
        total += (float) reparaciones.stream().mapToDouble(Reparacion::getPrecio).sum();
        total += (float) refacciones.stream().mapToDouble(Refaccion::getPrecio_venta).sum();

        float abono = 0.0f;
        abono += (float) reparaciones.stream().mapToDouble(Reparacion::getAbono).sum();

        float descuento = inputDescuento.getValue() == null ? 0.0f : Float.parseFloat(inputDescuento.getValue().toString());
        float subTotalValue = total - abono - descuento;

        float monto = inputMonto.getValue() == null ? 0.0f : Float.parseFloat(inputMonto.getValue().toString());
        MetodoPago metodoPago = (MetodoPago) comboBoxMethodPago.getSelectedItem();
        return new VentaRequest(reparaciones, refacciones, total, abono, descuento,subTotalValue, new PagoRequest(metodoPago, monto));
    }

}
