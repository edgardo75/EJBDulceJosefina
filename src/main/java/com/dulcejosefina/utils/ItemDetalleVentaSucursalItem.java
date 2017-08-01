package com.dulcejosefina.utils;
public class ItemDetalleVentaSucursalItem {
    private long id;
    private long idPack;
    private long idVentaProducto;
    private String codigo;
    private int presentacion;
    private String descripcion;
    private String nombrePack;
    private double precio;
    private double subtotal;
    private int cantidad;
    public ItemDetalleVentaSucursalItem() {
    }
    public long getId() {
        return id;
    }

    public long getIdPack() {
        return idPack;
    }

    public long getIdVentaProducto() {
        return idVentaProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getPresentacion() {
        return presentacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombrePack() {
        return nombrePack;
    }

    public double getPrecio() {
        return precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }    
}
