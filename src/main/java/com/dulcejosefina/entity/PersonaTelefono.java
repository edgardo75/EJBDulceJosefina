/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

/**
 *
 * @author Edgardo
 */
@Entity
public class PersonaTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @TableGenerator(name = "PersonaTelefonoIdGen",table = "ID_GEN_PERTEL", pkColumnName="PERTELNAME",pkColumnValue="PersonaTelefono", valueColumnName="PERTELKEY",
    allocationSize=1)
    @GeneratedValue(generator = "PersonaTelefonoIdGen",strategy = GenerationType.TABLE)
    @Id
    private Long id;    
    @ManyToOne(targetEntity = Persona.class,fetch = FetchType.LAZY)
    private Persona persona;
    @ManyToOne(targetEntity = Telefono.class,fetch = FetchType.LAZY)
    private Telefono telefono;

    public PersonaTelefono() {
    }

    public PersonaTelefono(Persona persona, Telefono telefono) {
        this.persona = persona;
        this.telefono = telefono;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
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
        if (!(object instanceof PersonaTelefono)) {
            return false;
        }
        PersonaTelefono other = (PersonaTelefono) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.PersonaTelefono[ id=" + id + " ]";
    }
    public String toXML(){
        String xml = "<itemTelefono>"+
                 "<idPersonaTelefono>"+this.getId()+"</idPersonaTelefono>\n"+
                this.getTelefono().toXML()+
                "</itemTelefono>";
        return xml;
    
    }
}
