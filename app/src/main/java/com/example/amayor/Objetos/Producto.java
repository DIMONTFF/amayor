package com.example.amayor.Objetos;

public class Producto {
    private int idProducto; // Primary key
    private int idTienda; // Foreign key to Tienda (idPersona)
    private String nombre;
    private double precio;
    private byte[] imagen; // New field for image

    public Producto(int idProducto, int idTienda, String nombre, double precio, byte[] imagen) {
        this.idProducto = idProducto;
        this.idTienda = idTienda;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters and Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}