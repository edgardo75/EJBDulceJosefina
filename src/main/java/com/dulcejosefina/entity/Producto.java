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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

@Entity
@NamedQueries({@NamedQuery(name = "Producto.fidAll",query = "SELECT p FROM Producto p")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "ProductoIdGen",table = "ID_GEN_PRODUCTO", pkColumnName="PRONAME",pkColumnValue="Producto", valueColumnName="PROKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "ProductoIdGen")
    @Id    
    @Column(name = "ID_PRODUCTO")    
    private Long id;    
    @Column(name = "DESCRIPCION",unique = true,nullable = false)
    private String descripcion;
    @Column(name = "PRECIO_UNITARIO_COMPRA",precision = 15,scale = 3,columnDefinition = "DECIMAL(15,3) default'0.000'")
    private BigDecimal precioUnitarioCompra;
    @Column(name = "PRECIO_UNITARIO_VENTA",precision = 15,scale = 3,columnDefinition = "DECIMAL(15,3) default'0.000'")
    private BigDecimal precioUnitarioVenta;
    @Column(name = "CODIGO_BARRA",unique = true,columnDefinition = "VARCHAR(100)DEFAULT'null'")
    private String codigoBarra;
    @Column(name = "PRIMER_CANTIDAD_INICIAL",columnDefinition = "INTEGER DEFAULT'0'")
    private int cantidadInicial;
    @Column(name = "CANTIDAD_INGRESADA",columnDefinition = "INTEGER DEFAULT'0'")
    private int cantidadIngresada;
    @Column(name = "CANTIDAD_TOTAL_ACTUAL",columnDefinition = "INTEGER DEFAULT'0'")
    private int cantidadTotalActual;
    @Column(name = "FECHA_INGRESO_INICIAL")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaIngresoInicial;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "FECHA_CANTIDAD_INGRESADA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCantidadIngresada;
    @Column(name = "FECHA_ULTIMA_COMPRA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimoIngreso;
    @Column(name = "FECHA_ULTIMA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimaVenta;
    @Column(name = "FECHA_ULTIMA_ACTUALIZACION")
    @Temporal(javax.persistence.TemporalType.DATE)    
    private Date fechaUltimaActualizacion;
    @Column(name = "PORCENTAJE_COMPRA",columnDefinition = "DECIMAL(12,2)DEFAULT'0'")
    private double porcentajeCompra;
    @Column(name = "PORCENTAJE_VENTA",columnDefinition = "DECIMAL(12,2)DEFAULT'0'")
    private double porcentajeVenta;
    @Column(name = "DETALLE_PRODUCTO",columnDefinition = "VARCHAR(255) DEFAULT''")
    private String detalleProducto;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,targetEntity = Proveedor.class,optional = false)
    private Proveedor proveedorFK;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "productoFK",targetEntity = CompraProducto.class)
    private List<CompraProducto> compra;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "productoFK",targetEntity = VentaProducto.class)
    private List<VentaProducto> venta;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "productoFK")
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,optional = true,targetEntity = Persona.class)
    private Persona personaFK;
    @OneToMany(mappedBy = "producto")
    private List<VentaSucursal>listaVentaSucursal;
    @OneToMany(mappedBy = "productoFK")
    private List<ImagenProducto> imagenProductoList;
    @OneToMany(mappedBy = "productoFK")
    private List<StockProducto>stockProductoList;
    @ManyToOne
    private Sucursal sucursalFK;
    public Producto(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioUnitarioCompra() {
        return precioUnitarioCompra;
    }

    public void setPrecioUnitarioCompra(BigDecimal precioUnitarioCompra) {
        this.precioUnitarioCompra = precioUnitarioCompra;
    }

    public BigDecimal getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public int getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(int cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public int getCantidadTotalActual() {
        return cantidadTotalActual;
    }

    public void setCantidadTotalActual(int cantidadTotalActual) {
        this.cantidadTotalActual = cantidadTotalActual;
    }

    public Date getFechaIngresoInicial() {
        return fechaIngresoInicial;
    }

    public void setFechaIngresoInicial(Date fechaIngresoInicial) {
        this.fechaIngresoInicial = fechaIngresoInicial;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaUltimoIngreso() {
        return fechaUltimoIngreso;
    }

    public void setFechaUltimoIngreso(Date fechaUltimoIngreso) {
        this.fechaUltimoIngreso = fechaUltimoIngreso;
    }

    public Date getFechaUltimaVenta() {
        return fechaUltimaVenta;
    }

    public void setFechaUltimaVenta(Date fechaUltimaVenta) {
        this.fechaUltimaVenta = fechaUltimaVenta;
    }

    public Date getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(Date fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    

    public List<CompraProducto> getCompra() {
        return compra;
    }

    public void setCompra(List<CompraProducto> compra) {
        this.compra = compra;
    }

    public List<VentaProducto> getVenta() {
        return venta;
    }

    public void setVenta(List<VentaProducto> venta) {
        this.venta = venta;
    }

    public Persona getPersonaFK() {
        return personaFK;
    }

    public void setPersonaFK(Persona personaFK) {
        this.personaFK = personaFK;
    }

    public List<VentaSucursal> getListaVentaSucursal() {
        return listaVentaSucursal;
    }

    public void setListaVentaSucursal(List<VentaSucursal> listaVentaSucursal) {
        this.listaVentaSucursal = listaVentaSucursal;
    }

    public List<ImagenProducto> getImagenProductoList() {
        return imagenProductoList;
    }

    public void setImagenProductoList(List<ImagenProducto> imagenProductoList) {
        this.imagenProductoList = imagenProductoList;
    }

    public List<StockProducto> getStockProductoList() {
        return stockProductoList;
    }

    public void setStockProductoList(List<StockProducto> stockProductoList) {
        this.stockProductoList = stockProductoList;
    }

    public Sucursal getSucursalFK() {
        return sucursalFK;
    }

    public void setSucursalFK(Sucursal sucursalFK) {
        this.sucursalFK = sucursalFK;
    }

    public String getDetalleProducto() {
        return detalleProducto;
    }

    public void setDetalleProducto(String detalleProducto) {
        this.detalleProducto = detalleProducto;
    }

    public Proveedor getProveedorFK() {
        return proveedorFK;
    }

    public void setProveedorFK(Proveedor proveedorFK) {
        this.proveedorFK = proveedorFK;
    }

    public int getCantidadIngresada() {
        return cantidadIngresada;
    }

    public void setCantidadIngresada(int cantidadIngresada) {
        this.cantidadIngresada = cantidadIngresada;
    }

    public Date getFechaCantidadIngresada() {
        return fechaCantidadIngresada;
    }

    public void setFechaCantidadIngresada(Date fechaCantidadIngresada) {
        this.fechaCantidadIngresada = fechaCantidadIngresada;
    }

    public double getPorcentajeCompra() {
        return porcentajeCompra;
    }

    public void setPorcentajeCompra(double porcentajeCompra) {
        this.porcentajeCompra = porcentajeCompra;
    }

    public double getPorcentajeVenta() {
        return porcentajeVenta;
    }

    public void setPorcentajeVenta(double porcentajeVenta) {
        this.porcentajeVenta = porcentajeVenta;
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Producto[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml =new StringBuilder("<item>\n");
        xml.append("<id>").append(this.getId()).append("</id>\n").append("<descripcion>").append(this.getDescripcion()).append("</descripcion>\n")
                .append("<precioUnitarioCompra>").append(this.getPrecioUnitarioCompra()).append("</precioUnitarioCompra>\n")
                .append("<precioUnitarioVenta>").append(this.getPrecioUnitarioVenta()).append("</precioUnitarioVenta>\n")
                .append("<codigoBarra>").append(this.getCodigoBarra()).append("</codigoBarra>\n")
                .append("<primerCantidadInicial>").append(this.getCantidadInicial()).append("</primerCantidadInicial>\n")
                .append("<cantidadTotalActual>").append(this.getCantidadTotalActual()).append("</cantidadTotalActual>\n")
                .append("<cantidadIngresada>").append(this.getCantidadIngresada()).append("</cantidadIngresada>\n")
                .append("<fechaIngresoInicial>").append(this.getFechaIngresoInicial()).append("</fechaIngresoInicial>\n")
                .append("<fechaCantidadIngresada>").append(this.getFechaCantidadIngresada()).append("</fechaCantidadIngresada>\n")
                .append("<fechaUltimaActualizacion>").append(this.getFechaUltimaActualizacion()).append("</fechaUltimaActualizacion>\n")
                .append("<fechaUltimaVenta>").append(this.getFechaUltimaVenta()).append("</fechaUltimaVenta>\n")
                .append("<fechaUltimaIngreso>").append(this.getFechaUltimoIngreso()).append("</fechaUltimaIngreso>\n")
                .append("<fechaVencimiento>").append(this.getFechaVencimiento()).append("</fechaVencimiento>\n")
                .append("<detalle>").append(this.getDetalleProducto()).append("</detalle>\n")
                .append("<proveedorId>").append(this.getProveedorFK().getId()).append("</proveedorId>\n")
                .append("<personaId>").append(this.getPersonaFK().getId()).append("</personaId>\n")
                .append("<sucursalId>").append(this.getSucursalFK().getId()).append("</sucursalId>\n")
                .append("<porcentajeCompra>").append(this.getPorcentajeCompra()).append("</porcentajeCompra")
                .append("<porcentajeVenta>").append(this.getPorcentajeVenta()).append("</porcentajeVenta>");
                if(!this.getCompra().isEmpty()){
                    xml.append("<detalleCompra>\n");
                    StringBuilder detalleCompra = new StringBuilder(5);
                    for (CompraProducto compraProducto : compra) {
                        detalleCompra.append(compraProducto.toXML());
                    }
                    xml.append(detalleCompra.append("</detalleCompra>\n"));
                }
                if(!this.getVenta().isEmpty()){
                    xml.append("<listDetalleVenta>\n");
                    StringBuilder detalleVenta = new StringBuilder(5);
                    
                    for(VentaProducto ventaProducto: venta){
                    
                        detalleVenta.append(ventaProducto.toXML());
                    }
                    xml.append(detalleVenta.append("</listDetalleVenta>\n"));
                
                }
                if(!this.getStockProductoList().isEmpty()){
                        xml.append("<listDetalleStock>\n");
                    StringBuilder detalleStock = new StringBuilder(5);
                    
                    for(StockProducto stock: stockProductoList){
                    
                        detalleStock.append(stock.toXML());
                    }
                    xml.append(detalleStock.append("</listDetalleStock>\n"));
                
                }
                if(!this.getListaVentaSucursal().isEmpty()){
                       xml.append("<listDetalleVentaSucursal>\n");
                    StringBuilder detalleVentaSucursal = new StringBuilder(5);
                    
                    for(VentaSucursal ventaSucursal: listaVentaSucursal){
                    
                        detalleVentaSucursal.append(ventaSucursal.toXML());
                    }
                    xml.append(detalleVentaSucursal.append("</listDetalleVentaSucursal>\n"));
                
                }
                 if(!this.getImagenProductoList().isEmpty()){
                       xml.append("<listImagenes>\n");
                    StringBuilder detalleImagenes = new StringBuilder(5);
                    
                    for(ImagenProducto imagen: imagenProductoList){
                    
                        detalleImagenes.append(imagen.toXML());
                    }
                    xml.append(detalleImagenes.append("</listImagenes>\n"));
                
                }
    return xml.toString();
    }
}
