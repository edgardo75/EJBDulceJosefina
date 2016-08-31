package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
@Entity
public class DetalleVentaSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "DetalleVentaSucursalIdGen",table = "ID_GEN_DETALLE_VENTA_SUC", pkColumnName="DETSUCNAME",pkColumnValue="DetalleVentaSucursal", valueColumnName="DETSUCKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "DetalleVentaSucursalIdGen")
    @Column(name = "ID_DETALLE_VENTA_SUCURSAL")    
    private Long id;
    @Column(name = "SUBTOTAL",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal subtotal;
    @Column(name = "DESCUENTO",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal descuento;
    @Column(name = "RECARGO",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal recargo;
    @Column(name = "ID_PRODUCTO")
    private long idProducto;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,targetEntity = VentaSucursal.class)
    private VentaSucursal ventaSucursal;

    public DetalleVentaSucursal(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public VentaSucursal getVentaSucursal() {
        return ventaSucursal;
    }

    public void setVentaSucursal(VentaSucursal ventaSucursal) {
        this.ventaSucursal = ventaSucursal;
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
        if (!(object instanceof DetalleVentaSucursal)) {
            return false;
        }
        DetalleVentaSucursal other = (DetalleVentaSucursal) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.DetalleVentaSucursal[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml = new StringBuilder("<itemDetalleVentaSucursal>");
        
        xml.append("<id>").append(this.getId()).append("</id>").append("<subtotal>").append(this.getSubtotal()).append("</subtotal>").append("<descuento>").append(this.getDescuento())
                .append("</descuento>").append("<recargo>").append(this.getRecargo()).append("</recargo>").append("<productoId>").append(this.getIdProducto()).append("</productoId>")
                .append("<ventaSucursalId>").append(this.getVentaSucursal().getId()).append("</ventaSucursalId>");
    return xml.toString();
    
    }
}
