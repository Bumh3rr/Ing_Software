package bumh3r.view.form;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.Categorias_Reparacion;
import bumh3r.model.Cliente;
import bumh3r.model.Detalles;
import bumh3r.model.Dispositivo;
import bumh3r.model.Empleado;
import bumh3r.model.Marca;
import bumh3r.model.Nota;
import bumh3r.model.Reparacion;
import bumh3r.model.Reparacion_Dispositivo;
import bumh3r.model.TipoDispositivo;
import bumh3r.model.TipoReparacion;
import bumh3r.model.TypeEmpleado;
import bumh3r.model.Venta;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
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

import static bumh3r.archive.PathResources.Icon.modal;

public class FormHistorialVentas extends Form {
    public static final String ID = FormHistorialVentas.class.getName();
    private Table<Venta> table;
    private InputText input_search;
    private ButtonDefault search;
    private JFormattedTextField inputDate;
    private DatePicker datePicker;

    @Override
    public void formInit() {
        Empleado empleados = new Empleado(
                1L,
                "Juan",
                "Perez",
                "RFC123456",
                "747-232-3232",
                "M",
                "juan.perez@example.com",
                "Jalisco",
                "Acatic",
                "Colony",
                "Street",
                "12345",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "Activo",
                new TypeEmpleado(1,"Tecnico")
        );

        List<TipoReparacion> tipoReparacions = new ArrayList<>();
        tipoReparacions.add(new TipoReparacion(1,"Reparación"));
        tipoReparacions.add(new TipoReparacion(1,"Diagnostico"));

        List<Categorias_Reparacion> categoriasReparacions = new ArrayList<>();
        categoriasReparacions.add(new Categorias_Reparacion('R',"Hardware",null));
        categoriasReparacions.add(new Categorias_Reparacion('A',"Software",null));

        List<Reparacion> reparacions = new ArrayList<>();
        reparacions.add(new Reparacion(1,"Reparación 1",null));
        reparacions.add(new Reparacion(2,"Reparación 1",null));
        reparacions.add(new Reparacion(3,"Reparación 1",null));
        reparacions.add(new Reparacion(4,"Reparación 1",null));


        LinkedList<Reparacion_Dispositivo> reparaciones = new LinkedList<>();
        reparaciones.add( new Reparacion_Dispositivo(
                        1,
                        tipoReparacions.get(0),
                        categoriasReparacions.get(0),
                        reparacions.get(0),
                        "Descripcion",
                        100.0,
                        100.0,
                        50.0,
                        false,
                        "",
                        Reparacion_Dispositivo.Status.LISTO,
                        empleados,
                        null
                )
        );

        TipoDispositivo tipoDispositivo = new TipoDispositivo(1,"Teléfono",null);
        Marca marca = new Marca(1,"Apple",null);
        Detalles detalles = new Detalles(1,false, LocalDateTime.now(),"",0);

        LinkedList<Dispositivo> dispositivos = new LinkedList<>();
        dispositivos.add( new Dispositivo(
                        1,
                        tipoDispositivo,
                        marca,
                        "iPhone 12 pro",
                        "1234567890",
                        detalles,
                        reparaciones

                )
        );


        Cliente cliente = new Cliente(1L, "Cliente 1", "1234567890", "1234567890", "Dirección 1", LocalDate.now(),null);

        LinkedList<Nota> notas = new LinkedList<>();
        notas.add(new Nota(
                1,
                "folio-1",
                LocalDateTime.now(),
                Nota.StatusNota.TERMINADO,
                empleados,
                cliente,
                dispositivos
        ));
        notas.add(new Nota(
                2,
                "folio-2",
                LocalDateTime.now(),
                Nota.StatusNota.EN_PROCESO,
                empleados,
                cliente,
                dispositivos
        ));
        notas.add(new Nota(
                3,
                "folio-3",
                LocalDateTime.now(),
                Nota.StatusNota.CANCELADO,
                empleados,
                cliente,
                dispositivos
        ));

        Venta venta = Venta.builder()
                .id(1l)
                .estado(Venta.Estado.PENDIENTE)
                .fecha(LocalDate.now())
                .nota(notas.get(0))
                .precioTotal(2500.00f)
                .build();



        LinkedList<Venta> ventas = new LinkedList<>();
        ventas.add(venta);
        showData(ventas);

    }

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
