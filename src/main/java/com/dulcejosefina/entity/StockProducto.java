/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class StockProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_STOCK")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;    
    @Column(name = "PRECIO_UNITARIO_COMPRA",precision = 15,scale = 3)
    private BigDecimal precioUnitarioCompra;
    @Column(name = "PRECIO_UNITARIO_VENTA",precision = 15,scale = 3)
    private BigDecimal precioUnitarioVenta;
    @Column(name = "CANTIDAD_AGREGADA")
    private int cantidadAgregada;
    @Column(name = "FECHA_AGREGADO_PRODUCTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAgregadoProducto;
    
    
    

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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockProducto)) {
            return false;
        }
        StockProducto other = (StockProducto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.StockProducto[ id=" + id + " ]";
    }
    
}
