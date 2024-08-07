package com.example.sai.Model;

public class SerieModel {

    private String serie;
    private int tipo;
    private int empresa;
    private int bodega;
    private int sucursal;
    private boolean tmu;
    private String texto;
    private boolean cambiaria;
    private boolean fel;
    private int correlativo;
    private boolean credito;

    // Getters y setters para cada campo

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }

    public int getEmpresa() { return empresa; }
    public void setEmpresa(int empresa) { this.empresa = empresa; }

    public int getBodega() { return bodega; }
    public void setBodega(int bodega) { this.bodega = bodega; }

    public int getSucursal() { return sucursal; }
    public void setSucursal(int sucursal) { this.sucursal = sucursal; }

    public boolean isTmu() { return tmu; }
    public void setTmu(boolean tmu) { this.tmu = tmu; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public boolean isCambiaria() { return cambiaria; }
    public void setCambiaria(boolean cambiaria) { this.cambiaria = cambiaria; }

    public boolean isFel() { return fel; }
    public void setFel(boolean fel) { this.fel = fel; }

    public int getCorrelativo() { return correlativo; }
    public void setCorrelativo(int correlativo) { this.correlativo = correlativo; }

    public boolean isCredito() { return credito; }
    public void setCredito(boolean credito) { this.credito = credito; }
}
