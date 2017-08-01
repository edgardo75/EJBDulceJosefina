package com.dulcejosefina.utils;
import java.util.List;
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
