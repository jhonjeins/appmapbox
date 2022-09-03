package com.jhonj.pgrs.modelos;

public class Reporte {

    private String nombre;
    private String correo;
    private String fecha;
    private String residuo;
    private int critico;
    private double latitud;
    private double longitud;


    public Reporte() {
    }

    public Reporte(String nombre, String correo, String fecha, String residuo, int critico, double latitud, double longitud) {
        this.nombre = nombre;
        this.correo = correo;
        this.critico = critico;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.residuo = residuo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha() { return fecha; }

    public void setFecha(String fecha) {this.fecha = fecha;}

    public String getResiduo(){ return residuo;}

    public void setResiduo (String residuo) {this.residuo = residuo;}

    public int getCritico() {
        return critico;
    }

    public void setCritico(int critico) {
        this.critico = critico;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
