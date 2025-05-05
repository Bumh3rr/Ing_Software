package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.Reparacion_Dispositivo;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import java.util.LinkedList;
import java.util.function.Function;
import net.miginfocom.swing.MigLayout;

public class PanelSelectReparacion extends Panel {
    private LabelForDescription description;
    private Table<Reparacion_Dispositivo> table;
    private LinkedList<Reparacion_Dispositivo> reparacionDispositivos;

    @Override
    public void panelInit() {
    }

    public PanelSelectReparacion(LinkedList<Reparacion_Dispositivo> reparacionDispositivos) {
        this.reparacionDispositivos = reparacionDispositivos;
        initComponents();
        init();
        showData(reparacionDispositivos);
    }

    private void initComponents() {
        table = new Table<>(new String[]{"Tipo", "Categoría ","Reparación", "Precio", "Abono","Estado"});
        table.setNameAccion("Seleccionar");
        description = new LabelForDescription("En este apartado podrás seleccionar las reparaciones que deseas agregar a la venta.");
    }

    private void init() {
        setLayout(new MigLayout("wrap,ins 0 n n n, w 650:750","[grow]","[grow 0][]"));
        add(description, "grow,gapx 10 10,gapy 1 15");
        add(table, "h 300!,growx,gapy 5 0");
    }

    public void showData(LinkedList<Reparacion_Dispositivo> reparacionDispositivos) {
        PoolThreads.getInstance().execute(() -> {
            Function<Reparacion_Dispositivo, Object[]> reparacion = repair -> new Object[]{
                    repair.getTipoReparacion().getNombre(),
                    repair.getCategoria().getName(),
                    repair.getReparacion().getName(),
                    String.format("$%.2f", repair.getPrecio()),
                    String.format("$%.2f", repair.getAbono()),
                    repair.getStatus().getValue()
            };
            table.setData(reparacionDispositivos, reparacion);
        });
    }


}
