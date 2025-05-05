package bumh3r.view.panel;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonAction;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.button.ButtonHelpIcon;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.swingButton.SwitchButton;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.interfaces.DeviceCallback;
import bumh3r.modal.CustomModal;
import bumh3r.model.*;
import bumh3r.notifications.Notify;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatCheckBox;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelRequestDevice extends Panel {
    public static final String KEY = PanelRequestDevice.class.getName();

    private JLabel label_precio, label_abono, label_sizeReparaciones;
    private FlatComboBox<TipoDispositivo> combox_tipoDispositive;
    private FlatComboBox<Marca> combox_marca;
    private FlatTextArea inputObservaciones;
    private InputText inputModelo, inputSerial;
    private FlatCheckBox checkBoxChip, checkBoxSD, checkBoxCargador, checkBoxFunda, checkBoxOther;
    private JButton helpIcon, helpIconShowAll, buttontRefreshTiposDevices;
    private ButtonAccentBase button_cancel;
    private ButtonAccentBase buttonReparations;
    private SwitchButton button_showAllMarcas;

    private ButtonDefault button_add;
    private ButtonAccentBase button_delete, button_update;

    public static LinkedList<Reparacion_Dispositivo> listRepair;
    private PanelRequestReparacion panelRequestReparacion;

    private Consumer<Dispositivo> consumerAddDevice;
    private DeviceCallback consumerUpdateDevice;
    private Consumer<Integer> consumerDeleteDevice;
    private Integer index_view = -1;

    @Override
    public void panelInit() {
        EventQueue.invokeLater(() -> {
            TipoDispositivo.ListTiposDispositivos.addItemsTipoDispositivo(combox_tipoDispositive);
        });
    }

    public void setLambdaAddDevice(Consumer<Dispositivo> consumer) {
        this.consumerAddDevice = consumer;
    }

    public void setLambdaUpdateDevice(DeviceCallback consumer) {
        this.consumerUpdateDevice = consumer;
    }

    public void setLambdaDeleteDevice(Consumer<Integer> consumer) {
        this.consumerDeleteDevice = consumer;
    }

    public PanelRequestDevice setType(Type type) {
        switch (type) {
            case VIEW -> setEnabledButtons(true);
            case ADD -> {
                clean();
                setEnabledButtons(false);
//                getPanelRequestReparacion().clean();
            }
        }
        return this;
    }

    public void setEnabledButtons(Boolean bool) {
        button_update.setVisible(bool);
        button_delete.setVisible(bool);
        button_cancel.setVisible(!bool);
        button_add.setVisible(!bool);
    }

    public PanelRequestDevice modeView(Dispositivo dispositivo, int index) {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            try {
                index_view = index;
                inputModelo.setText(dispositivo.getModel());
                inputSerial.setText(dispositivo.getSerial());
                inputObservaciones.setText(dispositivo.getDetalles().getObservaciones());

                //view utils
                int utils = dispositivo.getDetalles().getUtils();
                checkBoxChip.setSelected((utils & 1) != 0);
                checkBoxSD.setSelected((utils & 2) != 0);
                checkBoxCargador.setSelected((utils & 4) != 0);
                checkBoxFunda.setSelected((utils & 8) != 0);
                checkBoxOther.setSelected((utils & 16) != 0);

                listRepair = dispositivo.getListReparaciones();
                eventBack.accept(true);
                SwingUtilities.invokeLater(() -> {
                    if (combox_tipoDispositive.getModel() != null) {
                        combox_tipoDispositive.getModel().setSelectedItem(dispositivo.getType());
                    }
                    button_showAllMarcas.setSelected(true);
                    showAllMarcas(button_showAllMarcas.isSelected());
                    if (combox_marca.getModel() != null) {
                        System.out.println(dispositivo.getBrand());
                        combox_marca.getModel().setSelectedItem(dispositivo.getBrand());
                    }
                });

            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al cargar los datos del Dispositivo");
            }
        });
        return this;
    }

    public void clean() {
        SwingUtilities.invokeLater(() -> {
            inputModelo.setText("");
            inputSerial.setText("");

            listRepair = new LinkedList<>();

            index_view = -1;

            //clean checkBoxs
            checkBoxCargador.setSelected(false);
            checkBoxChip.setSelected(false);
            checkBoxSD.setSelected(false);
            checkBoxFunda.setSelected(false);
            checkBoxOther.setSelected(false);

            eventBack.accept(true);
            inputObservaciones.setText("");

            SwingUtilities.invokeLater(() -> {
                if (combox_tipoDispositive.getSelectedItem() != null) {
                    combox_tipoDispositive.setSelectedIndex(0);
                }
                if (combox_marca.getSelectedItem() != null && button_showAllMarcas.isSelected()) {
                    combox_marca.setSelectedIndex(0);
                }
            });
        });
    }

    public PanelRequestDevice() {
        initComponents();
        initListeners();
        init();
    }

    private void initComponents() {
        try {

            listRepair = new LinkedList<>();

            button_showAllMarcas = new SwitchButton();
            button_showAllMarcas.setBackground(UIManager.getColor("Component.accentColor"));
            inputModelo = new InputText("Modelo", 30).setIcon(modal + "ic_phonemovil.svg");
            inputSerial = new InputText("IMEI del Dispostivo", 20).setIcon(modal + "ic_code.svg");

            inputObservaciones = new FlatTextArea();
            inputObservaciones.setDocument(new LimitTextDocument(250));
            inputObservaciones.setLineWrap(true);
            inputObservaciones.setWrapStyleWord(true);

            combox_tipoDispositive = new FlatComboBox<>();
            combox_marca = new FlatComboBox<>();

            checkBoxChip = new FlatCheckBox();
            checkBoxSD = new FlatCheckBox();
            checkBoxCargador = new FlatCheckBox();
            checkBoxFunda = new FlatCheckBox();
            checkBoxOther = new FlatCheckBox();

            checkBoxChip.setText("Chip");
            checkBoxSD.setText("SD");
            checkBoxCargador.setText("Cargador");
            checkBoxFunda.setText("Funda");
            checkBoxOther.setText("Otro");

            label_precio = new JLabel();
            label_abono = new JLabel();
            label_sizeReparaciones = new JLabel();

            button_cancel = new ButtonAccentBase("Cancelar", "#ff420a");
            helpIcon = new ButtonHelpIcon();
            helpIconShowAll = new ButtonHelpIcon();

            buttonReparations = new ButtonAccentBase("Agregar Reparaciones", "@accentBaseColor")
                    .addStyles("arc:26;" + "margin:10,10,10,10;");


            button_add = new ButtonDefault("Agregar");
            button_delete = new ButtonAccentBase("Eliminar este Dispositivo", "#FF3366")
                    .addStyles("arc:26;" + "margin:10,10,10,10;");
            button_update = new ButtonAccentBase("Actualizar Informción", "#007BFF")
                    .addStyles("arc:26;" + "margin:10,10,10,10;");
            buttontRefreshTiposDevices = new ButtonAction.Refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        //events buttons
        buttonReparations.addActionListener((e) -> showReparaciones());
        button_cancel.addActionListener((e) -> CallBack());
        button_add.addActionListener((e) -> showInfoAddDevice());
        buttontRefreshTiposDevices.addActionListener((e) -> {
            TipoDispositivo.ListTiposDispositivos.getInstance().clean();
            TipoDispositivo.ListTiposDispositivos.addItemsTipoDispositivo(combox_tipoDispositive);
        });
        button_update.addActionListener((e) -> showInfoUpdate());
        button_delete.addActionListener((e) -> showWarningDelete());

        //Events checkBoxs
        checkBoxCargador.addActionListener((e) -> showDescriptionUtil(checkBoxCargador, "Descripción del Cargador", "Cargador", "Ejemplo: Color Blanco"));
        checkBoxChip.addActionListener((e) -> showDescriptionUtil(checkBoxChip, "Descripción del Chip", "Chip", "Ejemplo: Telcel"));
        checkBoxSD.addActionListener((e) -> showDescriptionUtil(checkBoxSD, "Descripción de la SD", "SD", "Ejemplo: 32GB"));
        checkBoxFunda.addActionListener((e) -> showDescriptionUtil(checkBoxFunda, "Descripción de la Funda", "Funda", "Ejemplo: Color Morado"));
        checkBoxOther.addActionListener((e) -> showDescriptionUtil(checkBoxOther, "Descripción del Otro", "Otro", "Descripción"));

        //Events comboBoxs
        combox_tipoDispositive.addActionListener((e) -> fillComboxMarcas());
        button_showAllMarcas.addEventSelected((x) -> showAllMarcas(x));
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets 5 25 0 25", "[grow 0,trail]15[fill,350:450]"));

        add(createTitle("Detalles del Dispositivo"), "span,grow 0,al center");
        add(getLabel("Tipo de Dispositivo:"));
        add(combox_tipoDispositive, "split 2");
        add(buttontRefreshTiposDevices, "grow 0,growy 0");
        add(getLabel("Marca:"));
        add(combox_marca, "grow,split 3");
        add(button_showAllMarcas, "grow 0,growy 0");
        add(helpIconShowAll, "grow 0,growy 0");
        add(getLabel("Modelo:"));
        add(inputModelo);
        add(getLabel("IMEI/SERIAL:"));
        add(inputSerial);
        add(getLabel("Utils:"));
        add(createCamposCheckBox());
        add(getLabel("Observaciones:"));
        add(createInputObservaciones());

        add(new JSeparator(), "span,grow,gapx 10 10,gapy 3");
        add(createTitle("Reparación"), "span,gapy 1 3,grow 0,al center");
        add(getLabel("Reparaciones:"));
        add(buttonReparations);

        add(new JSeparator(), "span,grow,gapx 10 10,gapy 3");
        add(createTitle("Presupuesto del Dispositivo"), "span,gapy 1 3,grow 0,al center");
        add(getLabel("Total: $"));
        add(label_precio);
        add(getLabel("Abono: $"));
        add(label_abono);
        add(getLabel("Reparaciones:"));
        add(label_sizeReparaciones);
        add(createCamposButtons(), "span 2,grow 0,al trail");

    }

    private JComponent createInputObservaciones() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 1"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        inputObservaciones.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        panel.add(inputObservaciones, "grow,push");
        return panel;
    }

    private JComponent createCamposCheckBox() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 0", "grow 0"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");
        panel.add(checkBoxChip);
        panel.add(checkBoxSD);
        panel.add(checkBoxCargador);
        panel.add(checkBoxFunda);
        panel.add(checkBoxOther);
        panel.add(helpIcon);
        return panel;
    }

    private JComponent createCamposButtons() {
        JPanel panel = new JPanel(new MigLayout("wrap,fill,insets 4 5 4 5,hidemode 3", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");
        panel.add(button_delete, "span,grow 0,al lead, split 2");
        panel.add(button_update, "grow 0,al trail");
        panel.add(button_cancel, "span,grow 0,al lead, split 2");
        panel.add(button_add, "grow 0,al trail");
        return panel;
    }

    private JComponent createTitle(String title) {
        return LabelTextArea.ForNote.getLabelTitle(title, 15.5f);
    }

    private JComponent getLabel(String text) {
        JLabel label = new JLabel(text, JLabel.TRAILING);
        label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD, 13.2f));
        return label;
    }

    public void CallBack() {
        EventQueue.invokeLater(() -> {
            ModalDialog.popModel(PanelCreateNote.ID);
        });
    }

    private void showReparaciones() {
        Toast.closeAll();
        ModalDialog.pushModal(new CustomModal(getPanelRequestReparacion(),
                "Reparaciones", modal + "ic_repair.svg", PanelCreateNote.ID, eventBack, false), PanelCreateNote.ID);
    }

    public Consumer<Boolean> eventBack = (e) -> {
        Toast.closeAll();
        var sos = "<html><b>no hay reparaciones agregadas</b></html>";
        buttonReparations.setText(!listRepair.isEmpty() ? "Visualizar Reparaciones" : "Agregar Reparaciones");
        label_precio.setText(!listRepair.isEmpty() ? InputFormattedDecimal.decimalFormat.format(listRepair.stream().mapToDouble(Reparacion_Dispositivo::getPrecio).sum())
                : sos);
        label_abono.setText(!listRepair.isEmpty() ? InputFormattedDecimal.decimalFormat.format(listRepair.stream().mapToDouble(Reparacion_Dispositivo::getAbono).sum())
                : sos);
        label_sizeReparaciones.setText(!listRepair.isEmpty() ? String.valueOf(listRepair.size()) : sos);
    };

    private PanelRequestReparacion getPanelRequestReparacion() {
        if (panelRequestReparacion == null) {
            panelRequestReparacion = new PanelRequestReparacion();
            panelRequestReparacion.panelInit();
            return panelRequestReparacion;
        }
        panelRequestReparacion.panelCheckUI();
        panelRequestReparacion.panelRefresh();
        return panelRequestReparacion;
    }

    private void showDescriptionUtil(FlatCheckBox checkBox, String title, String titleInput, String textHolder) {
//        if (checkBox.isSelected()) {
//            PanelInputUtils panel = new PanelInputUtils(titleInput, textHolder);
//            ModalDialog.showModal(SwingUtilities.windowForComponent(this),
//                    new SimpleModalBorder(panel, title, SimpleModalBorder.DEFAULT_OPTION, (controller, action) -> {
//                        controller.consume();
//                        if (action == SimpleModalBorder.YES_OPTION) {
//                            String text = panel.getText();
//                            if (!text.isEmpty()) {
//                                inputObservaciones.setText(inputObservaciones.getText() + "\n" + checkBox.getText() + ": " + text.toUpperCase());
//                            }
//                        } else if (action == SimpleModalBorder.CANCEL_OPTION) {
//                            checkBox.setSelected(false);
//                        }
//                        controller.close();
//                    }), Config.getModelShowDefault());
//        }
    }

    private void fillComboxMarcas() {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            if (button_showAllMarcas.isSelected()) {
                return;
            }

            TipoDispositivo selectedItem = (TipoDispositivo) combox_tipoDispositive.getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            SwingUtilities.invokeLater(() -> {
                try {
                    combox_marca.removeAllItems();
                    combox_marca.setModel(new DefaultComboBoxModel<>(Marca.ListMarcas.getInstance().getList(selectedItem).stream().toArray(Marca[]::new)));
                    combox_marca.setMaximumRowCount(10);
                    combox_marca.updateUI();

                } catch (Exception e) {
                    Notify.getInstance().showToast(Toast.Type.ERROR,
                            "Conexión inestable, Error al Obtener las marcas de " + selectedItem.getName()
                                    + "\nCausa: " + e.getLocalizedMessage());
                }
            });
        });
    }

    private void showAllMarcas(Boolean x) {
        if (x) {
            try {
                combox_marca.removeAllItems();
                combox_marca.setModel(new DefaultComboBoxModel<>(Marca.ListMarcas.getInstance().getListAll().toArray(Marca[]::new)));
                combox_marca.setMaximumRowCount(16);
                combox_marca.updateUI();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR,
                        "Conexión inestable, Error al Obtener las marcas de todos los Dispositivos"
                                + "\nCausa: " + e.getLocalizedMessage());
            }
        } else {
            fillComboxMarcas();
        }
    }

    private Boolean checkInputs() {
        if (combox_tipoDispositive.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione un tipo de dispositivo");
            return true;
        }

        if (combox_marca.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione una marca");
            return true;
        }

        if (verifyInputEmpty(inputModelo, "Modelo")) {
            return true;
        } else if (listRepair.isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Se requiere mínimo una reparación");
            return true;
        }
        return false;
    }

    private Boolean verifyInputEmpty(JTextField field, String str) {
        if (field.getText().strip().isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo " + str);
            return true;
        }
        return false;
    }

    private Dispositivo getDispositivo() throws Exception {
        TipoDispositivo tipo = (TipoDispositivo) combox_tipoDispositive.getSelectedItem();
        Marca marca = (Marca) combox_marca.getSelectedItem();
        String modelo = inputModelo.getText().isEmpty() ? null : inputModelo.getText().strip();
        String serial = inputSerial.getText().isEmpty() ? null : inputSerial.getText().strip();
        String observaciones = inputObservaciones.getText().isEmpty() ? null : inputObservaciones.getText().strip();

        int utils = 0;
        if (checkBoxChip.isSelected()) utils |= 1;      // Primer bit
        if (checkBoxSD.isSelected()) utils |= 2;      // Segundo bit
        if (checkBoxCargador.isSelected()) utils |= 4;      // Tercer bit
        if (checkBoxFunda.isSelected()) utils |= 8;      // Cuarto bit
        if (checkBoxOther.isSelected()) utils |= 16;      // Quinto bit
        return new Dispositivo(tipo, marca, modelo, serial, new Detalles(observaciones, utils), listRepair);
    }

    private void addDevice() {
        try {
            Dispositivo device = getDispositivo();
            if (device != null) {
                consumerAddDevice.accept(device);
                CallBack();
            }
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un erorr");
        }
    }

    private void updateDevice() {
        try {
            Dispositivo device = getDispositivo();
            if (device != null && index_view != -1) {
                consumerUpdateDevice.action(device, index_view);
                CallBack();
            }
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un erorr");
        }
    }

    private void deleteDevice() {
        try {
            if (index_view != -1) {
                consumerDeleteDevice.accept(index_view);
                CallBack();
            }
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un erorr");
        }
    }

    private void showInfoAddDevice() {
//        Toast.closeAll();
//        if (checkInputs()) {
//            return;
//        }
//        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
//                new PanelModalNote(PanelModalNote.Type.INFO, "Confirmación",
//                        "¿Los datos están correctos?"
//                        , (controller, action) -> {
//                    if (action == PanelModalNote.ACCEPT_OPTION) {
//                        addDevice();
//                    }
//                    controller.close();
//                }), Config.getModelShowModalPush(), PanelModalNote.ID);
    }


    public void showWarningDelete() {
//        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
//                new PanelModalNote(PanelModalNote.Type.WARNING, "Estas seguro?",
//                        "¿Estás seguro de eliminar este dispositivo?\n" +
//                                "Esta acción no se puede deshacer.", (controller, action) -> {
//                    if (action == PanelModalNote.ACCEPT_OPTION) {
//                        deleteDevice();
//                    }
//                    controller.close();
//                }), Config.getModelShowModalPush(), PanelModalNote.ID);
    }

    private void showInfoUpdate() {
//        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
//                new PanelModalNote(PanelModalNote.Type.INFO, "Confirmación",
//                        "¿Estas seguro de querer actualizar la información?\n" +
//                                "Esta acción no se puede deshacer.", (controller, action) -> {
//                    if (action == PanelModalNote.ACCEPT_OPTION) {
//                        updateDevice();
//                    }
//                    controller.close();
//                }), Config.getModelShowModalPush(), PanelModalNote.ID);
    }


    public enum Type {
        VIEW, ADD
    }
}
