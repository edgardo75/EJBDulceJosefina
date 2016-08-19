/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Edgardo
 */
@Entity
@NamedQueries({@NamedQuery(name = "Sucursal.findAll",query = "SELECT s FROM Sucursal s")})
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_SUCURSAL")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "NOMBRE",nullable = false,unique = true, columnDefinition = "varchar(50)")
    private String nombre;
    @Column(name = "DESCRIPCION",columnDefinition = "varchar(500) default''")
    private String descripcion;    

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
