package bumh3r.view.form;

import bumh3r.components.TableSimple;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.controller.ControladorInventario;
import bumh3r.model.New.RefaccionN;
import bumh3r.model.Proveedor;
import bumh3r.model.Refaccion;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatScrollPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import static bumh3r.archive.PathResources.Icon.modal;

public class FormInventario extends Form {
    private ButtonDefault buttonAddProducto;
    private TableSimple<RefaccionN> table;
    private ButtonAccentBase buttonSearch;
    private InputText search;

    @Override
    public void installController() {
        new ControladorInventario(this);
    }

    public FormInventario() {
        initComponents();
        init();
    }

    private void initComponents() {
        search = new InputText("Buscar refacción ...",200).setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonAccentBase("Buscar");
        buttonAddProducto = new ButtonDefault("Agregar Refacción");
        table = new TableSimple<>(new String[]{"ID", "Nombre", "Descripción","Categoría", "Stock", "Precio venta", "Precio compra", "Fecha de Registro", "Nombre del Proveedor"});
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Inventario", "En este apartado podrás registrar los producto de tu proveedor", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2", "grow", "[][fill]"));
        panel.add(search, "w 200!,grow 0,al lead,split 2");
        panel.add(buttonSearch, "grow 0");
        panel.add(buttonAddProducto, "grow 0,al trail");
        panel.add(table, "span,grow,push,gapy 5 0,gapx 0 2");
        return panel;
    }

    public void installEventShowPanelAddRefaccion(Runnable runnable) {
        buttonAddProducto.addActionListener(e -> runnable.run());
    }
}

