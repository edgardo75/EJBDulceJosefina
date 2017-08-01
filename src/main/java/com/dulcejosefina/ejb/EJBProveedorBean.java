package com.dulcejosefina.ejb;
import com.dulcejosefina.entity.DetallePedido;
import com.dulcejosefina.entity.Pedido;
import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.entity.Proveedor;
import com.dulcejosefina.entity.Sucursal;
import com.dulcejosefina.utils.DatosVentaSucursal;
import com.dulcejosefina.utils.ItemDetalleVentaSucursalItem;
import java.math.BigDecimal;
import java.util.Calendar;
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
     @WebMethod
    public long crearPedidoProveedor(String datosPedido) {
         DatosVentaSucursal datosPedidoProveedor = new DatosVentaSucursal();
                 datosPedidoProveedor = datosPedidoProveedor.transformarAObjeto(datosPedido);
                 Pedido pedido = new Pedido();
                 pedido.setCantidad(datosPedidoProveedor.getCantidad());
                 pedido.setDescuentoPesos(BigDecimal.valueOf(datosPedidoProveedor.getTotalDescuento()));
                 pedido.setFechaVenta(Calendar.getInstance().getTime());
                 pedido.setIdUsuarioExpidioPedido(datosPedidoProveedor.getIdUsuarioExpidioVenta());
                 pedido.setPorcentajeDescuento(BigDecimal.valueOf(datosPedidoProveedor.getPorcentajeDescuento()));
                 pedido.setPorcentajeRecargo(BigDecimal.valueOf(datosPedidoProveedor.getPorcentajeRecargo()));
                 pedido.setProveedorFK(em.find(Proveedor.class, datosPedidoProveedor.getProveedor().getId()));
                 pedido.setRecargoPesos(BigDecimal.valueOf(datosPedidoProveedor.getTotalRecargo()));
                 pedido.setSucursalFK(em.find(Sucursal.class,datosPedidoProveedor.getSucursal().getId()));
                 pedido.setTotalAPagar(BigDecimal.valueOf(datosPedidoProveedor.getTotalAPagar()));
                 pedido.setTotalDescuento(BigDecimal.valueOf(datosPedidoProveedor.getTotalDescuento()));
                 pedido.setTotalGeneral(BigDecimal.valueOf(datosPedidoProveedor.getTotalGeneral()));
                 pedido.setTotalRecargo(BigDecimal.valueOf(datosPedidoProveedor.getTotalRecargo()));
                 em.persist(pedido);
                 persistirDetallePedido(pedido,datosPedidoProveedor);   
        return pedido.getId().intValue();
    }
    private void persistirDetallePedido(Pedido pedido,DatosVentaSucursal datosPedidoProveedor){
        List<ItemDetalleVentaSucursalItem>lista=datosPedidoProveedor.getDetalleVenta().getList();
        for (ItemDetalleVentaSucursalItem item : lista) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setCantidad(item.getCantidad());
            detalle.setCodigo(item.getCodigo());
            detalle.setDescripcion(item.getDescripcion());
            detalle.setIdPack(item.getIdPack());
            detalle.setIdVentaProducto(item.getIdVentaProducto());
            detalle.setNombrePack(item.getNombrePack());
            detalle.setPrecio(item.getPrecio());
            detalle.setPresentacion(item.getPresentacion());
            detalle.setProducto(em.find(Producto.class, item.getId()));
            detalle.setSubtotal(BigDecimal.valueOf(item.getSubtotal()));
            em.persist(detalle);
        }
        Query consulta = em.createQuery("SELECT d FROM DetallePedido d WHERE d.pedido.id =:id");
        consulta.setParameter("id", pedido.getId());
        pedido.setDetallePedidoList(consulta.getResultList());
    }
    @WebMethod
    public String selectOnePedidoProveedor(long idPedido){
        String xml = "<Lista>";
        Pedido pedido = em.find(Pedido.class, idPedido);        
        return xml.concat(pedido.toXML()).concat("</Lista>");
    }
}