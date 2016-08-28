package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

@Entity
public class VentaProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "VentaProducto",table = "ID_GEN_PROD", pkColumnName="FNAME",pkColumnValue="VentaProducto", valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "ID_VENTA_PRODUCTO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "PRESENTACION",columnDefinition = "INTEGER default '0'")
    private Integer presentacion;
    @Column(name = "FECHA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVenta;    
    @Column(name = "DETALLE",columnDefinition = "VARCHAR(255) default''")
    private String detalle;
    @Column(name = "PORCENTAJE_APLICADO",columnDefinition = "DECIMAL(15,2) default '0.00'")
    private Double porcentajeAplicado;
    @Column(name = "TOTAL_VENTA",columnDefinition = "DECIMAL(15,3) default '0.000'")
    private BigDecimal totalVenta;
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,targetEntity = PackProducto.class)
    private PackProducto packFK;
    @ManyToOne(cascade = { CascadeType.ALL},fetch = FetchType.LAZY,targetEntity = Producto.class)
    private Producto productoFK;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Integer presentacion) {
        this.presentacion = presentacion;
    }

    public PackProducto getPackFK() {
        return packFK;
    }

    public void setPackFK(PackProducto packFK) {
        this.packFK = packFK;
    }

    public Producto getProductoFK() {
        return productoFK;
    }

    public void setProductoFK(Producto productoFK) {
        this.productoFK = productoFK;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

   

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public Double getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public void setPorcentajeAplicado(Double porcentajeAplicado) {
        this.porcentajeAplicado = porcentajeAplicado;
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
        if (!(object instanceof VentaProducto)) {
            return false;
        }
        VentaProducto other = (VentaProducto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Venta[ id=" + id + " ]";
    }
    
      public String toXML(){
    StringBuilder xml = new StringBuilder("<itemVenta>\n")
            .append("<id>").append(this.getId()).append("</id>\n").append("<fecha>").append(this.getFechaVenta()).append("</fecha>\n")
                    .append("<presentacion>").append(this.getPresentacion()).append("</presentacion>\n").append("<porcentaje>").append(this.getPorcentajeAplicado()).append("</porcentaje>\n")
                    .append("<detalle>").append(this.getDetalle()).append("</detalle>\n")
                    .append("<totalVenta>").append(this.getTotalVenta()).append("</totalVenta>\n")
                    .append("<packId>").append(this.getPackFK().getId()).append("</packId>\n")
            .append("</itemVenta>");
        return xml.toString();
    }
    
}
