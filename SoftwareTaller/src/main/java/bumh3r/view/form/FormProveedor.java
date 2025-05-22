package bumh3r.view.form;

import bumh3r.components.TableSimple;
import bumh3r.components.button.ButtonDefault;
import bumh3r.controller.ControladorProveedor;
import bumh3r.model.New.ProveedorN;
import bumh3r.model.Proveedor;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatScrollPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import java.util.List;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class FormProveedor extends Form {
    private ButtonDefault buttonAddProveedor;
    @Getter
    private TableSimple<ProveedorN> table;
    private final Function<ProveedorN, Object[]> dataMapper = usuarioMapper -> new Object[]{
            usuarioMapper.getId(),
            usuarioMapper.getNombre(),
            usuarioMapper.getTelefono(),
            usuarioMapper.getCorreo(),
            usuarioMapper.getDireccion(),
            DateFull.getDateFull(usuarioMapper.getFecha_registro())
    };

    @Override
    public void installController() {
        new ControladorProveedor(this);
    }

    @Override
    public void formInit() {
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        PoolThreads.getInstance().execute(getEventFormRefresh());
    }

    public void installEventAddCliente(Runnable event) {
        buttonAddProveedor.addActionListener(e -> event.run());
    }

    public FormProveedor() {
        initComponents();
        init();
    }

    private void initComponents() {
        buttonAddProveedor = new ButtonDefault("Agregar Proveedor");
        table = new TableSimple<>(new String[]{"ID", "Nombre", "Teléfono", "Correo", "Dirección", "Fecha de Registro"});
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Proveedores", "En el apartado de proveedores puedes gestionar tus proveedores, las cuales son agregar", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]", "[][fill]"));
        panel.add(buttonAddProveedor, "grow 0,al trail");
        panel.add(table, "grow,push,gapx 0 2");
        return panel;
    }

    public void addAllTable(List<ProveedorN> list) {
        SwingUtilities.invokeLater(() -> table.setDataAll(list, dataMapper));
    }

    public void addOneTable(ProveedorN proveedor) {
        SwingUtilities.invokeLater(() -> table.setDataOne(proveedor, dataMapper));
    }


    public static class Table extends JPanel {

        private JTable table;
        private JScrollPane scrollPane;
        private DefaultTableModel model;

        private String[] columnNames = {"ID", "Nombre", "Teléfono", "Correo", "Dirección", "Fecha de Registro"};

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
                        false, false, false, false, false, false
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

            //Size fields
            table.getColumnModel().getColumn(0).setMaxWidth(70);
            table.getColumnModel().getColumn(1).setPreferredWidth(90);
            table.getColumnModel().getColumn(2).setPreferredWidth(95);
            table.getColumnModel().getColumn(3).setPreferredWidth(112);
            table.getColumnModel().getColumn(4).setPreferredWidth(210);
            table.getColumnModel().getColumn(5).setPreferredWidth(209);

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

        public void setData(List<Proveedor> proveedors) {
            for (Proveedor proveedor : proveedors) {
                model.addRow(proveedor.toArray());
            }
        }

        public void cleanData() {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
        }

    }
}
