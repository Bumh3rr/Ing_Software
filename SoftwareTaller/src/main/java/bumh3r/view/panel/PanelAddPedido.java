package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputArea;
import bumh3r.components.input.InputFormatterNumber;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.New.ProveedorN;
import bumh3r.model.New.RefaccionN;
import bumh3r.request.DetallesPedidosRequest;
import bumh3r.request.PedidoRequest;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class PanelAddPedido extends Panel {
    public static final String ID = PanelAddPedido.class.getName();
    private InputFormatterNumber unidades;
    private LabelForDescription description;
    private InputArea descriptionArea;
    @Getter
    private FlatComboBox<Object> refaccion, proveedor;
    private ButtonDefault buttonAdd;
    private ButtonAccentBase buttonAddDetalle;
    @Getter
    private Table<DetallesPedidosRequest> table;
    private final Function<DetallesPedidosRequest, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.cantidad(),
            usuarioMapper.refaccionN()
    };

    public void installEventAddDetalle(Runnable runnable) {
        buttonAddDetalle.addActionListener(e -> runnable.run());
    }

    public void installEventAddPedido(Runnable runnable) {
        buttonAdd.addActionListener(e -> runnable.run());
    }

    public void installEventRemoveDetalle(Function<DetallesPedidosRequest,Void> event) {
        table.setRowClickListener(event);
    }

    public PanelAddPedido() {
        initComponents();
        init();
    }

    private void initComponents() {
        table = new Table<>(new String[]{"Unidades", "Refacción"},dataMapper);
        table.setNameAccion("Eliminar");
        table.installParentScroll(this);
        buttonAddDetalle = new ButtonAccentBase("Agregar Detalle");
        unidades = new InputFormatterNumber(10000);
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
        add(table, "grow,push,gapy 5 0,h 230!");
        add(buttonAdd, "grow 0,gapy 5,al trail");
    }

    public PedidoRequest getValue() {
        String descriptionValue = !this.descriptionArea.getText().isEmpty() ? this.descriptionArea.getText().strip() : null;
        ProveedorN proveedorValue = this.proveedor.getSelectedItem() instanceof ProveedorN ? (ProveedorN) this.proveedor.getSelectedItem() : null;
        List<DetallesPedidosRequest> detalles = table.getDataList();
        return new PedidoRequest(descriptionValue, proveedorValue, detalles);
    }

    public DetallesPedidosRequest getValueDetail() {
        Integer unidadesValue = this.unidades.getValue() != null ? Integer.valueOf(this.unidades.getValue().toString()) : null;
        RefaccionN refaccionValue = this.refaccion.getSelectedItem() instanceof RefaccionN ? (RefaccionN) this.refaccion.getSelectedItem() : null;
        return new DetallesPedidosRequest(unidadesValue, refaccionValue);
    }

    public void cleanValueDetail() {
        SwingUtilities.invokeLater(() -> {
            this.refaccion.setSelectedIndex(0);
            this.unidades.setValue(null);
        });
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

    public void addOneTable(DetallesPedidosRequest cliente) {
        PoolThreads.getInstance().execute(() -> table.addOne(cliente));
    }

    public void setProveedorModel(List<ProveedorN> list) {
        SwingUtilities.invokeLater(() -> {
            this.proveedor.removeAllItems();
            this.proveedor.addItem("Seleccione un Proveedor");
            list.forEach((proveedorN) -> this.proveedor.addItem(proveedorN));
        });
    }

    public void setRefaccionModel(List<RefaccionN> list) {
        SwingUtilities.invokeLater(() -> {
            this.refaccion.removeAllItems();
            this.refaccion.addItem("Seleccione la refacción a pedir");
            list.forEach((refaccionN) -> this.refaccion.addItem(refaccionN));
        });
    }

}