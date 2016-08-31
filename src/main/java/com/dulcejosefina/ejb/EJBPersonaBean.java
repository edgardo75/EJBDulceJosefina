/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Genero;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.Telefono;
import com.dulcejosefina.entity.TipoDocumento;
import com.dulcejosefina.entity.TipoPersona;
import com.dulcejosefina.utils.DatosPersona;
import com.dulcejosefina.utils.DatosTelefono;
import com.dulcejosefina.utils.ProjectHelpers;
import com.dulcejosefina.utils.TelefonoItem;
import com.dulcejosefina.utils.ValidateClientandUserData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.util.Date;
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
@WebService
@Stateless
@LocalBean
public class EJBPersonaBean {
    @PersistenceContext
    private EntityManager em;
    ValidateClientandUserData validateDataUser;
    String nombreyApellidoPattern;
    String numberPattern_Dni;
    String email_Pattern;
    public EJBPersonaBean(){
        this.nombreyApellidoPattern = "(?=^.{1,30}$)[[A-Z][a-z]\\p{IsLatin}]* ?[[a-zA-Z]\\p{IsLatin}]* ?[[a-zA-Z]\\p{IsLatin}]+$";
        this.numberPattern_Dni="(?=^.{1,10}$)\\d+$";
        this.email_Pattern="^[\\w\\-\\+\\*]+[\\w\\S]@(\\w+\\.)+[\\w]{2,4}$";
        validateDataUser = new ValidateClientandUserData();
    
    }
    @WebMethod
    public long crearPersona(String xmlPersona) {
        long retorno;
        DatosPersona getDatosPersona = (DatosPersona) transformarXmlToObjetXstream(xmlPersona);
        
        System.out.println("HOLA \n "+getDatosPersona.getApellido());
        
        retorno = (!getDatosPersona.getTipoPersona().equalsIgnoreCase("cliente")?chequearCamposRequeridos(getDatosPersona):1);
        if(retorno==1){
            if(validarCamposRequeridosNombreYApellido(getDatosPersona)){         
                
                            if(!validarNumeroIdentificacionPersonalYEmail(getDatosPersona)){
                                
                                retorno = verificarDatosDniCuilYLogin(getDatosPersona);
                                
                                

                                System.out.println("VALOR RETORNADO LUEGO DE VERIFICAR "+retorno);
                                System.out.println("VALOR DE CUIL "+getDatosPersona.getCuil().isEmpty());
                                System.out.println("VALOR DE DNI "+getDatosPersona.getDni());
                                System.out.println("VALOR DEL ID "+getDatosPersona.getId());
                                
                                if(getDatosPersona.getId()==0){
                                    switch((int)retorno){
                                        case 1:retorno=-6;
                                        break;
                                        case 2:retorno=-7;
                                        default:{
                                            retorno = procesarEmpleado(getDatosPersona);
                                        }

                                    }
                                }else{
                                    retorno = actualizarDatosPersona(getDatosPersona);

                                }

                            }else{retorno = -5;}              
                        }else{
                            retorno =-4;
                                    }
        }
        return retorno;
    }

    private Object transformarXmlToObjetXstream(String xml) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("persona", DatosPersona.class);
        xstream.alias("datosTelefono", DatosTelefono.class);
        xstream.addImplicitCollection(DatosTelefono.class, "datosTelefono");
        
        
        
