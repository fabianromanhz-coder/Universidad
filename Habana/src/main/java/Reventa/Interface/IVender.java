package Reventa.Interface;

import Reventa.DB.GestorDB;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IVender extends javax.swing.JDialog {
    private JScrollPane scroll;
    private String[] column = {"Producto", "Stock anterior", "StockActual"};
    DefaultTableModel model = new DefaultTableModel(column, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            int columnaAModificar = table.getColumnModel().getColumnIndex("StockActual");

            return column == columnaAModificar;
        }
    };
    private JTable table;
    private JButton btnAtras;
    private JButton btnAceptar;

    public IVender(Frame parent, boolean modal, DefaultTableModel modelProductos) {
        super(parent, modal);

        scroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable(model);
        btnAtras = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        scroll.setViewportView(table);

        //Llenamos la tabla con los nombres de los productos y sus stocks anteriores
        LLenarTabla();

        //Agg el editor a la tabla
        table.getColumnModel().getColumn(2).setCellEditor(Editor());

        btnAtras.setText("<-");
        btnAtras.addActionListener(e -> dispose());

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(Vender(modelProductos));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAceptar))
                                .addGap(13, 13, 13)
                                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAtras)
                                .addGap(52, 52, 52)
                                .addComponent(btnAceptar)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
        );

        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void LLenarTabla() {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM productos"
            );

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int stock = rs.getInt("stock");

                Object[] obj = {nombre, stock};
                model.addRow(obj);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar los datos desde la base de datos", "Warning", JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException(e);
        }
    }

    public DefaultCellEditor Editor() {
        return new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                JTextField campo = (JTextField) getComponent();
                String valorIngresado = campo.getText().trim();

                try {
                    int valor = Integer.parseInt(valorIngresado);

                    if (valor < 0) {
                        throw new NumberFormatException();
                    }

                    int row = table.getSelectedRow();

                    if (row > -1) {
                        try {
                            int column = table.getColumnModel().getColumnIndex("Stock anterior");
                            int stockAnterior = Integer.parseInt(table.getValueAt(row, column).toString());

                            if (stockAnterior < valor) {
                                throw new NumberFormatException();
                            }

                            campo.setBorder(new LineBorder(Color.GRAY, 1));
                            return super.stopCellEditing();

                        } catch (NumberFormatException e) {
                            throw new NumberFormatException(e.getMessage());
                        }
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    campo.setBorder(new LineBorder(Color.RED, 1));
                    campo.requestFocus();
                    return false;
                }
            }
        };
    }

    public ActionListener Vender(DefaultTableModel modelProducto) {
        return e -> {
            Connection conn;

            try {
                conn = GestorDB.getConn();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }

            for (int i = 0; i < table.getRowCount(); i++) {
                String nombre = String.valueOf(table.getValueAt(i, 0));
                int nuevoStock = Integer.parseInt(table.getValueAt(i, 2).toString());

                try {
                    PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE productos SET stock = ? WHERE nombre = ?"
                    );

                    stmt.setInt(1, nuevoStock);
                    stmt.setString(2, nombre);

                    stmt.executeUpdate();

                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }

            JOptionPane.showMessageDialog(null, "Cuadre exitoso");
            CargarTablaProducto(modelProducto);
            dispose();
        };
    }

    private void CargarTablaProducto(DefaultTableModel modelProducto) {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM productos"
            );

            ResultSet rs = stmt.executeQuery();

            modelProducto.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double pCompra = rs.getBigDecimal("pCompra").doubleValue();
                double pVenta = rs.getBigDecimal("pVenta").doubleValue();
                int stock = rs.getInt("stock");

                Object[] aux = {id, nombre, pCompra, pVenta, stock};

                modelProducto.addRow(aux);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}