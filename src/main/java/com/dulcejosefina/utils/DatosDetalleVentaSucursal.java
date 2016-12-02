/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.utils;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Edgardo
 */
public class DatosDetalleVentaSucursal {
    
    List<ItemDetalleVentaSucursalItem>list;

    public List<ItemDetalleVentaSucursalItem> getList() {
        return Collections.unmodifiableList(list);
    }
   
    
    
}
