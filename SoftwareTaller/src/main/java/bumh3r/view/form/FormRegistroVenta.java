package bumh3r.view.form;

import bumh3r.components.ContainerCards;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardNote;
import bumh3r.components.card.CardNoteSale;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.*;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.panel.PanelRegisterSale;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.modal.ModalDialog;
import static bumh3r.archive.PathResources.Icon.modal;

public class FormRegistroVenta extends Form {
    public static final String ID = FormRegistroVenta.class.getName();
    private JPanel panelNotes;
    private ContainerCards containerCards;
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

        refreshPanelNote(notas);
    }

    public FormRegistroVenta() {
        initComponents();
        init();
    }

    private void initComponents() {
        panelNotes = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        panelNotes.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panelNotes.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;");
        search = new ButtonDefault("Buscar Nota");
        containerCards = new ContainerCards(panelNotes);
        input_search = new InputText("Ingrese el folio de la nota", 100);
        search = new ButtonDefault("Buscar");
        datePicker = new DatePicker();
        inputDate = new JFormattedTextField();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(true);
        datePicker.setUsePanelOption(true);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]"));
        add(createHeader("Registro de Venta por Nota de Reparación", "En este apartado podrás buscar una nota de reparación existente y seleccionarla para continuar con el registro de venta.\n" +
                "Una vez seleccionada, podrás elegir las reparaciones realizadas, las refacciones utilizadas y registrar el pago del cliente.", 1));
        add(createBody());
    }

    private JComponent createBody() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2", "[grow][grow,trail]", "[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");

        panel.add(input_search, "w 200!,al lead,split 2");
        panel.add(search, "growx 0");
        panel.add(inputDate, "w 160!");
        panel.add(containerCards, "span,grow,push");
        return panel;
    }


    public void refreshPanelNote(LinkedList<Nota> list) {
        SwingUtilities.invokeLater(() -> {
            panelNotes.removeAll();
            list.forEach((note) -> {
                panelNotes.add(new CardNoteSale(note, createEventCard));
            });
            panelNotes.updateUI();
            EventQueue.invokeLater(() -> containerCards.getVerticalScrollBar().setValue(0));
            updateUI();
        });
    }

    private Consumer<Nota> createEventCard = (e) -> {
        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                CustomModal.builder()
                        .component(new PanelRegisterSale(e))
                        .title("Registro de Venta")
                        .buttonClose(true)
                        .icon(modal + "ic_sale.svg")
                        .ID(PanelRegisterSale.ID)
                        .build(),
                Config.getModelShowModalFromNote(),
                PanelRegisterSale.ID
        );
    };


}
