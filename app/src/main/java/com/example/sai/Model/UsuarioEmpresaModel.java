package com.example.sai.Model;

public class UsuarioEmpresaModel {


    private String nombre;
    private String nombreLargo;
    private int predeterminada;
    private boolean estado;

    // Getters and Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNombreLargo() { return nombreLargo; }
    public void setNombreLargo(String nombreLargo) { this.nombreLargo = nombreLargo; }

    public int getPredeterminada() { return predeterminada; }
    public void setPredeterminada(int predeterminada) { this.predeterminada = predeterminada; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

}
