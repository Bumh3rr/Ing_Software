package bumh3r.view.panel;

import bumh3r.components.MyScrollPane;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardDeviceNoteCreated;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.interfaces.CallBack;
import bumh3r.interfaces.DeviceCallback;
import bumh3r.modal.CustomModal;
import bumh3r.model.*;
import bumh3r.model.New.ClienteN;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.other.DateFull;
import bumh3r.notifications.Notify;
import bumh3r.request.NotaRequest;
import bumh3r.system.panel.Panel;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

import static bumh3r.archive.PathResources.Icon.home;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelAddNota extends Panel {
    public static String ID = PanelAddNota.class.getName();
    private JButton buttonCancel, buttonCreated;
    private JLabel precio, abono, size_repair;
    private ButtonAccentBase buttonAddDevice, buttonSearchCustomer;
    @Getter
    private FlatComboBox<Object> employee, customer;
    private JPanel panelDevices;

    public PanelAddNota() {
        initComponents();
        init();
    }

    public void installEventAddNota(Runnable event) {
        buttonCreated.addActionListener((e) -> event.run());
    }

    public void installEventAddDevice(Runnable event) {
        buttonAddDevice.addActionListener((e) -> event.run());
    }

    public void installEventSearchCustomer(Runnable event) {
        buttonSearchCustomer.addActionListener((e) -> event.run());
    }


    private void initComponents() {
        precio = new JLabel("$ 0.0");
        abono = new JLabel("$ 0.0");
        size_repair = new JLabel("0");
        buttonCancel = new ButtonAccentBase("Cancelar");
        employee = new FlatComboBox<>();
        buttonAddDevice = new ButtonAccentBase("Agregar");
        panelDevices = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), -1, 10));
        buttonCreated = new ButtonDefault("Crear Nota");
        customer = new FlatComboBox<>();
        buttonSearchCustomer = new ButtonAccentBase("Buscar Cliente");
        customer.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione al cliente en la búsqueda"}));
        employee.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione al Empleado"}));
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,ins 0 25 25 25,w 550!", "fill,grow"));
        add(createPanelFecha(), "grow 0,al center");

        add(createdGramaticalP("Recibido por"), "growx 0,al lead");
        add(employee, "growx");

        add(new JSeparator(), "growx,gapx 10 10,gapy 3");
        add(createdSubTitle("Cliente", 15.5f), "gapy 1 3,growx 0,al center");
        add(createdGramaticalP("Cliente seleccionado"), "growx 0,al lead");
        add(customer, "growx,split 2");
        add(buttonSearchCustomer, "growx 0");

        add(new JSeparator(), "growx,gapx 10 10,gapy 3");
        add(createdSubTitle("Dispositivos", 15.5f), "gapy 1 1,grow 0,al center");
        add(buttonAddDevice, "growx 0,al trail");
        add(createPanelDevices(), "growx");

        add(new JSeparator(), "grow,gapx 10 10,gapy 3");
        add(createdSubTitle("Presupuestos", 15.5f), "gapy 1 3,growx 0,al center");
        add(new LabelPublicaSans("Total:").type(FontPublicaSans.FontType.BOLD).size(13.2f), "split 6,growx 0");
        add(precio, "growx");
        add(new LabelPublicaSans("Anticipos:").type(FontPublicaSans.FontType.BOLD).size(13.2f));
        add(abono, "growx");
        add(new LabelPublicaSans("Reparaciones:").type(FontPublicaSans.FontType.BOLD).size(13.2f));
        add(size_repair, "growx");
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

    public NotaRequest getValue() {
        return null;
    }

    public void setEmployeeModel(List<EmpleadoN> list) {
        SwingUtilities.invokeLater(() -> {
            this.employee.removeAllItems();
            this.employee.addItem("Seleccione una Empleado");
            list.forEach((empleado) -> this.employee.addItem(empleado));
        });
    }

    public void setCustomerModel(ClienteN cliente) {
        SwingUtilities.invokeLater(() -> {
            this.customer.removeAllItems();
            this.customer.addItem(cliente);
        });
    }

//    private void updateInfoPresupuestos() {
//        PoolThreads.getInstance().getExecutorService().execute(() -> {
//            precio.setText("$ ".concat(InputFormattedDecimal.decimalFormat.format(listdevice.stream().mapToDouble(value -> value.getListReparaciones().stream().mapToDouble(Reparacion_Dispositivo::getPrecio).sum()).sum())));
//            abono.setText("$ ".concat(InputFormattedDecimal.decimalFormat.format(listdevice.stream().mapToDouble(value -> value.getListReparaciones().stream().mapToDouble(Reparacion_Dispositivo::getAbono).sum()).sum())));
//            size_repair.setText("%d".formatted(listdevice.stream().mapToInt(value -> value.getListReparaciones().size()).sum()));
//        });
//    }
//
//    private Consumer<Dispositivo> createAddDeviceEvent = (device) -> {
//        try {
//            Toast.closeAll();
//            listdevice.add(device);
//            int index = listdevice.indexOf(device);
//            SwingUtilities.invokeLater(() -> {
//                panelDevices.add(new CardDeviceNoteCreated(device, index, createDeleteEventCard(), createViewEventCard()));
//                panelDevices.updateUI();
//            });
//            updateUI();
//            updateInfoPresupuestos();
//            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Dispositivo agregado");
//        } catch (Exception ex) {
//            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un error al agregar el dispositivo");
//        }
//    };
//
//    private DeviceCallback createEventUpdateDevice = (device, index) -> {
//        try {
//            Toast.closeAll();
//            listdevice.remove(index);
//            panelDevices.remove(index);
//            listdevice.add(index, device);
//            SwingUtilities.invokeLater(() -> {
//                panelDevices.add(new CardDeviceNoteCreated(device, index, createDeleteEventCard(), createViewEventCard()));
//                panelDevices.updateUI();
//            });
//            updateInfoPresupuestos();
//            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Dispositivo Actualizado Correctamente");
//        } catch (Exception ex) {
//            Notify.getInstance().showToast(Toast.Type.ERROR, "Ocurrió un error al Actualizar el dispositivo");
//        }
//    };
//
//    private DeviceCallback createViewEventCard() {
//        return (device, index) -> {
//            Toast.closeAll();
//            ModalDialog.pushModal(new CustomModal(
//                    getPanelAddDevice().modeView(device, index).setType(PanelAddDispositivo.Type.VIEW),
//                    "Información del Dispositivo", home + "ic_device.svg",
//                    PanelAddNota.ID,
//                    createEventBack,
//                    false
//            ), PanelAddNota.ID);
//        };
//    }


}