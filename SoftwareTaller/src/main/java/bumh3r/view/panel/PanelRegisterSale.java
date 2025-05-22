package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.form.DescriptionForm;
import bumh3r.components.input.InputText;
import bumh3r.modal.CustomModal;
import bumh3r.model.MetodoPago;
import bumh3r.model.Nota;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion_Dispositivo;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelRegisterSale extends Panel {
    public static final String ID = PanelRegisterSale.class.getName();
    private DescriptionForm description;
    private Nota nota;
    private Table<Reparacion_Dispositivo> reparacionTable;
    private Table<Refaccion> refaccionTable;
    private JButton buttonSelectReparation, buttonSelectRepair, buttonGenerateSale;
    private List<Reparacion_Dispositivo> reparacionList;
    private List<Refaccion> refaccionList;
    private FlatComboBox<MetodoPago> comboBoxMethodPago;
    private InputText inputDescuento,inputMonto;

    public PanelRegisterSale(Nota nota) {
        this.nota = nota;
        initComponents();
        init();
    }

    private void initComponents() {
        description = new DescriptionForm("En este apartado podrás seleccionar las reparaciones, agregar refacciones y registrar el pago para completar la venta.");
        buttonSelectReparation = new ButtonAccentBase("Seleccionar Reparaciones","#ff8307");
        buttonSelectRepair = new ButtonAccentBase("Seleccionar Refacciones","#ff4013");
        buttonGenerateSale = new ButtonDefault("Generar Venta");

        reparacionTable = new Table<>(new String[]{"Tipo Reparación", "Reparación", "Precio","Abono"});
        reparacionTable.setNameAccion("Remover");

        refaccionTable = new Table<>(new String[]{"Nombre", "Unidades", "Precio Unidad","SubTotal"});
        refaccionTable.setNameAccion("Remover");

        comboBoxMethodPago = new FlatComboBox<>();
        MetodoPago.ListMetodosPago.addItemsMetodoPago(comboBoxMethodPago);

        inputDescuento = new InputText("Introduce el descuento", 100);
        inputMonto = new InputText("Introduce el monto", 100);
        buttonSelectReparation.addActionListener(e -> {
            ModalDialog.pushModal(
                    CustomModal.builder()
                            .component(new PanelSelectReparacion(this.nota.getDispositivos().get(0).getListReparaciones()))
                            .title("Seleccionar Reparaciónes")
                            .icon(modal + "ic_select.svg")
                            .buttonClose(false)
                            .rollback(()-> ModalDialog.popModel(ID))
                            .ID(ID)
                            .build(),
                    ID
            );
        });


        buttonSelectRepair.addActionListener(e -> {
            ModalDialog.pushModal(
                    CustomModal.builder()
                            .component(new PanelSelectRefacciones())
                            .title("Seleccionar Refacciones")
                            .icon(modal + "ic_repair.svg")
                            .buttonClose(false)
                            .rollback(()-> ModalDialog.popModel(ID))
                            .ID(ID)
                            .build(),
                    ID
            );
        });
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n n n", "[fill,grow]"));
        add(description);
        add(createdSubTitle("Reparaciones",15f),"grow 0,al center");
        add(buttonSelectReparation,"grow 0,al trail");
        add(reparacionTable,"h 150:180");
        add(new JSeparator(),"gapx 20 20");

        add(createdSubTitle("Refacciones",15f),"grow 0,al center");
        add(buttonSelectRepair,"grow 0,al trail");
        add(refaccionTable,"h 150:180");
        add(new JSeparator(),"gapx 20 20");

        add(createdSubTitle("Pago",15f),"grow 0,al center");
        add(created(),"gapx 20 20");
        add(extracted(),"gapx 20 20,gapy 10 10");

        add(buttonGenerateSale,"grow 0,al trail");
    }

    private JComponent created() {
        JPanel panel  = new JPanel(new MigLayout("wrap 2,fillx,insets 1 n 1 n","[fill,grow][fill,grow]"));
        panel.add(createdGramaticalP("Método de Pago"),"span,grow 0");
        panel.add(comboBoxMethodPago,"span,grow 0");
        panel.add(createdGramaticalP("Descuento"));
        panel.add(createdGramaticalP("Monto"));
        panel.add(inputDescuento);
        panel.add(inputMonto);
        return panel;
    }

    private JComponent extracted() {
        JPanel panel = new JPanel(new MigLayout("wrap 3,fillx,insets 1 n 1 n","center"));
        panel.add(createdGramaticalP("Precio Total"),"grow 0");
        panel.add(createdGramaticalP("Monto Abonado"),"grow 0");
        panel.add(createdGramaticalP("SubTotal"),"grow 0");
        panel.add(new JLabel("$100.00"));
        panel.add(new JLabel("$50.00"));
        panel.add(new JLabel("$50.00"));
        return panel;
    }


}
