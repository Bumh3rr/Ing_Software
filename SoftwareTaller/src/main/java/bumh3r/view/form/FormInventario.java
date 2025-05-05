package bumh3r.view.form;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
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
    private Table table;
    private ButtonAccentBase buttonSearch;
    private InputText search;

    @Override
    public void formInit() {
        Proveedor proveedor = new Proveedor(1L, "Proveedor 1", "1234567890", "example@gmail.com", "Dirección 1", "2021-09-01");
        Proveedor proveedor2 =new Proveedor(2L, "Proveedor 2", "1234567890", "example@gmail.com","Dirección 2", "2021-09-01");
        Proveedor proveedor3 =new Proveedor(3L, "Proveedor 3", "1234567890", "example@gmail.com","Dirección 3", "2021-09-01");

        List<Refaccion> refacciones = new ArrayList<>();
        refacciones.add(new Refaccion(1L, "Refacción 1", "description","Display", 10, 100.0, 150.0, LocalDate.now(), proveedor));
        refacciones.add(new Refaccion(2L, "Refacción 2", "description","Display", 10, 100.0, 150.0, LocalDate.now(), proveedor2));
        refacciones.add(new Refaccion(3L, "Refacción 3", "description","Display", 10, 100.0, 150.0, LocalDate.now(), proveedor3));
        table.setData(refacciones);
    }

    public FormInventario() {
        initComponents();
        initListeners();
        init();
    }

    private void initListeners() {
        buttonAddProducto.addActionListener((e) -> PanelsInstances.getInstance().showPanelAddRefaccion());
    }

    private void initComponents() {
        search = new InputText("Buscar refacción ...",200).setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonAccentBase("Buscar");
        buttonAddProducto = new ButtonDefault("Agregar Refacción");
        table = new Table();
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

    public static class Table extends JPanel {

        private JTable table;
        private JScrollPane scrollPane;
        private DefaultTableModel model;

        private String[] columnNames = {"ID", "Nombre", "Descripción","Categoría", "Stock", "Precio venta", "Precio compra", "Fecha de Registro", "Nombre del Proveedor"};

        public Table() {
            initComponentsTable();
            initListenersTable();
            initTable();
        }

        private void initComponentsTable() {
            table = new FlatTable();
            scrollPane = new FlatScrollPane();
            model = new DefaultTableModel(columnNames, 0) {
                boolean[] canEdit = new boolean[]{
                        false, false, false, false, false, false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
        }

        private void initListenersTable() {
        }

        private void initTable() {
            setLayout(new MigLayout("wrap,fillx,insets n", "fill", "[]-1[fill,grow]"));
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:16;"
                    + "background:$Table.background");

            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            table.setModel(model);

            //Center Data
            DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
            defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(defaultTableCellRenderer);
            }

            //Styles
            table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                    + "height:30;"
                    + "hoverBackground:null;"
                    + "pressedBackground:fade($Component.accentColor,5%);"
                    + "separatorColor:$TableHeader.background;");
            table.putClientProperty(FlatClientProperties.STYLE, ""
                    + "rowHeight:40;"
                    + "showHorizontalLines:true;"
                    + "intercellSpacing:0,1;"
                    + "selectionArc:20;"
                    + "cellFocusColor:$TableHeader.hoverBackground;"
                    + "selectionBackground:fade($Component.accentColor,10%);"
                    + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                    + "selectionForeground:$Table.foreground;");

            scrollPane.setViewportView(table);

            add(new JSeparator(), "grow 1");
            add(scrollPane);
            updateUI();
            revalidate();
        }

        public void setData(List<Refaccion> refacciones) {
            for (Refaccion refaccion : refacciones) {
                model.addRow(refaccion.toArray());
            }
        }

        public void cleanData() {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
        }

    }
}

