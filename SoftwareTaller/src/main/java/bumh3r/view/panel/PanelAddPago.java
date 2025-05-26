package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.MetodoPago;
import bumh3r.request.PagoRequest;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class PanelAddPago extends Panel {
    public static final String ID = PanelAddPago.class.getName();
    private LabelForDescription description;
    private InputFormattedDecimal input_monto;
    private FlatComboBox<MetodoPago> metodo_pago;
    private JLabel precio_total, monto_pendiente;
    @Getter
    private ButtonDefault agregarPago;

    public void installEventAgregarPago(Runnable runnable) {
        agregarPago.addActionListener(e -> runnable.run());
    }

    public PanelAddPago() {
        initComponents();
        init();
    }

    private void initComponents() {
        description = new LabelForDescription("En este apartado podrás agregar el pago de la venta.");
        input_monto = new InputFormattedDecimal(0);
        agregarPago = new ButtonDefault("Agregar Pago");
        metodo_pago = new FlatComboBox<>();
        precio_total = new JLabel();
        monto_pendiente = new JLabel();
        MetodoPago.addItemsMetodoPago(metodo_pago);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n n n,w 350:450", "[fill,grow]"));
        add(description, "growx");
        add(extracted(), "gapx 20 20,gapy 10 10");
        add(createdGramaticalP("Método de Pago"), "grow 0");
        add(metodo_pago, "growx");
        add(createdGramaticalP("Monto"), "grow 0");
        add(input_monto, "growx");
        add(agregarPago, "grow 0,al trail");
    }

    private JComponent extracted() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 1 n 1 n", "center"));
        panel.add(createdGramaticalP("Precio venta"), "grow 0");
        panel.add(createdGramaticalP("Monto Pendiente"), "grow 0");
        panel.add(precio_total);
        panel.add(monto_pendiente);
        return panel;
    }

    public void setValue(Float precio, Float montoPendiente) {
        SwingUtilities.invokeLater(() -> {
            precio_total.setText(String.format("$%.2f", precio));
            monto_pendiente.setText(String.format("$%.2f", montoPendiente));
            input_monto.setLimit(montoPendiente);
        });
    }

    public PagoRequest getValue() {
        MetodoPago metodo = (MetodoPago) metodo_pago.getSelectedItem();
        Float monto =  input_monto.getValue() == null ? null : Float.parseFloat(input_monto.getValue().toString());
        return new PagoRequest(metodo, monto);
    }

}
