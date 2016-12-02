package com.dulcejosefina.ejb;
import com.dulcejosefina.entity.DetalleVentaSucursal;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.entity.StockProducto;
import com.dulcejosefina.entity.Sucursal;
import com.dulcejosefina.entity.VentaSucursal;
import com.dulcejosefina.utils.DatosDetalleVentaSucursal;
import com.dulcejosefina.utils.DatosPersona;
import com.dulcejosefina.utils.DatosSucursal;
import com.dulcejosefina.utils.DatosVentaSucursal;
import com.dulcejosefina.utils.ItemDetalleVentaSucursalItem;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
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
public class EJBVentaSucursal {
@PersistenceContext
private EntityManager em;

  @Inject
  private EJBProductoBean productoBean;
  @Inject
  private EJBHistoricoVentaSucursal historicoBean;

    public EJBVentaSucursal() {
    }
  
    @WebMethod
    public int crearVenta(String xmlVenta){        
        DatosVentaSucursal datosVentaSucursal = transformarAObjeto(xmlVenta);       
        VentaSucursal venta = new VentaSucursal();
                    venta.setDescuentoPesos(BigDecimal.valueOf(datosVentaSucursal.getTotalDescuento()));
                    venta.setFechaVenta(Calendar.getInstance().getTime());
                    venta.setIdUsuarioExpidioVenta(datosVentaSucursal.getIdUsuarioExpidioVenta());
                    if(datosVentaSucursal.getPersona().getId()>0){
                        venta.setPersona(em.find(Persona.class, datosVentaSucursal.getPersona().getId()));
                    }
                    venta.setPorcentajeDescuento(BigDecimal.valueOf(datosVentaSucursal.getPorcentajeDescuento()));
                    venta.setPorcentajeRecargo(BigDecimal.valueOf(datosVentaSucursal.getPorcentajeRecargo()));
                    venta.setRecargoPesos(BigDecimal.valueOf(datosVentaSucursal.getTotalRecargo()));
                    venta.setSucursalFK(em.find(Sucursal.class, datosVentaSucursal.getSucursal().getId()));
                    venta.setTotalAPagar(BigDecimal.valueOf(datosVentaSucursal.getTotalAPagar()));
                    venta.setTotalDescuento(BigDecimal.valueOf(datosVentaSucursal.getTotalDescuento()));
                    venta.setTotalGeneral(BigDecimal.valueOf(datosVentaSucursal.getTotalGeneral()));
                    venta.setCantidad(datosVentaSucursal.getCantidad());
                    venta.setAnulado(0);
                    venta.setApellido(datosVentaSucursal.getPersona().getApellido());
                    venta.setNombre(datosVentaSucursal.getPersona().getNombre());
                    venta.setTotalRecargo(BigDecimal.valueOf(datosVentaSucursal.getTotalRecargo()));
        em.persist(venta);
        
        persistirDetalleVenta(venta,datosVentaSucursal);
        productoBean.actualizarStockProducto(venta, datosVentaSucursal);
        historicoBean.insertarHistoricoVentaSucursal(venta, datosVentaSucursal);
        em.flush();
//        
    return venta.getId().intValue();
    }

