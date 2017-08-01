package com.dulcejosefina.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
@Entity
@NamedQueries({@NamedQuery(name = "selectAllDetalleVentaListForIdVentaSucursal",query = "SELECT d FROM DetalleVentaSucursal d WHERE d.ventaSucursal.id =:id")})
public class DetalleVentaSucursal implements Serializable {    
   
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "DetalleVentaSucursalIdGen",table = "ID_GEN_DETALLE_VENTA_SUC", pkColumnName="DETSUCNAME",pkColumnValue="DetalleVentaSucursal", valueColumnName="DETSUCKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "DetalleVentaSucursalIdGen")
    @Column(name = "ID_DETALLE_VENTA_SUCURSAL")    
    private Long id;
    @Column(name = "SUBTOTAL",columnDefinition = "DECIMAL(15,3) DEFAULT'0.000'")
    private BigDecimal subtotal;
   
    
    @Column(name = "IDPACK")
    private long idPack;
    @Column(name = "IDVENTAPRODUCTO")
    private long idVentaProducto;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "PRESENTACION")
    private int presentacion;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "NOMBREPACK")
    private String nombrePack;
    @Column(name = "PRECIO")
    private double precio;    
    @Column(name = "CANTIDAD")
    private int cantidad;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = VentaSucursal.class)
    private VentaSucursal ventaSucursal;
    @ManyToOne
    private Producto producto;
    public DetalleVentaSucursal(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

   

    public long getIdPack() {
        return idPack;
    }

    public void setIdPack(long idPack) {
        this.idPack = idPack;
    }

    public long getIdVentaProducto() {
        return idVentaProducto;
    }

    public void setIdVentaProducto(long idVentaProducto) {
        this.idVentaProducto = idVentaProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(int presentacion) {
        this.presentacion = presentacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombrePack() {
        return nombrePack;
    }

    public void setNombrePack(String nombrePack) {
        this.nombrePack = nombrePack;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public VentaSucursal getVentaSucursal() {
        return ventaSucursal;
    }

    public void setVentaSucursal(VentaSucursal ventaSucursal) {
        this.ventaSucursal = ventaSucursal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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
        if (!(object instanceof DetalleVentaSucursal)) {
            return false;
        }
        DetalleVentaSucursal other = (DetalleVentaSucursal) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.DetalleVentaSucursal[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml = new StringBuilder("<itemDetalleVenta>");
        
        xml.append("<id>").append(this.getId()).append("</id>")
                //.append("<subtotal>").append(DecimalFormat.getInstance().format(this.getSubtotal())).append("</subtotal>")
                .append("<subtotal>").append(this.getSubtotal().setScale(2, RoundingMode.DOWN)).append("</subtotal>")
                .append("<idPack>").append(this.getIdPack()).append("</idPack>")
                .append("<idProducto>").append(this.getProducto().getId()).append("</idProducto>")
                .append("<IdVentaProducto>").append(this.getIdVentaProducto()).append("</IdVentaProducto>")
                .append("<codigo>").append((!(this.getCodigo() == null)?this.getCodigo():"")).append("</codigo>")
                .append("<presentacion>").append(this.getPresentacion()>=1000&&this.getNombrePack().equalsIgnoreCase("Precio Unitario")?Double.valueOf(this.getPresentacion())/1000+" kg":this.getPresentacion()).append("</presentacion>")
                .append("<precio>").append(new BigDecimal(this.getPrecio()).setScale(2, RoundingMode.DOWN)).append("</precio>")
                .append("<descripcion>").append(this.getDescripcion()).append("</descripcion>")
                .append("<nombrePack>").append(this.getNombrePack()).append("</nombrePack>")
                .append("<cantidad>").append(this.getCantidad()>50?Double.valueOf(this.getCantidad())+" gr":this.getCantidad()).append("</cantidad>");
    return xml.append("</itemDetalleVenta>").toString();
    
    }
}
