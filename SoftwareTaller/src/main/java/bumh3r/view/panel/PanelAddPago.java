package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.MetodoPago;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class PanelAddPago extends Panel {
    public static final String ID = PanelAddPago.class.getName();
    private LabelForDescription description;
    private InputText monto_recibido;
    private FlatComboBox<MetodoPago> metodo_pago;
    private ButtonDefault agregarPago;

    public PanelAddPago() {
        initComponents();
        init();
    }

    private void initComponents() {
        description = new LabelForDescription("En este apartado podrás agregar el pago de la venta.");
        monto_recibido = new InputText("Ingrese el monto recibido");
        agregarPago = new ButtonDefault("Agregar Pago");
        metodo_pago = new FlatComboBox<>();
        MetodoPago.ListMetodosPago.addItemsMetodoPago(metodo_pago);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n n n,w 350:450", "[fill,grow]"));
        add(description, "growx");
        add(extracted(),"gapx 20 20,gapy 10 10");
        add(createdGramaticalP("Método de Pago"),"grow 0");
        add(metodo_pago, "growx");
        add(createdGramaticalP("Monto"),"grow 0");
        add(monto_recibido, "growx");
        add(agregarPago, "grow 0,al trail");
    }


    private JComponent extracted() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 1 n 1 n","center"));
        panel.add(createdGramaticalP("Precio venta"),"grow 0");
        panel.add(createdGramaticalP("Monto Pendiente"),"grow 0");
        panel.add(new JLabel("$100.00"));
        panel.add(new JLabel("$50.00"));
        return panel;
    }

}
