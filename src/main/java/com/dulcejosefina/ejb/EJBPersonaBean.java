package com.dulcejosefina.ejb;
import com.dulcejosefina.entity.Genero;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.RolPersona;
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
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@WebService
@Stateless
@LocalBean
public class EJBPersonaBean {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private EJBTelefonoBean telefonoBean;        
    ValidateClientandUserData validateDataUser;
    String nombreyApellidoPattern;
    String numberPattern_Dni;
    String numberPattern_Cuil;
    String email_Pattern;
    public EJBPersonaBean(){
        this.nombreyApellidoPattern = "(?=^.{1,30}$)[[A-Z][a-z]\\p{IsLatin}]* ?[[a-zA-Z]\\p{IsLatin}]* ?[[a-zA-Z]\\p{IsLatin}]+$";
        this.numberPattern_Dni="(?=^.{1,10}$)\\d+$";
        this.numberPattern_Cuil="(?=^.{1,11}$)\\d+$";
        this.email_Pattern="^[\\w\\-\\+\\*]+[\\w\\S]@(\\w+\\.)+[\\w]{2,4}$";
        validateDataUser = new ValidateClientandUserData();    
    }
    @WebMethod
    public long crearPersona(String xmlPersona)  {
        long retorno = 0;        
        DatosPersona getDatosPersona = (DatosPersona) transformarXmlToObjetXstream(xmlPersona);  
        retorno = (!getDatosPersona.getTipoPersona().equalsIgnoreCase("cliente")?chequearCamposRequeridos(getDatosPersona):1);
        if(retorno==1){
            if(validarCamposRequeridosNombreYApellido(getDatosPersona)){        
                if(validarNumeroIdentificacionPersonalYEmail(getDatosPersona)){
                                retorno = verificarDatosDniCuilYLogin(getDatosPersona);                     
                                if(getDatosPersona.getId()==0){
                                    switch((int)retorno){
                                        case 1:retorno=-6;
                                        break;
                                        case 2:retorno=-7;
                                        default:{
                                            retorno = procesarEmpleado(xmlPersona,getDatosPersona);
                                        }
                                    }
                                }else{
                                    retorno = actualizarDatosPersona(xmlPersona,getDatosPersona);
                                }
                            }else{retorno = -5;}              
                        } else{
                            retorno =-4;
                         }
        }       
        return retorno;
    }
    private Object transformarXmlToObjetXstream(String xml) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("empleado", DatosPersona.class);
        xstream.alias("datosTelefono", DatosTelefono.class);
        xstream.alias("telefono", TelefonoItem.class);
        xstream.addImplicitCollection(DatosTelefono.class, "list");
        return xstream.fromXML(xml);
    }
    private boolean buscarLogin(String login) {
        Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.login =:login");
        return (consulta.setParameter("login", login).getResultList().size()==1) ;
    }
    private boolean buscarDNI(long dni) {
        Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.dni =:dni");
        return (consulta.setParameter("dni", dni).getResultList().size()==1);
    }

    private long procesarEmpleado(String xmlPersona, DatosPersona datosPersona){
        Persona persona = new Persona();          
            persona=persistirDatosPersona(persona,datosPersona);
            persona = (xmlPersona.contains("<datosTelefono>")?persistirListaTelefonoPersona(persona,datosPersona):persona)  ;           
          em.flush();
        return persona.getId();
    }
    @WebMethod
    public String selectLogin(){
        
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query consulta = em.createNamedQuery("empleadoFindAllEmpleadoYJefe");
        List<Persona>lista = consulta.getResultList();
        if(!lista.isEmpty()){
            for (Persona persona : lista) {
        
                xml.append("<item>");
                xml.append("<id>").append(persona.getId()).append("</id>");
                xml.append("<login>").append(persona.getLogin()).append("</login>");
                xml.append("<password>").append(persona.getPassword()).append("</password>");
                xml.append("</item>");
            }
        }
        return xml.append("</Lista>").toString();
    }
    @WebMethod
    public String selectAllEmpleadosJefesyCliente(){        
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query consulta = em.createNamedQuery("personaFindAll");
        List<Persona>lista = consulta.getResultList();
        if(!lista.isEmpty()){
            for (Persona persona : lista) {         
                xml.append("<item>");
                xml.append(persona.toXML());
                xml.append("<password>").append(persona.getPassword()).append("</password>");
                xml.append("</item>");
            }
        }
        return xml.append("</Lista>").toString();
    }
    private long chequearCamposRequeridos(DatosPersona datosPersona) {
        return (datosPersona.getNombre().isEmpty()||datosPersona.getApellido().isEmpty()||datosPersona.getLogin().isEmpty()||datosPersona.getPassword().isEmpty()?-1:1);
    }
    private boolean validarCamposRequeridosNombreYApellido(DatosPersona datosPersona) {
        return (validateDataUser.validate(datosPersona.getNombre().trim(), nombreyApellidoPattern)&&validateDataUser.validate(datosPersona.getApellido().trim(), nombreyApellidoPattern));
    }
    private boolean validarNumeroIdentificacionPersonalYEmail(DatosPersona datosPersona) {
        boolean retorno = false;
            if(!String.valueOf(datosPersona.getDni()).isEmpty()){
                retorno = validateDataUser.validate(String.valueOf(datosPersona.getDni()), numberPattern_Dni);
            }
            if(!String.valueOf(datosPersona.getCuil()).isEmpty()){
                retorno = validateDataUser.validate(String.valueOf(datosPersona.getCuil()), numberPattern_Cuil);
            }
            if(!datosPersona.getEmail().isEmpty()){
                    retorno = validateDataUser.isValidEmailAddress(datosPersona.getEmail());
            }
        return retorno;
    }
    private long verificarDatosDniCuilYLogin(DatosPersona datosPersona) {
        long retorno =0L;
        if(datosPersona.getTipoPersona().equalsIgnoreCase("cliente")&&(datosPersona.getDni()>0&& datosPersona.getCuil()>0)){
            retorno= (buscarDNI(datosPersona.getDni())||buscarCuil(datosPersona.getCuil())?2:0);
        }else{
            
            if(datosPersona.getCuil()>0&&datosPersona.getDni()>0){
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
    public boolean buscarCuil(long cuil) {
         Query consulta = em.createQuery("SELECT p FROM Persona p WHERE p.cuil =:cuil");
        return (consulta.setParameter("cuil", cuil).getResultList().size()==1);
    }
    private long actualizarDatosPersona(String xmlPersona, DatosPersona datosPersona) {
        Persona persona = em.find(Persona.class, datosPersona.getId());        
        if(persona!=null){                
            persona = persistirDatosPersona(persona, datosPersona);
            persona = (xmlPersona.contains("<datosTelefono>")?persistirListaTelefonoPersona(persona, datosPersona):persona) ;            
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
        
        persona.setNombre(datosPersona.getNombre().trim());
        persona.setApellido(datosPersona.getApellido().trim());
        if(!datosPersona.getLogin().isEmpty()){
            persona.setLogin(datosPersona.getLogin().trim());
        }
        if(!datosPersona.getPassword().isEmpty()){
            persona.setPassword(datosPersona.getPassword());            
        }        
        if(datosPersona.getDni()>0){
            persona.setDni(datosPersona.getDni());
        }
        
        if(datosPersona.getCuil()>0){
            persona.setCuil(datosPersona.getCuil());        
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
        switch(datosPersona.getRolPersona()){
            case "ADMINISTRADOR":persona.setRol(RolPersona.ADMINISTRADOR);
            break;
            case "OPERADOR":persona.setRol(RolPersona.OPERADOR);
            break;
            default:persona.setRol(RolPersona.INVITADO);
        }
        if(!datosPersona.getEmail().isEmpty()){
            persona.setEmail(datosPersona.getEmail());
        }else{
         persona.setEmail("");
        }
        
        persona.setClientePerefencial(datosPersona.getClientePerefencial());
        persona.setPuntosClientePrefencial(datosPersona.getPuntosClientePrefencial());
        persona.setDetalles(datosPersona.getDetalle());
        return persona;
    }

    private Persona persistirListaTelefonoPersona(Persona persona, DatosPersona datosPersona) {
     if(!datosPersona.getDatosTelefono().getList().isEmpty()){
            persona = telefonoBean.insertarListaTelefonoPersona(persona, datosPersona);
        } else {
        }
        return persona;
    }

    @WebMethod
    public boolean verificarDniCuil(String numeroDocOCuil) {
        int result = em.createQuery("SELECT p FROM Persona p WHERE p.dni=:dni OR p.cuil=:cuil").setParameter("dni", Long.valueOf(numeroDocOCuil)).setParameter("cuil", Long.valueOf(numeroDocOCuil)).getResultList().size();
        return result==1;
    }
    @WebMethod
    public String selectAllJefes() {
        String xml = "<Lista>";

        Query consulta =em.createNamedQuery("findAllJefeOnly");
        List<Persona>lista = consulta.getResultList();
        for (Persona persona : lista) {
        xml+="<item>"
                + "<id>"+persona.getId()+"</id>\n"
                + "<login>"+persona.getLogin()+"</login>\n"
        + "<password>"+persona.getPassword()+"</password>"
                + "</item>";
    }
        return xml+"</Lista>";
    }
 @WebMethod
    public String selectAllJefesAdmin() {
        
        String xml = "<Lista>";
        try {     
                Query consulta =em.createNamedQuery("findAllJefeOnlyAdmin");
                List<Persona>lista = consulta.getResultList();
                for (Persona persona : lista) {
                        xml+="<item>"
                                + "<id>"+persona.getId()+"</id>\n"
                                + "<login>"+persona.getLogin()+"</login>\n"
                        + "<password>"+persona.getPassword()+"</password>"
                                + "</item>";
                }
        } catch (Exception e) {
            
        }
        return xml+"</Lista>";
    }
@WebMethod
public boolean chequearPasswordDelUsuario(long idUsuario,String password){ 
    
    Persona persona = em.find(Persona.class, idUsuario);
    return ((persona.getPassword() == null ? password == null : persona.getPassword().equals(password)));
}
@WebMethod
public boolean sacarKeyYKeyPassword(){
    ProjectHelpers passTry = new ProjectHelpers();
Query consulta = em.createNamedQuery("personaFindAll");
List<Persona>lista = consulta.getResultList();
    for (Persona persona : lista) {
        String key=persona.getKeyPassword();
        String pass=persona.getPassword();
        persona.setPassword(passTry.decrypt(key, pass));
    }
return true;
}
}