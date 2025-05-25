package bumh3r.view.form;

import bumh3r.components.table.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.modal.Config;
import bumh3r.components.modal.CustomModal;
import bumh3r.model.Venta;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.utils.thread.PoolThreads;
import bumh3r.view.panel.PanelDetallesVenta;
import com.formdev.flatlaf.FlatClientProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.modal.ModalDialog;

import static bumh3r.utils.PathResources.Icon.modal;

public class FormHistorialVentas extends Form {
    public static final String ID = FormHistorialVentas.class.getName();
    private Table<Venta> table;
    private InputText input_search;
    private ButtonDefault search;
    private JFormattedTextField inputDate;
    private DatePicker datePicker;

    public FormHistorialVentas() {
        initComponents();
        init();
    }

    private void initComponents() {
        search = new ButtonDefault("Buscar Nota");
        input_search = new InputText("Ingrese el ID de la venta", 100);
        search = new ButtonDefault("Buscar");
        datePicker = new DatePicker();
        inputDate = new JFormattedTextField();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(true);
        datePicker.setUsePanelOption(true);
        table = new Table<>(new String[]{"ID", "ID Nota", "Total Venta", "Estado", "Fecha"},null);
        table.installParentScroll(this);
        table.setNameAccion("Ver Detalles");
        table.setRowClickListener(
                (venta) -> {
            ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                    CustomModal.builder()
                            .component(new PanelDetallesVenta())
                            .title("Detalles de la Venta")
                            .buttonClose(true)
                            .icon(modal + "ic_note.svg")
                            .ID(PanelDetallesVenta.ID)
                            .build(),
                    Config.getModelShowModalFromNote(),
                    PanelDetallesVenta.ID
            );
            return null;
        });
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]"));
        add(createHeader("Historial De Ventas", "El apartado de Historial de Ventas permite consultar todas las ventas registradas, buscar por ID, filtrar por fecha y ver los detalles completos de cada venta.", 1));
        add(createBody());
    }

    private JComponent createBody() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2", "[grow][grow,trail]", "[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");

        panel.add(input_search, "w 200!,al lead,split 2");
        panel.add(search, "growx 0");
        panel.add(inputDate, "w 160!");
        panel.add(table, "span,grow,push");
        return panel;
    }

    public void showData(LinkedList<Venta> reparacionDispositivos) {
        PoolThreads.getInstance().execute(() -> {
            Function<Venta, Object[]> reparacion = repair -> new Object[]{
                    repair.getId(),
                    repair.getNota().getId(),
                    String.format("$%.2f", repair.getPrecioTotal()),
                    repair.getEstado().getNombre(),
                    DateFull.getDateFull(repair.getFecha())
            };
            table.addAll(reparacionDispositivos);
        });
    }

}
