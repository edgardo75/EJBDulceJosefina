/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Edgardo
 */
@Entity
public class Modulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_MODULO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "CODIGOMODULO")
    private String codigoModulo;
    @Column(name = "NOMBREMODULO")
    private String nombreModulo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Perfil perfilModulo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public Perfil getPerfilModulo() {
        return perfilModulo;
    }

    public void setPerfilModulo(Perfil perfilModulo) {
        this.perfilModulo = perfilModulo;
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
        if (!(object instanceof Modulo)) {
            return false;
        }
        Modulo other = (Modulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Modulo[ id=" + id + " ]";
    }
    
}
