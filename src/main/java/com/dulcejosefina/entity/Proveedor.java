    package com.dulcejosefina.entity;

import java.io.Serializable;
import java.util.List;
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
@NamedQueries({@NamedQuery(name = "findAll.Proveedor", query = "SELECT p FROM Proveedor p"),@NamedQuery(name = "findProveedorByNombre",query = "SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE :nombre")})
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "ProveedorIdGen",table = "ID_GEN_PROVEEDOR", pkColumnName="PRVNAME",pkColumnValue="Proveedor", valueColumnName="PRVKEY",
    allocationSize=1)
    @Column(name = "ID_PROVEEDOR")
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "ProveedorIdGen")
    @Id    
    private Long id;
    @Column(name = "NOMBRE",nullable = false,unique = true)
    private String nombre;
    @Column(name="DETALLES",columnDefinition = "varchar(1000) default''")
    private String detalles;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "proveedorFK",targetEntity = Producto.class)
    private List<Producto>producto;

    public Proveedor(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public List<Producto> getProducto() {
        return producto;
    }

    public void setProducto(List<Producto> producto) {
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
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Fabrica[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml = new StringBuilder("<item>\n");
            xml.append("<id>").append(this.getId()).append("</id>\n").append("<nombre>").append(this.getNombre()).append("</nombre>\n").append("<detalle>")
                    .append(this.getDetalles()).append("</detalle>").append("</item>");            
    return xml.toString();    
    }
    
}
