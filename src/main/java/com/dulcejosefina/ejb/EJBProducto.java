package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.entity.Proveedor;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Edgardo
 */
@Stateless
@WebService(name = "wsProducto",serviceName = "ServiceProducto")
public class EJBProducto {
@PersistenceContext
private EntityManager em;
    @WebMethod(operationName = "seleccionarProductosAVencer")
    public String seleccionarProductosAConFechaVencimientoEnUnaSemana(){
        StringBuilder xml= new StringBuilder("<Lista>\n");
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 7);
        
        try {
            
        

                        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.fechaVencimiento BETWEEN :f1 and :f2 AND Cast(p.precioUnitarioVenta as Integer)>0");        

                         consulta.setParameter("f1", Calendar.getInstance(),TemporalType.TIMESTAMP);
                         consulta.setParameter("f2", calendario,TemporalType.TIMESTAMP);

                        List<Producto> lista =consulta.getResultList();
                         if(!lista.isEmpty()){
                                     for (Producto producto : lista) {
                                         if(producto.getCompra()!=null&&producto.getVenta()!=null){
                                          xml.append("<fecha1>").append(DateFormat.getDateInstance().format(Calendar.getInstance().getTime())).append("</fecha1>");
                                          xml.append("<fecha2>").append(DateFormat.getDateInstance().format(calendario.getTime())).append("</fecha2>");
                                          xml.append(producto.toXML());
                                         }
                                      }
                          }
                         xml.append("</Lista>");
        } catch (Exception e) {
            Logger.getLogger("Error en metodo seleccionarProductosAConFechaVencimientoEnUnaSemana "+e.getMessage());
        }finally{
        
        return xml.append("</Lista>").toString();
        }
    }
    @WebMethod(operationName = "seleccionarProductosSinStock")
    public String seleccionarProductosConStockMinimo(){
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.cantidadTotalActual <5 AND p.cantidadTotalActual >=0 AND Cast(p.precioUnitarioVenta as Integer) > 0 order by p.cantidadTotalActual desc");
        List<Producto>lista = consulta.getResultList();
        
        for (Producto producto : lista) {
            if(producto.getVenta()!=null){
                xml.append("<item>").append("<codigo>").append(producto.getCodigoBarra()).append("</codigo>")
                        .append("<descripcion>").append(producto.getDescripcion()).append("</descripcion>")
                        .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>").append("</item>");
            }
        }
    
        return xml.append("</Lista>").toString();
    }

   @WebMethod
    public String obtenerProductosFraccionadosSinStockPorProveedor(long idProveedor){
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.proveedorFK.id =:idProveedor AND p.fraccionado = 1 AND Cast(p.precioUnitarioVenta as Integer) > 0 AND p.cantidadTotalActual < 10 AND p.cantidadTotalActual >=0");
        consulta.setParameter("idProveedor", idProveedor);
        List<Producto>lista = consulta.getResultList();
         xml.append("<proveedor>").append("<![CDATA[").append(em.find(Proveedor.class, idProveedor).getNombre()).append("]]>").append("</proveedor>");
        for (Producto producto : lista) {
            xml.append("<item>");
            xml.append("<producto>").append(producto.getDescripcion()).append("</producto>").append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>");
            xml.append("</item>");
        }
        return xml.append("</Lista>").toString();
    }
    @WebMethod
    public String obtenerProductosSinStockPorProveedor(long idProveedor){
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.fraccionado=0 AND p.proveedorFK.id =:idProveedor AND Cast(p.precioUnitarioVenta as Integer) > 0 AND p.cantidadTotalActual < 10 AND p.cantidadTotalActual >=0");
        consulta.setParameter("idProveedor", idProveedor);
        List<Producto>lista=consulta.getResultList();
        xml.append("<proveedor>").append("<![CDATA[").append(em.find(Proveedor.class, idProveedor).getNombre()).append("]]>").append("</proveedor>\n");
        for (Producto producto : lista) {
            
            xml.append("<item>\n");
                    xml.append("<producto>").append(producto.getDescripcion()).append("</producto>\n")
                    .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>\n")
           .append("</item>\n");
            
        }
        
        return xml.append("</Lista>").toString();
    
    }

    
}
