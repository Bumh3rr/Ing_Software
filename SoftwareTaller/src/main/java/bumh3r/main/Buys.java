package bumh3r.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Buys extends JFrame {

    private JComboBox<String> notaComboBox;
    private JTable reparacionesTable, inventarioTable, carritoTable;
    private DefaultTableModel reparacionesModel, inventarioModel, carritoModel;
    private JTextField descuentoField, totalField;
    private JComboBox<String> metodoPagoComboBox;

    public Buys() {
        setTitle("Modulo de Ventas - Taller de Celulares");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior para seleccionar nota
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel notaLabel = new JLabel("Seleccionar Nota: ");
        notaComboBox = new JComboBox<>(new String[]{"Nota 001", "Nota 002", "Nota 003"});
        topPanel.add(notaLabel);
        topPanel.add(notaComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Panel central con tablas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);

        // Panel Izquierdo - Reparaciones y Inventario
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));

        reparacionesModel = new DefaultTableModel(new Object[]{"Reparación", "Precio", "Abono", "Estado"}, 0);
        reparacionesTable = new JTable(reparacionesModel);
        JScrollPane reparacionesScroll = new JScrollPane(reparacionesTable);
        leftPanel.add(new JLabel("Reparaciones"));
        leftPanel.add(reparacionesScroll);

        inventarioModel = new DefaultTableModel(new Object[]{"Producto", "Precio", "Cantidad Disponible"}, 0);
        inventarioTable = new JTable(inventarioModel);
        JScrollPane inventarioScroll = new JScrollPane(inventarioTable);
        leftPanel.add(new JLabel("Inventario"));
        leftPanel.add(inventarioScroll);

        splitPane.setLeftComponent(leftPanel);

        // Panel Derecho - Carrito y Pago
        JPanel rightPanel = new JPanel(new BorderLayout());
        carritoModel = new DefaultTableModel(new Object[]{"Descripción", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        carritoTable = new JTable(carritoModel);
        JScrollPane carritoScroll = new JScrollPane(carritoTable);
        rightPanel.add(new JLabel("Carrito de Ventas"), BorderLayout.NORTH);
        rightPanel.add(carritoScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        bottomPanel.add(new JLabel("Descuento (%):"));
        descuentoField = new JTextField();
        bottomPanel.add(descuentoField);

        bottomPanel.add(new JLabel("Método de Pago:"));
        metodoPagoComboBox = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
        bottomPanel.add(metodoPagoComboBox);

        bottomPanel.add(new JLabel("Total:"));
        totalField = new JTextField();
        totalField.setEditable(false);
        bottomPanel.add(totalField);

        JButton finalizarButton = new JButton("Finalizar Venta");
        bottomPanel.add(finalizarButton);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Buys::new);
    }
}
