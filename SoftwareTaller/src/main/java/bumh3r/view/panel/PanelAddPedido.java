package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputArea;
import bumh3r.components.input.InputFormatterNumber;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.modal.CustomModal;
import bumh3r.model.New.CategoriaN;
import bumh3r.model.New.ProveedorN;
import bumh3r.model.New.RefaccionN;
import bumh3r.request.DetallesPedidosRequest;
import bumh3r.request.PedidoRequest;
import bumh3r.system.panel.Panel;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelAddPedido extends Panel {
    public static final String ID = PanelAddPedido.class.getName();
    private InputFormatterNumber unidades;
    private LabelForDescription description;
    private InputArea descriptionArea;
    private FlatComboBox<Object> refaccion, proveedor;
    private ButtonDefault buttonAdd;
    private ButtonAccentBase buttonAddDetalle;
    private Table<DetallesPedidosRequest> table;

    public PanelAddPedido() {
        initComponents();
        initListeners();
        init();
    }

    private void initListeners() {
        buttonAddDetalle.addActionListener((x) -> {
        });
    }

    private void initComponents() {
        table = new Table<>(new String[]{"Unidades", "Refacción"});
        table.setNameAccion("Eliminar");
        table.installParentScroll(this);

        buttonAddDetalle = new ButtonAccentBase("Agregar Detalle");
        unidades = new InputFormatterNumber(100);

        refaccion = new FlatComboBox<>();
        proveedor = new FlatComboBox<>();
        proveedor.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione el proveedor"}));
        refaccion.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione la refacción a pedir"}));

        buttonAdd = new ButtonDefault("Agregar pedido");
        description = new LabelForDescription("En este apartado podrás registrar los productos a solicitar al pedido, asegurate de ingresar los datos correctos.");
        descriptionArea = new InputArea();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 450:550", "fill,grow"));
        add(description);
        add(createdSubTitle("Proveedor", 15), "grow 0,gapy 5 0,al center");
        add(createdGramaticalP("Proveedor"));
        add(proveedor);
        add(createdGramaticalP("Observaciones"));
        add(descriptionArea.createdInput());

        add(createdSubTitle("Detalles del Pedido", 15), "grow 0,gapy 5 0,al center");
        add(createdGramaticalP("Unidades"));
        add(unidades);
        add(createdGramaticalP("Refacción"));
        add(refaccion);
        add(buttonAddDetalle, "grow 0,gapy 3,al trail");

        add(createdSubTitle("Detalles", 15), "grow 0,gapy 5 0,al center");
        add(table, "grow,push,gapy 5 0,h 130!");
        add(buttonAdd, "grow 0,gapy 5,al trail");
    }

    public PedidoRequest getValue() {
        String descriptionValue = !this.descriptionArea.getText().isEmpty() ? this.descriptionArea.getText().strip() : null;
        ProveedorN proveedorValue = this.proveedor.getSelectedItem() instanceof ProveedorN ? (ProveedorN) this.proveedor.getSelectedItem() : null;
        List<DetallesPedidosRequest> detalles = table.getDataList();
        return new PedidoRequest(descriptionValue, proveedorValue, detalles);
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            this.descriptionArea.setText("");
            this.refaccion.setSelectedIndex(0);
            this.proveedor.setSelectedIndex(0);
            this.unidades.setValue(null);
            table.cleanData();
        });
    }

}