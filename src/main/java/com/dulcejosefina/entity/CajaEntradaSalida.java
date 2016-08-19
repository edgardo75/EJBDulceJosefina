/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edgardo
 */
@Entity
public class CajaEntradaSalida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_CAJA_ENTRADA_SALIDA")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "FECHA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @Column(name = "ENTRADA",columnDefinition = "DECIMAL(15,2) default'0,00'")
    private BigDecimal entrada;
    @Column(name = "SALIDA",columnDefinition = "DECIMAL(15, 2) default'0.00'")
    private BigDecimal salida;
    @Column(name = "ENTRADATARJETA", columnDefinition = "DECIMAL(15, 2) default'0.00'")
    private double entradaTarjeta;    
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "VENTASEFECTIVO", columnDefinition = "DECIMAL(15, 2) default'0.00'")
    private double ventasEfectivo;
    @Column(name = "ID_PERSONA")    
    private Integer idPersona;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "NUMEROCUPON",columnDefinition = "VARCHAR(20) default '0'")
    private String numerocupon;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ENEFECTIVO",columnDefinition = "CHAR(1) default '0'")
    private Character enefectivo;
    @Basic(fetch = FetchType.LAZY)        
    @Column(name = "HORA")
    @Temporal(TemporalType.TIME)
    private Date hora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public BigDecimal getSalida() {
        return salida;
    }

    public void setSalida(BigDecimal salida) {
        this.salida = salida;
    }

    public double getEntradaTarjeta() {
        return entradaTarjeta;
    }

    public void setEntradaTarjeta(double entradaTarjeta) {
        this.entradaTarjeta = entradaTarjeta;
    }

    public double getVentasEfectivo() {
        return ventasEfectivo;
    }

    public void setVentasEfectivo(double ventasEfectivo) {
        this.ventasEfectivo = ventasEfectivo;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getNumerocupon() {
        return numerocupon;
    }

    public void setNumerocupon(String numerocupon) {
        this.numerocupon = numerocupon;
    }

    public Character getEnefectivo() {
        return enefectivo;
    }

    public void setEnefectivo(Character enefectivo) {
        this.enefectivo = enefectivo;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
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
        if (!(object instanceof CajaEntradaSalida)) {
            return false;
        }
        CajaEntradaSalida other = (CajaEntradaSalida) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.CajaEntradaSalida[ id=" + id + " ]";
    }
    
}
