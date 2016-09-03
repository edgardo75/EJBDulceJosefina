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
public class DatosProducto {
    
    private long id;
    private String descripcion;
    private double precioUnitarioCompra;
    private double precioUnitarioVenta;
    private String codigoBarra;
    private int primerCantidadInicial;
    private int cantidadIngresada;
    private int cantidadTotalActual;
    private String fechaIngresoInicial;
    private String fechaVencimiento;
    private String fechaCantidadIngresada;
    private String fechaUltimaCompra;
    private String fechaUltimaVenta;
    private String fechaUltimaActualizacion;
    private String detalle;
    private double porcentajeCompra;
    private double porcentajeVenta;
    private DatosSucursal sucursal;
    private DatosProveedor proveedor;    
    private DatosCompraProducto compraProducto;
    private DatosVentaProducto ventaProducto;
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitarioCompra() {
        return precioUnitarioCompra;
    }

    public void setPrecioUnitarioCompra(double precioUnitarioCompra) {
        this.precioUnitarioCompra = precioUnitarioCompra;
    }

    public DatosSucursal getSucursalId() {
        return sucursal;
    }

    public DatosProveedor getProveedorId() {
        return proveedor;
    }

    

    public DatosCompraProducto getCompraProducto() {
        return compraProducto;
    }

    public DatosVentaProducto getVentaProducto() {
        return ventaProducto;
    }

    public double getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(double precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public int getPrimerCantidadInicial() {
        return primerCantidadInicial;
    }

    public void setPrimerCantidadInicial(int primerCantidadInicial) {
        this.primerCantidadInicial = primerCantidadInicial;
    }

    public int getCantidadIngresada() {
        return cantidadIngresada;
    }

    public void setCantidadIngresada(int cantidadIngresada) {
        this.cantidadIngresada = cantidadIngresada;
    }

    public int getCantidadTotalActual() {
        return cantidadTotalActual;
    }

    public void setCantidadTotalActual(int cantidadTotalActual) {
        this.cantidadTotalActual = cantidadTotalActual;
    }

    public String getFechaIngresoInicial() {
        return fechaIngresoInicial;
    }

    public void setFechaIngresoInicial(String fechaIngresoInicial) {
        this.fechaIngresoInicial = fechaIngresoInicial;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getFechaCantidadIngresada() {
        return fechaCantidadIngresada;
    }

    public void setFechaCantidadIngresada(String fechaCantidadIngresada) {
        this.fechaCantidadIngresada = fechaCantidadIngresada;
    }

    public String getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(String fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    public String getFechaUltimaVenta() {
        return fechaUltimaVenta;
    }

    public void setFechaUltimaVenta(String fechaUltimaVenta) {
        this.fechaUltimaVenta = fechaUltimaVenta;
    }

    public String getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(String fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public DatosSucursal getSucursal() {
        return sucursal;
    }

    public DatosProveedor getProveedor() {
        return proveedor;
    }

    

    public double getPorcentajeCompra() {
        return porcentajeCompra;
    }

    public double getPorcentajeVenta() {
        return porcentajeVenta;
    }
    
    
}
