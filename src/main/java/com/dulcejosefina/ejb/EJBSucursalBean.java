/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Sucursal;
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
public class EJBSucursalBean {
    @PersistenceContext
    private EntityManager em;
    /**
     * Web service operation
     * @param nombreSucursal
     * @param descripcion
     * @return 
     */
    @WebMethod(operationName = "crearSucursal")
    public short crearSucursal(@WebParam(name = "nombreSucursal") String nombreSucursal,String descripcion) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(nombreSucursal.toUpperCase());
        sucursal.setDescripcion(descripcion.toUpperCase());
        em.persist(sucursal);
        em.flush();
        return sucursal.getId().shortValue();
    }
    @WebMethod(operationName = "buscarTodasLasSucursales")
    public String buscarTodasLasSucursales(){
       StringBuilder xml = new StringBuilder(5);
        Query buscarSucursales = em.createNamedQuery("Sucursal.findAll");
        List<Sucursal> lista = buscarSucursales.getResultList();        
        xml.append("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
        xml.append("<Lista>\n");
        for (Sucursal sucursal : lista) {
            xml.append(sucursal.toXML());
        }
        xml.append("</Lista>");
       
    return xml.toString();
    
    }
}
