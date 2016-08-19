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

/**
 *
 * @author Edgardo
 */
@Entity
public class Fabrica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_FABRICA")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name="DETALLES")
    private String detalles;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof Fabrica)) {
            return false;
        }
        Fabrica other = (Fabrica) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Fabrica[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml = new StringBuilder("<item>\n");
            xml.append("<id>").append(this.getId()).append("</id>\n").append("<nombre>").append(this.getNombre()).append("</nombre>\n").append("<detalles>").append(this.getDetalles()).append("</detalles>\n").append("</item>");            
    return xml.toString();    
    }
    
}
