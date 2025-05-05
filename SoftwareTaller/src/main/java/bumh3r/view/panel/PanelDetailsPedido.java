package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.label.LabelForDescription;
import bumh3r.components.label.LabelTextArea;
import bumh3r.model.DetallesPedido;
import bumh3r.model.Pedido;
import bumh3r.model.Proveedor;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatScrollPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class PanelDetailsPedido extends Panel {
    private LabelForDescription description;
    private JTextArea descripcionText;
    private Table table;
    private LabelTextArea fecha, proveedor;
    private FlatComboBox<Pedido.Estado> status;
    private ButtonDefault update;
    private Pedido pedido;

    public PanelDetailsPedido(Pedido pedido) {
        this.pedido = pedido;
        initComponents();
        init();
    }

    private void initComponents() {
        table = new Table();
        update = new ButtonDefault("Actualizar Estado");
        description = new LabelForDescription("En este apartado podrás visualizar los detalles del pedido seleccionado.");
        fecha = new LabelTextArea(DateFull.getDateFull(pedido.getFecha()));
        proveedor = new LabelTextArea(String.format(" ID: %d | %s", pedido.getProveedor().getId(), pedido.getProveedor().getNombre()));
        descripcionText = new JTextArea();
        descripcionText.setText(pedido.getObservaciones());
        descripcionText.setDocument(new LimitTextDocument(250));
        descripcionText.setLineWrap(true);
        descripcionText.setWrapStyleWord(true);
        status = new FlatComboBox<>();
        status.setModel(new DefaultComboBoxModel<>(Pedido.Estado.values()));
        status.setSelectedItem(Pedido.Estado.valueOf(pedido.getEstado().name()));
        table.setData(pedido.getDetallesPedidos());
    }


    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 2 45 20 45,w 350:460", "fill,grow"));
        add(description, "gapy 0 5");

        add(createdGramaticalP("Fecha de Pedido"));
        add(fecha);
        add(createdGramaticalP("Proveedor"));
        add(proveedor);
        add(createdGramaticalP("Estado"));
        add(status,"grow,split 2");
        add(update,"grow 0");

        add(createdGramaticalP("Observaciones"));
        add(createInputObservaciones(), "grow,push,wrap");

        add(table, "h 250!,gapy 5 0");

    }

    private JComponent createInputObservaciones() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 1"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        descripcionText.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        panel.add(descripcionText, "grow,push");
        return panel;
    }

    public static class Table extends JPanel {

        private JTable table;
        private JScrollPane scrollPane;
        private DefaultTableModel model;

        private String[] columnNames = {"ID", "Producto", "Cantidad"};

        public Table() {
            initComponentsTable();
            initTable();
        }

        private void initComponentsTable() {
            table = new FlatTable();
            scrollPane = new FlatScrollPane();
            model = new DefaultTableModel(columnNames, 0) {
                boolean[] canEdit = new boolean[]{
                        false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            };
        }

        private void initTable() {
            setLayout(new MigLayout("wrap,fillx,insets 2 n n n", "fill"));
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:16;"
                    + "background:$Table.background");

            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            table.setModel(model);

            //Size fields
            table.getColumnModel().getColumn(0).setMaxWidth(70);
            table.getColumnModel().getColumn(1).setPreferredWidth(110);
            table.getColumnModel().getColumn(2).setPreferredWidth(75);

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
            add(scrollPane);
            updateUI();
            revalidate();
        }

        public void setData(LinkedList<DetallesPedido> detallesPedidos) {
            cleanData();
            for (DetallesPedido detallesPedido : detallesPedidos) {
                model.addRow(detallesPedido.toArray());
            }
        }

        public void cleanData() {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
        }

    }

}
