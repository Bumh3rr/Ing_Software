package bumh3r.view.panel;

import bumh3r.components.button.ButtonAction;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.button.ButtonHelpIcon;
import bumh3r.components.card.CardRepair;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.*;
import bumh3r.notifications.Notify;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.Promiseld;
import bumh3r.view.modal.PanelModalInfoReparacion;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import static bumh3r.model.other.Verify.isNotNull;

public class PanelRequestReparacion extends Panel {

    private final String KEY = getClass().getName();
    private JPanel panelReparaciones;
    private ButtonDefault buttonAddRepair;

    private FlatComboBox<TipoReparacion> combox_TipoReparacion;
    private FlatComboBox<Categorias_Reparacion> combox_CategoriaReparacion;
    private FlatComboBox<Reparacion> combox_reparacion;
    private FlatComboBox<Empleado> combox_Tecnico;
    private InputText inputDescripcion;
    private JButton buttontRefreshTecnicos, buttontRefreshCategoria, buttontRefreshTipoReparacion,button_helpLimit;

    private InputFormattedDecimal inputPrecio;
    private InputFormattedDecimal inputAbono;


    @Override
    public void panelInit() {
        SwingUtilities.invokeLater(() -> {
            TipoReparacion.ListTiposReparaciones.addItemsTipoReparaciones(combox_TipoReparacion);
            Categorias_Reparacion.ListCategorias.addItemsCategorias(combox_CategoriaReparacion);
        });


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
        reparacions.add(new Reparacion(1,"Reparación 1","bateria"));
        reparacions.add(new Reparacion(2,"Reparación 2","cargar"));
        reparacions.add(new Reparacion(3,"Reparación 4","display"));
        reparacions.add(new Reparacion(4,"Reparación 5",null));


        LinkedList<Reparacion_Dispositivo> reparaciones = new LinkedList<>();
        reparaciones.add( new Reparacion_Dispositivo(
                        1234234,
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

        reparaciones.add( new Reparacion_Dispositivo(
                        423423,
                        tipoReparacions.get(0),
                        categoriasReparacions.get(0),
                        reparacions.get(1),
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

        reparaciones.add( new Reparacion_Dispositivo(
                        53453,
                        tipoReparacions.get(0),
                        categoriasReparacions.get(0),
                        reparacions.get(2),
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
        addRepairsPanel(reparaciones);
    }

    @Override
    public void panelRefresh() {
        clean();
    }

    public PanelRequestReparacion() {
        initComponents();
        initListeners();
        init();
    }

    private void initComponents() {

        buttonAddRepair = new ButtonDefault("Agregar Reparación");

        buttontRefreshTipoReparacion = new ButtonAction.Refresh();
        buttontRefreshTecnicos = new ButtonAction.Refresh();
        buttontRefreshCategoria = new ButtonAction.Refresh();

        combox_CategoriaReparacion = new FlatComboBox<>();
        combox_CategoriaReparacion.setMaximumRowCount(6);

        combox_TipoReparacion = new FlatComboBox<>();

        combox_reparacion = new FlatComboBox<>();
        combox_reparacion.setMaximumRowCount(6);

        combox_Tecnico = new FlatComboBox<>();
        combox_Tecnico.setMaximumRowCount(6);

        inputDescripcion = new InputText();
        inputAbono = new InputFormattedDecimal(50000.00f);
        inputPrecio = new InputFormattedDecimal(50000.00f);
        button_helpLimit = new ButtonHelpIcon();
    }

    private void initListeners() {
        combox_CategoriaReparacion.addActionListener((e) -> fillComboxReparaciones());
        buttonAddRepair.addActionListener((e) -> addReparacion());

        button_helpLimit.addActionListener((x)->{
            Notify.closeAll();
        });
        buttontRefreshTecnicos.addActionListener((x) -> {
//            Empleado.ListTecnicos.getInstance().clean();
//            Tecnico.ListTecnicos.addItemsTecnicos(combox_Tecnico);
        });
        buttontRefreshCategoria.addActionListener((x) -> {
            Categorias_Reparacion.ListCategorias.getInstance().clean();
            Categorias_Reparacion.ListCategorias.addItemsCategorias(combox_CategoriaReparacion);
        });
        buttontRefreshTipoReparacion.addActionListener((x) -> {
            TipoReparacion.ListTiposReparaciones.getInstance().clean();
            TipoReparacion.ListTiposReparaciones.addItemsTipoReparaciones(combox_TipoReparacion);
        });
    }

    private void fillComboxReparaciones() {
        SwingUtilities.invokeLater(() -> {
            try {
                Categorias_Reparacion selectedItem = (Categorias_Reparacion) combox_CategoriaReparacion.getSelectedItem();
                combox_reparacion.removeAllItems();
                combox_reparacion.setModel(new DefaultComboBoxModel<>(
                        Reparacion.ListReparaciones.getInstance().getList(selectedItem).toArray(Reparacion[]::new)));
                combox_reparacion.setSelectedIndex(0);
                combox_reparacion.updateUI();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR,
                        "Conexión inestable, Nose Obtuvo la lista de reparaciones\n" +
                                "Causa: " + e.getLocalizedMessage());
            }
        });
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0,width 650:900", "[fill]", "[]20[]"));
        add(createPanelReparaciones(), "h 250!");
        add(createInput());
    }

    private JComponent createPanelReparaciones() {
        panelReparaciones = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.CENTER, new Dimension(350, -1), 10, 10));
        panelReparaciones.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;");
        JScrollPane scrollPane = new JScrollPane(panelReparaciones);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,0,0,0;"
                + "width:5;");
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,0,0,0;"
                + "width:5;");
        return scrollPane;
    }

    private JComponent createInput() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 0 20 20 20", "[grow 0,trail]15[fill,150:170]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");

        panel.add(new JSeparator(), "span,grow");
        panel.add(new LabelPublicaSans("Nueva Reparación").type(FontPublicaSans.FontType.BOLD_BLACK).size(16.5f), "span,grow 0,gapy 5 5,al center");

        panel.add(getLabel("Tipo:"));
        panel.add(combox_TipoReparacion, "grow,split 2");
        panel.add(buttontRefreshTipoReparacion, "grow 0,growy 0");

        panel.add(getLabel("Categoria:"));
        panel.add(combox_CategoriaReparacion, "split 3");
        panel.add(buttontRefreshCategoria, "grow 0,growy 0");
        panel.add(combox_reparacion);

        panel.add(getLabel("Técnico Encargado:"));
        panel.add(combox_Tecnico, "grow,split 2");
        panel.add(buttontRefreshTecnicos, "grow 0,growy 0");

        panel.add(getLabel("Descripción (opcional):"));
        panel.add(inputDescripcion);

        panel.add(getLabel("Precio:"));
        panel.add(inputPrecio, "split 4");

        panel.add(getLabel("Anticipo:"), "grow 0");
        panel.add(inputAbono);
        panel.add(button_helpLimit,"grow 0,growy 0");

        panel.add(buttonAddRepair, "span,w 250!,gapy 5,al center");

        panel.updateUI();
        return panel;
    }

