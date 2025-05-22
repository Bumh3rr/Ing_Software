package bumh3r.view.modal;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonAction;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.button.ButtonHelpIcon;
import bumh3r.components.card.CardRepair;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Categorias_Reparacion;
import bumh3r.model.Empleado;
import bumh3r.model.Reparacion;
import bumh3r.model.Reparacion_Dispositivo;
import bumh3r.model.TipoReparacion;
import bumh3r.notifications.Notify;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.Promiseld;
import bumh3r.view.panel.PanelRequestDevice;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.Toast;

import static bumh3r.archive.PathResources.Img.categorydevice;
import static bumh3r.model.other.Verify.isNotNull;

public class PanelModalInfoReparacion extends Panel {

    private final String KEY = getClass().getName();
    private JPanel panelReparaciones;
    private ButtonDefault buttonAddRepair;
    private FlatComboBox<TipoReparacion> combox_TipoReparacion;
    private FlatComboBox<Categorias_Reparacion> combox_CategoriaReparacion;
    private FlatComboBox<Reparacion> combox_reparacion;
    private FlatComboBox<Empleado> combox_Tecnico;
    private InputText inputDescripcion;
    private JButton buttontRefreshTecnicos, buttontRefreshCategoria, buttontRefreshTipoReparacion, button_helpLimit;
    private InputFormattedDecimal inputPrecio;
    private InputFormattedDecimal inputAbono;

    @Override
    public void panelInit() {
        SwingUtilities.invokeLater(() -> {
            TipoReparacion.ListTiposReparaciones.addItemsTipoReparaciones(combox_TipoReparacion);
            Categorias_Reparacion.ListCategorias.addItemsCategorias(combox_CategoriaReparacion);
        });
    }

    public PanelModalInfoReparacion setValue(List<Reparacion_Dispositivo> list) {
        addRepairsPanel(list);
        return this;
    }

    public PanelModalInfoReparacion() {
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

        button_helpLimit.addActionListener((x) -> {
            Notify.closeAll();
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
        panel.add(button_helpLimit, "grow 0,growy 0");

        panel.add(buttonAddRepair, "span,w 250!,gapy 5,al center");

        panel.updateUI();
        return panel;
    }

    private JComponent getLabel(String text) {
        JLabel label = new JLabel(text, JLabel.TRAILING);
        label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD, 13.2f));
        return label;
    }

    private void addReparacion() {
        Toast.closeAll();
        if (Promiseld.checkPromiseId(KEY)) return;
        Promiseld.commit(KEY);
        EventQueue.invokeLater(() -> {
            try {
                Reparacion_Dispositivo reparacionDispositivo = getReparacion();
                if (isNotNull(reparacionDispositivo)) {
                    PanelRequestDevice.listRepair.add(reparacionDispositivo);
                    panelReparaciones.add(new CardRepair(reparacionDispositivo, createEventCardDelete()));
                    panelReparaciones.updateUI();
                }
            } catch (Exception ex) {
                Notify.getInstance().showToast(Toast.Type.ERROR, ex.getMessage());
            } finally {
                Promiseld.terminate(KEY);
            }
        });
    }

    private Reparacion_Dispositivo getReparacion() {
        Toast.closeAll();
        if (toastIsEmpy()) return null;
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

        if (abono > precio) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El anticipo no puede ser mayor al precio");
            return true;
        }
        return false;
    }

    private void addRepairsPanel(List<Reparacion_Dispositivo> list) {
        panelReparaciones.removeAll();
        EventQueue.invokeLater(() -> {
            for (Reparacion_Dispositivo reparacionDispositivo : list) {
                panelReparaciones.add(new CardRepairInfo(reparacionDispositivo, createEventCardDelete()));
            }
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

    public class CardRepairInfo extends JPanel {
        private final Reparacion_Dispositivo reparacionDispositivo;
        private final Consumer<Reparacion_Dispositivo> event;
        private JLabel icon;

        public CardRepairInfo(Reparacion_Dispositivo reparacionDispositivo, Consumer<Reparacion_Dispositivo> event) {
            this.reparacionDispositivo = reparacionDispositivo;
            this.event = event;
            init();
        }

        private void init() {
            icon = new JLabel();
            icon.setIcon(createIcon());
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc:30;" +
                    "[light]background:darken($Panel.background,3%);" +
                    "[dark]background:lighten($Panel.background,3%);");

            setLayout(new MigLayout("wrap 2,insets 5", "[fill,center][]", "[grow 0,center]"));
            add(icon, "gapx 15");
            add(createBody());
            revalidate();
            updateUI();
        }

        private JPanel createBody() {
            JPanel body = new JPanel(new MigLayout("wrap,insets n", "[200]", "[][][]push"));
            body.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null");
            JLabel title = new JLabel(reparacionDispositivo.getCategoria().getName());
            title.putClientProperty(FlatClientProperties.STYLE, "" +
                    "font:bold +1;");
            JTextPane description = new JTextPane();
            description.setEditable(false);
            description.setCursor(new Cursor(Cursor.TEXT_CURSOR));
            description.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:0,0,0,0;" +
                    "background:null;" +
                    "[light]foreground:tint($Label.foreground,30%);" +
                    "[dark]foreground:shade($Label.foreground,30%)");
            description.setText(
                    "Reparación: " + reparacionDispositivo.getReparacion()
                            + "\nDescripción: " + (reparacionDispositivo.getDescripcion() == null || reparacionDispositivo.getDescripcion().isEmpty() ?
                            "campo vacío" : reparacionDispositivo.getDescripcion())
                            + "\nPrecio: $" + InputFormattedDecimal.decimalFormat.format(reparacionDispositivo.getPrecio())
                            + "\nAnticipo: $" + InputFormattedDecimal.decimalFormat.format(reparacionDispositivo.getAbono())
                            + "\nTécnico Encargado:\n " + reparacionDispositivo.getEmpleado().toString());

            ButtonAccentBase buttonAccentBase = new ButtonAccentBase("Actualizar Estado","#EF9D30");
            body.add(title);
            body.add(description);
            body.add(buttonAccentBase, "al center,gapy 0 5");
            return body;
        }

        private AvatarIcon createIcon() {
            AvatarIcon icon = new AvatarIcon(CardRepairInfo.class.getResource(PathResources.Img.categorydevice + "hardware.png"), 70, 70, 3.9f);
            icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
            icon.setBorder(2, 2);
            icon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#6d6d6d"), 0.1f));
            return icon;
        }

    }

}
