package bumh3r.view.modal;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonAction;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputArea;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.modal.CustomModal;
import bumh3r.model.Dispositivo;
import bumh3r.model.Marcas;
import bumh3r.model.TypeDevice;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatCheckBox;
import com.formdev.flatlaf.icons.FlatHelpButtonIcon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.ModalDialog;

import static bumh3r.archive.PathResources.Icon.home;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelModalInfoDevice extends Panel {
    public static final String ID = PanelModalInfoDevice.class.getName();
    public static final String KEY = PanelModalInfoDevice.class.getName();

    private LabelTextArea.ForNote id, tipoDispositivo, marca, modelo, serial, precioTotal, abonos;
    private FlatCheckBox checkBoxChip, checkBoxSD, checkBoxCargador, checkBoxFunda, checkBoxOther;
    private JLabel label_fullNameDevice, label_icon;
    private ButtonAction button_edit;
    private JButton button_reparaciones;
    private LabelTextArea label_tipo;
    private InputArea description;
    private Dispositivo dispositivo;

    public PanelModalInfoDevice setData(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
        label_icon.setIcon(getIcon(TypeDevice.Type.TELEFONO, Marcas.TypePhone.APPLE));
        label_fullNameDevice.setText(toTitleCase(dispositivo.getModel()));
        label_tipo.setText(dispositivo.getType().getName());

        id.setText(String.valueOf(dispositivo.getId()));
        tipoDispositivo.setText(dispositivo.getType().getName());
        precioTotal.setText("$ 0.00");
        abonos.setText("$ 0.00");

        marca.setText(dispositivo.getBrand().getNombre_marca());
        modelo.setText(toTitleCase(dispositivo.getModel()));
        serial.setText(dispositivo.getSerial());
        description.setText(dispositivo.getDetalles().getObservaciones());

        int utils = dispositivo.getDetalles().getUtils();
        checkBoxChip.setSelected((utils & 1) != 0);
        checkBoxSD.setSelected((utils & 2) != 0);
        checkBoxCargador.setSelected((utils & 4) != 0);
        checkBoxFunda.setSelected((utils & 8) != 0);
        checkBoxOther.setSelected((utils & 16) != 0);

        return this;
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.split("\\s+");
        StringBuilder titleCase = new StringBuilder();

        for (String word : words) {
            if (word.length() > 1) {
                if (word.equalsIgnoreCase("iphone")) {
                    titleCase.append("iPhone");
                } else {
                    titleCase.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1).toLowerCase());
                }
            } else {
                titleCase.append(word.toUpperCase());
            }
            titleCase.append(" ");
        }
        return titleCase.toString().trim();
    }

    public PanelModalInfoDevice() {
        initComponents();
        initListeners();
        init();
    }

    private void initComponents() {
        label_fullNameDevice = new JLabel("Campo vació");
        label_fullNameDevice.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 30f));
        label_fullNameDevice.setBorder(BorderFactory.createEmptyBorder());

        label_tipo = new LabelTextArea("Campo vació");
        label_tipo.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 12f));
        label_tipo.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%);" +
                "background:null;" +
                "font:bold +2;");
        label_tipo.setBorder(BorderFactory.createEmptyBorder());

        label_icon = new JLabel();

        id = new LabelTextArea.ForNote();
        tipoDispositivo = new LabelTextArea.ForNote();
        marca = new LabelTextArea.ForNote();
        modelo = new LabelTextArea.ForNote();
        serial = new LabelTextArea.ForNote();
        description = new InputArea();

        checkBoxChip = getInstanceCheckBox("Chip");
        checkBoxSD = getInstanceCheckBox("SD");
        checkBoxCargador = getInstanceCheckBox("Cargador");
        checkBoxFunda = getInstanceCheckBox("Funda");
        checkBoxOther = getInstanceCheckBox("Otro");

        button_reparaciones = new ButtonDefault("Ver Reparaciones");
        precioTotal = new LabelTextArea.ForNote();
        abonos = new LabelTextArea.ForNote();
        button_edit = new ButtonAction("");
        button_edit.setContentAreaFilled(false);
        button_edit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private FlatCheckBox getInstanceCheckBox(String text) {
        FlatCheckBox checkBox = new FlatCheckBox();
        checkBox.setText(text);
        checkBox.setSelected(true);
        checkBox.setEnabled(false);
        checkBox.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;"
                + "disabledText:lighten(@foreground,10%);");
        checkBox.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13f));
        return checkBox;
    }


    private void initListeners() {
        button_reparaciones.addActionListener((e) -> {
            SwingUtilities.invokeLater(() -> {
                ModalDialog.pushModal(
                        CustomModal.builder()
                                .component(new PanelModalInfoReparacion().setValue(this.dispositivo.getListReparaciones()))
                                .icon(modal + "ic_repair.svg")
                                .buttonClose(false)
                                .title("Reparaciones del Dispositivo")
                                .consumer((x) -> ModalDialog.popModel(PanelModalInfoDevice.ID))
                                .ID(PanelModalInfoDevice.ID)
                                .build(),
                        PanelModalInfoDevice.ID);
            });
        });
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0,w 550!", "[fill]"));
        add(createHeader(), "al lead,grow 0");
        add(createBody(), "grow,push");
    }

    private JComponent createHeader() {
        JPanel header = new JPanel(new MigLayout("wrap 2,fillx,insets 5 20 5 0", "[grow 0,center]5[lead,grow 0]"));
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");
        header.add(label_icon, "span 1 2,,gapx 30");
        header.add(createNameDevice(), "span 1 2,growy 0");
        return header;
    }

    private JComponent createNameDevice() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,ins 0", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");
        panel.add(label_fullNameDevice);
        panel.add(label_tipo);
        return panel;
    }

    private JComponent createBody() {
        JPanel body = new JPanel(new MigLayout("wrap 2,fillx,insets 0 15", "[][grow 0]", "[][]"));
        body.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:40;"
                + "border:10,10,10,10;"
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");

        body.add(new LabelPublicaSans("Detalles del Dispositivo").type(FontPublicaSans.FontType.BOLD_BLACK).size(18f));
        body.add(button_edit);
        body.add(new JSeparator(), "span,grow");

        body.add(LabelTextArea.ForNote.getLabelGramatical("ID:"), "span,grow 0,gapx 15,split 2");
        body.add(id);
        body.add(LabelTextArea.ForNote.getLabelGramatical("Tipo de Dispositivo:"), "span,grow 0,gapx 15,split 2");
        body.add(tipoDispositivo);
        body.add(LabelTextArea.ForNote.getLabelGramatical("Marca:"), "span,grow 0,gapx 15,split 2");
        body.add(marca);
        body.add(LabelTextArea.ForNote.getLabelGramatical("Modelo:"), "span,grow 0,gapx 15,split 2");
        body.add(modelo);
        body.add(LabelTextArea.ForNote.getLabelGramatical("IMEI / SERIAL:"), "span,grow 0,gapx 15,split 2");
        body.add(serial);
        body.add(LabelTextArea.ForNote.getLabelGramatical("Utils:"), "span,grow 0,gapx 15,split 2");
        body.add(createCamposCheckBox(), "grow");
        body.add(LabelTextArea.ForNote.getLabelGramatical("Observaciones:"), "span,grow 0,gapx 10");
        body.add(description.createdInput(), "span,grow,gapx 10");

        body.add(new LabelPublicaSans("Reparaciones").type(FontPublicaSans.FontType.BOLD_BLACK).size(18f), "span,grow 0");
        body.add(button_reparaciones, "span,grow");

        body.add(new LabelPublicaSans("Presupuestos").type(FontPublicaSans.FontType.BOLD_BLACK).size(18f), "span,grow 0");
        body.add(LabelTextArea.ForNote.getLabelGramatical("Precio Total: "), "span,grow 0,gapx 15,split 2");
        body.add(precioTotal);
        body.add(LabelTextArea.ForNote.getLabelGramatical("Abonos: "), "span,grow 0,gapx 15,split 2");
        body.add(abonos);

        return body;
    }

    private JComponent createCamposCheckBox() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 0", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");
        panel.add(checkBoxChip);
        panel.add(checkBoxSD);
        panel.add(checkBoxCargador);
        panel.add(checkBoxFunda);
        panel.add(checkBoxOther);
        panel.add(new JButton(new FlatHelpButtonIcon()) {
            @Override
            public void updateUI() {
                putClientProperty(FlatClientProperties.STYLE, ""
                        + "border:0,4,0,0");
                setContentAreaFilled(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                super.updateUI();
            }

            @Override
            public void addActionListener(ActionListener l) {
                super.addActionListener((e) -> {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Ayuda");
                });
            }
        }, "grow 0,al trail");
        return panel;
    }


    private AvatarIcon getIcon(TypeDevice.Type type, Marcas.TypePhone marca) {
        switch (type) {
            case TELEFONO -> {
                switch (marca) {
                    case APPLE -> {
                        return createIcon("apple.png");
                    }
                    case SAMSUNG -> {
                        return createIcon("samsung.png");
                    }
                    default -> {
                        return createIcon(type.getUrlIcon());
                    }
                }
            }
            default -> {
                return createIcon(type.getUrlIcon());
            }
        }
    }

    private AvatarIcon createIcon(String url) {
        AvatarIcon icon = new AvatarIcon(PanelModalInfoDevice.class.getResource(PathResources.Img.typeDevices + url), 110, 110, 3.9f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);
        icon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#6d6d6d"), 0.1f));
        return icon;
    }

}
