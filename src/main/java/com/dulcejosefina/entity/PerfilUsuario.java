package com.dulcejosefina.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class PerfilUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_PERFIL_USUARIO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "CODIGO_PERFIL",columnDefinition = "varchar(45)")
    private String codigoPerfil;
    @Column(name = "NOMBRE_PERFIL",columnDefinition = "varchar(45)")
    private String nombrePerfil;
    @OneToOne(optional = false)
    private Persona perfilPersona;
    @OneToMany(mappedBy = "perfilModulo",targetEntity = ModuloUsuario.class)
    private List<ModuloUsuario> modulo;
    public PerfilUsuario(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(String codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }   

    public Persona getPerfilPersona() {
        return perfilPersona;
    }

    public void setPerfilPersona(Persona perfilPersona) {
        this.perfilPersona = perfilPersona;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public List<ModuloUsuario> getModulo() {
        return modulo;
    }

    public void setModulo(List<ModuloUsuario> modulo) {
        this.modulo = modulo;
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
        if (!(object instanceof PerfilUsuario)) {
            return false;
        }
        PerfilUsuario other = (PerfilUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Perfil[ id=" + id + " ]";
    }
    
}
