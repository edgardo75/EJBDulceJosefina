package com.dulcejosefina.utils;
public class DatosCajaEntradaSalida {
    private long id;
    private String fecha;
    private double entrada;
    private double salida;
    private double entradaTarjeta;
    private double ventaEfectivo;
    private long idPersona;
    private String numeroCupon;
    private char enEfectivo;
    private String hora;
    private String detalle;
    public long getId() {
        return id;
    }
    public String getFecha() {
        return fecha;
    }
    public double getEntrada() {
        return entrada;
    }
    public double getSalida() {
        return salida;
    }
    public double getEntradaTarjeta() {
        return entradaTarjeta;
    }
    public double getVentaEfectivo() {
        return ventaEfectivo;
    }
    public long getIdPersona() {
        return idPersona;
    }
    public String getNumeroCupon() {
        return numeroCupon;
    }
    public char getEnEfectivo() {
        return enEfectivo;
    }
    public String getHora() {
        return hora;
    }
    public String getDetalle() {
        return detalle;
    }    
}