package bumh3r.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatScrollPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class TableSimple<T> extends JPanel {
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private String[] columnNames;

    public TableSimple(String[] columnNames) {
        this.columnNames = columnNames;
        initComponentsTable();
        initTable();
    }

    private void initComponentsTable() {
        table = new FlatTable();
        scrollPane = new FlatScrollPane();
        model = new DefaultTableModel(columnNames, 0) {
            boolean[] canEdit = new boolean[columnNames.length];

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }

    public void installParentScroll(JPanel parent) {
        scrollPane.addMouseWheelListener(e -> {
            e.consume();
            MouseWheelEvent newEvent = new MouseWheelEvent(
                    parent,
                    e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    e.getX(), e.getY(),
                    e.getClickCount(),
                    e.isPopupTrigger(),
                    e.getScrollType(),
                    e.getScrollAmount(),
                    e.getWheelRotation()
            );
            parent.dispatchEvent(newEvent);
        });
    }

    private void initTable() {
        setLayout(new MigLayout("wrap,fillx,insets n", "fill", "[fill,grow]"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:16;"
                + "background:$Table.background");

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        table.setModel(model);

        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }

        // Center Data
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(defaultTableCellRenderer);
        }

        // Styles
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

    public void addAll(List<T> data, Function<T, Object[]> mapper) {
        cleanData();
        for (T item : data) {
            model.addRow(mapper.apply(item));
        }
    }

    public void addOne(T data, Function<T, Object[]> mapper) {
        model.addRow(mapper.apply(data));
    }


    public void cleanData() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }
}