package com.dulcejosefina.utils;
import java.util.List;
public class DatosCompraProducto {
    private String detalle;
    private String porcentajeAplicado;
    private String idProducto;
    private String fecha;
  private List<DatosCompraProductoItem>listShop;
    public String getDetalle() {
        return detalle;
    }

    public String getPorcentajeAplicado() {
        return porcentajeAplicado;
    }
    public List<DatosCompraProductoItem>getList(){
  return listShop;
  }

    public String getIdProducto() {
        return idProducto;
    }

    public String getFecha() {
        return fecha;
    }
    
}
