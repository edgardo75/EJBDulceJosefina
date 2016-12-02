package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

@Entity
public class HistoricoVentaSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "HistoricoVentaSucursalIdGen",table = "ID_GEN_HIST_VENTA_SUC", pkColumnName="HISTSUCNAME",pkColumnValue="HistoricoVentaSucursal", valueColumnName="HISTSUCKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "HistoricoVentaSucursalIdGen")
    @Column(name = "ID_HISTORICO_VENTA_SUCURSAL")    
    private Long id;
    @Column(name="FECHA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVenta;
    @Column(name = "CANTIDAD",precision = 15,scale = 2,columnDefinition = "INTEGER default'0'")
    private Integer cantidad;
    @Column(name = "PORCENTAJE_DESCUENTO",columnDefinition = "DECIMAL(15,2) DEFAULT'0.00'")
    private BigDecimal porcentajeDescuento;
    @Column(name = "PORCENTAJE_RECARGO",columnDefinition = "DECIMAL(15,2) DEFAULT'0.00'")
    private BigDecimal porcentajeRecargo;
    @Column(name = "DESCUENTO_PESOS",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal descuentoPesos;
    @Column(name = "RECARGO_PESOS",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal recargoPesos;
    @Column(name = "TOTAL_VENTA",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal totalVenta;
    @ManyToOne(targetEntity = VentaSucursal.class,fetch = FetchType.LAZY)
    private VentaSucursal ventaSucursal;

    public HistoricoVentaSucursal(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Integer getCantida() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public void setPorcentajeRecargo(BigDecimal porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
    }

    public BigDecimal getDescuentoPesos() {
        return descuentoPesos;
    }

    public void setDescuentoPesos(BigDecimal descuentoPesos) {
        this.descuentoPesos = descuentoPesos;
    }

    public BigDecimal getRecargoPesos() {
        return recargoPesos;
    }

    public void setRecargoPesos(BigDecimal recargoPesos) {
        this.recargoPesos = recargoPesos;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public VentaSucursal getVentaSucursal() {
        return ventaSucursal;
    }

    public void setVentaSucursal(VentaSucursal ventaSucursal) {
        this.ventaSucursal = ventaSucursal;
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
        if (!(object instanceof HistoricoVentaSucursal)) {
            return false;
        }
        HistoricoVentaSucursal other = (HistoricoVentaSucursal) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.HistoricoVenta[ id=" + id + " ]";
    }
    
}
