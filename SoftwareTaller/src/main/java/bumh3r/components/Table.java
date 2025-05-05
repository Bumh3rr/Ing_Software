package bumh3r.components;

import bumh3r.components.button.ButtonDefault;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatScrollPane;
import com.formdev.flatlaf.extras.components.FlatTable;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

public class Table<T> extends JPanel {
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private String[] columnNames;
    @Setter
    private Function<T, Void> rowClickListener;
    private List<T> dataList;
    @Setter
    private String nameAccion = "Acción";

    public Table(String[] columnNames) {
        this.columnNames = columnNames;
        this.dataList = new ArrayList<>();
        initComponentsTable();
        initTable();
    }

    private void initComponentsTable() {
        table = new FlatTable();
        scrollPane = new FlatScrollPane();

        String[] columnsWithAction = new String[columnNames.length + 1];
        System.arraycopy(columnNames, 0, columnsWithAction, 0, columnNames.length);
        columnsWithAction[columnNames.length] = "Acción";

        model = new DefaultTableModel(columnsWithAction, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columnsWithAction.length - 1;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return (column == columnsWithAction.length - 1) ? JButton.class : Object.class;
            }
        };
    }

    private void initTable() {
        setLayout(new MigLayout("wrap,fill,insets n", "fill","grow 0"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:16;"
                + "background:$Table.background");

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        table.setModel(model);

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new ButtonEditor(new ButtonDefault()));

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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row != -1 && column != table.getColumnCount() - 1 && rowClickListener != null) {
                    T item = dataList.get(row);
                    rowClickListener.apply(item);
                }
            }
        });
    }

    public void setData(LinkedList<T> data, Function<T, Object[]> dataMapper) {
        cleanData();
        dataList.clear();
        for (T item : data) {
            Object[] rowData = dataMapper.apply(item);
            Object[] rowWithButton = new Object[rowData.length + 1];
            System.arraycopy(rowData, 0, rowWithButton, 0, rowData.length);
            // Set "View" as button text
            rowWithButton[rowData.length] = nameAccion;
            model.addRow(rowWithButton);
            dataList.add(item);
        }
    }

    public void cleanData() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;

        public ButtonEditor(JButton button) {
            super(new JCheckBox());
            this.button = button;
            this.button.setOpaque(true);
            this.button.addActionListener((x) -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.label = value.toString();
            this.button.setText(label);
            this.currentRow = row;
            this.isPushed = true;
            return this.button;
        }

        @Override
        public Object getCellEditorValue() {
            if (this.isPushed && rowClickListener != null && this.currentRow < dataList.size()) {
                T item = dataList.get(currentRow);
                rowClickListener.apply(item);
            }
            this.isPushed = false;
            return this.label;
        }

        @Override
        public boolean stopCellEditing() {
            this.isPushed = false;
            return super.stopCellEditing();
        }
    }
}