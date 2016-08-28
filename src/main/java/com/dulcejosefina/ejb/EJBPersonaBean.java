/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Genero;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.TipoDocumento;
import com.dulcejosefina.entity.TipoPersona;
import com.dulcejosefina.utils.DatosPersona;
import com.dulcejosefina.utils.ProjectHelpers;
import com.dulcejosefina.utils.ValidateClientandUserData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.util.Date;
import java.util.Iterator;
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
    public long crearEmpleado(String xmlEmpleado) {
        long retorno;
        DatosPersona getDatosEmpleado = transformarEmpleado(xmlEmpleado);
        System.out.println("HOLA \n "+getDatosEmpleado.getApellido());
        
        retorno = (!getDatosEmpleado.getTipoPersona().equalsIgnoreCase("cliente")?chequearCamposRequeridos(getDatosEmpleado):1);
        if(retorno==1){
            if(validarCamposRequeridosNombreYApellido(getDatosEmpleado)){         
                
                            if(!validarNumeroIdentificacionPersonalYEmail(getDatosEmpleado)){
                                
                                retorno = verificarDatosDniCuilYLogin(getDatosEmpleado);
                                
                                

                                System.out.println("VALOR RETORNADO LUEGO DE VERIFICAR "+retorno);
                                System.out.println("VALOR DE CUIL "+getDatosEmpleado.getCuil().isEmpty());
                                System.out.println("VALOR DE DNI "+getDatosEmpleado.getDni());
                                System.out.println("VALOR DEL ID "+getDatosEmpleado.getId());
                                
                                if(getDatosEmpleado.getId()==0){
                                    switch((int)retorno){
                                        case 1:retorno=-6;
                                        break;
                                        case 2:retorno=-7;
                                        default:{
                                            retorno = procesarEmpleado(getDatosEmpleado);
                                        }

                                    }
                                }else{
                                    retorno = actualizarDatosEmpleado(getDatosEmpleado);

                                }

                            }else{retorno = -5;}              
                        }else{
                            retorno =-4;
                                    }
        }
        return retorno;
    }

    private DatosPersona transformarEmpleado(String xmlEmpleado) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("empleado", DatosPersona.class);
        
        return (DatosPersona) xstream.fromXML(xmlEmpleado);
    }

    private boolean buscarLogin(String login) {
        Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.login =:login");
        
        return (consulta.setParameter("login", login).getResultList().size()==1) ;
    }

    private boolean buscarDNI(String dni) {
        Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.dni =:dni");
        return (consulta.setParameter("dni", String.valueOf(dni)).getResultList().size()==1);
    }

    private long procesarEmpleado(DatosPersona datosEmpleado) {
        Persona persona = new Persona();        
        persona=persistirDatos(persona,datosEmpleado);
        em.persist(persona);
        return persona.getId();
        
    }
    @WebMethod
    public String selectAllEmpleadosYJefes(){
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

    private long chequearCamposRequeridos(DatosPersona datosEmpleado) {
        
        return (datosEmpleado.getNombre().isEmpty()||datosEmpleado.getApellido().isEmpty()||datosEmpleado.getLogin().isEmpty()||datosEmpleado.getPassword().isEmpty()?-1:1);
    }

    private boolean validarCamposRequeridosNombreYApellido(DatosPersona datosEmpleado) {
        
        
        return (validateDataUser.validate(datosEmpleado.getNombre(), nombreyApellidoPattern)&&validateDataUser.validate(datosEmpleado.getApellido(), nombreyApellidoPattern));
        
      
    }

    private boolean validarNumeroIdentificacionPersonalYEmail(DatosPersona datosEmpleado) {
        boolean retorno = false;
        if(!String.valueOf(datosEmpleado.getDni()).isEmpty()){
            retorno = validateDataUser.validate(String.valueOf(datosEmpleado.getDni()), numberPattern_Dni);
            
        }else{
            if(!String.valueOf(datosEmpleado.getCuil()).isEmpty()){
                retorno = validateDataUser.validate(String.valueOf(datosEmpleado.getCuil()), numberPattern_Dni);
            
            }else{
                if(!datosEmpleado.getEmail().isEmpty()){
                    retorno = validateDataUser.isValidEmailAddress(datosEmpleado.getEmail());
            
                }
            }
        }
        
        
        return retorno;
        
    }

    private long verificarDatosDniCuilYLogin(DatosPersona datosEmpleado) {
        long retorno =0L;
        if(datosEmpleado.getTipoPersona().equalsIgnoreCase("cliente")&&(!datosEmpleado.getDni().isEmpty()&& !datosEmpleado.getCuil().isEmpty())){
            retorno= (buscarDNI(datosEmpleado.getDni())||buscarCuil(datosEmpleado.getCuil())?2:0);
        }else{
            
            if(!datosEmpleado.getCuil().isEmpty()&&!datosEmpleado.getDni().isEmpty()){
                retorno= (buscarDNI(datosEmpleado.getDni())||buscarCuil(datosEmpleado.getCuil())?2:0);
            }else{
                if(!datosEmpleado.getLogin().isEmpty()){
                    retorno= (buscarLogin(datosEmpleado.getLogin())?1:0);

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

    private long actualizarDatosEmpleado(DatosPersona datosEmpleado) {
        Persona empleado = em.find(Persona.class, datosEmpleado.getId());
        
        if(empleado!=null){    
            System.out.print("valores de empleado "+datosEmpleado.getTipoPersona()+" "+empleado.getTiposPersona());
            empleado = persistirDatos(empleado, datosEmpleado);
            em.merge(empleado);
            em.flush();
            return empleado.getId();
        }else{
            return -3;
        }
        
        
        
    }

    private Persona persistirDatos(Persona persona, DatosPersona datosEmpleado) {
        ProjectHelpers passworTry = new ProjectHelpers();
         persona.setNombre(datosEmpleado.getNombre());
        persona.setApellido(datosEmpleado.getApellido());
        if(!datosEmpleado.getLogin().isEmpty()){
            persona.setLogin(datosEmpleado.getLogin());
        }
        if(!datosEmpleado.getPassword().isEmpty()){
            persona.setPassword(passworTry.encrypt(datosEmpleado.getPassword()));
            persona.setKeyPassword(passworTry.encryptionKey);
        }
        
        if(!datosEmpleado.getDni().isEmpty()){
            
            persona.setDni(Integer.valueOf(datosEmpleado.getDni()));
        }
        if(!datosEmpleado.getCuil().isEmpty()){
        
            persona.setCuil(Integer.valueOf(datosEmpleado.getCuil()));
        
        }
        
        persona.setEstado('1');
        persona.setFechaCarga(new Date());
        
        switch(datosEmpleado.getGenero()){
           case "FEMENINO":persona.setGenero(Genero.FEMENINO);
           break;
           
           default: persona.setGenero(Genero.MASCULINO);
        }
        switch(datosEmpleado.getTipoDocumento()){
            case "LC":persona.setTipoDocumento(TipoDocumento.LC);
            break;
            case "LE":persona.setTipoDocumento(TipoDocumento.LE);
            break;
            case "PASAPORTE":persona.setTipoDocumento(TipoDocumento.PASAPORTE);
            break;
            
            default:persona.setTipoDocumento(TipoDocumento.DNI);
        
        }
        switch(datosEmpleado.getTipoPersona()){
            case "JEFE":persona.setTiposPersona(TipoPersona.JEFE);
            break;
            case "CLIENTE":persona.setTiposPersona(TipoPersona.CLIENTE);
            break;
            default:persona.setTiposPersona(TipoPersona.EMPLEADO);
        }
        if(!datosEmpleado.getEmail().isEmpty()){
            persona.setEmail(datosEmpleado.getEmail());
        }else{
         persona.setEmail("");
        }
        
        persona.setDetalles(datosEmpleado.getDetalle());
        System.out.print("VALOR DE CUIL AND DNI "+persona.getCuil()+" "+persona.getDni());
        return persona;
    }

   
   
}

