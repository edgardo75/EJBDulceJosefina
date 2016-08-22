/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Edgardo
 */
@Entity
public class VentaSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_VENTA_SUCURSAL")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name="FECHA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVenta;
    @Column(name = "CANTIDAD",precision = 15,scale = 2)
    private BigDecimal cantida;
    @Column(name = "PORCENTAJE_DESCUENTO")
    private BigDecimal porcentajeDescuento;
    @Column(name = "PORCENTAJE_RECARGO")
    private BigDecimal porcentajeRecargo;
    @Column(name = "DESCUENTO_PESOS")
    private BigDecimal descuentoPesos;
    @Column(name = "RECARGO_PESOS")
    private BigDecimal recargoPesos;
    @Column(name = "TOTAL_VENTA")
    private BigDecimal totalVenta;
    

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
        if (!(object instanceof VentaSucursal)) {
            return false;
        }
        VentaSucursal other = (VentaSucursal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.VentaSucursal[ id=" + id + " ]";
    }
    
}
