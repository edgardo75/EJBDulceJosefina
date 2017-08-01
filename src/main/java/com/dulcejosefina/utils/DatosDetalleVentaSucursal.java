package com.dulcejosefina.utils;
import java.util.Collections;
import java.util.List;
public class DatosDetalleVentaSucursal {    
    List<ItemDetalleVentaSucursalItem>list;
    public List<ItemDetalleVentaSucursalItem> getList() {
        return Collections.unmodifiableList(list);
    }  
}
