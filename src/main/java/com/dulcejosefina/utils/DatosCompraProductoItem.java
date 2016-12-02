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
    private int presentacion;
    private String fecha;        
    private double precio;
    private String porcentaje;
    private long packProductoId;

    public long getId() {
        return id;
    }

    public int getPresentacion() {
        return presentacion;
    }

  

    public String getFecha() {
        return fecha;
    }

    public double getPrecio() {
        return precio;
    }

   

    public long getPackProductoId() {
        return packProductoId;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

}
