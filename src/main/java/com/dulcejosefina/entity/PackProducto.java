package com.dulcejosefina.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
@NamedQueries({@NamedQuery(name = "PackProducto.fidAll",query = "SELECT p FROM PackProducto p")})
public class PackProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "PackProductoIdGen",table = "ID_GEN_PACK_PROD", pkColumnName="PACKNAME",pkColumnValue="PackProducto", valueColumnName="PACKKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "PackProductoIdGen")
    @Column(name = "ID_PACK_PRODUCTO")    
    private Long id;
    @Column(name = "DESCRIPCION", unique = true,nullable = false)
    private String descripcion;
    @OneToMany(cascade = {CascadeType.REFRESH},mappedBy = "packFK",orphanRemoval = true,fetch = FetchType.LAZY)
    private List<CompraProducto> compra;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "packFK",orphanRemoval = true,fetch = FetchType.LAZY)
    private List<VentaProducto> venta;
    public PackProducto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<CompraProducto> getCompra() {
        return compra;
    }

    public void setCompra(List<CompraProducto> compra) {
        this.compra = compra;
    }

    public List<VentaProducto> getVenta() {
        return venta;
    }

    public void setVenta(List<VentaProducto> venta) {
        this.venta = venta;
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
        if (!(object instanceof PackProducto)) {
            return false;
        }
        PackProducto other = (PackProducto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.PACK[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml = new StringBuilder("<item>\n");
                xml.append("<id>").append(this.getId()).append("</item>").append("<descripcion>").append(this.getDescripcion()).append("</descripcion>")
                 .append("</item>");
    
        return xml.toString();
    }
}
