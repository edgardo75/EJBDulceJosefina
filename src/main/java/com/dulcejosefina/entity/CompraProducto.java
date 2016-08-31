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
public class CompraProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "CompraProductoIdGen",table = "ID_GEN_COMPRA_SUC", pkColumnName="CONAME",pkColumnValue="CompraProducto", valueColumnName="COKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "CompraProductoIdGen")
    @Column(name = "ID_COMPRA_PRODUCTO")    
    private Long id;
    @Column(name = "PRESENTACION",columnDefinition = "INTEGER default '0'")
    private BigDecimal presentacion;
    @Column(name = "FECHA_COMPRA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCompra;
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,targetEntity = Producto.class)
    private Producto productoFK;
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,targetEntity = PackProducto.class)
    private PackProducto packFK;
    @Column(name = "PORCENTAJE_APLICADO",columnDefinition = "DECIMAL(15,2)DEFAULT'0.00'")
    private Double porcentajeAplicado;
    @Column(name = "DETALLE",columnDefinition = "VARCHAR(255)DEFAULT''")
    private String detalle;
    @Column(name = "TOTAL_COMPRA",columnDefinition = "DECIMAL(15,3) default '0.000'")
    private BigDecimal totalCompra;
    
    public CompraProducto(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(BigDecimal presentacion) {
        this.presentacion = presentacion;
    }

    public Producto getProducto() {
        return productoFK;
    }

    public void setProducto(Producto producto) {
        this.productoFK = producto;
    }

    

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Producto getProductoFK() {
        return productoFK;
    }

    public void setProductoFK(Producto productoFK) {
        this.productoFK = productoFK;
    }

    public PackProducto getPackFK() {
        return packFK;
    }

    public void setPackFK(PackProducto packFK) {
        this.packFK = packFK;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(BigDecimal totalCompra) {
        this.totalCompra = totalCompra;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompraProducto)) {
            return false;
        }
        CompraProducto other = (CompraProducto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Compra[ id=" + id + " ]";
    }
    public String toXML(){
    StringBuilder xml = new StringBuilder("<itemCompra>\n")
            .append("<id>").append(this.getId()).append("</id>\n").append("<fecha>").append(this.getFechaCompra()).append("</fecha>\n")
                    .append("<presentacion>").append(this.getPresentacion()).append("</presentacion>\n").append("<porcentaje>").append(this.getPorcentajeAplicado()).append("</porcentaje>\n")
                    .append("<detalle>").append(this.getDetalle()).append("</detalle>\n")
                    .append("<totalCompra>").append(this.getTotalCompra()).append("</totalCompra>\n")
                    .append("<packId>").append(this.getPackFK().getId()).append("</packId>\n")
            .append("</itemCompra>");
        return xml.toString();
    }
}
