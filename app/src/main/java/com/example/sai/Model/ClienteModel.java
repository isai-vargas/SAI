package com.example.sai.Model;

public class ClienteModel {
    private int codigo;
    private String nombre;
    private String nit;
    private String direccion;
    private String razon;
    private String direccionEntrega;
    private String correo;
    private String telefono;
    private boolean credito;
    private double limiteCredito;
    private int tipo;
    private String descripcionTipo;
    private String naciemiento;
    private String nacionalidad;
    private String identificacion;
    private int numeroPuntosAfiliacion;
    private boolean estadoPuntosSalud;

    // Getters y setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isCredito() {
        return credito;
    }

    public void setCredito(boolean credito) {
        this.credito = credito;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }

    public String getNaciemiento() {
        return naciemiento;
    }

    public void setNaciemiento(String naciemiento) {
        this.naciemiento = naciemiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public int getNumeroPuntosAfiliacion() {
        return numeroPuntosAfiliacion;
    }

    public void setNumeroPuntosAfiliacion(int numeroPuntosAfiliacion) {
        this.numeroPuntosAfiliacion = numeroPuntosAfiliacion;
    }

    public boolean isEstadoPuntosSalud() {
        return estadoPuntosSalud;
    }

    public void setEstadoPuntosSalud(boolean estadoPuntosSalud) {
        this.estadoPuntosSalud = estadoPuntosSalud;
    }
}
