package com.example.amayor.Objetos;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Tienda extends Persona {
    private String nombreTienda;
    private TipoTienda tipoTienda;
    private LinkedHashMap<Producto, Integer> inventario;
    private LinkedList<Pedido> listaPedidos;

    public Tienda(int idPersona, String usuario, String numTelef, String contrasenia, String nombreTienda, TipoTienda tipoTienda) {
        super(idPersona, usuario, numTelef, contrasenia);
        this.nombreTienda = nombreTienda;
        this.tipoTienda = tipoTienda;
        this.inventario = new LinkedHashMap<>();
        this.listaPedidos = new LinkedList<>();
    }

    // Getters and Setters
    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public TipoTienda getTipoTienda() {
        return tipoTienda;
    }

    public void setTipoTienda(TipoTienda tipoTienda) {
        this.tipoTienda = tipoTienda;
    }

    public LinkedHashMap<Producto, Integer> getInventario() {
        return inventario;
    }

    public void setInventario(LinkedHashMap<Producto, Integer> inventario) {
        this.inventario = inventario;
    }

    public LinkedList<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(LinkedList<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    // Methods
    public void anadirProductoInventario(Producto producto, int cantidad) {
        if (cantidad > 0) {
            inventario.merge(producto, cantidad, Integer::sum);
        }
    }

    public void eliminarProductoInventario(Producto producto, int cantidad) {
        if (inventario.containsKey(producto) && cantidad > 0) {
            int nuevaCantidad = inventario.get(producto) - cantidad;
            if (nuevaCantidad <= 0) {
                inventario.remove(producto);
            } else {
                inventario.put(producto, nuevaCantidad);
            }
        }
    }

    public void anadirPedido(Pedido pedido) {
        listaPedidos.add(pedido);
    }

    public void eliminarPedido(Pedido pedido) {
        listaPedidos.remove(pedido);
    }
}