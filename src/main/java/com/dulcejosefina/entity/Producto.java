package com.dulcejosefina.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
@Entity
@NamedQueries({@NamedQuery(name = "Producto.findAll",query = "SELECT p FROM Producto p order by p.id ASC"),
    @NamedQuery(name = "proveedorFindAll",query = "SELECT p FROM Producto p WHERE p.proveedorFK.id =:id"),
    @NamedQuery(name = "findProductById",query = "SELECT p FROM Producto p WHERE p.id =:id"),
    @NamedQuery(name="findProductoByDescripcionAprox",query="SELECT p FROM Producto p WHERE LOWER(p.descripcion) LIKE :descripcion or LOWER(p.descripcion) LIKE :descripcion1 order by p.descripcion asc"),
    @NamedQuery(name="findProductoByDescripcion",query="SELECT p FROM Producto p WHERE LOWER(p.descripcion) =:descripcion"),
    @NamedQuery(name = "findVentaProducto",query = "SELECT p FROM Producto p where p.precioUnitarioVenta>0"),
    @NamedQuery(name = "findProductoByCodigoBarraConVentas",query = "SELECT p FROM Producto p WHERE p.codigoBarra =:codigo AND p.precioUnitarioVenta > 0"),
        @NamedQuery(name = "findProductoByCodigoBarraOnly",query = "SELECT p FROM Producto p WHERE p.codigoBarra =:codigo")
})
@Table(name = "PRODUCTO",indexes = {@Index(name = "codigoBarra_Index",columnList = "CODIGO_BARRA")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "ProductoIdGen",table = "ID_GEN_PRODUCTO", pkColumnName="PRONAME",pkColumnValue="Producto", valueColumnName="PROKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "ProductoIdGen")
    @Id    
    @Column(name = "ID_PRODUCTO")    
    private Long id;    
    @Column(name = "DESCRIPCION",nullable = false)
    private String descripcion;
    @Column(name = "PRECIO_UNITARIO_COMPRA",precision = 15,scale = 3,columnDefinition = "DECIMAL(15,3) default'0.000'")
    private BigDecimal precioUnitarioCompra;
    @Column(name = "PRECIO_UNITARIO_VENTA",precision = 15,scale = 3,columnDefinition = "DECIMAL(15,3) default'0.000'")
    private BigDecimal precioUnitarioVenta;
    @Column(name = "CODIGO_BARRA",columnDefinition = "VARCHAR(100)DEFAULT'null'",unique = true,nullable = true)
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
    private Date fechaUltimaCompra;
    @Column(name = "FECHA_ULTIMA_VENTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimaVenta;
    @Column(name = "FECHA_ULTIMA_ACTUALIZACION")
    @Temporal(javax.persistence.TemporalType.DATE)    
    private Date fechaUltimaActualizacion;
    @Column(name = "FECHA_ULTIMO_INGRESO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaUltimoIngreso;    
    @Column(name = "PORCENTAJE_COMPRA",columnDefinition = "DECIMAL(12,2)DEFAULT'0'")
    private double porcentajeCompra;
    @Column(name = "PORCENTAJE_VENTA",columnDefinition = "DECIMAL(12,2)DEFAULT'0'")
    private double porcentajeVenta;
    @Column(name="FRACCIONADO")
    private int fraccionado;
    @Column(name = "DETALLE_PRODUCTO",columnDefinition = "VARCHAR(255) DEFAULT''")
    private String detalleProducto;
    @Column(name = "DETALLE_COMPRA")
    private String detalleCompra;
    @Column(name = "DETALLE_VENTA")
    private String detalleVenta;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Proveedor.class)
    private Proveedor proveedorFK;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "productoFK",targetEntity = CompraProducto.class)
    private List<CompraProducto> compra;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "productoFK",targetEntity = VentaProducto.class)
    private List<VentaProducto> venta;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "productoFK")
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Persona.class)
    private Persona personaFK;
    @OneToMany(mappedBy = "producto")
    private List<DetalleVentaSucursal>listaDetalleVentaSucursal;
    @OneToMany(mappedBy = "productoFK",targetEntity = ImagenProducto.class)
    private List<ImagenProducto> imagenProductoList;
    @OneToMany(mappedBy = "productoFK",targetEntity = StockProducto.class)
    private List<StockProducto>stockProductoList;
    @OneToMany(mappedBy = "producto",targetEntity = DetallePresupuesto.class)
    private List<DetallePresupuesto> detallepresupuestosList;
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

    public Date getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(Date fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
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

    public List<DetalleVentaSucursal> getListaDetalleVentaSucursal() {
        return listaDetalleVentaSucursal;
    }

    public void setListaDetalleVentaSucursal(List<DetalleVentaSucursal> listaDetalleVentaSucursal) {
        this.listaDetalleVentaSucursal = listaDetalleVentaSucursal;
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

    public String getDetalleCompra() {
        return detalleCompra;
    }

    public void setDetalleCompra(String detalleCompra) {
        this.detalleCompra = detalleCompra;
    }

    public String getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(String detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public Date getFechaUltimoIngreso() {
        return fechaUltimoIngreso;
    }

    public void setFechaUltimoIngreso(Date fechaUltimoIngreso) {
        this.fechaUltimoIngreso = fechaUltimoIngreso;
    }

    public List<DetallePresupuesto> getDetallepresupuestosList() {
        return detallepresupuestosList;
    }

    public void setDetallepresupuestosList(List<DetallePresupuesto> detallepresupuestosList) {
        this.detallepresupuestosList = detallepresupuestosList;
    }

    public int getFraccionado() {
        return fraccionado;
    }

    public void setFraccionado(int fraccionado) {
        this.fraccionado = fraccionado;
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
                .append("<codigoBarra>").append("<![CDATA[").append(this.getCodigoBarra()).append("]]>").append("</codigoBarra>\n")
                .append("<primerCantidadInicial>").append(this.getCantidadInicial()).append("</primerCantidadInicial>\n")
                .append("<cantidadTotalActual>").append(this.getCantidadTotalActual()).append("</cantidadTotalActual>\n")
                .append("<cantidadIngresada>").append(this.getCantidadIngresada()).append("</cantidadIngresada>\n")
                .append("<fraccionado>").append(this.getFraccionado()).append("</fraccionado>\n")
                .append("<fechaIngresoInicial>").append(this.getFechaIngresoInicial()!=null?DateFormat.getDateInstance().format(this.getFechaIngresoInicial()):0).append("</fechaIngresoInicial>\n")
                .append("<fechaCantidadIngresada>").append(this.getFechaCantidadIngresada()!=null?DateFormat.getDateInstance().format(this.getFechaCantidadIngresada()):0).append("</fechaCantidadIngresada>\n")
                .append("<fechaUltimaActualizacion>").append(this.getFechaUltimaActualizacion()!=null?DateFormat.getDateInstance().format(this.getFechaUltimaActualizacion()):0).append("</fechaUltimaActualizacion>\n")
                .append("<fechaUltimaVenta>").append(this.getFechaUltimaVenta()!=null?new SimpleDateFormat("dd/MM/yyyy").format(this.getFechaUltimaVenta()):0).append("</fechaUltimaVenta>\n")
                .append("<fechaUltimaIngreso>").append(this.getFechaUltimoIngreso()!=null?new SimpleDateFormat("dd/MM/yyyy").format(this.getFechaUltimoIngreso()):0).append("</fechaUltimaIngreso>\n")
                .append("<fechaVencimiento>").append(this.getFechaVencimiento()!=null?DateFormat.getDateInstance().format(this.getFechaVencimiento()):0 ).append("</fechaVencimiento>\n")
                .append("<detalle>").append(this.getDetalleProducto()!=null?this.getDetalleProducto():"").append("</detalle>\n")
                .append("<sucursal>")
                    .append("<id>").append(this.getSucursalFK().getId()).append("</id>\n")
                    .append("<nombre>").append(this.getSucursalFK().getNombre()).append("</nombre>")
                .append("</sucursal>")
                .append("<proveedor>")
                        .append("<id>").append(this.getProveedorFK().getId()).append("</id>\n")
                        .append("<nombre>").append("<![CDATA[").append(this.getProveedorFK().getNombre()).append("]]>").append("</nombre>")
                .append("</proveedor>")
                .append("<personaId>").append(this.getPersonaFK().getId()).append("</personaId>\n")
                
                .append("<porcentajeCompra>").append(this.getPorcentajeCompra()).append("</porcentajeCompra>")
                .append("<porcentajeVenta>").append(this.getPorcentajeVenta()).append("</porcentajeVenta>");
                if(!this.getCompra().isEmpty()){
                    xml.append("<detalleCompra>\n");
                    StringBuilder detailCompra = new StringBuilder(5);
                    
                    detailCompra.append("<detalle>").append(this.getDetalleCompra()!=null?this.getDetalleCompra():"").append("</detalle>")
                            .append("<porcentaje>").append(this.getPorcentajeCompra()).append("</porcentaje>")
                            .append("<fecha>").append(new SimpleDateFormat("dd/MM/yyyy").format(this.getFechaUltimaCompra())).append("</fecha>");
                    for (CompraProducto compraProducto : compra) {
                        if(compraProducto.getPrecio().doubleValue()>0){
                            detailCompra.append(compraProducto.toXML());
                        }
                    }
                    xml.append(detailCompra.append("</detalleCompra>\n"));
                }
                if(!this.getVenta().isEmpty()){
                    xml.append("<listDetalleVenta>\n");
                    StringBuilder detailVenta = new StringBuilder(5);
                    detailVenta.append("<detalle>").append(this.getDetalleVenta()!=null?this.getDetalleVenta():"").append("</detalle>")
                            .append("<porcentaje>").append(this.getPorcentajeVenta()).append("</porcentaje>")
                            .append("<fecha>").append(this.getFechaUltimaVenta()!=null?DateFormat.getDateInstance().format(this.getFechaUltimaVenta()):0).append("</fecha>");
                    for(VentaProducto ventaProducto: venta){                    
                        if(ventaProducto.getPrecio().doubleValue()>0){
                            detailVenta.append(ventaProducto.toXML());
                        }
                    }
                    xml.append(detailVenta.append("</listDetalleVenta>\n"));                
                }
                if(!this.getStockProductoList().isEmpty()){
                        xml.append("<listDetalleStock>\n");
                    StringBuilder detalleStock = new StringBuilder(5);
                    List<StockProducto>lista = this.getStockProductoList();
                    for(StockProducto stock: lista){
                    
                        detalleStock.append(stock.toXML());
                    }
                    xml.append(detalleStock.append("</listDetalleStock>\n"));
                
                }
                if(!this.getListaDetalleVentaSucursal().isEmpty()){
                       xml.append("<listDetalleVentaSucursal>\n");
                    StringBuilder detalleVentaSucursal = new StringBuilder(5);
                    List<DetalleVentaSucursal>lista = this.getListaDetalleVentaSucursal();
                    for(DetalleVentaSucursal ventaSucursal: lista){
                    
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
    return xml.append("</item>\n").toString();
    }
}
