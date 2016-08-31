package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

@Entity
@NamedQueries({@NamedQuery(name = "Tarjeta.findAll",query = "SELECT t FROM Tarjeta t")})
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "TarjetaIdGen",table = "ID_GEN_TAR", pkColumnName="TARNAME",pkColumnValue="Tarjeta", valueColumnName="TARKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "TarjetaIdGen")
    @Id
    @Column(name = "ID_TARJETA")
    
    private Long id;
    @Column(name = "NOMBRE",unique = true,nullable = false)
    private String nombre;

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
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Tarjeta[ id=" + id + " ]";
    }
    public String toXML(){
    StringBuilder xml = new StringBuilder("<item>\n");
            xml.append("<id>").append(this.getId()).append("</id>\n").append("<nombre>").append(this.getNombre()).append("</nombre>\n").append("</item>\n");
            
    return xml.toString();
    
    }
    
}