    private DatosVentaSucursal transformarAObjeto(String datosVenta) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("venta", DatosVentaSucursal.class);
        xstream.alias("persona", DatosPersona.class);
        xstream.alias("sucursal",DatosSucursal.class);
        xstream.alias("detalleVenta",DatosDetalleVentaSucursal.class);
        xstream.alias("itemVenta", ItemDetalleVentaSucursalItem.class);
        xstream.addImplicitCollection(DatosDetalleVentaSucursal.class, "list");
        return (DatosVentaSucursal) xstream.fromXML(datosVenta);
    }

    private void persistirDetalleVenta(VentaSucursal venta, DatosVentaSucursal datosVentaSucursal) {
        
        List<ItemDetalleVentaSucursalItem>lista=datosVentaSucursal.getDetalleVenta().getList();
        for (ItemDetalleVentaSucursalItem itemDetalleVentaSucursalItem : lista) {
            DetalleVentaSucursal detalle = new DetalleVentaSucursal();
                detalle.setVentaSucursal(venta);
                detalle.setCodigo(itemDetalleVentaSucursalItem.getCodigo());
                detalle.setIdVentaProducto(itemDetalleVentaSucursalItem.getIdVentaProducto());
                detalle.setDescripcion(itemDetalleVentaSucursalItem.getDescripcion());
                detalle.setIdPack(itemDetalleVentaSucursalItem.getIdPack());
                detalle.setProducto(em.find(Producto.class, itemDetalleVentaSucursalItem.getId()));
                detalle.setNombrePack(itemDetalleVentaSucursalItem.getNombrePack());
                detalle.setPrecio(itemDetalleVentaSucursalItem.getPrecio());
                detalle.setPresentacion(itemDetalleVentaSucursalItem.getPresentacion());
                detalle.setCantidad(itemDetalleVentaSucursalItem.getCantidad());
                detalle.setSubtotal(BigDecimal.valueOf(itemDetalleVentaSucursalItem.getSubtotal()));
                em.persist(detalle);
        }
        
        Query consulta = em.createNamedQuery("selectAllDetalleVentaListForIdVentaSucursal");
        consulta.setParameter("id", venta.getId());
        venta.setListaDetalleVentaSucursal(consulta.getResultList());
    }
