package Reventa.Interface;

import Reventa.DB.GestorDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AggViaje extends javax.swing.JDialog {
    JButton btnAtras;
    JButton btnAceptar;
    JTextField fecha;
    JLabel txtFecha;
    JLabel txtDestino;
    JLabel txtGastos;
    JTextField destino;
    JTextField gastos;

    public AggViaje(Frame parent, boolean modal, Map<String, DefaultTableModel> models) {
        super(parent, modal);

        btnAtras = new javax.swing.JButton();
        fecha = new javax.swing.JTextField();
        txtFecha = new javax.swing.JLabel();
        destino = new javax.swing.JTextField();
        txtDestino = new javax.swing.JLabel();
        gastos = new javax.swing.JTextField();
        txtGastos = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();

        btnAtras.setText("<-");
        btnAtras.addActionListener(e -> dispose());

        txtFecha.setText("Fecha:");

        txtDestino.setText("Destino:");

        txtGastos.setText("Gastos:");

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(GuardarDatos(models));
        gastos.addActionListener(GuardarDatos(models));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnAceptar)
                                .addGap(16, 16, 16))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(txtDestino)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(txtGastos)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(gastos)))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnAtras)
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtFecha))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDestino))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtGastos)
                                        .addComponent(gastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnAceptar)
                                .addGap(0, 10, Short.MAX_VALUE))
        );


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ActionListener GuardarDatos(Map<String, DefaultTableModel> models) {

        return (e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            LocalDate localDate;

            try {
                localDate = LocalDate.parse(fecha.getText(), formatter);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Comprobar fecha (dd/MM/yy)", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Double.parseDouble(gastos.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Comprobar celda del dinero", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection conn;

            try {
                conn = GestorDB.getConn();

                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO viajes (fecha, destino, gastos) VALUES (?,?,?)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                );


                stmt.setDate(1, Date.valueOf(localDate));
                stmt.setString(2, destino.getText());
                stmt.setBigDecimal(3, new BigDecimal(gastos.getText()));

                int x = stmt.executeUpdate();

                if (x == 1) {
                    JOptionPane.showMessageDialog(this, "Guardado");
                    dispose();

                    int id;
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        id = rs.getInt(1);

                        new AggProducto(null, true, id, models);
                    } else {
                        JOptionPane.showMessageDialog(null, "Hubo un problema al sacar el id del viaje", "Warning", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Hubo un error al intentar insertar el viaje en la base de datos", "Warning", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
