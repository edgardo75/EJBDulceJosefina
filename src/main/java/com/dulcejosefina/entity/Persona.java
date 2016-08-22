/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.entity;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edgardo
 */
@Entity
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;    
    @Id
    @TableGenerator(name = "PersonaIdGen",table = "ID_GEN_PER", pkColumnName="FNAME",pkColumnValue="Personas", valueColumnName="FKEY",
    allocationSize=1)
    @GeneratedValue(generator = "PersonaIdGen",strategy = GenerationType.TABLE)
    @Column(name = "ID_PERSONA")
    private long id;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "NUMERO_DOCUMENTO",unique = true,columnDefinition = "int")
    private int dni;    
    @Column(name = "CUIL",unique = true,columnDefinition = "int")
    private int cuil;
    @Column(name = "EMAIL",unique = true)
    private String email;
    @Column(name="LOGIN",unique = true,columnDefinition = "VARCHAR(50)")
    private String login;
    @Column(name="PASSWORD",columnDefinition = "VARCHAR(12) DEFAULT ''")
    private String password;
    @Column(name = "FECHACARGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    @Column(name = "ESTADO")
    private char estado;
    @Column(name = "DETALLES",columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String detalles;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    @Enumerated(EnumType.STRING)
    private TiposDocumento tipoDocumento;
    @Enumerated(EnumType.STRING)
    private TiposPersona tiposPersona;
    @OneToOne(orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "perfilPersona")
    private Perfil perfil;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "personaFK")
    private List<Producto> producto;

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

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public TiposDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public TiposPersona getTiposPersona() {
        return tiposPersona;
    }

    public void setTiposPersona(TiposPersona tiposPersona) {
        this.tiposPersona = tiposPersona;
    }

    public List<Producto> getProducto() {
        return producto;
    }

    public void setProducto(List<Producto> producto) {
        this.producto = producto;
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
        if (!Objects.equals(this.apellido, other.apellido)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Persona[ id=" + id + " ]";
    }
    
}
