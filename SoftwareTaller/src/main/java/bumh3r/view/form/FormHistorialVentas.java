package bumh3r.view.form;

import bumh3r.components.input.InputFormatterNumber;
import bumh3r.components.table.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.modal.Config;
import bumh3r.components.modal.CustomModal;
import bumh3r.controller.ControladorHistorialVentas;
import bumh3r.model.Venta;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.utils.thread.PoolThreads;
import bumh3r.view.panel.PanelDetallesVenta;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
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
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.datetime.PanelDateOptionLabel;
import raven.modal.ModalDialog;

import static bumh3r.utils.PathResources.Icon.modal;

public class FormHistorialVentas extends Form {
    public static final String ID = FormHistorialVentas.class.getName();
    @Getter
    private Table<Venta> table;
    @Getter
    private InputFormatterNumber input_search;
    private ButtonDefault search;
    private JFormattedTextField inputDate;
    @Getter
    private DatePicker datePicker;
    private ControladorHistorialVentas controlador;
    private Function<Venta, Object[]> ventas = venta -> new Object[]{
            venta.getId(),
            venta.getNota().getFolio(),
            venta.getNota().getCliente().toString(),
            String.format("$%.2f", venta.getTotal()),
            String.format("$%.2f", venta.getDescuento()),
            String.format("$%.2f", venta.getAbono()),
            venta.getEstado().getNombre(),
            DateFull.getDateFull(venta.getFecha())
    };

    @Override
    public void installController() {
        this.controlador = new ControladorHistorialVentas(this);
    }

    @Override
    public void formRefresh() {
        PoolThreads.getInstance().execute(getEventFormRefresh());
    }

    @Override
    public void formInit() {
        table.setRowClickListener(controlador.mostrarPantallaDetalleVenta());
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    public void installEventSearchBuy(Runnable event) {
        search.addActionListener(e -> event.run());
        input_search.addActionListener(e -> event.run());
    }

    public void installEventFilterByDate(Runnable event) {
        datePicker.addDateSelectionListener((x) -> event.run());
    }

    public FormHistorialVentas() {
        initComponents();
        init();
    }

    private void initComponents() {
        search = new ButtonDefault("Buscar Nota");
        input_search = new InputFormatterNumber( 100000);
        search = new ButtonDefault("Buscar");
        inputDate = new JFormattedTextField();
        datePicker = new DatePicker();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(false);
        datePicker.setPanelDateOptionLabel(createDefaultPanelDateOptionLabel());
        datePicker.setUsePanelOption(true);
        table = new Table<>(new String[]{"ID", "Nota Folio", "Cliente", "Venta Total", "Descuento", "Abono", "Estado", "Fecha"}, ventas);
        table.installParentScroll(this);
        table.setNameAccion("Ver Detalles");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]"));
        add(createHeader("Historial De Ventas", "El apartado de Historial de Ventas permite consultar todas las ventas registradas,\nbuscar por ID, filtrar por fecha y ver los detalles completos de cada venta.", 1));
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

    private PanelDateOptionLabel createDefaultPanelDateOptionLabel() {
        PanelDateOptionLabel defaultPanelDateOptionLabel = new PanelDateOptionLabel();
        defaultPanelDateOptionLabel.add("Hoy", PanelDateOptionLabel.LabelCallback.TODAY);
        defaultPanelDateOptionLabel.add("Ayer", PanelDateOptionLabel.LabelCallback.YESTERDAY);
        defaultPanelDateOptionLabel.add("Últimos 7 días", PanelDateOptionLabel.LabelCallback.LAST_7_DAYS);
        defaultPanelDateOptionLabel.add("Últimos 30 días", PanelDateOptionLabel.LabelCallback.LAST_30_DAYS);
        defaultPanelDateOptionLabel.add("Este mes", PanelDateOptionLabel.LabelCallback.THIS_MONTH);
        defaultPanelDateOptionLabel.add("Mes pasado", PanelDateOptionLabel.LabelCallback.LAST_MONTH);
        defaultPanelDateOptionLabel.add("El año pasado", PanelDateOptionLabel.LabelCallback.LAST_YEAR);
        defaultPanelDateOptionLabel.add("Personalizar", PanelDateOptionLabel.LabelCallback.CUSTOM);
        return defaultPanelDateOptionLabel;
    }

    public void addAllTable(List<Venta> list) {
        table.addAll(list);
    }

}
