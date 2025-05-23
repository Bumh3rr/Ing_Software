package bumh3r.view.form;

import bumh3r.components.TableSimple;
import bumh3r.components.button.ButtonDefault;
import bumh3r.controller.ControladorProveedor;
import bumh3r.model.New.ProveedorN;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
import java.util.List;
import java.util.function.Function;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class FormProveedor extends Form {
    private ButtonDefault buttonAddProveedor;
    @Getter
    private TableSimple<ProveedorN> table;
    private final Function<ProveedorN, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.getId(),
            usuarioMapper.getNombre(),
            usuarioMapper.getTelefono(),
            usuarioMapper.getCorreo(),
            usuarioMapper.getDireccion(),
            DateFull.getDateOnly(usuarioMapper.getFecha_registro())
    };

    @Override
    public void installController() {
        new ControladorProveedor(this);
    }

    @Override
    public void formInit() {
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        PoolThreads.getInstance().execute(getEventFormRefresh());
    }

    public void installEventAddCliente(Runnable event) {
        buttonAddProveedor.addActionListener(e -> event.run());
    }

    public FormProveedor() {
        initComponents();
        init();
    }

    private void initComponents() {
        buttonAddProveedor = new ButtonDefault("Agregar Proveedor");
        table = new TableSimple<>(new String[]{"ID", "Nombre", "Teléfono", "Correo", "Dirección", "Fecha de Registro"});
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Proveedores", "En el apartado de proveedores puedes gestionar tus proveedores, las cuales son agregar", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]", "[][fill]"));
        panel.add(buttonAddProveedor, "grow 0,al trail");
        panel.add(table, "grow,push,gapx 0 2");
        return panel;
    }

    public void addAllTable(List<ProveedorN> list) {
        SwingUtilities.invokeLater(() -> table.addAll(list, dataMapper));
    }

    public void addOneTable(ProveedorN proveedor) {
        SwingUtilities.invokeLater(() -> table.addOne(proveedor, dataMapper));
    }
}
