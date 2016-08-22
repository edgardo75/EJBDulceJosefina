/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Edgardo
 */
@Entity
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_VENTA")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "PRESENTACION")
    private String presentacion;
    
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY,targetEntity = Pack.class)
    private Pack packFK;
    @ManyToOne(cascade = { CascadeType.ALL},fetch = FetchType.LAZY,targetEntity = Producto.class)
    private Producto productoFK;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Pack getPackFK() {
        return packFK;
    }

    public void setPackFK(Pack packFK) {
        this.packFK = packFK;
    }

    public Producto getProductoFK() {
        return productoFK;
    }

    public void setProductoFK(Producto productoFK) {
        this.productoFK = productoFK;
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
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Venta[ id=" + id + " ]";
    }
    
}
