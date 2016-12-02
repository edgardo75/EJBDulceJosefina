/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.utils;

import java.util.List;

/**
 *
 * @author Edgardo
 */
public class DatosVentaProducto {
     private String detalle;
    private String porcentajeAplicado;
    private String idProducto;
    private String fecha;
   private List<DatosVentaProductoItem>listSale;
   
   public List<DatosVentaProductoItem>getList(){
       return listSale;
   }

    public String getDetalle() {
        return detalle;
    }

    public String getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getFecha() {
        return fecha;
    }
    
    
    
}
