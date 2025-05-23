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