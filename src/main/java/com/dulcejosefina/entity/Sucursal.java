package com.dulcejosefina.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;

@Entity
@NamedQueries({@NamedQuery(name = "Sucursal.findAll",query = "SELECT s FROM Sucursal s")})
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "SucursalIdGen",table = "ID_GEN_SUC", pkColumnName="FNAME",pkColumnValue="Sucursal", valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "ID_SUCURSAL")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "NOMBRE",nullable = false,unique = true)
    private String nombre;
    @Column(name = "DESCRIPCION",columnDefinition = "varchar(500) default''")
    private String descripcion;    
    @Column(name = "FECHA_ULTIMA_COMPRA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimaCompra;
    @Column(name = "FECHA_ULTIMA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimaVenta;
    @OneToMany(mappedBy = "sucursalFK",cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,orphanRemoval = true,targetEntity = VentaSucursal.class)
    private List<VentaSucursal> ventaSucursalList;
    @OneToMany(mappedBy = "sucursalFK",cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,orphanRemoval = true,targetEntity = Producto.class)
    private List<Producto>productoList;
    
    public Sucursal(){}
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(Date fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    public Date getFechaUltimaVenta() {
        return fechaUltimaVenta;
    }

    public void setFechaUltimaVenta(Date fechaUltimaVenta) {
        this.fechaUltimaVenta = fechaUltimaVenta;
    }

    public List<VentaSucursal> getVentaSucursalList() {
        return ventaSucursalList;
    }

    public void setVentaSucursalList(List<VentaSucursal> ventaSucursalList) {
        this.ventaSucursalList = ventaSucursalList;
    }

    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Sucursal[ id=" + id + " ]";
    }
    public String toXML(){
    StringBuilder xml = new StringBuilder("<item>\n");
            xml.append("<id>").append(this.getId()).append("</id>\n").append("<nombre>").append(this.getNombre()).append("</nombre>\n")
                    .append("<descripcion>").append(this.getDescripcion()).append("</descripcion>\n").append("</item>\n");
            
    return xml.toString();
    
    }
    
}
