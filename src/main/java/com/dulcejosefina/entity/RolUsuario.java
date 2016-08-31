package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

@Entity
public class RolUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_ROL_USUARIO")    
    @TableGenerator(name = "RolUsuarioIdGen",table = "ID_GEN_ROLUSER", pkColumnName="ROLUSERNAME",pkColumnValue="RolUsuario", valueColumnName="ROLUSERKEY",
    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "RolUsuarioIdGen")
    private Long id;
    @Column(name = "CODIGOROLE",columnDefinition = "varchar(45)")
    private String codigoRole;
    @Column(name = "NOMBREROLE",columnDefinition = "varchar(45)")
    private String nombreRole;
    @OneToOne()
    private Persona persona;

    public RolUsuario(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoRole() {
        return codigoRole;
    }

    public void setCodigoRole(String codigoRole) {
        this.codigoRole = codigoRole;
    }

    public String getNombreRole() {
        return nombreRole;
    }

    public void setNombreRole(String nombreRole) {
        this.nombreRole = nombreRole;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
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
        if (!(object instanceof RolUsuario)) {
            return false;
        }
        RolUsuario other = (RolUsuario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Role[ id=" + id + " ]";
    }
    
}
