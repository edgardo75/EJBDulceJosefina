/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Fabrica;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Edgardo
 */
@WebService
@Stateless
@LocalBean
public class EJBFabricaBean {
@PersistenceContext
private EntityManager em;
    /**
     * Web service operation
     * @param nombreFabrica
     * @param detalles
     * @return 
     */
    @WebMethod(operationName = "crearFabrica")
    public short crearFabrica(@WebParam(name = "nombreFabrica") String nombreFabrica, @WebParam(name = "detalles") String detalles) {
        Fabrica fabrica = new Fabrica();
        fabrica.setNombre(nombreFabrica);
        fabrica.setDetalles(detalles);
        em.persist(fabrica);
        em.flush();
        return fabrica.getId().shortValue();
    }

    
}
