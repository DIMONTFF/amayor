package com.example.amayor.Objetos;

import java.util.LinkedHashMap;

public class Pedido {
    private int idPedido; // Primary key
    private int idCliente; // Foreign key to Cliente (idPersona)
    private int idTienda; // Foreign key to Tienda (idPersona)
    private Integer idRepartidor; // Foreign key to Repartidor (idPersona, nullable)
    private String nombreCliente;
    private String direccion;
    private double importe;
    private LinkedHashMap<Producto, Integer> descripcion;
    private Estado estado;

    public Pedido(int idPedido, int idCliente, int idTienda, Integer idRepartidor, String nombreCliente, String direccion, double importe, LinkedHashMap<Producto, Integer> descripcion, Estado estado) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.idTienda = idTienda;
        this.idRepartidor = idRepartidor;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.importe = importe;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters and Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public Integer getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(Integer idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public LinkedHashMap<Producto, Integer> getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(LinkedHashMap<Producto, Integer> descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    // Methods
    public void anadirProductoPedido(Producto producto, int cantidad) {
        if (cantidad > 0) {
            descripcion.merge(producto, cantidad, Integer::sum);
            actualizarImporte();
        }
    }

    public void eliminarProductoPedido(Producto producto, int cantidad) {
        if (descripcion.containsKey(producto) && cantidad > 0) {
            int nuevaCantidad = descripcion.get(producto) - cantidad;
            if (nuevaCantidad <= 0) {
                descripcion.remove(producto);
            } else {
                descripcion.put(producto, nuevaCantidad);
            }
            actualizarImporte();
        }
    }

    private void actualizarImporte() {
        this.importe = descripcion.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                .sum();
    }
}