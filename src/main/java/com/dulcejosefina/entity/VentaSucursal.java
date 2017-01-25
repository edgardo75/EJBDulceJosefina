package com.dulcejosefina.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@NamedQueries({@NamedQuery(name = "findVentasDiaBySucursalAndFechaYHora",query = "SELECT v FROM VentaSucursal v WHERE  v.fechaVenta = CAST(CURRENT_DATE as DATE) and v.horaVenta BETWEEN ?1 AND ?2 and v.anulado=0 order by v.id desc"),
                @NamedQuery(name = "findVentaDelDiaPorFecha",query = "SELECT v FROM VentaSucursal v WHERE v.fechaVenta = Cast(:fecha as DATE) order by v.id desc"),
                @NamedQuery(name = "findAllVentas",query = "SELECT v FROM VentaSucursal v order by v.id desc"),
                @NamedQuery(name = "findVentaBySucursal",query = "SELECT v FROM VentaSucursal v WHERE v.id =:idVenta AND v.sucursalFK.id =:idSucursal AND cast(v.anulado as integer) = 0 order by v.id desc")})
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
    @Column(name = "HORA_VENTA")
    @Temporal(TemporalType.TIME)
    private Date horaVenta;
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
    @Column(name = "ID_USUARIO_EXPIDIO_VENTA")
    private long idUsuarioExpidioVenta;
    @Column(name="NOMBRE_PERSONA_CLIENTE",length = 50)
    private String nombre;
    @Column(name="APELLIDO_PERSONA_CLIENTE",length = 50)
    private String apellido;    
    @Column(name="ANULADO")
    private int anulado;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "ventaSucursal",orphanRemoval = true,targetEntity = DetalleVentaSucursal.class)
    private List<DetalleVentaSucursal>listaDetalleVentaSucursal;
    
    @ManyToOne 
    Persona persona;
    @OneToOne()
    private CajaEntradaSalida cajaFK;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "ventaSucursal",orphanRemoval = true,targetEntity = HistoricoVentaSucursal.class)
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

    public int getAnulado() {
        return anulado;
    }

    public void setAnulado(int anulado) {
        this.anulado = anulado;
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

    public Date getHoraVenta() {
        return horaVenta;
    }

    public void setHoraVenta(Date horaVenta) {
        this.horaVenta = horaVenta;
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

   

    public List<DetalleVentaSucursal> getListaDetalleVentaSucursal() {
        return listaDetalleVentaSucursal;
    }

    public void setListaDetalleVentaSucursal(List<DetalleVentaSucursal> listaDetalleVentaSucursal) {
        this.listaDetalleVentaSucursal = listaDetalleVentaSucursal;
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

    public long getIdUsuarioExpidioVenta() {
        return idUsuarioExpidioVenta;
    }

    public void setIdUsuarioExpidioVenta(long idUsuarioExpidioVenta) {
        this.idUsuarioExpidioVenta = idUsuarioExpidioVenta;
    }

   

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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
    StringBuilder xml = new StringBuilder(5);
    
    
                    xml.append("<id>").append(this.getId()).append("</id>")
                            .append("<hora>").append(this.getHoraVenta()!=null?new SimpleDateFormat("HH:mm ss").format(this.getHoraVenta()):0).append("</hora>")
                            .append("<fecha>").append(this.getFechaVenta()!=null?new SimpleDateFormat("dd/MM/yyyy").format(this.getFechaVenta()):0).append("</fecha>")
                            .append("<cantidad>").append(this.getCantidad()).append("</cantidad>")
                            .append("<porcentajeDescuento>").append(this.getPorcentajeDescuento()).append("</porcentajeDescuento>")
                            .append("<porcentajeRecargo>").append(this.getPorcentajeRecargo()).append("</porcentajeRecargo>")
                            .append("<descuentoPesos>").append(this.getDescuentoPesos()).append("</descuentoPesos>")
                            .append("<recargoPesos>").append(this.getRecargoPesos()).append("</recargoPesos>")
                            .append("<totalGeneral>").append(DecimalFormat.getInstance().format(this.getTotalGeneral())).append("</totalGeneral>")
                            .append("<totalAPagar>").append(DecimalFormat.getInstance().format(this.getTotalAPagar())).append("</totalAPagar>")
                            .append("<nombre>").append(this.getNombre()).append("</nombre>")
                            .append("<apellido>").append(this.getApellido()).append("</apellido>")           
                            .append("<anulado>").append(this.getAnulado()).append("</anulado>")
                            .append("<sucursal>").append(this.getSucursalFK().getNombre()).append("</sucursal>");                            

                    if(!this.getListaDetalleVentaSucursal().isEmpty()){
                        xml.append("<detalleVenta>");
                        StringBuilder xmlDetalle = new StringBuilder(5);
                            for (DetalleVentaSucursal detalle : listaDetalleVentaSucursal) {
                                xmlDetalle.append(detalle.toXML());
                            }
        
        
        xml.append(xmlDetalle).append("</detalleVenta>");
    
    
    }
            
        return xml.toString();
    }
    
}
