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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
public class CajaEntradaSalida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "CajaEntradaSalidaIdGen",table = "ID_GEN_CAJA", pkColumnName="CESNAME",pkColumnValue="CajaEntradaSalida", valueColumnName="CESKEY",
    allocationSize=1)    
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "CajaEntradaSalidaIdGen")
    @Column(name = "ID_CAJA_ENTRADA_SALIDA")
    private Long id;
    @Column(name = "FECHA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @Column(name = "ENTRADA",columnDefinition = "DECIMAL(15,3) default'0.000'")
    private BigDecimal entrada;
    @Column(name = "SALIDA",columnDefinition = "DECIMAL(15, 3) default'0.000'")
    private BigDecimal salida;
    @Column(name = "ENTRADATARJETA", columnDefinition = "DECIMAL(15, 3) default'0.000'")
    private double entradaTarjeta;    
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "VENTASEFECTIVO", columnDefinition = "DECIMAL(15, 3) default'0.000'")
    private double ventasEfectivo;
    @Column(name = "ID_PERSONA")    
    private Integer idPersona;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "NUMEROCUPON",columnDefinition = "VARCHAR(20) default '0'")
    private String numerocupon;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ENEFECTIVO",columnDefinition = "CHAR(1) default '1'")
    private Character enefectivo;
    @Basic(fetch = FetchType.LAZY)        
    @Column(name = "HORA")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "DETALLE",columnDefinition = "VARCHAR(255)default''")
    private String detalle;    
    @OneToOne(mappedBy = "cajaFK")
    private VentaSucursal ventaSucursal;
    
    public CajaEntradaSalida(){}
    
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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
