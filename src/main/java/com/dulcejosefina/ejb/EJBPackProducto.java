/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.PackProducto;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
public class EJBPackProducto {
@PersistenceContext
private EntityManager em;
    public short crearPack(String nombrePack) {
        PackProducto pack = new PackProducto();
        pack.setDescripcion(nombrePack);
        em.persist(pack);
        return pack.getId().shortValue();
    }
    public String selectAllPackProducto(){
        StringBuilder lista = new StringBuilder( "<Lista>\n");
        Query consulta = em.createNamedQuery("PackProducto.fidAll");
        List<PackProducto>listaPack = consulta.getResultList();
        for (PackProducto packProducto : listaPack) {
            lista.append(packProducto.toXML());
        }
    
        return lista.append("</Lista>\n").toString();
    }
    
}
