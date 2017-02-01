/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

/**
 *
 * @author Edgardo
 */
@Entity
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableGenerator(name = "PedidoIdGen",table = "ID_GEN_PEDIDO",pkColumnName = "PEDNAME",pkColumnValue = "Pedido",valueColumnName = "PEDKEY",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "PedidoIdGen")
    @Id
    @Column(name = "ID_PEDIDO")
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
    @Column(name = "TOTAL_RECARGO",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal totalRecargo;
    @Column(name = "TOTAL_GENERAL",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal totalGeneral;
    @Column(name = "TOTAL_A_PAGAR",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal totalAPagar;
    @Column(name = "TOTAL_DESCUENTO",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal totalDescuento;
    @Column(name = "ID_USUARIO_EXPIDIO_PEDIDO")
    private long idUsuarioExpidioPedido;    
    @OneToMany(mappedBy = "pedido",orphanRemoval = true)
    private List<DetallePedido>detallePedidoList;
    @ManyToOne
    private Sucursal sucursalFK;
    @ManyToOne
    private Proveedor proveedorFK;
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

    public BigDecimal getTotalRecargo() {
        return totalRecargo;
    }

    public void setTotalRecargo(BigDecimal totalRecargo) {
        this.totalRecargo = totalRecargo;
    }

    public BigDecimal getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(BigDecimal totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public long getIdUsuarioExpidioPedido() {
        return idUsuarioExpidioPedido;
    }

    public void setIdUsuarioExpidioPedido(long idUsuarioExpidioPedido) {
        this.idUsuarioExpidioPedido = idUsuarioExpidioPedido;
    }

    public Proveedor getProveedorFK() {
        return proveedorFK;
    }

    public void setProveedorFK(Proveedor proveedorFK) {
        this.proveedorFK = proveedorFK;
    }

    public List<DetallePedido> getDetallePedidoList() {
        return detallePedidoList;
    }

    public void setDetallePedidoList(List<DetallePedido> detallePedidoList) {
        this.detallePedidoList = detallePedidoList;
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
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Pedido[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder item = new StringBuilder("<pedido>");
              item.append("<id>").append(this.getId()).append("</id>")
                      .append("<fecha>").append(this.getFechaVenta()!=null?new SimpleDateFormat("dd/MM/yyyy").format(this.getFechaVenta()):0).append("</fecha>")
                      .append("<cantidad>").append(this.getCantidad()).append("</cantidad>")
                            .append("<porcentajeDescuento>").append(this.getPorcentajeDescuento()).append("</porcentajeDescuento>")
                            .append("<porcentajeRecargo>").append(this.getPorcentajeRecargo()).append("</porcentajeRecargo>")
                            .append("<descuentoPesos>").append(this.getDescuentoPesos()).append("</descuentoPesos>")
                            .append("<recargoPesos>").append(this.getRecargoPesos()).append("</recargoPesos>")
                            .append("<totalGeneral>").append(this.getTotalGeneral()).append("</totalGeneral>")
                            .append("<totalAPagar>").append(this.getTotalAPagar()).append("</totalAPagar>")
                      .append("<proveedor><![CDATA[").append(this.getProveedorFK().getNombre()).append("]]></proveedor>");
              
                if(!this.getDetallePedidoList().isEmpty()){
                        item.append("<detallePedido>");
                        for (DetallePedido detallePedido : detallePedidoList) {
                                item.append(detallePedido.toXML());
                        }
              
                }
                item.append("</detallePedido>").append("</pedido>");
              return item.toString();
        
    
    }
}
