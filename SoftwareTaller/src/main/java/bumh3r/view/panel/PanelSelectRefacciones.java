package bumh3r.view.panel;

import bumh3r.components.table.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.Cliente;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import bumh3r.system.panel.Panel;
import bumh3r.utils.thread.PoolThreads;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.function.Function;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import static bumh3r.utils.PathResources.Icon.modal;

public class PanelSelectRefacciones extends Panel {
    @Getter
    private InputText input;
    private ButtonDefault buttonSearch;
    private LabelForDescription description;
    @Getter
    private Table<Refaccion> table;
    private Function<Refaccion, Object[]> function = repair -> new Object[]{
            repair.getNombre(),
            repair.getCategoria(),
            String.format("$%.2f", repair.getPrecio_venta()),
            repair.getStock()
    };

    public void installEventSelect(Function<Refaccion, Void> event) {
        table.setRowClickListener(event);
    }

    public void installEventSearch(Runnable event) {
        buttonSearch.addActionListener(e -> event.run());
        input.addActionListener(e -> event.run());
    }

    public PanelSelectRefacciones() {
        initComponents();
        init();
        panelInit();
    }

    private void initComponents() {
        input = new InputText("Introduce el nombre refacción", 100).setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonDefault("Buscar");
        table = new Table<>(new String[]{"Nombre", "Categoría", "Precio", "Stock"}, function);
        table.setNameAccion("Agregar");
        description = new LabelForDescription("En este panel puedes buscar la refacción por su nombre y seleccionarla.");
    }

    private void init() {
        setLayout(new MigLayout("wrap,ins 0 n n n, w 600:680", "[grow]", "[grow 0][]"));
        add(description, "grow,gapx 10 10,gapy 1 15");
        add(input, "w 200!,grow 0,al lead,split 2");
        add(buttonSearch, "grow 0");
        add(table, "h 300!,growx,gapy 5 0");
    }

}
