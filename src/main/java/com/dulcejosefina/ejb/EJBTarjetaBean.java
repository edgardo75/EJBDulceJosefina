/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Tarjeta;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebMethod;
import javax.jws.WebParam;
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
public class EJBTarjetaBean {
@PersistenceContext
private EntityManager em;

    /**
     * Web service operation
     * @param nombreTarjeta
     * @return 
     */
    @WebMethod(operationName = "crearTarjeta")
    public short crearTarjeta(@WebParam(name = "nombreTarjeta") String nombreTarjeta) {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNombre(nombreTarjeta.toUpperCase());
        em.persist(tarjeta);
        return tarjeta.getId().shortValue();
    }
    @WebMethod(operationName = "buscarTodasLasTarjetas")
    public String buscarTodasLasTarjetas(){
        StringBuilder xml = new StringBuilder(5);
        Query consulta = em.createNamedQuery("Tarjeta.findAll");
        xml.append("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
        xml.append("<Lista>\n");
        List<Tarjeta> lista = consulta.getResultList();
        for (Tarjeta tarjeta : lista) {
            xml.append(tarjeta.toXML());
        }    
        return xml.toString();
    }

}
