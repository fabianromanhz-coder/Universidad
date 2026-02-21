package Reventa.Modelos;

public class Producto {
    String nombre;
    double pCompra;
    double pVenta;
    int stock;

    public Producto(String nombre, double pCompra, double pVenta, int stock) {
        this.nombre = nombre;
        this.pCompra = pCompra;
        this.pVenta = pVenta;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getpCompra() {
        return pCompra;
    }

    public void setpCompra(double pCompra) {
        this.pCompra = pCompra;
    }

    public double getpVenta() {
        return pVenta;
    }

    public void setpVenta(double pVenta) {
        this.pVenta = pVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
