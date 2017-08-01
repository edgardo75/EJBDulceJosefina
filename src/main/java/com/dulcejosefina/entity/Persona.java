package com.dulcejosefina.entity;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({@NamedQuery(name = "empleadoFindAllEmpleadoYJefe",query = "SELECT p FROM Persona p WHERE p.tiposPersona = com.dulcejosefina.entity.TipoPersona.EMPLEADO OR p.tiposPersona = com.dulcejosefina.entity.TipoPersona.JEFE"),
@NamedQuery(name="findAllJefeOnly",query = "SELECT p FROM Persona p WHERE p.tiposPersona = com.dulcejosefina.entity.TipoPersona.JEFE"),    
@NamedQuery(name="findAllJefeOnlyAdmin",query = "SELECT p FROM Persona p WHERE p.tiposPersona = com.dulcejosefina.entity.TipoPersona.JEFE AND p.rol = com.dulcejosefina.entity.RolPersona.ADMINISTRADOR"),    
@NamedQuery(name="personaFindAll",query = "SELECT p  FROM Persona p ORDER BY p.fechaCarga desc")})
@Table(name = "PERSONA",indexes = {@Index(name = "dni_Index",columnList = "NUMERO_DOCUMENTO"),
    @Index(name = "cuil_Index",columnList = "CUIL"),
    @Index(name = "email_Index",columnList = "EMAIL"),
    @Index(name = "login_Index",columnList = "LOGIN")
})
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;    
    @TableGenerator(name = "PersonaIdGen",table = "ID_GEN_PER", pkColumnName="PERNAME",pkColumnValue="Persona", valueColumnName="PERKEY",
    allocationSize=1)
    @GeneratedValue(generator = "PersonaIdGen",strategy = GenerationType.TABLE)
    @Id    
    @Column(name = "ID_PERSONA")
    private long id;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "NUMERO_DOCUMENTO",nullable = true,columnDefinition = "NUMERIC(18,0) default'null'")
    private long dni;    
    @Column(name = "CUIL", nullable = true,columnDefinition = "NUMERIC(18,0) default'null'")
    private long cuil;    
    @Column(name = "EMAIL",columnDefinition = "VARCHAR(100)default'null'")
    private String email;
    @Column(name="LOGIN",columnDefinition = "VARCHAR(12)default'null'")
    private String login;
    @Column(name="PASSWORD",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String password;
    @Column(name="KEYPASSWORD",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String keyPassword;
    @Column(name = "FECHACARGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    @Column(name = "ESTADO",columnDefinition = "VARCHAR(1) DEFAULT'1'")
    private char estado;
    @Column(name = "DETALLES",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String detalles;
    @Column(name = "GENERO",columnDefinition = "VARCHAR(10) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private Genero genero;
    @Column(name = "ROLPERSONA",columnDefinition = "VARCHAR(15) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private RolPersona rol;
    @Column(name = "TIPO_DOCUMENTO",columnDefinition = "VARCHAR(8) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
    @Column(name = "TIPO_PERSONA",columnDefinition = "VARCHAR(8) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private TipoPersona tiposPersona;
    @Column(name = "PUNTOS_CLIENTE_PREFENCIAL",columnDefinition = "INTEGER DEFAULT'0'")
    private int puntosClientePrefencial;
    @Column(name = "CLIENTE_PREFERENCIAL",columnDefinition = "CHAR DEFAULT'0'")
    private char clientePerefencial;        
    @Column(name = "FECHA_ULTIMA_COMPRA_CLIENTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaCompraCliente;
    @OneToOne(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "perfilPersona")
    private PerfilUsuario perfil;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "personaFK")
    private List<Producto> producto;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "persona")
    private List<VentaSucursal> ventaSucursal;
    @OneToMany(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "persona")
    private List<PersonaTelefono>listaPersonaTelefono;
    
    public Persona(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public long getCuil() {
        return cuil;
    }

    public void setCuil(long cuil) {
        this.cuil = cuil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public TipoPersona getTiposPersona() {
        return tiposPersona;
    }

    public void setTiposPersona(TipoPersona tiposPersona) {
        this.tiposPersona = tiposPersona;
    }

    public List<Producto> getProducto() {
        return producto;
    }

    public void setProducto(List<Producto> producto) {
        this.producto = producto;
    }

    public List<VentaSucursal> getVentaSucursal() {
        return ventaSucursal;
    }

    public RolPersona getRol() {
        return rol;
    }

    public void setRol(RolPersona rol) {
        this.rol = rol;
    }

    public void setVentaSucursal(List<VentaSucursal> ventaSucursal) {
        this.ventaSucursal = ventaSucursal;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public Integer getPuntosClientePrefencial() {
        return puntosClientePrefencial;
    }

    public void setPuntosClientePrefencial(Integer puntosClientePrefencial) {
        this.puntosClientePrefencial = puntosClientePrefencial;
    }

    public char getPerefencial() {
        return clientePerefencial;
    }

    public void setPerefencial(char clientePerefencial) {
        this.clientePerefencial = clientePerefencial;
    }

    public char getClientePerefencial() {
        return clientePerefencial;
    }

    public void setClientePerefencial(char clientePerefencial) {
        this.clientePerefencial = clientePerefencial;
    }

    

    public Date getFechaUltimaCompraCliente() {
        return fechaUltimaCompraCliente;
    }

    public void setFechaUltimaCompraCliente(Date fechaUltimaCompraCliente) {
        this.fechaUltimaCompraCliente = fechaUltimaCompraCliente;
    }

    public List<PersonaTelefono> getListaPersonaTelefono() {
        return listaPersonaTelefono;
    }

    public void setListaPersonaTelefono(List<PersonaTelefono> listaPersonaTelefono) {
        this.listaPersonaTelefono = listaPersonaTelefono;
    }
    
   

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + Objects.hashCode(this.nombre);
        hash = 97 * hash + Objects.hashCode(this.apellido);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Persona other = (Persona) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return Objects.equals(this.apellido, other.apellido);
    }
    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Persona[ id=" + id + " ]";
    }
    public String toXML(){        
    StringBuilder xml = new StringBuilder(5);    
    String fechaUltimaCompra = !(this.getFechaUltimaCompraCliente()==null)?DateFormat.getDateTimeInstance().format(this.getFechaUltimaCompraCliente()):"";
    xml.append("<id>").append(this.getId()).append("</id>").append("<nombre>").append(this.getNombre()).append("</nombre>").append("<apellido>").append(this.getApellido()).append("</apellido>").append("<numeroDocumento>").append(this.getDni())
       .append("</numeroDocumento>").append("<cuil>").append(this.getCuil()).append("</cuil>").append("<email>").append(this.getEmail()).append("</email>").append("<login>").append(this.getLogin()).append("</login>")
            .append("<fechaCarga>").append(DateFormat.getDateTimeInstance().format(this.getFechaCarga())).append("</fechaCarga>").append("<detalle>").append(this.getDetalles()).append("</detalle>").append("<genero>").append(this.getGenero().toString()).append("</genero>")
            .append("<tipoDocu>").append(this.getTipoDocumento().toString()).append("</tipoDocu>").append("<rolPersona>").append(this.getRol().toString()).append("</rolPersona>").append("<tipoPersona>").append(this.getTiposPersona().toString()).append("</tipoPersona>")
            .append("<estado>").append(this.getEstado()).append("</estado>").append("<clientePreferencial>").append(this.getPerefencial()).append("</clientePreferencial>")
            .append("<puntosClientePreferencial>").append(this.getPuntosClientePrefencial()).append("</puntosClientePreferencial>")
            .append("<fechaUltimaCompraCliente>").append(fechaUltimaCompra).append("</fechaUltimaCompraCliente>");
            xml.append("<personaTelefono>");
            if(this.getListaPersonaTelefono().isEmpty()){
                xml.append("</personaTelefono>");
            }else{
                List<PersonaTelefono>lista = this.getListaPersonaTelefono();
                for(PersonaTelefono personaTelefono:lista){
                    xml.append(personaTelefono.toXML());
                }
                xml.append("</personaTelefono>");
            }
            
            
        return xml.toString();
    }
    
}
