package com.dulcejosefina.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({@NamedQuery(name = "empleadoFindAllEmpleadoYJefe",query = "SELECT p FROM Persona p WHERE p.tiposPersona = com.dulcejosefina.entity.TipoPersona.EMPLEADO OR p.tiposPersona = com.dulcejosefina.entity.TipoPersona.JEFE"),
@NamedQuery(name="personaFindAll",query = "SELECT p FROM Persona p ORDER BY p.fechaCarga desc")})
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;    
    @Id
    @TableGenerator(name = "PersonaIdGen",table = "ID_GEN_PER", pkColumnName="FNAME",pkColumnValue="Persona", valueColumnName="FKEY",
    allocationSize=1)
    @GeneratedValue(generator = "PersonaIdGen",strategy = GenerationType.TABLE)
    @Column(name = "ID_PERSONA")
    private long id;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "NUMERO_DOCUMENTO",unique = true,columnDefinition = "INTEGER default'null'")
    private int dni;    
    @Column(name = "CUIL",unique = true,columnDefinition = "INTEGER default'null'")
    private int cuil;    
    @Column(name = "EMAIL",unique = true,columnDefinition = "VARCHAR(100)default'null'")
    private String email;
    @Column(name="LOGIN",unique = true,columnDefinition = "VARCHAR(12)default'null'")
    private String login;
    @Column(name="PASSWORD",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String password;
    @Column(name="KEYPASSWORD",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String keyPassword;
    @Column(name = "FECHACARGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    @Column(name = "ESTADO")
    private char estado;
    @Column(name = "DETALLES",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String detalles;
    @Column(name = "GENERO",columnDefinition = "VARCHAR(10) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private Genero genero;
    @Column(name = "TIPO_DOCUMENTO",columnDefinition = "VARCHAR(8) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
    @Column(name = "TIPO_PERSONA",columnDefinition = "VARCHAR(8) DEFAULT ''")
    @Enumerated(EnumType.STRING)
    private TipoPersona tiposPersona;
    @OneToOne(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "perfilPersona")
    private PerfilUsuario perfil;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "personaFK")
    private List<Producto> producto;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "persona")
    private List<VentaSucursal> ventaSucursal;
    
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

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getCuil() {
        return cuil;
    }

    public void setCuil(int cuil) {
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

    public void setVentaSucursal(List<VentaSucursal> ventaSucursal) {
        this.ventaSucursal = ventaSucursal;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
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
    xml.append("<id>").append(this.getId()).append("</id>").append("<nombre>").append(this.getNombre()).append("</nombre>").append("<apellido>").append(this.getApellido()).append("</apellido>").append("<numeroDocumento>").append(this.getDni())
       .append("</numeroDocumento>").append("<cuil>").append(this.getCuil()).append("</cuil>").append("<email>").append(this.getEmail()).append("</email>").append("<login>").append(this.getLogin()).append("</login>")
            .append("<fechaCarga>").append(SimpleDateFormat.getDateTimeInstance().format(this.getFechaCarga())).append("</fechaCarga>").append("<detalle>").append(this.getDetalles()).append("</detalle>").append("<genero>").append(this.getGenero().toString()).append("</genero>")
            .append("<tipoDocu>").append(this.getTipoDocumento().toString()).append("</tipoDocu>").append("<tipoPersona>").append(this.getTiposPersona().toString()).append("</tipoPersona>")
            .append("<estado>").append(this.getEstado()).append("</estado>");
        return xml.toString();
    }
    
}
