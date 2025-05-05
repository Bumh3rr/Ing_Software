package bumh3r.view.form;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.*;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.view.panel.PanelClienteNotes;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import static bumh3r.archive.PathResources.Icon.modal;

public class FormCliente extends Form {
    public static final String ID = FormCliente.class.getName();
    private ButtonDefault buttonAddProducto;
    private ButtonAccentBase buttonSearch;
    private InputText search;
    private Table<Cliente> table;

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
        Detalles detalles = new Detalles(1,false,LocalDateTime.now(),"",0);

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

        showData(clientes);
    }
    public FormCliente() {
        initComponents();
        initListeners();
        init();
    }


    private void initListeners() {
        buttonAddProducto.addActionListener((e) -> PanelsInstances.getInstance().showPanelAddCliente());
    }

    private void initComponents() {
        search = new InputText(6).setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonAccentBase("Buscar");
        buttonAddProducto = new ButtonDefault("Agregar Cliente");
        table = new Table<>(new String[]{"ID", "Nombre", "Teléfono 1", "Teléfono 2", "Dirección", "Fecha de Registro"});
        table.setNameAccion("Ver Notas");
        table.setRowClickListener((cliente)->{
            ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                    CustomModal.builder()
                            .component(new PanelClienteNotes(cliente))
                            .icon(modal + "ic_note.svg")
                            .title("Notas del Cliente")
                            .buttonClose(true)
                            .ID(ID)
                            .build(),
                    Config.getModelShowModalFromNote(),
                    ID
            );
            return null;
        });

    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Clientes", "En este apartado podrás visualizar los clientes registrados en el sistema. Puedes Visualizar las notas por Cliente", 1));
        add(body());
        updateUI();
    }
    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2", "grow", "[][fill]"));
        panel.add(search, "w 200!,grow 0,al lead,split 2");
        panel.add(buttonSearch, "grow 0");
        panel.add(buttonAddProducto, "grow 0,al trail");
        panel.add(table, "span,grow,push,gapy 5 0,gapx 0 2");
        return panel;
    }

    public void showData(LinkedList<Cliente> clientes) {
        PoolThreads.getInstance().execute(() -> {
            Function<Cliente, Object[]> dataMapper = usuarioMapper -> new Object[]{
                    usuarioMapper.getId(),
                    usuarioMapper.getName(),
                    usuarioMapper.getPhone1(),
                    usuarioMapper.getPhone2(),
                    usuarioMapper.getAddress(),
                    DateFull.getDateFull(usuarioMapper.getDete_register())
            };
            table.setData(clientes, dataMapper);
        });
    }

}