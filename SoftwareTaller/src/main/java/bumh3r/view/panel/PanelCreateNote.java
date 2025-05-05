package bumh3r.view.panel;

import bumh3r.components.MyScrollPane;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardDeviceNoteCreated;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.interfaces.CallBack;
import bumh3r.interfaces.DeviceCallback;
import bumh3r.modal.CustomModal;
import bumh3r.model.*;
import bumh3r.model.other.DateFull;
import bumh3r.notifications.Notify;
import bumh3r.system.panel.Panel;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.view.form.FormNotes;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import static bumh3r.archive.PathResources.Icon.home;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelCreateNote extends Panel {
    public static String ID = PanelCreateNote.class.getName();
    private JButton buttonCancel,buttonCreated;
    private JLabel label_precio, label_abono, label_sizeRepairs;
    private ButtonAccentBase buttonAddDevice, buttonSearchCustomer;
    private FlatComboBox<String> comboBox_employees;
    private FlatComboBox<String> comboBox_customer;
    private JPanel panelDevices;
    private PanelRequestDevice panelRequestDevice;
    private PanelRequestDetailsNote panelRequestDetailsNote;
    private LinkedList<Dispositivo> listdevice;

    public PanelCreateNote() {
        initComponents();
        initListeners();
        init();
    }

    private void initComponents() {
        listdevice = new LinkedList<>();
        label_precio = new JLabel("$ 0.0");
        label_abono = new JLabel("$ 0.0");
        label_sizeRepairs = new JLabel("0");
        buttonCancel = new ButtonAccentBase("Cancelar");
        comboBox_employees = new FlatComboBox<>();
        buttonAddDevice = new ButtonAccentBase("Agregar");
        panelDevices = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), -1, 10));
        buttonCreated = new ButtonDefault("Crear Nota");
        comboBox_customer = new FlatComboBox<>();
        buttonSearchCustomer = new ButtonAccentBase("Buscar Cliente");

        comboBox_customer.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione al cliente en la búsqueda"}));
        comboBox_employees.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione al Empleado"}));
    }

    private void initListeners() {
        buttonAddDevice.addActionListener((x) -> showPanelAddDevice());

        buttonSearchCustomer.addActionListener((e) -> {
            ModalDialog.pushModal(
                    CustomModal.builder()
                            .component(PanelsInstances.getInstance().getPanelModal(PanelSearchCliente.class))
                            .icon(modal + "ic_search_panel.svg")
                            .title("Buscar Cliente")
                            .ID(PanelCreateNote.ID)
                            .consumer((x)-> ModalDialog.popModel(ID))
                            .buttonClose(false)
                    .build(),
                    PanelCreateNote.ID
            );
        });
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,ins 0 25 25 25,w 550!","fill,grow"));
        add(createPanelFecha(), "grow 0,al center");

        add(createdGramaticalP("Recibido por"), "growx 0,al lead");
        add(comboBox_employees, "growx");

        add(new JSeparator(), "growx,gapx 10 10,gapy 3");
        add(createdSubTitle("Cliente",15.5f), "gapy 1 3,growx 0,al center");
        add(createdGramaticalP("Cliente seleccionado"), "growx 0,al lead");
        add(comboBox_customer, "growx,split 2");
        add(buttonSearchCustomer,"growx 0");

        add(new JSeparator(), "growx,gapx 10 10,gapy 3");
        add(createdSubTitle("Dispositivos",15.5f), "gapy 1 1,grow 0,al center");
        add(buttonAddDevice, "growx 0,al trail");
        add(createPanelDevices(), "growx");

        add(new JSeparator(), "grow,gapx 10 10,gapy 3");
        add(createdSubTitle("Presupuestos",15.5f), "gapy 1 3,growx 0,al center");
        add(getLabel("Total:"), "split 6,growx 0");
        add(label_precio, "growx");
        add(getLabel("Anticipos:"));
        add(label_abono, "growx");
        add(getLabel("Reparaciones:"));
        add(label_sizeRepairs, "growx");

        add(buttonCancel, "growx 0,gapy 5,al lead,split 2");
        add(buttonCreated, "growx 0,al trail");
    }

    private JComponent createPanelFecha() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fill,insets 1 n 1 n", "[grow 0]10[grow 0]"));
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Fecha:"), "grow 0,split 2");
        panel.add(new LabelTextArea.ForNote((DateFull.getDateOnly(LocalDateTime.now()))));
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Día:"), "grow 0,split 2");
        panel.add(new LabelTextArea.ForNote((DateFull.getWeekOnly(LocalDateTime.now()))));
        return panel;
    }

    private JComponent createPanelDevices() {
        JPanel panel = new JPanel(new MigLayout("fillx,ins 10,h 90:n:240"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panelDevices.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:5,10,5,10;"
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panel.add(new MyScrollPane(panelDevices), "grow,push");
        return panel;
    }

    private JComponent getLabel(String text) {
        JLabel label = new JLabel(text, JLabel.LEADING);
        label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD, 13.2f));
        return label;
    }

    private void showPanelAddDevice() {
        ModalDialog.pushModal(new CustomModal(getPanelAddDevice().setType(PanelRequestDevice.Type.ADD),
                "Nuevo Dispositivo", home + "ic_device.svg", PanelCreateNote.ID, createEventBack, false), PanelCreateNote.ID);
    }

    private PanelRequestDevice getPanelAddDevice() {
        if (panelRequestDevice == null) {
            panelRequestDevice = new PanelRequestDevice();
            panelRequestDevice.panelInit();
            panelRequestDevice.setLambdaAddDevice(createAddDeviceEvent);
            panelRequestDevice.setLambdaDeleteDevice(createDeleteEventCard());
            return panelRequestDevice;
        }
        return panelRequestDevice;
    }

    private PanelRequestDetailsNote getPanelDetailsNote() {
        if (panelRequestDetailsNote == null) {
            panelRequestDetailsNote = new PanelRequestDetailsNote();
            panelRequestDetailsNote.panelInit();
            return panelRequestDetailsNote;
        }
        panelRequestDetailsNote.panelCheckUI();
        return panelRequestDetailsNote;
    }

    private void refreshCardsDevices() {
        SwingUtilities.invokeLater(() -> {
            panelDevices.removeAll();
            for (int index = 0; index < listdevice.size(); index++) {
                panelDevices.add(new CardDeviceNoteCreated(listdevice.get(index), index, createDeleteEventCard(), createViewEventCard()));
            }
            panelDevices.updateUI();
        });
    }

    private void updateInfoPresupuestos() {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            label_precio.setText("$ ".concat(InputFormattedDecimal.decimalFormat.format(listdevice.stream().mapToDouble(value -> value.getListReparaciones().stream().mapToDouble(Reparacion_Dispositivo::getPrecio).sum()).sum())));
            label_abono.setText("$ ".concat(InputFormattedDecimal.decimalFormat.format(listdevice.stream().mapToDouble(value -> value.getListReparaciones().stream().mapToDouble(Reparacion_Dispositivo::getAbono).sum()).sum())));
            label_sizeRepairs.setText("%d".formatted(listdevice.stream().mapToInt(value -> value.getListReparaciones().size()).sum()));
        });
    }

    private CallBack.Success eventSuccess = (folio) -> {
        showPanelModalSuccess("Nota Creada Exitosamente",
                "La Nota se ha creado exitosamente con el folio: " + folio);
    };
    private CallBack.Fail eventFail = (message) -> {
        showPanelModalError("Se ha producido un error :(",
                "Hubo un problema al crear la Nota en la base de datos,\nconsulte con el support técnico para tratar el problema"
                        + "\n\nCausa: " + message);
    };

    private void showPanelModalSuccess(String title, String message) {

    }

    private void showPanelModalError(String title, String message) {

    }

    private Consumer<Boolean> createEventBack = (x) -> {
        Toast.closeAll();
        ModalDialog.popModel(PanelCreateNote.ID);
    };

    private Consumer<Dispositivo> createAddDeviceEvent = (device) -> {
        try {
            Toast.closeAll();
            listdevice.add(device);
            int index = listdevice.indexOf(device);
            SwingUtilities.invokeLater(() -> {
                panelDevices.add(new CardDeviceNoteCreated(device, index, createDeleteEventCard(), createViewEventCard()));
                panelDevices.updateUI();
            });
            updateUI();
            updateInfoPresupuestos();
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Dispositivo agregado");
        } catch (Exception ex) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un error al agregar el dispositivo");
        }
    };
    private DeviceCallback createEventUpdateDevice = (device, index) -> {
        try {
            Toast.closeAll();
            listdevice.remove(index);
            panelDevices.remove(index);
            listdevice.add(index, device);
            SwingUtilities.invokeLater(() -> {
                panelDevices.add(new CardDeviceNoteCreated(device, index, createDeleteEventCard(), createViewEventCard()));
                panelDevices.updateUI();
            });
            updateInfoPresupuestos();
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Dispositivo Actualizado Correctamente");
        } catch (Exception ex) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un error al Actualizar el dispositivo");
        }
    };

    //Events of CardDevice
    private Consumer<Integer> createDeleteEventCard() {
        return (i) -> {
            try {
                Toast.closeAll();
                listdevice.remove((int) i);
                refreshCardsDevices();
                updateInfoPresupuestos();
                Notify.getInstance().showToast(Toast.Type.SUCCESS, "Dispositivo eliminado");
            } catch (Exception ex) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un error al eliminar el dispositivo");
            }
        };
    }

    private DeviceCallback createViewEventCard() {
        return (device, index) -> {
            Toast.closeAll();
            ModalDialog.pushModal(new CustomModal(
                    getPanelAddDevice().modeView(device, index).setType(PanelRequestDevice.Type.VIEW),
                    "Información del Dispositivo", home + "ic_device.svg",
                    PanelCreateNote.ID,
                    createEventBack,
                    false
            ), PanelCreateNote.ID);
        };
    }

}