package bumh3r.view.modal;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardDeviceInfo;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.custom.ButtonOverflow;
import bumh3r.components.input.InputArea;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.model.Dispositivo;
import bumh3r.model.Nota;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;

import static bumh3r.utils.PathResources.Icon.home;


public class PanelModalInfoNote extends Modal {
    private ContainerCards<Dispositivo> containerCards;
    private LabelPublicaSans estado, folio;
    private LabelTextArea.ForNote fecha, dia, hora, empleado;
    private InputText id, nombre, telefono_movil, telefono_fijo;
    private InputArea inputDomicilio;
    private JButton close;
    private Nota nota;
    private ButtonOverflow buttonOverflow;
    private ActionListener eventUpdateStatus;

    public void installEventClose(Runnable event) {
        close.addActionListener(e -> event.run());
    }

    public void installEventDetailsDevice(BiConsumer<Dispositivo, Runnable> event) {
        containerCards.installDependent1(event);
    }

    public PanelModalInfoNote(Nota nota, Runnable event) {
        this.eventUpdateStatus = e -> {
            event.run();
            setEstado();
        };
        this.nota = nota;
        initComponents();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardDeviceInfo.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), -1, 10));
        containerCards.setLongitud(1000);

        String color = this.nota.getEstado().getBackgroundStatus();
        this.estado = new LabelPublicaSans(this.nota.getEstado().getValue()).size(18f).style(""
                + "arc:33;"
                + "border: 8,20,8,20,shade(" + color + ",3%);"
                + "foreground:shade(" + color + ",3%);"
                + "background:fade(" + color + ",8%);");

        folio = new LabelPublicaSans("000000").size(24f);
        fecha = new LabelTextArea.ForNote();
        dia = new LabelTextArea.ForNote();
        hora = new LabelTextArea.ForNote();
        empleado = new LabelTextArea.ForNote();
        id = getInstance();
        nombre = getInstance();
        telefono_movil = getInstance();
        telefono_fijo = getInstance();
        inputDomicilio = new InputArea();
        inputDomicilio.setEnabled(false);
        close = new ButtonDefault("Cerrar");
        ButtonOverflow.ButtonOption option1 = new ButtonOverflow.ButtonOption("Actualizar Estado", "#EF9D30",home+ "ic_status.svg", this.eventUpdateStatus);
        buttonOverflow = new ButtonOverflow(option1);
    }

    private InputText getInstance() {
        InputText input = new InputText("campo vació", 300) {
            @Override
            public void setText(String t) {
                if (t == null || t.isEmpty()) {
                    t = "";
                }
                super.setText(t);
            }
        };
        input.setEditable(false);
        return input;
    }

    @Override
    public void installComponent() {
        setLayout(new MigLayout("wrap 2,fillx,w 500:650,insets 10 15", "fill,grow 0,center"));
        add(folio, "grow");
        add(estado, "split 2");
        add(buttonOverflow);
        add(createDetailsNote(), "span,grow");
        add(new JSeparator(), "span,grow,gapx 10 10,gapy 5 5");
        add(Panel.createdSubTitle("Detalles del Cliente", 18f), "span,grow 0,al center,wrap 5");
        add(createPanelDetailsCustomer(), "span,grow,gapx 20 20");
        add(new JSeparator(), "span,grow,gapx 10 10,gapy 5 5");
        add(Panel.createdSubTitle("Dispositivos", 18f), "span,grow 0,al center,wrap 5");
        add(createPanelDevices(), "span,growx");
        add(new JSeparator(), "span,grow,gapx 10 10,gapy 5 5");
        add(close, "span,grow 0,al trail");
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

    private JComponent createDetailsNote() {
        JPanel panel = new JPanel(new MigLayout("wrap 3,fillx,insets 5", "", ""));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Fecha:"), "grow 0,split 2");
        panel.add(fecha);
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Día:"), "grow 0,split 2");
        panel.add(dia);
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Hora:"), "grow 0,split 2");
        panel.add(hora);
        panel.add(LabelTextArea.ForNote.getLabelGramatical("Empleado:"), "span,grow 0,split 2");
        panel.add(empleado, "grow");
        return panel;
    }

    private JComponent createPanelDetailsCustomer() {
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

    public void setValue() {
        id.setText(nota.getCliente().getId().toString());
        nombre.setText(nota.getCliente().getNombre());
        telefono_movil.setText(nota.getCliente().getTelefono_movil());
        telefono_fijo.setText(nota.getCliente().getTelefono_fijo());
        inputDomicilio.setText(nota.getCliente().getDireccion());

        folio.setText(String.format("Folio: %s", nota.getFolio()));
        estado.setText(nota.getEstado().getValue());
        fecha.setText(DateFull.getDateOnly(nota.getFecha_registro()));
        dia.setText(DateFull.getWeekOnly(nota.getFecha_registro()));
        hora.setText(DateFull.getHourOnly(nota.getFecha_registro()));
        empleado.setText(String.format("ID: %d | %s %s", nota.getEmpleado().getId(), nota.getEmpleado().getNombre(), nota.getEmpleado().getApellido()));
        containerCards.addItemsAll(this.nota.getDispositivos());
    }

    public void setEstado(){
        String color = this.nota.getEstado().getBackgroundStatus();
        estado.putClientProperty(FlatClientProperties.STYLE,
                "arc:33;"
                        + "border: 8,20,8,20,shade(" + color + ",3%);"
                        + "foreground:shade(" + color + ",3%);"
                        + "background:fade(" + color + ",8%);");
        estado.setText(this.nota.getEstado().getValue());
    }

}
