package com.jhonj.pgrs.modelos;

public class Reporte {

    private String nombre;
    private String correo;
    private int critico;
    private double latitud;
    private double longitud;
    private String geo;


    public Reporte() {
    }

    public Reporte(String nombre, String correo, int critico, double latitud, double longitud, String geo) {
        this.nombre = nombre;
        this.correo = correo;
        this.critico = critico;
        this.latitud = latitud;
        this.longitud = longitud;
        this.geo = geo;
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

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }
}
