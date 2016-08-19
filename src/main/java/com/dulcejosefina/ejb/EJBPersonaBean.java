/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

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
public class EJBPersonaBean {
    @PersistenceContext
    private EntityManager em;

    public short crearPersona(String xmlPersona) {
        System.out.println("HOLA");
        return 0;
    }

   
}
