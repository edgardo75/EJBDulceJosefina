/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.PersonaTelefono;
import com.dulcejosefina.entity.Telefono;
import com.dulcejosefina.utils.DatosPersona;
import com.dulcejosefina.utils.TelefonoItem;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Edgardo
 */
@Stateless
@LocalBean
public class EJBTelefonoBean {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private EJBPersonaTelefono personaTelefono;
    
    public Persona insertarListaTelefonoPersona(Persona persona,DatosPersona datosPersona){
        
        List<TelefonoItem>lista = datosPersona.getDatosTelefono().getList();
        Telefono item = null;
        
        for(TelefonoItem telefono:lista){
            System.out.print(datosPersona.getDni()+" "+telefono.getNumero()+" "+telefono.getPrefijo().trim());
            Query consulta = em.createQuery("SELECT t FROM Telefono t WHERE t.numero=:numero AND t.prefijo=:prefijo");
            consulta.setParameter("numero", Long.valueOf(telefono.getNumero().trim()));
            consulta.setParameter("prefijo", Long.valueOf(telefono.getPrefijo().trim()));
            //Telefono item = em.find(Telefono.class,telefono.getId());
            
            if(consulta.getResultList().isEmpty()){
                
               item = new Telefono(Long.valueOf(telefono.getNumero().trim()), Long.valueOf(telefono.getPrefijo().trim()));
                
                
                
//                em.persist(perTel);
//                Query consulta1 = em.createQuery("SELECT p FROM PersonaTelefono p WHERE p.telefono.numero =:numero");
//                            consulta1.setParameter("numero", Long.valueOf(telefono.getNumero()));
//                item.setTelefonoPersona(consulta1.getResultList());
                
                em.persist(item);
                
                this.personaTelefono.agregarRelacionPersonaTelefono(persona, item);
                
                
               actualizarListaTelefono(item,persona);
            
            }else{
                
                if(telefono.getId()>0){
                    Telefono phone = em.find(Telefono.class, telefono.getId());
                    this.personaTelefono.agregarRelacionPersonaTelefono(persona, phone);
                    actualizarListaTelefono(phone, persona);
                }else{
                    List<Telefono> listaTelefono = consulta.getResultList();
                    for (Telefono telefono1 : listaTelefono) {
                        this.personaTelefono.agregarRelacionPersonaTelefono(persona, telefono1);
                        actualizarListaTelefono(telefono1, persona);
                    }
                }
                
            
            }
        }
        
        
        
        Query consultaPersona = em.createQuery("SELECT p FROM PersonaTelefono p WHERE p.persona.id =:idPersona");
        consultaPersona.setParameter("idPersona", persona.getId());
        persona.setListaPersonaTelefono(consultaPersona.getResultList());
        em.merge(persona);
        
      

                        
            return persona;
    }

    private void actualizarListaTelefono(Telefono item, Persona persona) {
         Query consultaTelefono = em.createQuery("select p FROM PersonaTelefono p WHERE p.telefono.numero =:numero AND p.persona.id=:idPersona");
                    consultaTelefono.setParameter("numero", item.getNumero());
                    consultaTelefono.setParameter("idPersona", persona.getId());
                    item.setTelefonoPersona(consultaTelefono.getResultList());
                    em.merge(item);
    }
}
