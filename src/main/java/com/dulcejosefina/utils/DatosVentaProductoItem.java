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
public class DatosVentaProductoItem {
     private long id;
    private double presentacion;
    private String fechaVenta;
    private String detalle;
    private double porcentajeAplicado;
    private double totalVenta;
    private long packProductoId;

    public long getId() {
        return id;
    }

    public double getPresentacion() {
        return presentacion;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public String getDetalle() {
        return detalle;
    }

    public double getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

   

    public long getPackProductoId() {
        return packProductoId;
    }
}
