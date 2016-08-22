/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Edgardo
 */
@Entity
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_PRODUCTO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @NotNull(message = "INGRESE EL NOMBRE DEL PRODUCTO!!!")
    @Column(name = "DESCRIPCION",unique = true,columnDefinition = "varchar(60)")
    private String descripcion;
    @Column(name = "PRECIO_UNITARIO_COMPRA",precision = 15,scale = 3)
    private BigDecimal precioUnitarioCompra;
    @Column(name = "PRECIO_UNITARIO_VENTA",precision = 15,scale = 3)
    private BigDecimal precioUnitarioVenta;
    @Column(name = "CODIGO_BARRA",unique = true)
    private String codigoBarra;
    @Column(name = "PRIMER_CANTIDAD_INICIAL")
    private int cantidadInicial;
    @Column(name = "CANTIDAD_TOTAL_ACTUAL")
    private int cantidadTotalActual;
    @Column(name = "FECHA_INGRESO_INICIAL")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaIngresoInicial;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "FECHA_ULTIMA_COMPRA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimoIngreso;
    @Column(name = "FECHA_ULTIMA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimaVenta;
    @Column(name = "FECHA_ULTIMA_ACTUALIZACIÓN")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimaActualizacion;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,targetEntity = Fabrica.class,optional = false)
    private Fabrica fabricaFK;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "productoFK",targetEntity = Compra.class)
    private List<Compra> compra;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "productoFK",targetEntity = Venta.class)
    private List<Venta> venta;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "productoFK")
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,optional = true,targetEntity = Persona.class)
    private Persona personaFK;
    
    
    
    

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
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Producto[ id=" + id + " ]";
    }
    
}
