package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.components.input.InputFormatterNumber;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextCP;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.New.CategoriaN;
import bumh3r.model.New.ProveedorN;
import bumh3r.request.RefaccionRequest;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class PanelAddRefaccion extends Panel {
    private JTextArea description;
    private InputText nombre, descripcion;
    @Getter
    private FlatComboBox<Object> category, proveedor;
    private InputFormatterNumber stock;
    private InputFormattedDecimal precio_venta, precio_compra;
    private ButtonDefault buttonAdd;

    public void installEventAddRefaccion(Runnable runnable) {
        buttonAdd.addActionListener(e -> runnable.run());
    }

    public PanelAddRefaccion() {
        initComponents();
        init();
    }

    private void initComponents() {
        nombre = new InputText("Ingrese Nombre de la refacción", 100);
        descripcion = new InputText("Ingrese la descripción (Opcional)", 100);
        stock = new InputFormatterNumber(5000);
        stock.setPlaceholderText("Ingrese el stock");
        precio_venta = new InputFormattedDecimal(5000);
        precio_compra = new InputFormattedDecimal(5000);
        category = new FlatComboBox<>();
        proveedor = new FlatComboBox<>();
        category.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione una categoría"}));
        proveedor.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione un Proveedor"}));
        buttonAdd = new ButtonDefault("Agregar");
        description = new JTextArea("En este apartado podrás registrar los datos de una nueva refacción, asegurate de ingresar los datos correctos.");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBorder(BorderFactory.createEmptyBorder());
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "background:null");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 450:550", "fill,grow"));
        add(description);
        add(createdSubTitle("Datos de la Refacción"), "grow 0,gapy 5 0,al center");
        add(createdGramaticalP("Nombre"), "gapy 10");
        add(nombre);
        add(createdGramaticalP("descripción"));
        add(descripcion);
        add(createdGramaticalP("categoría"));
        add(category);
        add(createdGramaticalP("Unidades en Stock"));
        add(stock);
        add(createdGramaticalP("Precio de Venta"));
        add(precio_venta);
        add(createdGramaticalP("Precio de Compra"));
        add(precio_compra);
        add(createdGramaticalP("Proveedor"));
        add(proveedor);

        add(buttonAdd, "grow 0,gapy 15,al trail");
    }

    private JComponent createdSubTitle(String str) {
        return new LabelPublicaSans(str).size(15f).type(FontPublicaSans.FontType.BOLD_BLACK);
    }

    public RefaccionRequest getValue() {
        String nombreValue = !this.nombre.getText().isEmpty() ? this.nombre.getText().strip() : null;
        String descripcionValue = !this.descripcion.getText().isEmpty() ? this.descripcion.getText().strip() : null;
        Integer stock = this.stock.getValue() != null ? Integer.valueOf(this.stock.getValue().toString()) : null;
        Float precio_venta = this.precio_venta.getValue() != null ? Float.valueOf(this.precio_venta.getValue().toString()) : 0.0f;
        Float precio_compra = this.precio_compra.getValue() != null ? Float.valueOf(this.precio_compra.getValue().toString()) : 0.0f;
        CategoriaN categoria = this.category.getSelectedItem() instanceof CategoriaN ? (CategoriaN) this.category.getSelectedItem() : null;
        ProveedorN proveedorValue = this.proveedor.getSelectedItem() instanceof ProveedorN ? (ProveedorN) this.proveedor.getSelectedItem() : null;
        return new RefaccionRequest(nombreValue, descripcionValue, stock, precio_venta, precio_compra, proveedorValue, categoria);
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            nombre.setText("");
            descripcion.setText("");
            stock.setValue(null);
            precio_venta.setValue(null);
            precio_compra.setValue(null);
            category.setSelectedIndex(0);
            proveedor.setSelectedIndex(0);
        });
    }

    public void setCategoryModel(List<CategoriaN> list) {
        SwingUtilities.invokeLater(() -> {
            this.category.removeAllItems();
            this.category.addItem("Seleccione una categoría");
            list.forEach((categoria) -> this.category.addItem(categoria));
        });
    }

    public void setProveedorModel(List<ProveedorN> list) {
        SwingUtilities.invokeLater(() -> {
            this.proveedor.removeAllItems();
            this.proveedor.addItem("Seleccione un Proveedor");
            list.forEach((proveedorN) -> this.proveedor.addItem(proveedorN));
        });
    }
}
