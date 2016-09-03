/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.utils.DatosCompraProducto;
import com.dulcejosefina.utils.DatosCompraProductoItem;
import com.dulcejosefina.utils.DatosPackProducto;
import com.dulcejosefina.utils.DatosProducto;
import com.dulcejosefina.utils.DatosProveedor;
import com.dulcejosefina.utils.DatosSucursal;
import com.dulcejosefina.utils.DatosVentaProducto;
import com.dulcejosefina.utils.DatosVentaProductoItem;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
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
@Stateless
@LocalBean
@WebService
public class EJBProductoBean {
@PersistenceContext
private EntityManager em;
    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "selectAllProducto")
    public String selectAllProducto() {
        StringBuilder xml = new StringBuilder("<Lita>\n");
        Query consulta = em.createNamedQuery("Producto.fidAll");
        List<Producto>lista = consulta.getResultList();
        for (Producto producto : lista) {
            xml.append(producto.toXML());
        }
        return xml.toString();
    }
@WebMethod
    public long crearProducto(String xmlProducto) {
        long retorno = 0;
        DatosProducto getDatosProducto = (DatosProducto) transformaAObjetos(xmlProducto);
        
        
        
        return 0L;
    }

    public long aplicarPorcentajeProducto(long idProducto, String porcentaje, String modalidad) {
        return 0L;
    }

    private Object transformaAObjetos(String xmlProducto) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("producto", DatosProducto.class);
        xstream.alias("sucursal", DatosSucursal.class);
        xstream.alias("proveedor", DatosProveedor.class);
        xstream.alias("compraProducto", DatosCompraProducto.class);
        xstream.alias("ventaProducto", DatosVentaProducto.class);
        xstream.alias("itemCompra", DatosCompraProductoItem.class);
        xstream.alias("itemVenta", DatosVentaProductoItem.class);
        xstream.addImplicitCollection(DatosCompraProducto.class, "listShop");
        xstream.addImplicitCollection(DatosVentaProducto.class, "listSale");
        return xstream.fromXML(xmlProducto);
    }

   
}
