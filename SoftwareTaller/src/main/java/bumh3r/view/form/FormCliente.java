package bumh3r.view.form;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.controller.ControladorCliente;
import bumh3r.model.New.ClienteN;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import static bumh3r.archive.PathResources.Icon.modal;

public class FormCliente extends Form {
    public static final String ID = FormCliente.class.getName();
    private ButtonDefault buttonAddProducto;
    private ButtonAccentBase buttonSearch;
    @Getter
    private InputText search;
    private Table<ClienteN> table;
    private ControladorCliente controladorCliente;
    private final Function<ClienteN, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.getId(),
            usuarioMapper.getNombre(),
            usuarioMapper.getTelefono_movil(),
            usuarioMapper.getTelefono_fijo(),
            usuarioMapper.getDireccion(),
            DateFull.getDateOnly(usuarioMapper.getFecha_registro())
    };

    @Override
    public void installController() {
        this.controladorCliente = new ControladorCliente(this);
    }

    @Override
    public void formInit() {
        table.setRowClickListener(this.controladorCliente.mostrarNotasCliente);
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    public void installEventAddCliente(Runnable runnable) {
        buttonAddProducto.addActionListener(e -> runnable.run());
    }

    public void installEventSearchCliente(Runnable runnable) {
        buttonSearch.addActionListener(e -> runnable.run());
        search.addActionListener(e -> runnable.run());
    }

    public FormCliente() {
        initComponents();
        init();
    }

    private void initComponents() {
        search = new InputText("Nombre del Cliente", 100).setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonAccentBase("Buscar");
        buttonAddProducto = new ButtonDefault("Agregar Cliente");
        table = new Table<>(new String[]{"ID", "Nombre", "Teléfono Móvil", "Teléfono Fijo", "Dirección", "Fecha de Registro"});
        table.setNameAccion("Ver Notas");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Clientes", "En este apartado podrás visualizar los clientes registrados en el sistema. Puedes Visualizar las notas por Cliente", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2", "grow", "[][fill]"));
        panel.add(search, "w 250!,grow 0,al lead,split 2");
        panel.add(buttonSearch, "grow 0");
        panel.add(buttonAddProducto, "grow 0,al trail");
        panel.add(table, "span,grow,push,gapy 5 0,gapx 0 2");
        return panel;
    }

    public void addAllTable(List<ClienteN> clientes) {
        PoolThreads.getInstance().execute(() -> table.addAll(clientes, dataMapper));
    }

    public void addOneTable(ClienteN cliente) {
        PoolThreads.getInstance().execute(() -> table.addOne(cliente, dataMapper));
    }
}