package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.TableSimple;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputArea;
import bumh3r.components.label.LabelForDescription;
import bumh3r.components.label.LabelTextArea;
import bumh3r.model.New.DetallesPedidoN;
import bumh3r.model.New.PedidoN;
import bumh3r.model.Pedido;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class PanelDetailsPedido extends Panel {
    private LabelForDescription description;
    private InputArea descripcionText;
    private TableSimple<DetallesPedidoN> table;
    private LabelTextArea fecha, proveedor;
    @Getter
    private FlatComboBox<PedidoN.EstadoPedido> status;
    private ButtonDefault update;
    private final Function<DetallesPedidoN, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.getId(),
            usuarioMapper.getRefaccion().getNombre(),
            usuarioMapper.getCantidad()
    };

    public PanelDetailsPedido() {
        initComponents();
        init();
    }

    public void setPedido(PedidoN pedido) {
        fecha.setText(DateFull.getDateFull(pedido.getFecha_pedido()));
        proveedor.setText(String.format(" ID: %d | %s", pedido.getProveedor().getId(), pedido.getProveedor().getNombre()));
        status.setSelectedItem(pedido.getEstado());
        descripcionText.setText(pedido.getObservaciones());
    }

    public void setDetails(List<DetallesPedidoN> detalles) {
        table.addAll(detalles, dataMapper);
    }

    public void installEventUpdate(Runnable runnable) {
        update.addActionListener(e -> runnable.run());
    }

    private void initComponents() {
        table = new TableSimple<>(new String[]{"ID", "Refacción", "Cantidad"});
        table.installParentScroll(this);
        update = new ButtonDefault("Actualizar Estado");
        description = new LabelForDescription("En este apartado podrás visualizar los detalles del pedido seleccionado.");
        proveedor = new LabelTextArea();
        fecha = new LabelTextArea();
        descripcionText = new InputArea();
        descripcionText.setEnabled(false);
        status = new FlatComboBox<>();
        status.setModel(new DefaultComboBoxModel<>(PedidoN.EstadoPedido.values()));
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 2 45 20 45,w 350:460", "fill,grow"));
        add(description, "gapy 0 5");
        add(createdGramaticalP("Fecha de Pedido"));
        add(fecha);
        add(createdGramaticalP("Proveedor"));
        add(proveedor);
        add(createdGramaticalP("Estado"));
        add(status,"grow,split 2");
        add(update,"grow 0");
        add(createdGramaticalP("Observaciones"));
        add(descripcionText.createdInput(), "grow,push,wrap");
        add(table, "h 250!,gapy 5 0");
    }

}
