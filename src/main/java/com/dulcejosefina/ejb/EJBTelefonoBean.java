package com.dulcejosefina.ejb;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.PersonaTelefono;
import com.dulcejosefina.entity.Telefono;
import com.dulcejosefina.utils.DatosPersona;
import com.dulcejosefina.utils.TelefonoItem;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@Stateless
@LocalBean
public class EJBTelefonoBean {
    @PersistenceContext
    private EntityManager em;      
    public Persona insertarListaTelefonoPersona(Persona persona,DatosPersona datosPersona){        
        List<TelefonoItem>lista = datosPersona.getDatosTelefono().getList();
        Telefono item = null;        
        for(TelefonoItem telefono:lista){            
            Query consulta = em.createQuery("SELECT t FROM Telefono t WHERE t.numero=:numero AND t.prefijo=:prefijo");
            consulta.setParameter("numero", Long.valueOf(telefono.getNumero().trim()));
            consulta.setParameter("prefijo", Long.valueOf(telefono.getPrefijo().trim()));            
            if(consulta.getResultList().isEmpty()){                
               item = new Telefono(Long.valueOf(telefono.getNumero().trim()), Long.valueOf(telefono.getPrefijo().trim()));         
                em.persist(item);                
                agregarRelacionPersonaTelefono(persona, item);          
                actualizarListaTelefono(item,persona);            
            }else{                
                if(telefono.getId()>0){
                    Telefono phone = em.find(Telefono.class, telefono.getId());
                    agregarRelacionPersonaTelefono(persona, phone);
                    actualizarListaTelefono(phone, persona);
                }else{
                    List<Telefono> listaTelefono = consulta.getResultList();
                    for (Telefono telefono1 : listaTelefono) {
                        agregarRelacionPersonaTelefono(persona, telefono1);
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
     private void agregarRelacionPersonaTelefono(Persona persona, Telefono telefono) {
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