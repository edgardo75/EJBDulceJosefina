/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

/**
 *
 * @author Edgardo
 */
@Entity
public class Telefono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "telefonoIDGen",table = "id_gen_Tel",pkColumnName = "pkTelefono",pkColumnValue = "Telefono",valueColumnName = "TELKEY",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "telefonoIDGen")
    private Long id;
    @Column(name = "NUMERO",unique = true)
    private long numero;
    @Column(name = "PREFIJO")
    private long prefijo;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "telefono")
    private List<PersonaTelefono> telefonoPersona;

    public Telefono() {
    }

    public Telefono(long numero, long prefijo) {
        this.numero = numero;
        this.prefijo = prefijo;
    }
    
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public long getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(long prefijo) {
        this.prefijo = prefijo;
    }

    public List<PersonaTelefono> getTelefonoPersona() {
        return telefonoPersona;
    }

    public void setTelefonoPersona(List<PersonaTelefono> telefonoPersona) {
        this.telefonoPersona = telefonoPersona;
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
        if (!(object instanceof Telefono)) {
            return false;
        }
        Telefono other = (Telefono) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Telefono[ id=" + id + " ]";
    }
    public String toXML(){
        String item = "<id>" + this.getId() + "</id>" + "<numero>" + this.getNumero() + "</numero>" + "<prefijo>" + this.getPrefijo() + "</prefijo>";
                
    return item;
    }
}
