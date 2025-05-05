package bumh3r.view.form;

import bumh3r.components.ContainerCards;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardNote;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.modal.Config;
import bumh3r.model.*;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.modal.PanelModalInfoDevice;
import bumh3r.view.modal.PanelModalInfoNote;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.modal.ModalDialog;

import static bumh3r.archive.PathResources.Icon.modal;

public class FormNotes extends Form {
    public static final String KEY = FormNotes.class.getName();
    private InputText search;
    private JFormattedTextField inputDate;
    private DatePicker datePicker;
    private ButtonDefault button_createNote;
    private ButtonAccentBase buttonSearch;
    private JPanel panelNotes;
    private ContainerCards containerCards;
    
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
                        Reparacion_Dispositivo.Status.PENDIENTE,
                        empleados,
                        null
                )
        );

        TipoDispositivo tipoDispositivo = new TipoDispositivo(1,"Teléfono",null);
        Marca marca = new Marca(1,"Apple",null);
        Detalles detalles = new Detalles(1,false,LocalDateTime.now(),"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam ",32);

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
                "folio-12132",
                LocalDateTime.now(),
                Nota.StatusNota.EN_PROCESO,
                empleados,
                cliente,
                dispositivos
        ));
        cliente.setNotas(notas);

        LinkedList<Cliente> clientes = new LinkedList<>();
        clientes.add(cliente);

        refreshPanelNote(notas);

    }

    public FormNotes() {
        initComponents();
        initListeners();
        init();
    }


    private void initComponents() {
        search = new InputText("Buscar Folio ...", 6);
        datePicker = new DatePicker();
        inputDate = new JFormattedTextField();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(true);
        datePicker.setUsePanelOption(true);
        button_createNote = new ButtonDefault("Crear Nota");
        buttonSearch = new ButtonAccentBase("Buscar");

        panelNotes = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        panelNotes.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panelNotes.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;");
        containerCards = new ContainerCards(panelNotes);
    }

    private void initListeners() {
        button_createNote.addActionListener((e) -> PanelsInstances.getInstance().showPanelCreateNote());
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]", "[grow 0]10[fill]"));
        add(createHeader("Notas", "En este apartado puedes visualizar las notas por fecha y visualizar el contenido de cada una de ellas", 1));
        add(createBody(), "grow,push,gapx 20 20");
    }

     private JComponent createBody() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2,insets 5", "[]push[]", "[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");

        panel.add(search, "w 180!,al lead,split 2");
        panel.add(buttonSearch, "growx 0");
        panel.add(button_createNote, "w 100!,split 2");
        panel.add(inputDate, "w 160!");

        panel.add(containerCards, "span,grow,push");
        return panel;
    }

    public void refreshPanelNote(LinkedList<Nota> list) {
        SwingUtilities.invokeLater(() -> {
            panelNotes.removeAll();
            list.forEach((note) -> {
                panelNotes.add(new CardNote(note, eventViewPreferences));
            });
            panelNotes.updateUI();
            EventQueue.invokeLater(() -> containerCards.getVerticalScrollBar().setValue(0));
            updateUI();
        });
    }

    private Consumer<Nota> eventViewPreferences = e -> {
        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                new PanelModalInfoNote().setData(e),
                Config.getModelShowModalFromNote(),
                PanelModalInfoDevice.ID
        );
    };

}