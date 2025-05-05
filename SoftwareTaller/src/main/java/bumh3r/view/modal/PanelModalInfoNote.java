package bumh3r.view.modal;

import bumh3r.components.Button_Overflow_Menu;
import bumh3r.components.ContainerCards;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardDeviceNote;
import bumh3r.components.input.InputArea;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.modal.CustomModal;
import bumh3r.model.Dispositivo;
import bumh3r.model.Nota;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.Modal;
import raven.modal.component.ModalBorderAction;

import static bumh3r.archive.PathResources.Icon.home;

public class PanelModalInfoNote extends Modal {
    public static final String ID = PanelModalInfoNote.class.getName();

    private LabelPublicaSans label_status;
    private LabelTextArea.ForNote label_fecha;
    private LabelTextArea.ForNote label_dia;
    private LabelTextArea.ForNote label_hora;
    private LabelTextArea.ForNote label_empleado;
    private LabelPublicaSans label_folio;
    private InputText id, nombre, telefono_movil, telefono_fijo;
    private InputArea inputDomicilio;
    private JButton close;
    private ContainerCards containerCards;
    private JPanel panelDevices;

    public PanelModalInfoNote setData(Nota nota) {
        id.setText(nota.getCliente().getId().toString());
        nombre.setText(nota.getCliente().getName());
        telefono_movil.setText(nota.getCliente().getPhone1());
        telefono_fijo.setText(nota.getCliente().getPhone2());
        inputDomicilio.setText(nota.getCliente().getAddress());

        label_folio.setText(nota.getFolio());
        label_status.setText(nota.getStatus().getValue());
        label_fecha.setText(DateFull.getDateOnly(nota.getFecha_nota()));
        label_dia.setText(DateFull.getWeekOnly(nota.getFecha_nota()));
        label_hora.setText(DateFull.getHourOnly(nota.getFecha_nota()));
        label_empleado.setText(nota.getRecido_por().toString());
        refreshPanelDevice(nota.getDispositivos());
        return this;
    }

    public PanelModalInfoNote() {
        initComponents();
    }

    private void initComponents() {
        panelDevices = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        panelDevices.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;"
                + "[light]background:darken(@background,1%);"
                + "[dark]background:lighten(@background,5%)");
        containerCards = new ContainerCards(panelDevices);

        Nota.StatusNota cancelado = Nota.StatusNota.EN_PROCESO;
        String color = cancelado.getBackgroundStatus();
        label_status = new LabelPublicaSans(cancelado.getValue()).size(18f).style(""
                + "arc:33;"
                + "border: 8,20,8,20,shade(" + color + ",3%);"
                + "foreground:shade(" + color + ",3%);"
                + "background:fade(" + color + ",8%);");

        label_folio = new LabelPublicaSans("NOTA-2024-000010").size(24f);

        label_fecha = new LabelTextArea.ForNote();
        label_dia = new LabelTextArea.ForNote();
        label_hora = new LabelTextArea.ForNote();
        label_empleado = new LabelTextArea.ForNote();

        id = new InputText();
        nombre = new InputText();
        telefono_movil = new InputText();
        telefono_fijo = new InputText();
        inputDomicilio = new InputArea();

        close = new ButtonDefault("Cerrar");
    }

    @Override
    public void installComponent() {
        setLayout(new MigLayout("wrap 2,fillx,w 500:650,insets 10 15", "fill,grow 0,center"));

        add(label_folio, "grow");
        add(label_status, "split 2");
        add(new Button_Overflow_Menu(null));

        add(createDetailsNote(), "span,grow");

        add(new JSeparator(), "span,grow,gapx 10 10,gapy 5 5");
        add(Panel.createdSubTitle("Detalles del Cliente", 18f), "span,grow 0,al center,wrap 5");
        add(extracted(), "span,grow,gapx 20 20");

        add(new JSeparator(), "span,grow,gapx 10 10,gapy 5 5");
        add(Panel.createdSubTitle("Dispositivos", 18f), "span,grow 0,al center,wrap 5");
        add(containerCards, "span,grow, h 200!");
        add(new JSeparator(), "span,grow,gapx 10 10,gapy 5 5");
        add(close, "span,grow 0,al trail");
    }

    private JComponent createDetailsNote() {
        JPanel panel = new JPanel(new MigLayout("wrap 3,fillx,insets 5", "", ""));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;"
        );
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Fecha:"), "grow 0,split 2");
        panel.add(label_fecha);
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Día:"), "grow 0,split 2");
        panel.add(label_dia);
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Hora:"), "grow 0,split 2");
        panel.add(label_hora);
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Empleado:"), "span,grow 0,split 2");
        panel.add(label_empleado, "grow");
        return panel;
    }

    private JComponent extracted() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 1 n 1 n", "center,fill,grow"));
        panel.add(Panel.createdGramaticalP("ID"));
        panel.add(Panel.createdGramaticalP("Nombre"));
        panel.add(id, "grow");
        panel.add(nombre, "grow");
        panel.add(Panel.createdGramaticalP("Teléfono Móvil"));
        panel.add(Panel.createdGramaticalP("Teléfono Fijo"));
        panel.add(telefono_movil, "grow");
        panel.add(telefono_fijo, "grow");

        panel.add(Panel.createdGramaticalP("Dirección"), "span,grow 0");
        panel.add(inputDomicilio.createdInput(), "span,grow");
        return panel;
    }

    private void refreshPanelDevice(LinkedList<Dispositivo> list) {
        panelDevices.removeAll();
        for (Dispositivo device : list) {
            panelDevices.add(new CardDeviceNote(device, createEventCard()));
        }
    }

    private Consumer<Dispositivo> createEventCard() {
        return e -> {
            SwingUtilities.invokeLater(() -> {
                ModalDialog.pushModal(
                        CustomModal.builder()
                                .component(new PanelModalInfoDevice().setData(e))
                                .icon(home + "ic_device.svg")
                                .buttonClose(false)
                                .title("Dispositivo")
                                .consumer((x) -> ModalDialog.popModel(PanelModalInfoDevice.ID))
                                .ID(PanelModalInfoDevice.ID)
                                .build(),
                        PanelModalInfoDevice.ID);
            });
        };
    }

}