        return xstream.fromXML(xml);
    }

    private boolean buscarLogin(String login) {
        Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.login =:login");
        
        return (consulta.setParameter("login", login).getResultList().size()==1) ;
    }

    private boolean buscarDNI(String dni) {
        Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.dni =:dni");
        return (consulta.setParameter("dni", String.valueOf(dni)).getResultList().size()==1);
    }

    private long procesarEmpleado(DatosPersona datosPersona) {
        Persona persona = new Persona();        
        persona=persistirDatosPersona(persona,datosPersona);
        persona = persistirListaTelefonoPersona(persona,datosPersona);
        return persona.getId();
        
    }
    @WebMethod
    public String selectAllEmpleadosJefesyCliente(){
        ProjectHelpers passTry = new ProjectHelpers();
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query consulta = em.createNamedQuery("personaFindAll");
        
        
        List<Persona>lista = consulta.getResultList();
        
        if(!lista.isEmpty()){
            for (Persona persona : lista) {
                String resultado = persona.getPassword() != null?passTry.decrypt(persona.getKeyPassword(), persona.getPassword()):"";
                xml.append("<item>");
                xml.append(persona.toXML());
                xml.append("<password>").append(resultado).append("</password>");
                xml.append("</item>");
            }
        }
        
        
        return xml.append("</Lista>").toString();
    }

    private long chequearCamposRequeridos(DatosPersona datosPersona) {
        
        return (datosPersona.getNombre().isEmpty()||datosPersona.getApellido().isEmpty()||datosPersona.getLogin().isEmpty()||datosPersona.getPassword().isEmpty()?-1:1);
    }

    private boolean validarCamposRequeridosNombreYApellido(DatosPersona datosPersona) {
        
        
        return (validateDataUser.validate(datosPersona.getNombre(), nombreyApellidoPattern)&&validateDataUser.validate(datosPersona.getApellido(), nombreyApellidoPattern));
        
      
    }

    private boolean validarNumeroIdentificacionPersonalYEmail(DatosPersona datosPersona) {
        boolean retorno = false;
        if(!String.valueOf(datosPersona.getDni()).isEmpty()){
            retorno = validateDataUser.validate(String.valueOf(datosPersona.getDni()), numberPattern_Dni);
            
        }else{
            if(!String.valueOf(datosPersona.getCuil()).isEmpty()){
                retorno = validateDataUser.validate(String.valueOf(datosPersona.getCuil()), numberPattern_Dni);
            
            }else{
                if(!datosPersona.getEmail().isEmpty()){
                    retorno = validateDataUser.isValidEmailAddress(datosPersona.getEmail());
            
                }
            }
        }
        
        
        return retorno;
        
    }

    private long verificarDatosDniCuilYLogin(DatosPersona datosPersona) {
        long retorno =0L;
        if(datosPersona.getTipoPersona().equalsIgnoreCase("cliente")&&(!datosPersona.getDni().isEmpty()&& !datosPersona.getCuil().isEmpty())){
            retorno= (buscarDNI(datosPersona.getDni())||buscarCuil(datosPersona.getCuil())?2:0);
        }else{
            
            if(!datosPersona.getCuil().isEmpty()&&!datosPersona.getDni().isEmpty()){
                retorno= (buscarDNI(datosPersona.getDni())||buscarCuil(datosPersona.getCuil())?2:0);
            }else{
                if(!datosPersona.getLogin().isEmpty()){
                    retorno= (buscarLogin(datosPersona.getLogin())?1:0);

                }
            }
        }
       return retorno;
            
        
       
    }
    @WebMethod
    public boolean buscarCuil(String cuil) {
         Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.cuil =:cuil");
        return (consulta.setParameter("cuil", String.valueOf(cuil)).getResultList().size()==1);
    }

    private long actualizarDatosPersona(DatosPersona datosPersona) {
        Persona persona = em.find(Persona.class, datosPersona.getId());
        
        if(persona!=null){    
            
            persona = persistirDatosPersona(persona, datosPersona);
            persona = persistirListaTelefonoPersona(persona, datosPersona);
            em.merge(persona);
            em.flush();
            return persona.getId();
        }else{
            return -3;
        }
        
        
        
    }

    private Persona persistirDatosPersona(Persona persona, DatosPersona datosPersona) {
        
        persona = datosRequeridosImportantes(persona,datosPersona);
        persona = datosSecundarios(persona,datosPersona);
        
        em.persist(persona);
        
        return persona;
    }

    private Persona datosRequeridosImportantes(Persona persona, DatosPersona datosPersona) {
        ProjectHelpers passworTry = new ProjectHelpers();
         persona.setNombre(datosPersona.getNombre());
        persona.setApellido(datosPersona.getApellido());
        if(!datosPersona.getLogin().isEmpty()){
            persona.setLogin(datosPersona.getLogin());
        }
        if(!datosPersona.getPassword().isEmpty()){
            persona.setPassword(passworTry.encrypt(datosPersona.getPassword()));
            persona.setKeyPassword(passworTry.encryptionKey);
        }
        
        if(!datosPersona.getDni().isEmpty()){
            
            persona.setDni(Integer.valueOf(datosPersona.getDni()));
        }
        if(!datosPersona.getCuil().isEmpty()){
        
            persona.setCuil(Integer.valueOf(datosPersona.getCuil()));
        
        }
        
        persona.setEstado(datosPersona.getEstado());
        persona.setFechaCarga(new Date());
        return persona;
    }

    private Persona datosSecundarios(Persona persona, DatosPersona datosPersona) {
        switch(datosPersona.getGenero()){
           case "FEMENINO":persona.setGenero(Genero.FEMENINO);
           break;
           
           default: persona.setGenero(Genero.MASCULINO);
        }
        switch(datosPersona.getTipoDocumento()){
            case "LC":persona.setTipoDocumento(TipoDocumento.LC);
            break;
            case "LE":persona.setTipoDocumento(TipoDocumento.LE);
            break;
            case "PASAPORTE":persona.setTipoDocumento(TipoDocumento.PASAPORTE);
            break;
            
            default:persona.setTipoDocumento(TipoDocumento.DNI);
        
        }
        switch(datosPersona.getTipoPersona()){
            case "JEFE":persona.setTiposPersona(TipoPersona.JEFE);
            break;
            case "CLIENTE":persona.setTiposPersona(TipoPersona.CLIENTE);
            break;
            default:persona.setTiposPersona(TipoPersona.EMPLEADO);
        }
        if(!datosPersona.getEmail().isEmpty()){
            persona.setEmail(datosPersona.getEmail());
        }else{
         persona.setEmail("");
        }
        
        persona.setDetalles(datosPersona.getDetalle());
        return persona;
    }

    private Persona persistirListaTelefonoPersona(Persona persona, DatosPersona datosPersona) {
        if(!persona.getListaTelefono().isEmpty()){
            persona.getListaTelefono().clear();
            em.flush();
            List<TelefonoItem>lista = datosPersona.getDatosTelefono().getList();
            for(TelefonoItem telefono:lista){
                Telefono item = em.find(Telefono.class,telefono.getId());
                if(item==null){
                    Telefono tel = new Telefono();
                    tel.setNumero(telefono.getNumero());
                    tel.setPrefijo(tel.getPrefijo());
                    tel.setPersonaTelefono(persona);
                    em.merge(tel);
                }
                
            }
            Query consulta = em.createQuery("SELECT t FROM Telefono t WHERE t.personaTelefono.id =:id");
            consulta.setParameter("id", persona.getId());
            List<Telefono>listaTelefonoPersona = consulta.getResultList();
            persona.setListaTelefono(listaTelefonoPersona);
            em.merge(persona);
            
        }
        return persona;
    }

   
   
}

