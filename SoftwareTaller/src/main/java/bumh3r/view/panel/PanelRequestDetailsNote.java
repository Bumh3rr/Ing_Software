package bumh3r.view.panel;

import bumh3r.components.MyScrollPane;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardDeviceinfo;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.interfaces.CallBack;
import bumh3r.model.Dispositivo;
import bumh3r.model.MetodoPago;
import bumh3r.model.Nota;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import java.awt.Cursor;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class PanelRequestDetailsNote extends Panel {
    private ButtonDefault button_confirm;
    private JPanel panelDevices;
    private FlatTextArea atendido_por, nombreCliente, domicilioCliente, movilCliente, fijoCliente, cantidadTotal, anticipos, cantidadPendiente;
    private FlatComboBox<MetodoPago> comboBoxMetodoPago;
    private FlatComboBox<Nota.StatusNota> comboBoxStatusNote;
    private JComponent label_paymentMethod;
    private Nota nota;

    @Override
    public void panelInit() {
        SwingUtilities.invokeLater(() -> {
            Nota.StatusNota.addItemsStatusNote(comboBoxStatusNote);
        });
    }

    public void addCallBack(CallBack.Success eventSuccess, CallBack.Fail eventFail) {
//        ServiceCreatedNote.getInstance().install(eventSuccess, eventFail);
    }

    public PanelRequestDetailsNote setData(Nota nota) {
        this.nota = nota;

        PoolThreads.getInstance().getExecutorService().execute(() -> {
            addItemsCardDevice(nota.getDispositivos());
        });

        atendido_por.setText(nota.getRecido_por().toString());
        nombreCliente.setText(nota.getCliente().getName());
        domicilioCliente.setText(nota.getCliente().getAddress());
        movilCliente.setText(nota.getCliente().getPhone1());
        fijoCliente.setText(nota.getCliente().getPhone2());

//        cantidadTotal.setText(InputFormattedDecimal.decimalFormat.format(nota.getPrecioTotal()));
//        anticipos.setText(InputFormattedDecimal.decimalFormat.format(nota.getAnticipoTotal()));
//        cantidadPendiente.setText(InputFormattedDecimal.decimalFormat.format(nota.getCantidadPendiente()));

        if (true) {
            comboBoxMetodoPago.setVisible(true);
            label_paymentMethod.setVisible(true);

            if (comboBoxMetodoPago.getItemCount() != 0) {
                comboBoxMetodoPago.setSelectedIndex(0);
            } else {
                SwingUtilities.invokeLater(() -> {
                    MetodoPago.ListMetodosPago.addItemsMetodoPago(comboBoxMetodoPago);
                });
            }

        } else {
            label_paymentMethod.setVisible(false);
            comboBoxMetodoPago.setVisible(false);
        }

        if (comboBoxStatusNote.getItemCount() != 0) {
            comboBoxStatusNote.setSelectedIndex(0);
        }
        return this;
    }

    public PanelRequestDetailsNote() {
        initComponents();
        initListeners();
        init();
    }

    private void initComponents() {
        button_confirm = new ButtonDefault("Confirmar");
        atendido_por = getInstanceTextArea();
        nombreCliente = getInstanceTextArea();
        domicilioCliente = getInstanceTextArea();
        movilCliente = getInstanceTextArea();
        fijoCliente = getInstanceTextArea();
        cantidadTotal = getInstanceTextArea();
        anticipos = getInstanceTextArea();
        cantidadPendiente = getInstanceTextArea();
        comboBoxMetodoPago = new FlatComboBox<>();
        comboBoxStatusNote = new FlatComboBox<>();
        label_paymentMethod = getLabelGramatical("Método de Pago:");
        nota = new Nota();
    }

    private void initListeners() {
        button_confirm.addActionListener((e) -> confirm());
    }

    private void confirm() {
//        if (this.nota == null) {
//            return;
//        }
//
//        if (Toast.checkPromiseId(ServiceCreatedNote.KEY)) {
//            return;
//        }
//
//        this.nota.setStatus((comboBoxStatusNote.getItemCount() != 0) ? ((Nota.StatusNota) comboBoxStatusNote.getSelectedItem()) : Nota.StatusNota.EN_PROCESO);
//        ServiceCreatedNote.getInstance().addNota(this.nota, 1);
//
//        if (this.nota.getAnticipoTotal() > 0) {
//            if (comboBoxMetodoPago.getItemCount() == 0) {
//                Notify.getInstance().showToast(Toast.Type.WARNING, "No se ha seleccionado un método de pago");
//                return;
//            }
//            ServiceCreatedNote.getInstance().addCobro(new Cobro(this.nota.getAnticipoTotal(), LocalDateTime.now(),
//                    (MetodoPago) comboBoxMetodoPago.getSelectedItem(),
//                    (this.nota.getCantidadPendiente() == 0) ? Cobro.TipoCobro.PAGO_TOTAL : Cobro.TipoCobro.ABONO));
//        }
//        ModalDialog.popModel(FormCreateNote.ID);
//        ServiceCreatedNote.getInstance().commit(SwingUtilities.windowForComponent(this));
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets n", "[fill]", "[]10[]"));
        add(details(), "grow");
        add(button_confirm, "span,grow 0,al trail");
    }

    private JComponent details() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,insets 7,fillx", "[fill][fill]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:36;" +
                "[light]background:darken(@background,2%);" +
                "[dark]background:#2D2D2E;"
        );
        panel.add(createDetailsNote());
        panel.add(createDetailsDevices());
        return panel;
    }

    private JComponent createDetailsNote() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,insets 5 15 10 15,hidemode 3", "[grow 0,trail]8[250:n:300,lead]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:16;" +
                "[light]background:darken(@background,2%);" +
                "[dark]background:#2D2D2E;"

        );
        panel.add(getLabelTitle("Detalles de la Nota"), "span,grow 0,gapy 10 5,al center");
        panel.add(getLabelGramatical("Status:"));
        panel.add(comboBoxStatusNote, "grow 0");
        panel.add(getLabelGramatical("Fecha:"));
        panel.add(getLabel(DateFull.getDateOnly(LocalDateTime.now())), "grow");
        panel.add(getLabelGramatical("Hora:"));
        panel.add(getLabel(DateFull.getHourOnly(LocalDateTime.now())), "grow");
        panel.add(getLabelGramatical("Atendió:"));
        panel.add(atendido_por, "grow");
        panel.add(new JSeparator(), "span,grow,gapy 10 5,gapx 40 40");

        panel.add(getLabelTitle("Detalles de Cliente"), "span,grow 0,gapy 0 5,al center");
        panel.add(getLabelGramatical("Nombre:"));
        panel.add(nombreCliente, "grow");
        panel.add(getLabelGramatical("Domicilio:"));
        panel.add(domicilioCliente, "grow");
        panel.add(getLabelGramatical("Tel. Movíl:"));
        panel.add(movilCliente, "grow");
        panel.add(getLabelGramatical("Tel. Fijo:"));
        panel.add(fijoCliente, "grow");
        panel.add(new JSeparator(), "span,grow,gapy 10 5,gapx 40 40");

        panel.add(getLabelTitle("Presupuesto Total"), "span,grow 0,gapy 0 5,al center");
        panel.add(getLabelGramatical("Cantidad:"));
        panel.add(cantidadTotal, "grow");
        panel.add(getLabelGramatical("Anticipos:"));
        panel.add(anticipos, "grow");
        panel.add(getLabelGramatical("Pendiente:"));
        panel.add(cantidadPendiente, "grow");
        panel.add(label_paymentMethod, "span,grow 0,al lead");
        panel.add(comboBoxMetodoPago, "span,grow");
        return panel;
    }

    private JComponent createDetailsDevices() {
        JButton component = new JButton();
        component.setLayout(new MigLayout("wrap,fillx,insets 5 15 10 15,w 400!", "[fill]", "[][470!,grow 0]"));
        component.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:36;" +
                "[light]background:@background;" +
                "[dark]background:@background;" +
                "hoverBackground:null;" +
                "pressedBackground:null;" +
                "pressedSelectedBorderColor:null;" +
                "pressedBorderColor:null;" +
                "hoverBorderColor:null;" +
                "hoverSelectedBorderColor:null;" +
                "focusedSelectedBorderColor:null;" +
                "focusedBorderColor:null;" +
                "selectedBackground:null;" +
                "innerFocusWidth:0;" +
                "innerFocusWidth:0;" +
                "borderWidth:1.8;" +
                "[light]borderColor:#e0e0e0;"
        );

        component.add(getLabelTitle("Detalle de los Dispositivos"), "grow 0,gapy 10 5,al lead");
        component.add(createPanelDevices());
        return component;
    }

    private JComponent createPanelDevices() {
        panelDevices = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(200, -1), -1, 10));
        panelDevices.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:5,5,5,5;" +
                "[light]background:@background;" +
                "[dark]background:@background;"
        );
        MyScrollPane scroll = new MyScrollPane(panelDevices);
        scroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;" +
                "[light]background:@background;" +
                "[dark]background:@background;"
        );
        return scroll;
    }

    private FlatTextArea getInstanceTextArea() {
        FlatTextArea textArea = new FlatTextArea() {
            @Override
            public void setText(String t) {
                if (t == null || t.isEmpty()) {
                    super.setText("el campo esta vació");
                    putClientProperty(FlatClientProperties.STYLE, ""
                            + "background:null;"
                            + "[light]foreground:lighten(@foreground,10%);"
                            + "[dark]foreground:darken(@foreground,10%);"
                    );
                } else {
                    super.setText(t);
                    putClientProperty(FlatClientProperties.STYLE, ""
                            + "background:null;"
                    );
                }
            }
        };
        textArea.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");
        textArea.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13f));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        return textArea;
    }

    private FlatTextArea getLabel(String text) {
        FlatTextArea textArea = new FlatTextArea();
        textArea.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");
        textArea.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13f));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        textArea.setText(text);
        return textArea;
    }

    private JComponent getLabelTitle(String text) {
        JLabel label = new LabelPublicaSans(text).size(16f).style("" +
                "[light]foreground:lighten(@foreground,15%);"
                + "[dark]foreground:darken(@foreground,15%);");
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private JComponent getLabelGramatical(String text) {
        JLabel label = LabelTextArea.ForNote.getLabelGramatical(text);
        label.setHorizontalAlignment(JLabel.TRAILING);
        return label;
    }

    private void addItemsCardDevice(LinkedList<Dispositivo> listdevice) {
        panelDevices.removeAll();
        for (Dispositivo device : listdevice) {
            SwingUtilities.invokeLater(() -> {
                panelDevices.add(new CardDeviceinfo(device.getModel(), device.getBrand().getUrl_icono(), device.getListReparaciones()));
            });
        }
        panelDevices.updateUI();
    }

}

