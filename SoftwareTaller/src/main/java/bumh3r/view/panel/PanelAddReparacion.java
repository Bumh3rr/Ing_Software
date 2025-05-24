package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardRepair;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.*;
import bumh3r.model.New.ReparacionN;
import bumh3r.request.ReparacionRequest;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Dimension;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class PanelAddReparacion extends Panel {
    private ContainerCards<ReparacionN> containerCards;
    private ButtonDefault buttonAddRepair;
    private FlatComboBox<Object> categoria, tecnico;
    private InputText descripcion,reparacion;
    private InputFormattedDecimal inputPrecio;
    private InputFormattedDecimal inputAbono;

    public PanelAddReparacion() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardRepair.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.CENTER, new Dimension(350, -1), 10, 10));
        buttonAddRepair = new ButtonDefault("Agregar Reparación");
        categoria = new FlatComboBox<>();
        categoria.setMaximumRowCount(6);
        tecnico = new FlatComboBox<>();
        tecnico.setMaximumRowCount(6);
        descripcion = new InputText(100);
        reparacion = new InputText(50);
        inputAbono = new InputFormattedDecimal(50000.00f);
        inputPrecio = new InputFormattedDecimal(50000.00f);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0,width 650:900", "[fill]", "[]20[]"));
        add(containerCards, "h 250!");
        add(createInput());
    }

    private JComponent createInput() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx,insets 0 20 20 20", "[grow 0,trail]15[fill,150:170]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        panel.add(new JSeparator(), "span,grow");
        panel.add(new LabelPublicaSans("Nueva Reparación").type(FontPublicaSans.FontType.BOLD_BLACK).size(16.5f), "span,grow 0,gapy 5 5,al center");
        panel.add(getLabel("Categoría:"));
        panel.add(categoria);
        panel.add(getLabel("Reparación:"));
        panel.add(reparacion);
        panel.add(getLabel("Técnico Encargado:"));
        panel.add(tecnico);
        panel.add(getLabel("Descripción (opcional):"));
        panel.add(descripcion);
        panel.add(getLabel("Precio:"));
        panel.add(inputPrecio, "split 3");
        panel.add(getLabel("Anticipo:"), "grow 0");
        panel.add(inputAbono);
        panel.add(buttonAddRepair, "span,w 250!,gapy 5,al center");

        panel.updateUI();
        return panel;
    }

    private JComponent getLabel(String text) {
        JLabel label = new JLabel(text, JLabel.TRAILING);
        label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD, 13.2f));
        return label;
    }

    private ReparacionRequest getReparacion() {
        String categoria = (String) this.categoria.getSelectedItem();
        String reparacion = !this.reparacion.getText().isEmpty() ? this.reparacion.getText().strip() : null;
        String descripcion = !this.descripcion.getText().isEmpty() ? this.descripcion.getText().strip() : null;
        Float precio = inputPrecio.getValue() == null ? 0.0f : Float.parseFloat(inputPrecio.getValue().toString());
        Float abono = inputAbono.getValue() == null ? 0.0f : Float.parseFloat(inputAbono.getValue().toString());
        Empleado empleado = tecnico.getSelectedItem() instanceof Empleado ? (Empleado) tecnico.getSelectedItem() : null;
        return new ReparacionRequest(tipoReparacion, categoria, reparacion, descripcion, precio, abono, empleado, status);
    }

}
