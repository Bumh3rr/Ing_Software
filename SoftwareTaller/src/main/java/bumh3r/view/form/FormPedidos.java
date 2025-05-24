package bumh3r.view.form;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.controller.ControladorPedido;
import bumh3r.model.New.PedidoN;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class FormPedidos extends Form {
    private ButtonDefault buttonAddProducto;
    @Getter
    private Table<PedidoN> table;
    private ControladorPedido controladorPedido;
    private final Function<PedidoN, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.getId(),
            usuarioMapper.getProveedor().getNombre(),
            DateFull.getDateFull(usuarioMapper.getFecha_pedido()),
            usuarioMapper.getEstado(),
            usuarioMapper.getObservaciones()
    };

    @Override
    public void installController() {
        this.controladorPedido = new ControladorPedido(this);
    }

    @Override
    public void formInit() {
        table.setRowClickListener(controladorPedido.mostrarDetallesPedido);
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        PoolThreads.getInstance().execute(getEventFormRefresh());
    }

    public void installEventAddPedido(Runnable runnable) {
        buttonAddProducto.addActionListener(e -> runnable.run());
    }

    public FormPedidos() {
        initComponents();
        init();
    }

    private void initComponents() {
        buttonAddProducto = new ButtonDefault("Agregar Pedido");
        table = new Table<>(new String[]{"ID", "Nombre del Proveedor", "Fecha del Pedido", "Estado", "Observaciones"}, dataMapper);
        table.setNameAccion("Ver Detalles");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Pedidos", "Apartado visualizar los pedidos, ver su estado, ver sus detalles y agregar.", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]", "[][fill]"));
        panel.add(buttonAddProducto, "grow 0,al trail");
        panel.add(table, "grow,push,gapy 5 0,gapx 0 2");
        return panel;
    }

    public void addOneTable(PedidoN pedido) {
        PoolThreads.getInstance().execute(() -> table.addOne(pedido));
    }

    public void addAllTable(List<PedidoN> pedidos) {
        PoolThreads.getInstance().execute(() -> table.addAll(pedidos));
    }

}