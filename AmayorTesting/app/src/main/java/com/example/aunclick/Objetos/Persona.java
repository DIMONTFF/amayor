package com.example.amayor.Objetos;

public abstract class Persona {
    private int idPersona; // Primary key
    private String usuario;
    private String numTelef;
    private String contrasenia;

    public Persona(int idPersona, String usuario, String numTelef, String contrasenia) {
        this.idPersona = idPersona;
        this.usuario = usuario;
        this.numTelef = numTelef;
        this.contrasenia = contrasenia;
    }

    // Getters and Setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNumTelef() {
        return numTelef;
    }

    public void setNumTelef(String numTelef) {
        this.numTelef = numTelef;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}