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
public class DatosCompraProductoItem {
      private long id;
    private double presentacion;
    private String fecha;
    private double porcentajeAplicado;
    private String detalle;
    private double totalCompra;
    private long packProductoId;

    public long getId() {
        return id;
    }

    public double getPresentacion() {
        return presentacion;
    }

    public String getFecha() {
        return fecha;
    }

    public double getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public String getDetalle() {
        return detalle;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public long getPackProductoId() {
        return packProductoId;
    }

}
