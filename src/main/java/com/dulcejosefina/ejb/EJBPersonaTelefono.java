/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.PersonaTelefono;
import com.dulcejosefina.entity.Telefono;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Edgardo
 */
@Stateless
@LocalBean
public class EJBPersonaTelefono {
    @PersistenceContext
    private EntityManager em;

    public void agregarRelacionPersonaTelefono(Persona persona, Telefono telefono) {
        PersonaTelefono perTel=null;
        Query consulta = em.createQuery("SELECT p from PersonaTelefono p WHERE p.persona.id =:idPersona AND p.telefono.numero=:numero");
        consulta.setParameter("idPersona", persona.getId());
        consulta.setParameter("numero", telefono.getNumero());
        
        if(consulta.getResultList().isEmpty()){
                perTel = new PersonaTelefono(persona, telefono);
                
                em.persist(perTel);
        
        
        }
        
        
       
    }

    
}
