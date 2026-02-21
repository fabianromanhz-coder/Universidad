package Reventa.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorDB {
    private static String url = "jdbc:mysql://localhost:3306/inventario";
    private static String user = "root";
    private static String password = "1234";
    private static Connection conn = null;

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static Connection getConn() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = conectar();
        }
        return conn;
    }

    public static void closeConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}