@WebMethod
    public String selectUnaVenta(long idVenta) {
        StringBuilder xml = new StringBuilder("<Lista>");
        VentaSucursal venta = em.find(VentaSucursal.class, idVenta);
        xml.append("<venta>").append("<usuario>").append(em.find(Persona.class, venta.getIdUsuarioExpidioVenta()).getNombre()).append("</usuario>")
                .append(venta.toXML()).append("</venta>\n").append("</Lista>\n");
        return xml.toString();
    }

    @WebMethod
    public String selectVentasHastaElMomento(){
        
        Query consulta = em.createNamedQuery("findVentasDiaBySucursal");       
        List<VentaSucursal>lista = consulta.getResultList();
        return recorreLista(lista);
        
    }
    @WebMethod
    public String selectVentasDeUnDiaDeterminado(String fecha){         
        Query consulta = em.createNamedQuery("findVentaDelDiaPorFecha");    
        consulta.setParameter("fecha",fecha);
        List<VentaSucursal>lista = consulta.getResultList();
        return recorreLista(lista);
    }

    private String recorreLista(List<VentaSucursal> lista) {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        double totalVentas=0;
        for (VentaSucursal ventaSucursal : lista) {            
            xml.append("<venta>\n");
            xml.append("<idVenta>").append(ventaSucursal.getId()).append("</idVenta>\n");
            xml.append("<totalApagar>").append(ventaSucursal.getTotalAPagar()).append("</totalApagar>\n");
            xml.append("<sucursal>").append(ventaSucursal.getSucursalFK().getNombre()).append("</sucursal>\n");
            xml.append("<empleado>").append(em.find(Persona.class, ventaSucursal.getIdUsuarioExpidioVenta()).getNombre()).append("</empleado>\n");
            xml.append("<cliente>").append(ventaSucursal.getNombre()).append("</cliente>\n");
            for(DetalleVentaSucursal detalle:ventaSucursal.getListaDetalleVentaSucursal()){
                xml.append("<item>\n");
                xml.append("<codigo>").append(detalle.getCodigo()).append("</codigo>\n")
                        .append("<nombre>").append(detalle.getDescripcion()).append("</nombre>\n")                        
                        .append("<presentacion>").append(detalle.getPresentacion()).append("</presentacion>\n")                        
                        .append("<precio>").append(DecimalFormat.getInstance().format(detalle.getPrecio())).append("</precio>\n")
                        .append("<cantidad>").append(detalle.getCantidad()).append("</cantidad>\n")
                        .append("<subtotal>").append(DecimalFormat.getInstance().format(detalle.getSubtotal())).append("</subtotal>\n");
                
                xml.append("</item>\n");
            totalVentas=totalVentas+detalle.getSubtotal().doubleValue();
            }
            xml.append("</venta>\n");
            
        }
        xml.append("<totalVentas>").append(DecimalFormat.getInstance().format(totalVentas)).append("</totalVentas>\n");
        
        return xml.append("</Lista>").toString();
    }
    @WebMethod
    public long anularVenta(long idVenta){
        long retorno=0;
        int cantidadItem=0;
        VentaSucursal venta = em.find(VentaSucursal.class, idVenta);
        venta.setAnulado(1);
        List<DetalleVentaSucursal>lista = venta.getListaDetalleVentaSucursal();
        for (DetalleVentaSucursal detalleVentaSucursal : lista) {
            cantidadItem=0;
            Producto producto = em.find(Producto.class, detalleVentaSucursal.getProducto().getId());
            if(detalleVentaSucursal.getNombrePack().equalsIgnoreCase("precio unitario")){
                cantidadItem=detalleVentaSucursal.getCantidad();
                producto.setCantidadTotalActual(producto.getCantidadTotalActual()+cantidadItem);
                
            }else{
                 cantidadItem= detalleVentaSucursal.getCantidad()*detalleVentaSucursal.getPresentacion();
                producto.setCantidadTotalActual(producto.getCantidadTotalActual()+cantidadItem);
                
            }
            
            producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
            StockProducto stockProducto = new StockProducto();
                stockProducto.setCantidadActual(producto.getCantidadTotalActual());
                stockProducto.setCantidadAgregada(0);
                stockProducto.setCantidadInicial(0);
                stockProducto.setDetalle("SE VOLVIO A REPONER STOCK POR VENTA ANULADA "+venta.getId()+" CANTIDAD REPUESTA "+cantidadItem);
                stockProducto.setFechaAgregadoProducto(producto.getFechaCantidadIngresada());
                stockProducto.setPorcentajeCompra(detalleVentaSucursal.getProducto().getPorcentajeCompra());
                stockProducto.setPorcentajeVenta(detalleVentaSucursal.getProducto().getPorcentajeVenta());
                stockProducto.setPrecioUnitarioCompra(BigDecimal.ZERO);
                stockProducto.setPrecioUnitarioVenta(BigDecimal.ZERO);
                stockProducto.setProducto(producto);
                em.persist(stockProducto);
                
                
                
                 Query consulta = em.createNamedQuery("findAllStockForIdProduct");
             consulta.setParameter("id", producto.getId());
             
             producto.setStockProductoList(consulta.getResultList());
            
        }
        
       
        
        
        em.flush();
        retorno=venta.getId();
    return retorno;
    }
    @WebMethod
    public int getRecordCountVentas(){
        Query consulta = em.createNamedQuery("findAllVentas");
        return consulta.getResultList().size();
    }
    @WebMethod
    public String verVentasPaginadas(int index,int recordCount){
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createNamedQuery("findAllVentas");
        consulta.setMaxResults(recordCount);
        consulta.setFirstResult(index*recordCount);
        List<VentaSucursal>lista = consulta.getResultList();
        
        for (VentaSucursal ventaSucursal : lista) {
            xml.append("<item>").append(ventaSucursal.toXML()).append("</item>");
        }
        
        return xml.append("</Lista>").toString();
    
    }
   @WebMethod(operationName = "selectAllVentas")
   public String selectAllVentas(){
   StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createNamedQuery("findAllVentas");
        List<VentaSucursal>lista = consulta.getResultList();
        for (VentaSucursal ventaSucursal : lista) {
           xml.append("<item>").append(ventaSucursal.toXML()).append("</item>");
       }
        return xml.append("</Lista>").toString();
   }
   @WebMethod(operationName = "selectVenta")
   public String selectVenta(long idVenta){
   StringBuilder xml = new StringBuilder("<Lista>");
        VentaSucursal venta = em.find(VentaSucursal.class, idVenta);  
        if(venta == null){
            xml.append("vacio").append("</Lista>");
        }else{
        xml.append("<item>").append(venta.toXML()).append("</item>").append("</Lista>");
        
        }
       
        
   return xml.toString();
   }
}
