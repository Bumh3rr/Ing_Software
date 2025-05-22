package bumh3r.view.form;
import bumh3r.components.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.DetallesPedido;
import bumh3r.model.Pedido;
import bumh3r.model.Proveedor;
import bumh3r.model.Refaccion;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.view.panel.PanelDetailsPedido;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import static bumh3r.archive.PathResources.Icon.modal;

public class FormPedidos extends Form {
    private static final String ID = FormPedidos.class.getName();
    private ButtonDefault buttonAddProducto;
    private Table<Pedido> table;

    @Override
    public void formInit() {
        Proveedor proveedor = new Proveedor(1L, "Proveedor 1", "1234567890", "example@gmail.com", "Dirección 1", "2021-09-01");
        Proveedor proveedor2 = new Proveedor(2L, "Proveedor 2", "1234567890", "example@gmail.com", "Dirección 2", "2021-09-01");
        Proveedor proveedor3 = new Proveedor(3L, "Proveedor 3", "1234567890", "example@gmail.com", "Dirección 3", "2021-09-01");

        List<Refaccion> refacciones = new ArrayList<>();
        refacciones.add(new Refaccion(1L, "Refacción 1", "description","Display", 10, 100.0, 150.0, LocalDate.now(), proveedor));
        refacciones.add(new Refaccion(2L, "Refacción 2", "description","Display", 10, 100.0, 150.0, LocalDate.now(), proveedor2));
        refacciones.add(new Refaccion(3L, "Refacción 3", "description","Display", 10, 100.0, 150.0, LocalDate.now(), proveedor3));


        LinkedList<DetallesPedido> detallesPedidos = new LinkedList<>();
        detallesPedidos.add(new DetallesPedido(1L, proveedor, refacciones.get(0),12));
        detallesPedidos.add(new DetallesPedido(2L, proveedor, refacciones.get(1),12));
        detallesPedidos.add(new DetallesPedido(3L, proveedor, refacciones.get(2),12));

        LinkedList<Pedido> pedidos = new LinkedList<>();
        pedidos.add(new Pedido(1L, proveedor, LocalDate.now(), Pedido.Estado.CANCELADO, "Observaciones",detallesPedidos));
        pedidos.add(new Pedido(2L, proveedor2, LocalDate.now(), Pedido.Estado.PENDIENTE, "Observaciones",detallesPedidos));
        pedidos.add(new Pedido(3L, proveedor3, LocalDate.now(), Pedido.Estado.ENTREGADO, "Observaciones",detallesPedidos));
        showData(pedidos);
    }

    public FormPedidos() {
        initComponents();
        initListeners();
        init();
    }

    private void initListeners() {
        buttonAddProducto.addActionListener((e) -> PanelsInstances.getInstance().showPanelAddPedido());
    }

    private void initComponents() {
        buttonAddProducto = new ButtonDefault("Agregar Pedido");
        table = new Table<>(new String[] {"ID", "Nombre del Proveedor", "Fecha del Pedido", "Estado", "Observaciones"});
        table.setNameAccion("Ver Detalles");
        table.setRowClickListener(o -> {
            ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                    CustomModal.builder()
                            .component(new PanelDetailsPedido(o))
                            .icon(modal + "ic_inventario.svg")
                            .title("Detalles del Pedido")
                            .buttonClose(true)
                            .ID(ID)
                            .build(),
                    Config.getModelShowModalFromNote(),
                    ID
            );
            return null;
        });
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

    public void showData(LinkedList<Pedido> pedidos) {
        PoolThreads.getInstance().execute(() -> {
            Function<Pedido, Object[]> dataMapper = usuarioMapper -> new Object[]{
                    usuarioMapper.getId(),
                    usuarioMapper.getProveedor().getNombre(),
                    DateFull.getDateFull(usuarioMapper.getFecha()),
                    usuarioMapper.getEstado(),
                    usuarioMapper.getObservaciones()
            };
            table.addAll(pedidos, dataMapper);
        });
    }
    
}