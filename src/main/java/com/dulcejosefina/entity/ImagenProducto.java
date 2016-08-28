package com.dulcejosefina.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

@Entity
public class ImagenProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name = "ImagenProductoIdGen",table = "ID_GEN_IMAGEN_PROD", pkColumnName="FNAME",pkColumnValue="ImagenProducto", valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "ID_IMAGEN_PRODUCTO")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "PATHIMAGEN")
    private String pathImagenEnDisco;
    @Column(name="NOMBRE_IMAGEN")
    private String nombreImagen;
    @Column(name = "EXTENSION")
    private String extension;
    @Column(name = "MAGNITUD")
    private String magnitud;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Producto productoFK;
    
    
    public ImagenProducto(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathImagenEnDisco() {
        return pathImagenEnDisco;
    }

    public void setPathImagenEnDisco(String pathImagenEnDisco) {
        this.pathImagenEnDisco = pathImagenEnDisco;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    public Producto getProductoFK() {
        return productoFK;
    }

    public void setProductoFK(Producto productoFK) {
        this.productoFK = productoFK;
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
        if (!(object instanceof ImagenProducto)) {
            return false;
        }
        ImagenProducto other = (ImagenProducto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.dulcejosefina.entity.ImagenProducto[ id=" + id + " ]";
    }
    public String toXML(){
        StringBuilder xml = new StringBuilder("<itemImageProducto>");
    
        return xml.append("<id>").append(this.getId()).append("</id>").append("<path>").append(this.getPathImagenEnDisco()).append("</path").append("<nombre>").append(this.getNombreImagen()).append("</nombre>")
                .append("<extension>").append(this.getExtension()).append("</extension>").append("<magnitud>").append(this.getMagnitud()).append("</magnitud>").append("<productoId")
                .append(this.getProductoFK().getId()).append("</productoId>").append("</itemImageProducto>").toString();
    }
}
