package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Proveedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@WebService
@Stateless
@LocalBean
public class EJBProveedorBean {
@PersistenceContext
private EntityManager em;
    /**
     * Web service operation
     * @param nombreProveedor
     * @param detalles
     * @return 
     */
    @WebMethod(operationName = "crearProveedor")
    public int crearProveedor(@WebParam(name = "nombreProveedor") String nombreProveedor, @WebParam(name = "detalles") String detalles) {
        int retorno =0;
        
        Proveedor proveedor = new Proveedor();
        Query consulta = em.createQuery("SELECT p FROM Proveedor p WHERE p.nombre =:proveedor");
        consulta.setParameter("proveedor".toLowerCase().trim(), nombreProveedor.toLowerCase().trim());
        
        if(consulta.getResultList().isEmpty()){
            proveedor.setNombre(nombreProveedor.toUpperCase().trim());              
            proveedor.setDetalles(detalles);
              em.persist(proveedor);
              retorno = proveedor.getId().shortValue();
        em.flush();
        }else{
            List<Proveedor>lista = consulta.getResultList();
            for (Proveedor proveedor1 : lista) {
                retorno=proveedor1.getId().intValue();
            }
            
        }
        
      
        
        return retorno;
    }

    @WebMethod
    public String selectAllProveedor(){
        StringBuilder lista = new StringBuilder( "<Lista>\n");
        Query consulta = em.createNamedQuery("findAll.Proveedor");
        List<Proveedor> ListaProveedor = consulta.getResultList();
        
        for (Proveedor proveedor : ListaProveedor) {
            lista.append(proveedor.toXML());
        }
    return lista.append("</Lista>").toString();
    }
}