    private JComponent getLabel(String text) {
        JLabel label = new JLabel(text,JLabel.TRAILING);
        label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD,13.2f));
        return label;
    }

    private void addReparacion() {
        Toast.closeAll();

        if (Promiseld.checkPromiseId(KEY)) {
            return;
        }

        Promiseld.commit(KEY);
        EventQueue.invokeLater(() -> {
            try {
                Reparacion_Dispositivo reparacionDispositivo = getReparacion();
                if (isNotNull(reparacionDispositivo)) {
                    PanelRequestDevice.listRepair.add(reparacionDispositivo);
                    panelReparaciones.add(new CardRepair(reparacionDispositivo, createEventCardDelete()));
                    panelReparaciones.updateUI();
                    clean();
                }
            } catch (Exception ex) {
                Notify.getInstance().showToast(Toast.Type.ERROR, ex.getMessage());
            } finally {
                Promiseld.terminate(KEY);
            }
        });
    }

    public void clean() {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            inputPrecio.setValue(null);
            inputAbono.setValue(null);
            inputDescripcion.setText("");

            if (combox_CategoriaReparacion.getSelectedItem() != null) {
                combox_CategoriaReparacion.setSelectedIndex(0);
            }
            if (combox_Tecnico.getSelectedItem() != null) {
                combox_CategoriaReparacion.setSelectedIndex(0);
            }
        });
    }

    private Reparacion_Dispositivo getReparacion() {
        Toast.closeAll();

        if (toastIsEmpy()) {
            return null;
        }

        TipoReparacion tipoReparacion = (TipoReparacion) combox_TipoReparacion.getSelectedItem();
        Categorias_Reparacion categoria = (Categorias_Reparacion) combox_CategoriaReparacion.getSelectedItem();
        Reparacion reparacion = (Reparacion) combox_reparacion.getSelectedItem();
        Empleado empleado = (Empleado) combox_Tecnico.getSelectedItem();
        String descripcion = inputDescripcion.getText().strip();
        Double precio = inputPrecio.getValue() == null ? 0.0 : Double.parseDouble(inputPrecio.getValue().toString());
        Double abono = inputAbono.getValue() == null ? 0.0 : Double.parseDouble(inputAbono.getValue().toString());
        Reparacion_Dispositivo.Status status = Reparacion_Dispositivo.Status.PENDIENTE;

        return new Reparacion_Dispositivo(tipoReparacion, categoria, reparacion, descripcion, precio, abono, empleado, status);
    }

    private Boolean toastIsEmpy() {
        if (combox_Tecnico.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione el técnico");
            return true;
        }
        if (combox_TipoReparacion.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione el tipo");
            return true;
        }
        if (combox_CategoriaReparacion.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione Una Categoria");
            return true;
        }
        if (combox_reparacion.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione Una Reparación");
            return true;
        }

        Double precio = inputPrecio.getValue() == null ? 0.0 : Double.parseDouble(inputPrecio.getValue().toString());
        Double abono = inputAbono.getValue() == null ? 0.0 : Double.parseDouble(inputAbono.getValue().toString());

        if (abono > precio){
            Notify.getInstance().showToast(Toast.Type.WARNING, "El anticipo no puede ser mayor al precio");
            return true;
        }
        return false;
    }

    private void addRepairsPanel(LinkedList<Reparacion_Dispositivo> list) {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            panelReparaciones.removeAll();
            EventQueue.invokeLater(() -> {
                for (Reparacion_Dispositivo reparacionDispositivo : list) {
                    panelReparaciones.add(new CardRepair(reparacionDispositivo, createEventCardDelete()));
                }
                panelReparaciones.updateUI();
            });
        });
    }

    private Consumer<Reparacion_Dispositivo> createEventCardDelete() {
        return e -> {
            int i = PanelRequestDevice.listRepair.indexOf(e);
            if (i != -1) {
                PanelRequestDevice.listRepair.remove(i);
                EventQueue.invokeLater(() -> {
                    panelReparaciones.remove(i);
                    panelReparaciones.revalidate();
                    panelReparaciones.updateUI();
                });
            }
        };
    }

}
