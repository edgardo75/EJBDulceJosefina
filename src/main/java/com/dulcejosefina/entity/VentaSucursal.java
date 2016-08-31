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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

@Entity
public class VentaSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "VentaSucursalIdGen",table = "ID_GEN_VTASUC", pkColumnName="VTASUCNAME",pkColumnValue="VentaSucursal", valueColumnName="VTASUCKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "VentaSucursalIdGen")
    @Id    
    @Column(name = "ID_VENTA_SUCURSAL")    
    private Long id;
    @Column(name="FECHA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVenta;
    @Column(name = "CANTIDAD",precision = 15,scale = 2,columnDefinition = "INTEGER default '0'")
    private Integer cantidad;
    @Column(name = "PORCENTAJE_DESCUENTO",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal porcentajeDescuento;
    @Column(name = "PORCENTAJE_RECARGO",columnDefinition = "DECIMAL(15,3) DEFAULT'0.00'")
    private BigDecimal porcentajeRecargo;
    @Column(name = "DESCUENTO_PESOS",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal descuentoPesos;
    @Column(name = "RECARGO_PESOS",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal recargoPesos;
    @Column(name = "TOTAL_VENTA",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal totalVenta;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "ventaSucursal",orphanRemoval = true,targetEntity = DetalleVentaSucursal.class)
    private List<DetalleVentaSucursal>listaDetalleVentaSucursal;
    @ManyToOne
    private Producto producto;
    @ManyToOne 
    Persona persona;
    @OneToOne()
    private CajaEntradaSalida cajaFK;
    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY,mappedBy = "ventaSucursal",orphanRemoval = true,targetEntity = HistoricoVentaSucursal.class)
    private List<HistoricoVentaSucursal>historicoVentaSucursal;
    @ManyToOne
    private Sucursal sucursalFK;

    public VentaSucursal(){}
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantida(Integer cantidad) {
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

    public List<DetalleVentaSucursal> getListaDetalleVentaSucursal() {
        return listaDetalleVentaSucursal;
    }

    public void setListaDetalleVentaSucursal(List<DetalleVentaSucursal> listaDetalleVentaSucursal) {
        this.listaDetalleVentaSucursal = listaDetalleVentaSucursal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public CajaEntradaSalida getCaja() {
        return cajaFK;
    }

    public void setCaja(CajaEntradaSalida cajaFK) {
        this.cajaFK = cajaFK;
    }

    public CajaEntradaSalida getCajaFK() {
        return cajaFK;
    }

    public void setCajaFK(CajaEntradaSalida cajaFK) {
        this.cajaFK = cajaFK;
    }

    public List<HistoricoVentaSucursal> getHistoricoVentaSucursal() {
        return historicoVentaSucursal;
    }

    public void setHistoricoVentaSucursal(List<HistoricoVentaSucursal> historicoVentaSucursal) {
        this.historicoVentaSucursal = historicoVentaSucursal;
    }

    public Sucursal getSucursalFK() {
        return sucursalFK;
    }

    public void setSucursalFK(Sucursal sucursalFK) {
        this.sucursalFK = sucursalFK;
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.VentaSucursal[ id=" + id + " ]";
    }
    
    public String toXML(){
    StringBuilder xml = new StringBuilder("<itemVentaSucursal>");
    
    
    xml.append("<id>").append(this.getId()).append("</id>").append("<fecha>").append(this.getFechaVenta()).append("</fecha>")
            .append("<cantidadd>").append(this.getCantidad()).append("</cantidadd").append("<porcentajeDescuento>").append(this.getPorcentajeDescuento()).append("</porcentajeDescuento>")
            .append("<porcentajeRecargo>").append(this.getPorcentajeRecargo()).append("</porcentajeRecargo>").append("<descuentoPesos>").append(this.getDescuentoPesos()).append("</descuentoPesos>")
            .append("<recargoPesos>").append(this.getRecargoPesos()).append("</recargoPesos").append("<totalVenta>").append(this.getTotalVenta()).append("</totalVenta>");
    
    if(!this.getListaDetalleVentaSucursal().isEmpty()){
        xml.append("<detalleVenta>");
        StringBuilder xmlDetalle = new StringBuilder(5);
        for(DetalleVentaSucursal detalle: listaDetalleVentaSucursal){
            xml.append(detalle.toString());
        
        }
        
        
        xml.append("</detalleVenta>");
    
    
    }
            
        return null;
    }
    
}
