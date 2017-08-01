package com.dulcejosefina.ejb;
import com.dulcejosefina.entity.DetallePresupuesto;
import com.dulcejosefina.entity.Presupuesto;
import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.entity.Sucursal;
import com.dulcejosefina.utils.DatosVentaSucursal;
import com.dulcejosefina.utils.ItemDetalleVentaSucursalItem;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@Stateless
@LocalBean
@WebService
public class EJBPresupuestoBean {
    @PersistenceContext
    private EntityManager em;
    @WebMethod
    public int crearPresupuesto(String xmlPresupuesto){
         DatosVentaSucursal datosPresupuesto = new DatosVentaSucursal();
                 datosPresupuesto = datosPresupuesto.transformarAObjeto(xmlPresupuesto);
                Presupuesto presupuesto = new Presupuesto();
                presupuesto.setDescuentoPesos(BigDecimal.valueOf(datosPresupuesto.getTotalDescuento()));
                presupuesto.setFechaVenta(Calendar.getInstance().getTime());
                presupuesto.setIdUsuarioExpidioVenta(datosPresupuesto.getIdUsuarioExpidioVenta());
                presupuesto.setApellido(datosPresupuesto.getPersona().getApellido());
                presupuesto.setNombre(datosPresupuesto.getPersona().getNombre());
                presupuesto.setPorcentajeDescuento(BigDecimal.valueOf(datosPresupuesto.getPorcentajeDescuento()));
                presupuesto.setPorcentajeRecargo(BigDecimal.valueOf(datosPresupuesto.getPorcentajeRecargo()));
                presupuesto.setRecargoPesos(BigDecimal.valueOf(datosPresupuesto.getTotalRecargo()));
                presupuesto.setTotalAPagar(BigDecimal.valueOf(datosPresupuesto.getTotalAPagar()));
                presupuesto.setTotalDescuento(BigDecimal.valueOf(datosPresupuesto.getTotalDescuento()));
                presupuesto.setTotalGeneral(BigDecimal.valueOf(datosPresupuesto.getTotalGeneral()));
                presupuesto.setTotalRecargo(BigDecimal.valueOf(datosPresupuesto.getTotalRecargo()));
                presupuesto.setCantidad(datosPresupuesto.getCantidad());
                presupuesto.setSucursal(em.find(Sucursal.class, datosPresupuesto.getSucursal().getId()));
                em.persist(presupuesto);
         persistirDetallePresupuesto(presupuesto,datosPresupuesto);
          em.flush();    
    return presupuesto.getId().intValue();
    }
    private void persistirDetallePresupuesto(Presupuesto presupuesto, DatosVentaSucursal datosPresupuesto) {
       List<ItemDetalleVentaSucursalItem>lista=datosPresupuesto.getDetalleVenta().getList();
        for (ItemDetalleVentaSucursalItem item : lista) {
            DetallePresupuesto detalle = new DetallePresupuesto();
                detalle.setPresupuesto(presupuesto);
                detalle.setCodigo(item.getCodigo());
                detalle.setIdVentaProducto(item.getIdVentaProducto());
                detalle.setDescripcion(item.getDescripcion());
                detalle.setIdPack(item.getIdPack());
                detalle.setProducto(em.find(Producto.class, item.getId()));
                detalle.setNombrePack(item.getNombrePack());
                detalle.setPrecio(item.getPrecio());
                detalle.setPresentacion(item.getPresentacion());
                detalle.setCantidad(item.getCantidad());
                detalle.setSubtotal(BigDecimal.valueOf(item.getSubtotal()));
                em.persist(detalle);
        }
           Query consulta = em.createNamedQuery("selectAllDetallePresupuestoById");           
                consulta.setParameter("id", presupuesto.getId());
        presupuesto.setDetallepresupuestosList(consulta.getResultList());
    }
    @WebMethod
    public String selectOnePresupuesto(long idPresupuesto){
        StringBuilder xml = new StringBuilder("<Lista>");
        Presupuesto presupuesto = em.find(Presupuesto.class, idPresupuesto);
        xml.append("<presupuesto>").append(presupuesto.toXML()).append("</presupuesto>\n").append("</Lista>\n");            
        return xml.toString();
    }
}
