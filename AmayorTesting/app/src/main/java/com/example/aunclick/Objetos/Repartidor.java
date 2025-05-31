package com.example.amayor.Objetos;

import java.util.LinkedList;

public class Repartidor extends Persona {
    private LinkedList<Pedido> listaPedidos;

    public Repartidor(int idPersona, String usuario, String contrasenia, String numTelefono) {
        super(idPersona, usuario, contrasenia, numTelefono);
        this.listaPedidos = new LinkedList<>();
    }

    // Getters and Setters
    public LinkedList<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(LinkedList<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    // Methods
    public void anadirPedido(Pedido pedido) {
        listaPedidos.add(pedido);
    }

    public void eliminarPedido(Pedido pedido) {
        listaPedidos.remove(pedido);
    }
}