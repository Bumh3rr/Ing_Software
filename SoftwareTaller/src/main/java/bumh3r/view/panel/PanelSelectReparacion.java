package bumh3r.view.panel;

import bumh3r.components.table.Table;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import bumh3r.system.panel.Panel;
import bumh3r.utils.thread.PoolThreads;
import java.util.List;
import java.util.function.Function;
import net.miginfocom.swing.MigLayout;

public class PanelSelectReparacion extends Panel {
    private LabelForDescription description;
    private Table<Reparacion> table;
    private Function<Reparacion, Object[]> reparacion = repair -> new Object[]{
            repair.getCategoria(),
            repair.getReparacion(),
            String.format("$%.2f", repair.getPrecio()),
            String.format("$%.2f", repair.getAbono()),
            repair.getEstado().getEstado()
    };

    public void installEventSelect(Function<Reparacion, Void> event) {
        table.setRowClickListener(event);
    }

    public PanelSelectReparacion() {
        initComponents();
        init();
    }

    private void initComponents() {
        table = new Table<>(new String[]{"Categoría ", "Reparación", "Precio", "Abono", "Estado"}, reparacion);
        table.setNameAccion("Seleccionar");
        description = new LabelForDescription("En este apartado podrás seleccionar las reparaciones que deseas agregar a la venta.");
    }

    private void init() {
        setLayout(new MigLayout("wrap,ins 0 n n n, w 650:750", "[grow]", "[grow 0][]"));
        add(description, "grow,gapx 10 10,gapy 1 15");
        add(table, "h 300!,growx,gapy 5 0");
    }

    public void addData(List<Reparacion> reparacions) {
        table.addAll(reparacions);
    }
}
