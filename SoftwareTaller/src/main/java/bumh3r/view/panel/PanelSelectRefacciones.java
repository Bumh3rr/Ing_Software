package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.Proveedor;
import bumh3r.model.Refaccion;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.function.Function;
import net.miginfocom.swing.MigLayout;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelSelectRefacciones extends Panel {
    private InputText input;
    private ButtonDefault buttonSearch;
    private LabelForDescription description;
    private Table<Refaccion> table;

    @Override
    public void panelInit() {
        Proveedor proveedor = new Proveedor(1L, "Proveedor 1", "1234567890", "example@gmail.com", "Dirección 1", "2021-09-01");
        Proveedor proveedor2 = new Proveedor(2L, "Proveedor 2", "1234567890", "example@gmail.com", "Dirección 2", "2021-09-01");
        Proveedor proveedor3 = new Proveedor(3L, "Proveedor 3", "1234567890", "example@gmail.com", "Dirección 3", "2021-09-01");

        LinkedList<Refaccion> refacciones = new LinkedList<>();
        refacciones.add(new Refaccion(1L, "Refacción 1", "description", "Display", 10, 100.0, 150.0, LocalDate.now(), proveedor));
        refacciones.add(new Refaccion(2L, "Refacción 2", "description", "Display", 10, 100.0, 150.0, LocalDate.now(), proveedor2));
        refacciones.add(new Refaccion(3L, "Refacción 3", "description", "Display", 10, 100.0, 150.0, LocalDate.now(), proveedor3));
        showData(refacciones);
    }

    public PanelSelectRefacciones() {
        initComponents();
        init();
        panelInit();
    }

    private void initComponents() {
        input = new InputText("Introduce el nombre refacción").setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonDefault("Buscar");
        table = new Table<>(new String[]{"Nombre", "Categoría", "Precio", "Stock"});
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

    public void showData(LinkedList<Refaccion> reparacionDispositivos) {
        PoolThreads.getInstance().execute(() -> {
            Function<Refaccion, Object[]> reparacion = repair -> new Object[]{
                    repair.getNombre(),
                    repair.getCategoria(),
                    String.format("$%.2f", repair.getPrecioVenta()),
                    repair.getStock()
            };
            table.addAll(reparacionDispositivos, reparacion);
        });
    }

}
