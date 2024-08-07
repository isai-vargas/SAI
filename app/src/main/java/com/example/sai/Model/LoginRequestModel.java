package com.example.sai.Model;

public class LoginRequestModel {
    private String nombre;
    private String clave;
    private int predeterminada;

    // Constructor
    public LoginRequestModel(String nombre, String clave, int predeterminada) {
        this.nombre = nombre;
        this.clave = clave;
        this.predeterminada = predeterminada;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getPredeterminada() {
        return predeterminada;
    }

    public void setPredeterminada(int predeterminada) {
        this.predeterminada = predeterminada;
    }
}
