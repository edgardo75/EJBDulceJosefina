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
    public short crearProveedor(@WebParam(name = "nombreProveedor") String nombreProveedor, @WebParam(name = "detalles") String detalles) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(nombreProveedor.toUpperCase());  
        proveedor.setDetalles(detalles);
        em.persist(proveedor);
        em.flush();
        return proveedor.getId().shortValue();
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
