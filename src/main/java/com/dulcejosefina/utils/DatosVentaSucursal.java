/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.utils;

/**
 *
 * @author Edgardo
 */
public class DatosVentaSucursal {
    private long id;
    private String fechaVenta;
    private int cantidad;
    private double porcentajeDescuento;
    private double porcentajeRecargo;
    private double totalRecargo;
    private double totalGeneral;
    private double totalAPagar;
    private double totalDescuento;
    private long idUsuarioExpidioVenta;
    private DatosPersona persona;
    private DatosSucursal sucursal;
    private DatosDetalleVentaSucursal detalleVenta;

    public long getId() {
        return id;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public double getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public double getTotalRecargo() {
        return totalRecargo;
    }

    public double getTotalGeneral() {
        return totalGeneral;
    }

    public double getTotalAPagar() {
        return totalAPagar;
    }

    public double getTotalDescuento() {
        return totalDescuento;
    }

    public DatosPersona getPersona() {
        return persona;
    }

    public DatosSucursal getSucursal() {
        return sucursal;
    }

    public DatosDetalleVentaSucursal getDetalleVenta() {
        return detalleVenta;
    }

    public long getIdUsuarioExpidioVenta() {
        return idUsuarioExpidioVenta;
    }

    
}
