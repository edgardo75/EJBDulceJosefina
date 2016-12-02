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
    private int presentacion;
    private String fecha;
    private String porcentaje;
    private double precio;
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

    public String getPorcentaje() {
        return porcentaje;
    }

    
   

    public long getPackProductoId() {
        return packProductoId;
    }
}
