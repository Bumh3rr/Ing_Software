package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputFormatterNumber;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextCP;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

public class PanelAddRefaccion extends Panel {
    private JTextArea description;
    private InputText nombre, descripcion, precio_venta, precio_compra;
    private FlatComboBox category, proveedor;
    private InputFormatterNumber stock;
    private ButtonDefault buttonAdd;

    public PanelAddRefaccion() {
        initComponents();
        init();
    }

    private void initComponents() {
        nombre = new InputText("Ingrese Nombre de la refacción",100);
        descripcion = new InputText("Ingrese la descripción (Opcional)",100);
        stock = new InputFormatterNumber(10000);
        stock.setPlaceholderText("Ingrese el stock");
        precio_venta = new InputText("Ingrese el precio venta",13);
        precio_compra = new InputText("Ingrese el precio compra",45);

        category = new FlatComboBox();
        proveedor = new FlatComboBox();
        proveedor.setModel(new DefaultComboBoxModel(new String[]{"Seleccione el proveedor"}));
        category.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione la categoría"}));

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

    public void installEventAddRefaccion(Runnable runnable) {
        buttonAdd.addActionListener(e -> runnable.run());
    }

//    public RefaccionRequest getValue(){
//        String nombreValue = !this.nombre.getText().isEmpty() ? this.nombre.getText().strip() : null;
//        String descripcionValue = !this.descripcion.getText().isEmpty() ? this.descripcion.getText().strip() : null;
//        Integer stock = !this.stock.getText().isEmpty() ? Integer.valueOf(this.stock.getText().strip()) : null;
//
//        String telefono_movil = this.phone_movil.getValue() != null ? this.phone_movil.getValue().toString() : null;
//        String telefono_fijo = this.phone_fijo.getValue() != null ? this.phone_fijo.getValue().toString() : null;
//        String direccionValue = !this.address.getText().isEmpty() ? this.address.getText().strip() : null;
//
//        return
//    }
}
