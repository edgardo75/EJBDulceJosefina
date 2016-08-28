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
@Stateless
@LocalBean
@WebService
public class EJBProductoBean {
@PersistenceContext
private EntityManager em;
    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "selectAllProducto")
    public String selectAllProducto() {
        StringBuilder xml = new StringBuilder("<Lita>\n");
        Query consulta = em.createNamedQuery("Producto.fidAll");
        List<Producto>lista = consulta.getResultList();
        for (Producto producto : lista) {
            xml.append(producto.toXML());
        }
        return xml.toString();
    }

   
}
