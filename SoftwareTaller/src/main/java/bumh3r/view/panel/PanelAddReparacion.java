package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardRepair;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.utils.fonts.FontPublicaSans;
import bumh3r.model.Empleado;
import bumh3r.model.Reparacion;
import bumh3r.request.ReparacionRequest;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Dimension;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class PanelAddReparacion extends Panel {
    private ContainerCards<ReparacionRequest> containerCards;
    private ButtonDefault buttonAddRepair;
    @Getter
    private FlatComboBox<Object> categoria, tecnico;
    private InputText descripcion, reparacion;
    private InputFormattedDecimal inputPrecio, inputAbono;

    public void installEventAddRepair(Runnable event) {
        buttonAddRepair.addActionListener(e -> event.run());
    }

    public void installEventDeleteCardRepair(BiConsumer<ReparacionRequest, Runnable> event) {
        containerCards.installDependent1(event);
    }

    public PanelAddReparacion() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardRepair.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.CENTER, new Dimension(350, -1), 10, 10));
        containerCards.setLongitud(1000);
        buttonAddRepair = new ButtonDefault("Agregar Reparación");
        categoria = new FlatComboBox<>();
        categoria.addItem("Seleccione una categoría");
        for (Reparacion.CategoriaReparacion item : Reparacion.CategoriaReparacion.values()) {
            categoria.addItem(item);
        }

        tecnico = new FlatComboBox<>();
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

    public ReparacionRequest getValue() {
        Reparacion.CategoriaReparacion categoria = this.categoria.getSelectedItem() instanceof Reparacion.CategoriaReparacion ? (Reparacion.CategoriaReparacion) this.categoria.getSelectedItem() : null;
        String reparacion = !this.reparacion.getText().isEmpty() ? this.reparacion.getText().strip() : null;
        String descripcion = !this.descripcion.getText().isEmpty() ? this.descripcion.getText().strip() : null;
        Float precio = inputPrecio.getValue() == null ? 0.0f : Float.parseFloat(inputPrecio.getValue().toString());
        Float abono = inputAbono.getValue() == null ? 0.0f : Float.parseFloat(inputAbono.getValue().toString());
        Empleado empleado = tecnico.getSelectedItem() instanceof Empleado ? (Empleado) tecnico.getSelectedItem() : null;
        return new ReparacionRequest(reparacion, categoria, descripcion, precio, abono, empleado);
    }

    public List<ReparacionRequest> getRepairs() {
        return containerCards.getListItems();
    }

    public void addCardOne(ReparacionRequest reparacion) {
        containerCards.addItemOne(reparacion);
    }

    public void deleteCardOne(ReparacionRequest reparacion) {
        containerCards.delete(reparacion);
    }

    public void setTechnicianModel(List<Empleado> list) {
        tecnico.removeAllItems();
        tecnico.addItem("Seleccione un técnico");
        for (Empleado empleado : list) {
            tecnico.addItem(empleado);
        }
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            categoria.setSelectedIndex(0);
            reparacion.setText("");
            descripcion.setText("");
            inputPrecio.setValue(null);
            inputAbono.setValue(null);
            tecnico.setSelectedIndex(0);
        });
    }

    public void cleanCards() {
        SwingUtilities.invokeLater(containerCards::cleanCards);
    }
}
