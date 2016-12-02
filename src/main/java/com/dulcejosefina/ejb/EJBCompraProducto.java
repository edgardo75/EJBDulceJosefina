/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Edgardo
 */
@WebService
@Stateless
@LocalBean
public class EJBCompraProducto {
@PersistenceContext
private EntityManager em;
    @WebMethod
    public String buscarCompraProducto(long idProducto){
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query consulta = em.createQuery("SELECT p FROM Producto p where p.id=:id");
        consulta.setParameter("id", idProducto);
        List<Producto>lista = consulta.getResultList();
        for (Producto producto : lista) {
            xml.append(producto.toXML());
        }
    return xml.append("</Lista>").toString();
    }
}
