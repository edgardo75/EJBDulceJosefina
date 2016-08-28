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
public class DatosStockProducto {
    
    private long id;
    private double precioUnitarioCompra;
    private double precioUnitarioVenta;
    private int cantidadAgregada;
    private String fechaAgregada;

    public long getId() {
        return id;
    }

    public double getPrecioUnitarioCompra() {
        return precioUnitarioCompra;
    }

    public double getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public int getCantidadAgregada() {
        return cantidadAgregada;
    }

    public String getFechaAgregada() {
        return fechaAgregada;
    }
    
    
}
