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
    private double descuentoPesos;
    private double recargoPesos;
    private double totalVenta;

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

    public double getDescuentoPesos() {
        return descuentoPesos;
    }

    public double getRecargoPesos() {
        return recargoPesos;
    }

    public double getTotalVenta() {
        return totalVenta;
    }
    
    
}
