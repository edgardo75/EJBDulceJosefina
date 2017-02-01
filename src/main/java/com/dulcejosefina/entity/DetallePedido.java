/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

/**
 *
 * @author Edgardo
 */
@Entity
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "DetallePedidoIdGen",table = "ID_GEN_DETALLEPEDIDO",pkColumnName = "DETPEDNAME",pkColumnValue = "DetallePedido",valueColumnName = "DETPEDKEY",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "DetallePedidoIdGen")
    @Column(name = "ID_DETALLE_PEDIDO")
    private Long id;
    
    @Column(name = "SUBTOTAL",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal subtotal;   
    @Column(name = "IDPACK")
    private long idPack;
    @Column(name = "IDVENTAPRODUCTO")
    private long idVentaProducto;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "PRESENTACION")
    private int presentacion;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "NOMBREPACK")
    private String nombrePack;
    @Column(name = "PRECIO")
    private double precio;    
    @Column(name = "CANTIDAD")
    private int cantidad;
    
    @ManyToOne
    private Pedido pedido;
    @ManyToOne
    private Producto producto;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public long getIdPack() {
        return idPack;
    }

    public void setIdPack(long idPack) {
        this.idPack = idPack;
    }

    public long getIdVentaProducto() {
        return idVentaProducto;
    }

    public void setIdVentaProducto(long idVentaProducto) {
        this.idVentaProducto = idVentaProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(int presentacion) {
        this.presentacion = presentacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombrePack() {
        return nombrePack;
    }

    public void setNombrePack(String nombrePack) {
        this.nombrePack = nombrePack;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePedido)) {
            return false;
        }
        DetallePedido other = (DetallePedido) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.DetallePedido[ id=" + id + " ]";
    }
    public String toXML(){
    StringBuilder xml = new StringBuilder("<itemDetallePedido>");
    xml.append("<id>").append(this.getId()).append("</id>")
            .append("<subtotal>").append(this.getSubtotal()).append("</subtotal>")
                .append("<idPack>").append(this.getIdPack()).append("</idPack>")
                .append("<IdVentaProducto>").append(this.getIdVentaProducto()).append("</IdVentaProducto>")
                .append("<codigo>").append((!(this.getCodigo() == null)?this.getCodigo():"")).append("</codigo>")
                .append("<presentacion>").append(this.getPresentacion()).append("</presentacion>")
                .append("<precio>").append(this.getPrecio()).append("</precio>")
                .append("<descripcion>").append(this.getDescripcion()).append("</descripcion>")
                .append("<nombrePack>").append(this.getNombrePack()).append("</nombrePack>")
                .append("<cantidad>").append(this.getCantidad()).append("</cantidad>");     
    return xml.append("</itemDetallePedido>").toString();
    
    
    }
    
}
