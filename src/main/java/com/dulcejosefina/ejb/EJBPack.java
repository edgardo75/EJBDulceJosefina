/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Pack;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
public class EJBPack {
@PersistenceContext
private EntityManager em;
    public short crearPack(String nombrePack) {
        Pack pack = new Pack();
        pack.setDescripcion(nombrePack);
        em.persist(pack);
        return pack.getId().shortValue();
    }

    
}
