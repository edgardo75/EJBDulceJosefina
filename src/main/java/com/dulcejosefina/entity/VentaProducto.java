package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

@Entity
public class VentaProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "VentaProductoIdGen", table = "ID_GEN_VTAPROD", pkColumnName = "VTAPRODNAME", pkColumnValue = "VentaProducto", valueColumnName = "VTAPRODKEY",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "VentaProductoIdGen")
    @Id
    @Column(name = "ID_VENTA_PRODUCTO")
    private Long id;
    @Column(name = "PRESENTACION", columnDefinition = "INTEGER default '0'")
    private Integer presentacion;

    @Column(name = "PRECIO", columnDefinition = "DECIMAL(15,3) default '0.000'")
    private BigDecimal precio;
    @Column(name = "PORCENTAJE", columnDefinition = "DECIMAL(15,3) default '0.000'")
    private double porcentaje;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PackProducto.class)
    private PackProducto packFK;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Producto.class)
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

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
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

    public String toXML() {
        StringBuilder xml = new StringBuilder("<itemVenta>\n");
        xml.append("<id>").append(this.getId()).append("</id>\n")
                .append("<presentacion>").append(this.getPresentacion()).append("</presentacion>")
                .append("<precio>").append(this.getPrecio()).append("</precio>\n")
                .append("<nombrePack>").append(this.getPackFK().getDescripcion()).append("</nombrePack>\n")
                .append("<packId>").append(this.getPackFK().getId()).append("</packId>\n")
                .append("<porcentaje>").append(this.getPorcentaje()).append("</porcentaje>\n")
                .append("</itemVenta>");
        return xml.toString();

    }

}
