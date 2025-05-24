package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.New.ClienteN;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelSearchCliente extends Panel {
    private LabelForDescription description;
    @Getter
    private InputText input;
    private ButtonDefault buttonSearch;
    @Getter
    private Table<ClienteN> table;
    private final Function<ClienteN, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.getId(),
            usuarioMapper.getNombre(),
            usuarioMapper.getTelefono_movil(),
            usuarioMapper.getTelefono_fijo(),
            usuarioMapper.getDireccion(),
            DateFull.getDateOnly(usuarioMapper.getFecha_registro())
    };

    public void installEventSearch(Runnable event) {
        buttonSearch.addActionListener(e -> event.run());
        input.addActionListener(e -> event.run());
    }

    public void installEventSelect(Function<ClienteN, Void> event) {
        table.setRowClickListener(event);
    }

    public PanelSearchCliente() {
        initComponents();
        init();
    }

    private void initComponents() {
        input = new InputText("Introduce el nombre del cliente").setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonDefault("Buscar");
        table = new Table<>(new String[]{"ID", "Nombre", "Teléfono 1", "Teléfono 2", "Dirección", "Fecha de registro"}, dataMapper);
        table.setNameAccion("Seleccionar");
        description = new LabelForDescription("En este panel puedes buscar al cliente por su número de teléfono y seleccionarlo.");
    }

    private void init() {
        setLayout(new MigLayout("wrap,ins 0 n n n, w 600:680", "[grow]", "[grow 0][]"));
        add(description, "grow,gapx 10 10,gapy 1 15");
        add(input, "w 200!,grow 0,al lead,split 2");
        add(buttonSearch, "grow 0");
        add(table, "h 300!,growx,gapy 5 0");
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            this.table.cleanData();
            this.input.setText("");
        });
    }

    public Consumer<ClienteN> addOneCard = (cliente) -> table.addOne(cliente);
    public Consumer<List<ClienteN>> addAllCard = (list) -> table.addAll(list);

}
