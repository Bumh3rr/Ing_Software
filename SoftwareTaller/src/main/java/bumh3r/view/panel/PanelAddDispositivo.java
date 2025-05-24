package bumh3r.view.panel;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputArea;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelTextArea;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.*;
import bumh3r.request.DispositivoRequest;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatCheckBox;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelAddDispositivo extends Panel {
    private JLabel precio, abono, sizeReparaciones;
    private FlatComboBox<Object> tipoDispositive, marca;
    private InputArea inputObservaciones;
    private InputText inputModelo, inputSerial;
    private FlatCheckBox checkBoxChip, checkBoxSD, checkBoxCargador, checkBoxFunda, checkBoxOther;
    private JButton button_cancel, buttonReparations, button_add;

    public void installEventAddDispositivo(Runnable event) {
        button_add.addActionListener(e -> event.run());
    }

    public void installEventAddReparaciones(Runnable event) {
        buttonReparations.addActionListener(e -> event.run());
    }

    public void installEventCancel(Runnable event) {
        button_cancel.addActionListener(e -> event.run());
    }

    public PanelAddDispositivo() {
        initComponents();
        init();
    }

    private void initComponents() {
        inputModelo = new InputText("Modelo", 50).setIcon(modal + "ic_phonemovil.svg");
        inputSerial = new InputText("IMEI del Dispositivo", 20).setIcon(modal + "ic_code.svg");
        inputObservaciones = new InputArea();
        tipoDispositive = new FlatComboBox<>();
        marca = new FlatComboBox<>();
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
        precio = new JLabel();
        abono = new JLabel();
        sizeReparaciones = new JLabel();
        button_cancel = new ButtonAccentBase("Cancelar", "#ff420a");
        buttonReparations = new ButtonAccentBase("Agregar Reparaciones", "@accentBaseColor").addStyles("arc:26;" + "margin:10,10,10,10;");
        button_add = new ButtonDefault("Agregar");
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets 5 25 0 25", "[grow 0,trail]15[fill,350:450]"));

        add(createTitle("Detalles del Dispositivo"), "span,grow 0,al center");
        add(getLabel("Tipo de Dispositivo:"));
        add(tipoDispositive, "growx");
        add(getLabel("Marca:"));
        add(marca, "growx");
        add(getLabel("Modelo:"));
        add(inputModelo);
        add(getLabel("IMEI/SERIAL:"));
        add(inputSerial);
        add(getLabel("Utils:"));
        add(createCamposCheckBox());
        add(getLabel("Observaciones:"));
        add(inputObservaciones.createdInput());

        add(new JSeparator(), "span,grow,gapx 10 10,gapy 3");
        add(createTitle("Reparación"), "span,gapy 1 3,grow 0,al center");
        add(getLabel("Reparaciones:"));
        add(buttonReparations);

        add(new JSeparator(), "span,grow,gapx 10 10,gapy 3");
        add(createTitle("Presupuesto del Dispositivo"), "span,gapy 1 3,grow 0,al center");
        add(getLabel("Total: $"));
        add(precio);
        add(getLabel("Abono: $"));
        add(abono);
        add(getLabel("Reparaciones:"));
        add(sizeReparaciones);
        add(Box.createHorizontalBox(), "span,gapy 10");
        add(button_cancel, "growx 0,al trail");
        add(button_add, "growx 0,al trail");
        add(Box.createHorizontalBox(), "span,gapy 7");
    }

    private JComponent createCamposCheckBox() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 0", "grow 0"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");
        panel.add(checkBoxChip);
        panel.add(checkBoxSD);
        panel.add(checkBoxCargador);
        panel.add(checkBoxFunda);
        panel.add(checkBoxOther);
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

    public void setPresupuesto(List<Reparacion_Dispositivo> list) {
        var sos = "<html><b>no hay reparaciones agregadas</b></html>";
        precio.setText(!list.isEmpty() ? InputFormattedDecimal.decimalFormat.format(list.stream().mapToDouble(Reparacion_Dispositivo::getPrecio).sum()) : sos);
        abono.setText(!list.isEmpty() ? InputFormattedDecimal.decimalFormat.format(list.stream().mapToDouble(Reparacion_Dispositivo::getAbono).sum()) : sos);
        sizeReparaciones.setText(!list.isEmpty() ? String.valueOf(list.size()) : sos);
    }

    public DispositivoRequest getValue() {
        String tipo = (String) tipoDispositive.getSelectedItem();
        String marca = (String) this.marca.getSelectedItem();
        String modelo = inputModelo.getText().isEmpty() ? null : inputModelo.getText().strip();
        String serial = inputSerial.getText().isEmpty() ? null : inputSerial.getText().strip();
        String observaciones = inputObservaciones.getText().isEmpty() ? null : inputObservaciones.getText().strip();
        int utils = 0;
        if (checkBoxChip.isSelected()) utils |= 1;      // Primer bit
        if (checkBoxSD.isSelected()) utils |= 2;      // Segundo bit
        if (checkBoxCargador.isSelected()) utils |= 4;      // Tercer bit
        if (checkBoxFunda.isSelected()) utils |= 8;      // Cuarto bit
        if (checkBoxOther.isSelected()) utils |= 16;      // Quinto bit
        return new DispositivoRequest(tipo, marca, modelo, serial, utils, observaciones);
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            tipoDispositive.setSelectedIndex(0);
            marca.setSelectedIndex(0);
            inputModelo.setText("");
            inputSerial.setText("");
            inputObservaciones.setText("");
            checkBoxChip.setSelected(false);
            checkBoxSD.setSelected(false);
            checkBoxCargador.setSelected(false);
            checkBoxFunda.setSelected(false);
            checkBoxOther.setSelected(false);
        });
    }
}
