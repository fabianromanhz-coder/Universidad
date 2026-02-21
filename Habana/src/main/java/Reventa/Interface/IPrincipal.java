package Reventa.Interface;

import Reventa.DB.GestorDB;
import Reventa.ListasEnlazadas.ListaEnlazada;
import Reventa.Modelos.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class IPrincipal extends javax.swing.JFrame {

    private JScrollPane scrollProductosViaje;
    private String[] columnProductoViaje = {"ID", "Nombre", "PCompra", "PVenta", "Stock"};
    private DefaultTableModel modelProductoViaje = new DefaultTableModel(columnProductoViaje, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable tableProductosViaje;
    private JButton btnAggV;
    private JScrollPane scrollViajes;
    private String[] columnViaje = {"ID", "Fecha", "Destino", "Gastos"};
    private DefaultTableModel modelViajes  = new DefaultTableModel(columnViaje, 0) {
        @Override
        public boolean isCellEditable(int Row, int column) {
            return false;
        }
    };
    private JTable tableViajes;
    private JScrollPane scrollProductos;
    private String[] columnProducto = {"ID", "Nombre", "PCompra", "PVenta", "Stock"};
    private DefaultTableModel modelProducto = new DefaultTableModel(columnProducto, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable tableProductos;
    private JLabel txtViajes;
    private JLabel txtProductoViaje;
    private JLabel txtProducto;
    private JButton btnVender;
    private JButton btnSalir;
    private JLabel txtDineroInvertido;
    private JLabel txtDineroGanancias;
    private ListaEnlazada<Producto> lista = new ListaEnlazada<>();

    public IPrincipal() {
        initComponents();
    }

    private void initComponents() {

        scrollProductosViaje = new javax.swing.JScrollPane();
        tableProductosViaje = new javax.swing.JTable(modelProductoViaje);
        btnAggV = new javax.swing.JButton();
        scrollViajes = new javax.swing.JScrollPane();
        tableViajes = new javax.swing.JTable(modelViajes);
        scrollProductos = new javax.swing.JScrollPane();
        tableProductos = new javax.swing.JTable(modelProducto);
        txtViajes = new javax.swing.JLabel();
        txtProductoViaje = new javax.swing.JLabel();
        txtProducto = new javax.swing.JLabel();
        btnVender = new javax.swing.JButton();
        btnSalir = new JButton();
        txtDineroInvertido = new JLabel();
        txtDineroGanancias = new JLabel();

        CargarTablas();

        btnAggV.setText("Agg Viaje");

        Map<String, DefaultTableModel> models = new HashMap<>();
        models.put("productoViaje", modelProductoViaje);
        models.put("viajes", modelViajes);
        models.put("producto", modelProducto);

        btnAggV.addActionListener(e -> new AggViaje(this, true, models));

        btnVender.setText("Vender");
        btnVender.addActionListener(e -> new IVender(null, true, modelProducto));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(e -> dispose());

        scrollProductosViaje.setViewportView(tableProductosViaje);
        scrollViajes.setViewportView(tableViajes);
        scrollProductos.setViewportView(tableProductos);

        txtViajes.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 24)); // NOI18N
        txtViajes.setText("Viajes");

        txtProductoViaje.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 24)); // NOI18N
        txtProductoViaje.setText("Producto viaje");

        txtProducto.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD | Font.ITALIC, 24)); // NOI18N
        txtProducto.setText("Productos");

        CalcularInversionPorProducto();
        txtDineroInvertido.setText("Dinero invertido: "+DineroTotalInvertido(0));

        txtDineroGanancias.setText("Dinero Ganancias: "+(DineroTotalGanancia(0)));

        tableViajes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CargarTablaProductoViaje();
            }
        });


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnAggV)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(btnSalir)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(btnVender))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(scrollProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtDineroInvertido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtDineroGanancias, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtViajes, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollViajes, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                                .addComponent(txtProductoViaje, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(24, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(scrollProductosViaje, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(txtDineroInvertido)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAggV)
                                        .addComponent(txtDineroGanancias))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVender)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addComponent(txtProducto))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSalir)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtViajes)
                                        .addComponent(txtProductoViaje))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollProductosViaje, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(scrollViajes, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void CargarTablas() {
        CargarTablaViajes();
        CargarTablaProducto();
    }

    private void CargarTablaViajes() {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM viajes"
            );

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                String destino = rs.getString("destino");
                double gastos = rs.getBigDecimal("gastos").doubleValue();

                Object[] aux = {id, fecha, destino, gastos};

                modelViajes.addRow(aux);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void CargarTablaProducto() {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM productos"
            );

            ResultSet rs = stmt.executeQuery();

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

    private void CargarTablaProductoViaje() {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT p.*\n" +
                            "FROM productos p \n" +
                            "INNER JOIN detalles_compra d ON p.id = d.id_producto\n" +
                            "INNER JOIN viajes v ON d.id_viaje = v.id\n" +
                            "WHERE v.id = ?"
            );

            stmt.setInt(1, BuscarIDTablaViajes());

            ResultSet rs = stmt.executeQuery();

            modelProductoViaje.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double pCompra = rs.getBigDecimal("pCompra").doubleValue();
                double pVenta = rs.getBigDecimal("pVenta").doubleValue();
                int stock = rs.getInt("stock");

                Object[] aux = {id, nombre, pCompra, pVenta, stock};

                modelProductoViaje.addRow(aux);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int BuscarIDTablaViajes() {
        int row = tableViajes.getSelectedRow();

        if (row > -1) {
            int column = tableViajes.getColumnModel().getColumnIndex("ID");

            return Integer.parseInt(tableViajes.getValueAt(row, column).toString());
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un viaje de la tabla", "Warning", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public void CalcularInversionPorProducto() {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmtTienda = conn.prepareStatement(
                    "SELECT * FROM productos"
            );

            ResultSet rs = stmtTienda.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                double precioC = Double.parseDouble(rs.getBigDecimal("pCompra").toString());
                double precioV = Double.parseDouble(rs.getBigDecimal("pVenta").toString());
                int stock = rs.getInt("stock");

                lista.add(new Producto(nombre, precioC, precioV, stock));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double DineroTotalInvertido(int i) {
        if (i >= lista.getSize()) {
            return 0.0;
        }

        return (lista.get(i).getpCompra() * lista.get(i).getStock()) + DineroTotalInvertido(i+1);
    }

    public Double DineroTotalGanancia(int i) {
        if (i >= lista.getSize()) {
            return 0.0;
        }

        return (lista.get(i).getpVenta() * lista.get(i).getStock()) + DineroTotalGanancia(i+1);
    }
}