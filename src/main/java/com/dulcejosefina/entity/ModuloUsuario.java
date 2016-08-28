package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ModuloUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_MODULO_USUARIO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "CODIGOMODULO")
    private String codigoModulo;
    @Column(name = "NOMBREMODULO")
    private String nombreModulo;
    @ManyToOne(fetch = FetchType.LAZY)
    private PerfilUsuario perfilModulo;
    public ModuloUsuario(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public PerfilUsuario getPerfilModulo() {
        return perfilModulo;
    }

    public void setPerfilModulo(PerfilUsuario perfilModulo) {
        this.perfilModulo = perfilModulo;
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
        if (!(object instanceof ModuloUsuario)) {
            return false;
        }
        ModuloUsuario other = (ModuloUsuario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.Modulo[ id=" + id + " ]";
    }
    
}
