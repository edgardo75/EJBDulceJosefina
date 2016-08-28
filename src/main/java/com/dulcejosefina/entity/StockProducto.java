package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

@Entity
public class StockProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "StockProductoIdGen",table = "ID_GEN_STOCK_PROD", pkColumnName="FNAME",pkColumnValue="StockProducto", valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "ID_STOCK_PRODUCTO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;    
    @Column(name = "PRECIO_UNITARIO_COMPRA",precision = 15,scale = 3,columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal precioUnitarioCompra;
    @Column(name = "PRECIO_UNITARIO_VENTA",precision = 15,scale = 3,columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal precioUnitarioVenta;
    @Column(name = "CANTIDAD_AGREGADA",columnDefinition = "INTEGER default'0'")
    private int cantidadAgregada;
    @Column(name = "FECHA_AGREGADO_PRODUCTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAgregadoProducto;
    
    @ManyToOne(targetEntity = Producto.class,cascade = CascadeType.REFRESH,optional = true)
    private Producto productoFK;
    
    public StockProducto(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public BigDecimal getPrecioUnitarioCompra() {
        return precioUnitarioCompra;
    }

    public void setPrecioUnitarioCompra(BigDecimal precioUnitarioCompra) {
        this.precioUnitarioCompra = precioUnitarioCompra;
    }

    public BigDecimal getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public int getCantidadAgregada() {
        return cantidadAgregada;
    }

    public void setCantidadAgregada(int cantidadAgregada) {
        this.cantidadAgregada = cantidadAgregada;
    }

    public Date getFechaAgregadoProducto() {
        return fechaAgregadoProducto;
    }

    public void setFechaAgregadoProducto(Date fechaAgregadoProducto) {
        this.fechaAgregadoProducto = fechaAgregadoProducto;
    }

    public Producto getProducto() {
        return productoFK;
    }

    public void setProducto(Producto productoFK) {
        this.productoFK = productoFK;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockProducto)) {
            return false;
        }
        StockProducto other = (StockProducto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.StockProducto[ id=" + id + " ]";
    }
      public String toXML(){
    StringBuilder xml = new StringBuilder("<itemStock>\n")
            .append("<id>").append(this.getId()).append("</id>\n").append("<preciounitarioCompra>").append(this.getPrecioUnitarioCompra()).append("</preciounitarioCompra>\n")
                    .append("<precioUnitarioVenta>").append(this.getPrecioUnitarioVenta()).append("</precioUnitarioVenta>\n")
                    .append("<cantidadAgregada>").append(this.getCantidadAgregada()).append("</cantidadAgregada>\n")
                    .append("<fechaAgregado>").append(this.getFechaAgregadoProducto()).append("</fechaAgregado>\n")                    
            .append("</itemStock>");
        return xml.toString();
    }
}
