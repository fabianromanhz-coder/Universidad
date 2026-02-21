package Reventa.Interface;

import Reventa.DB.GestorDB;
import Reventa.Modelos.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggProducto extends JDialog {
    private JScrollPane scroll;
    private JTable table;
    private JButton btnAtras;
    private JLabel txtNombre;
    private JTextField nombre;
    private JLabel txtPCompra;
    private JLabel txtPVenta;
    private JLabel txtStock;
    private JTextField pCompra;
    private JTextField pVenta;
    private JTextField stock;
    private JButton btnAgregar;
    private JButton btnTerminar;
    private String[] column = {"Nombre", "PCompra", "PVenta", "Stock"};
    private DefaultTableModel model = new DefaultTableModel(column, 0);
    private List<Producto> lista = new ArrayList<>();
    private Map<String, DefaultTableModel> models;


    public AggProducto(Frame parent, boolean modal, int idViaje, Map<String, DefaultTableModel> models) {
        super(parent, modal);
        this.models = models;

        scroll = new JScrollPane();
        table = new JTable(model);
        txtNombre = new JLabel();
        nombre = new JTextField();
        txtPCompra = new JLabel();
        txtPVenta = new JLabel();
        txtStock = new JLabel();
        pCompra = new JTextField();
        pVenta = new JTextField();
        stock = new JTextField();
        btnAgregar = new JButton();
        btnTerminar = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        scroll.setViewportView(table);

        txtNombre.setText("Nombre:");

        txtPCompra.setText("Precio Compra:");

        txtPVenta.setText("Precio Venta:");

        txtStock.setText("stock:");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(GuardarEnLaLista());
        stock.addActionListener(GuardarEnLaLista());

        btnTerminar.setText("Terminar");
        btnTerminar.addActionListener(GuardarDB(idViaje));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnAgregar))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(txtPCompra)
                                                                        .addComponent(txtPVenta)
                                                                        .addComponent(txtStock)
                                                                        .addComponent(txtNombre))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(nombre)
                                                                        .addComponent(pCompra)
                                                                        .addComponent(pVenta)
                                                                        .addComponent(stock, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)))
                                                        .addComponent(btnTerminar))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addComponent(scroll, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtNombre)
                                                        .addComponent(nombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtPCompra)
                                                        .addComponent(pCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtPVenta)
                                                        .addComponent(pVenta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtStock)
                                                        .addComponent(stock, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addComponent(btnAgregar)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnTerminar))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(scroll, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ActionListener GuardarEnLaLista() {
        return e -> {
            if (!Validaciones()) {
                return;
            }

            String name = nombre.getText().trim();
            double precioC = Double.parseDouble(pCompra.getText());
            double precioV = Double.parseDouble(pVenta.getText());
            int cantidad = Integer.parseInt(stock.getText());


            lista.add(new Producto(name, precioC, precioV, cantidad));

            Object[] obj = {name, precioC, precioV, cantidad};
            model.addRow(obj);

            nombre.setText("");
            pCompra.setText("");
            pVenta.setText("");
            stock.setText("");

            nombre.requestFocus();
        };
    }


    public boolean Validaciones() {
        try {
            Double.parseDouble(pCompra.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Verifique la validez del precio de compra", "Warning", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

        try {
            Double.parseDouble(pVenta.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Verifique la validez del precio de venta", "Warning", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        try {
            Integer.parseInt(stock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Verifique la validez del stock", "Warning", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

        return true;
    }

    public ActionListener GuardarDB(int idViaje) {
        return e -> {

            for (Producto p: lista) {

                try {
                    Connection conn = GestorDB.getConn();

                    PreparedStatement stmt = conn.prepareStatement(
                            "SELECT * FROM productos WHERE nombre = ? LIMIT 1"
                    );

                    stmt.setString(1, p.getNombre());

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        AgregarProductoExistente(idViaje, conn, p.getNombre(), p.getStock());
                    } else {
                        AgregarNuevoProducto(idViaje, conn, p.getNombre(), p.getpCompra(), p.getpVenta(), p.getStock());
                    }

                } catch (SQLException exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
    }

    public void AgregarNuevoProducto(int idViaje, Connection conn, String name, double precioC, double precioV, int cantidad) {

        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO productos (nombre, pCompra, pVenta, stock) VALUES (?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, name);
            stmt.setBigDecimal(2, BigDecimal.valueOf(precioC));
            stmt.setBigDecimal(3, BigDecimal.valueOf(precioV));
            stmt.setInt(4, cantidad);

            stmt.executeUpdate();

            int idProducto;
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                idProducto = rs.getInt(1);

                PreparedStatement stmtDetallesCompra = conn.prepareStatement(
                        "INSERT INTO detalles_compra (id_viaje, id_Producto) VALUES (?,?)"
                );

                stmtDetallesCompra.setInt(1, idViaje);
                stmtDetallesCompra.setInt(2, idProducto);

                stmtDetallesCompra.executeUpdate();

                ActualizarTablasPrincipales();

                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar al menos un producto", "Warning", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "No se pudo ingresar los datos en la base de datos", "Warning", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(exception);
        }
    }

    public void AgregarProductoExistente(int idViaje, Connection conn, String name, int cantidad) {
        try {
            PreparedStatement stmtID = conn.prepareStatement(
                    "SELECT id FROM productos WHERE nombre = ?"
            );

            stmtID.setString(1, name);

            ResultSet rsID = stmtID.executeQuery();

            int idProducto = -1;
            if (rsID.next()) {
                idProducto = rsID.getInt("id");
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE productos SET stock = stock + ? WHERE nombre = ?"
            );

            stmt.setInt(1, cantidad);
            stmt.setString(2, name);

            int comprobar = stmt.executeUpdate();

            if (comprobar != 1) {
                JOptionPane.showMessageDialog(null, "No se pudo agg el producto", "Warning", JOptionPane.ERROR_MESSAGE);
            } else {

                PreparedStatement stmtDetallesCompra = conn.prepareStatement(
                        "INSERT INTO detalles_compra (id_viaje, id_Producto) VALUES (?,?)"
                );

                stmtDetallesCompra.setInt(1, idViaje);
                stmtDetallesCompra.setInt(2, idProducto);

                stmtDetallesCompra.executeUpdate();

                ActualizarTablasPrincipales();

                dispose();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ActualizarTablasPrincipales() {
        CargarTablaViajes(models.get("viajes"));
        CargarTablaProducto(models.get("producto"));
    }

    private void CargarTablaViajes(DefaultTableModel modelViajes) {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM viajes"
            );

            ResultSet rs = stmt.executeQuery();

            modelViajes.setRowCount(0);

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

