package bumh3r.view.panel;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardDevice;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.utils.fonts.FontPublicaSans;
import bumh3r.model.Cliente;
import bumh3r.model.Empleado;
import bumh3r.model.other.DateFull;
import bumh3r.request.DispositivoRequest;
import bumh3r.request.NotaRequest;
import bumh3r.request.ReparacionRequest;
import bumh3r.system.panel.Panel;
import bumh3r.utils.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class PanelAddNota extends Panel {
    private ContainerCards<DispositivoRequest> containerCards;
    private JButton buttonCancel, buttonCreated;
    private JLabel precio, abono, size_repair;
    private ButtonAccentBase buttonAddDevice, buttonSearchCustomer;
    @Getter
    private FlatComboBox<Object> employee, customer;

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

    public void installEventDeleteDevice(BiConsumer<DispositivoRequest, Runnable> event) {
        containerCards.installDependent1(event);
    }

    public void installEventCancelar(Runnable event) {
        buttonCancel.addActionListener((x)-> event.run());
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardDevice.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), -1, 10));
        precio = new JLabel("$ 0.0");
        abono = new JLabel("$ 0.0");
        size_repair = new JLabel("0");
        buttonCancel = new ButtonAccentBase("Cancelar");
        employee = new FlatComboBox<>();
        buttonAddDevice = new ButtonAccentBase("Agregar");
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
        containerCards.getPanelCards().putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panel.add(containerCards, "grow,push");
        return panel;
    }

    public NotaRequest getValue() {
        Empleado empleado = employee.getSelectedItem() instanceof Empleado ? (Empleado) employee.getSelectedItem() : null;
        Cliente cliente = customer.getSelectedItem() instanceof Cliente ? (Cliente) customer.getSelectedItem() : null;
        List<DispositivoRequest> list = containerCards.getListItems();
        return new NotaRequest(empleado, cliente, list);
    }

    public void setEmployeeModel(List<Empleado> list) {
        SwingUtilities.invokeLater(() -> {
            this.employee.removeAllItems();
            this.employee.addItem("Seleccione una Empleado");
            list.forEach((empleado) -> this.employee.addItem(empleado));
        });
    }

    public void setCustomerModel(Cliente cliente) {
        SwingUtilities.invokeLater(() -> {
            this.customer.removeAllItems();
            this.customer.addItem(cliente);
        });
    }

    public void setPresupuesto(List<DispositivoRequest> list) {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            precio.setText("$ ".concat(InputFormattedDecimal.decimalFormat.format(list.stream().mapToDouble(value -> value.getReparaciones().stream().mapToDouble(ReparacionRequest::precio).sum()).sum())));
            abono.setText("$ ".concat(InputFormattedDecimal.decimalFormat.format(list.stream().mapToDouble(value -> value.getReparaciones().stream().mapToDouble(ReparacionRequest::abono).sum()).sum())));
            size_repair.setText("%d".formatted(list.stream().mapToInt(value -> value.getReparaciones().size()).sum()));
        });
    }

    public void addCardDevice(DispositivoRequest dispositivo) {
        containerCards.addItemOne(dispositivo);
        setPresupuesto(containerCards.getListItems());
    }

    public void deleteCardDevice(DispositivoRequest dispositivo) {
        containerCards.delete(dispositivo);
        setPresupuesto(containerCards.getListItems());
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            containerCards.cleanCards();
            precio.setText("$ 0.0");
            abono.setText("$ 0.0");
            size_repair.setText("0");
            customer.setSelectedIndex(0);
            employee.removeAllItems();
            customer.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione al cliente en la búsqueda"}));
        });
    }
}