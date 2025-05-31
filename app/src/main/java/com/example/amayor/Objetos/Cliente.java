package com.example.amayor.Objetos;

public class Cliente extends Persona {
    private String direccion;

    public Cliente(int idPersona, String usuario, String contrasenia, String numTelefono, String direccion) {
        super(idPersona, usuario, contrasenia, numTelefono);
        this.direccion = direccion;
    }

    // Getters and Setters
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
