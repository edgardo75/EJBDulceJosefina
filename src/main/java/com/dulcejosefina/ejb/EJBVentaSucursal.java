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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public String crearVenta(String xmlVenta){        
        String resultado = "";
        DatosVentaSucursal datosVentaSucursal = transformarAObjeto(xmlVenta);  
        resultado=VerificarStockProductosDeVentaAntesDeAlmacenar(datosVentaSucursal);
        
        if(resultado.isEmpty()){
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
                                venta.setHoraVenta(Calendar.getInstance().getTime());
                                venta.setApellido(datosVentaSucursal.getPersona().getApellido());
                                venta.setNombre(datosVentaSucursal.getPersona().getNombre());
                                venta.setTotalRecargo(BigDecimal.valueOf(datosVentaSucursal.getTotalRecargo()));
                                em.persist(venta);

                    persistirDetalleVenta(venta,datosVentaSucursal);
                    productoBean.actualizarStockProducto(venta, datosVentaSucursal);
                    historicoBean.insertarHistoricoVentaSucursal(venta, datosVentaSucursal);
                    em.flush();
                    resultado = String.valueOf(venta.getId().intValue());
        }
      
    
    return resultado;
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
        if(venta.getAnulado()==0){
        xml.append("<venta>").append("<usuario>").append(em.find(Persona.class, venta.getIdUsuarioExpidioVenta()).getNombre()).append("</usuario>")
                .append(venta.toXML()).append("</venta>\n").append("</Lista>\n");
        }
        return xml.toString();
    }

    @WebMethod
    public String selectVentasHastaElMomento(){
        LocalDateTime timePoint = LocalDateTime.now(ZoneId.systemDefault());   
        Query consulta = em.createNamedQuery("findVentasDiaBySucursalAndFechaYHora");  
        if(timePoint.getHour()>7 && timePoint.getHour()<15){        
                                String horaManana1 = ResourceBundle.getBundle("config").getString("HORA_MANANA1");
                                String horaManana2 = ResourceBundle.getBundle("config").getString("HORA_MANANA2");   
                                 try {
                                     consulta.setParameter("1", new SimpleDateFormat("hh").parse(horaManana1));
                                     consulta.setParameter("2", new SimpleDateFormat("HH").parse(horaManana2));
                                } catch (ParseException ex) {
                                     Logger.getLogger(EJBVentaSucursal.class.getName()).log(Level.SEVERE, null, ex);
                                } 
                            } else {
                                if(timePoint.getHour()>=15&&timePoint.getHour()<23){                                    
                                        String horaTarde1 = ResourceBundle.getBundle("config").getString("HORA_TARDE1");
                                        String horaTarde2 = ResourceBundle.getBundle("config").getString("HORA_TARDE2");   
                                        
                                        try {
                                              consulta.setParameter("1", new SimpleDateFormat("hh").parse(horaTarde1));
                                              consulta.setParameter("2", new SimpleDateFormat("HH").parse(horaTarde2));
                                         } catch (ParseException ex) {
                                             Logger.getLogger(EJBVentaSucursal.class.getName()).log(Level.SEVERE, null, ex);
                                         }                                 
                                        
                                }
                            }        
            
        
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
            xml.append("<venta>\n")
            .append("<idVenta>").append(ventaSucursal.getId()).append("</idVenta>\n")
            .append("<hora>").append(ventaSucursal.getHoraVenta()!=null?new SimpleDateFormat("HH:mm ss").format(ventaSucursal.getHoraVenta()):0).append("</hora>")
            .append("<totalApagar>").append(ventaSucursal.getTotalAPagar()).append("</totalApagar>\n")
            .append("<sucursal>").append(ventaSucursal.getSucursalFK().getNombre()).append("</sucursal>\n")
            .append("<empleado>").append(em.find(Persona.class, ventaSucursal.getIdUsuarioExpidioVenta()).getNombre()).append("</empleado>\n")
            .append("<cliente>").append(ventaSucursal.getNombre()).append("</cliente>\n");
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
            
            cantidadItem=setearCantidadProducto(detalleVentaSucursal,producto);
            
            
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
   public String selectVenta(long idVenta,long idSucursal){
   StringBuilder xml = new StringBuilder("<Lista>");
       // VentaSucursal venta = em.find(VentaSucursal.class, idVenta);  
        Query consultaVentaBySucursal=em.createNamedQuery("findVentaBySucursal").setParameter("idVenta", idVenta).setParameter("idSucursal", idSucursal);
        List<VentaSucursal>lista = consultaVentaBySucursal.getResultList();
        if(lista.isEmpty()){
               xml.append("vacio").append("</Lista>");
        }else{
                for (VentaSucursal ventaSucursal : lista) {
                         xml.append("<item>").append(ventaSucursal.toXML()).append("</item>").append("</Lista>");
                }
        
        }
        
        
//        if(venta == null){
//            xml.append("vacio").append("</Lista>");
//        }else{
//            if(venta.getAnulado()==0&&venta.getSucursalFK().getId()==idSucursal){
//                xml.append("<item>").append(venta.toXML()).append("</item>").append("</Lista>");
//            }
//        
//        }
       
        
   return xml.toString();
   }
   
   @WebMethod()
   public long eliminarProductoDeUnaVenta(long idDetalleVenta,long idProducto,long idVenta){
       int resultadoEliminarProductoDeVenta=0;
       
                    VentaSucursal venta = em.find(VentaSucursal.class, idVenta);
            if(venta.getAnulado()==0){        
                    Producto producto = em.find(Producto.class, idProducto);
                    Query consultaDetalleVentaProductoAEliminar = em.createQuery("SELECT d FROM DetalleVentaSucursal d WHERE d.id =:idDetalleVenta AND d.producto.id =:idProducto");
                    consultaDetalleVentaProductoAEliminar.setParameter("idDetalleVenta", idDetalleVenta);
                    consultaDetalleVentaProductoAEliminar.setParameter("idProducto", idProducto);
                    List<DetalleVentaSucursal>lista = consultaDetalleVentaProductoAEliminar.getResultList();
                    for (DetalleVentaSucursal detalle : lista) {
                        resultadoEliminarProductoDeVenta=setearCantidadProducto(detalle, producto);
                        deleteDetalleVentaAndUpdateList(idDetalleVenta,idVenta,venta);           
                        actualizarStockProducto(producto,detalle);
                        venta.setCantidad(venta.getCantidad()-1);
                        venta.setTotalGeneral(BigDecimal.valueOf(venta.getTotalGeneral().doubleValue()-detalle.getSubtotal().doubleValue()));
                        Double subtotalConDescuentosYRecargos=(venta.getTotalGeneral().doubleValue()+venta.getDescuentoPesos().doubleValue())-venta.getRecargoPesos().doubleValue();
                        
                        venta.setTotalAPagar(BigDecimal.valueOf(subtotalConDescuentosYRecargos));
                        
                    }

                    if(venta.getListaDetalleVentaSucursal().isEmpty()){
                        Query deleteVenta = em.createQuery("DELETE FROM VentaSucursal v WHERE v.id =:idVenta");
                        deleteVenta.setParameter("idVenta", idVenta);
                        deleteVenta.executeUpdate();
                        resultadoEliminarProductoDeVenta=-1;
                    }
                em.flush();
            }else{
                resultadoEliminarProductoDeVenta=-2;
            }
   return resultadoEliminarProductoDeVenta;
   }

    private int setearCantidadProducto(DetalleVentaSucursal detalleVentaSucursal, Producto producto) {
        int cantidadItem=0;
        if(detalleVentaSucursal.getNombrePack().equalsIgnoreCase("precio unitario")){
                cantidadItem=detalleVentaSucursal.getCantidad();
                producto.setCantidadTotalActual(producto.getCantidadTotalActual()+cantidadItem);
                
            }else{
                 cantidadItem= detalleVentaSucursal.getCantidad()*detalleVentaSucursal.getPresentacion();
                producto.setCantidadTotalActual(producto.getCantidadTotalActual()+cantidadItem);
                
            }
        return cantidadItem;
        
        
    }

    private void deleteDetalleVentaAndUpdateList(long idDetalleVenta, long idVenta, VentaSucursal venta) {
       Query consultaDelete = em.createQuery("DELETE FROM DetalleVentaSucursal d WHERE d.id =:idDetalleVenta");         
                    consultaDelete.setParameter("idDetalleVenta", idDetalleVenta);                    
                    consultaDelete.executeUpdate();                    
                    Query consultaDetalleIdVentaSucursal = em.createNamedQuery("selectAllDetalleVentaListForIdVentaSucursal");
                    consultaDetalleIdVentaSucursal.setParameter("id", idVenta);
                    venta.setListaDetalleVentaSucursal(consultaDetalleIdVentaSucursal.getResultList());
                    
    }

    private String VerificarStockProductosDeVentaAntesDeAlmacenar(DatosVentaSucursal datosVentaSucursal) {
        String resultadoVerificacionStock="";
        boolean stock = false;
        List<ItemDetalleVentaSucursalItem>lista = datosVentaSucursal.getDetalleVenta().getList();
        
        
            if(lista.size()>0){
                    for (int i = 0; i < lista.size()&&!stock; i++) {
                        ItemDetalleVentaSucursalItem item = lista.get(i);
                        Producto producto = em.find(Producto.class, item.getId());

                            if(item.getNombrePack().equalsIgnoreCase("precio unitario")){
                                    if(producto.getCantidadTotalActual()-item.getCantidad()<0){
                                        resultadoVerificacionStock = new StringBuilder("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" no se puede agregar al listado por que no tiene stock suficiente, el actual es ").append(producto.getCantidadTotalActual()).
                                                append("\n cantidad ingresada ").append(item.getCantidad()).append(" verifique!".toUpperCase()).toString();
                                        stock = true;
                                    }
                            }else{
                                int stockItem = item.getCantidad()*item.getPresentacion();
                                if(producto.getCantidadTotalActual()<stockItem){
                                    resultadoVerificacionStock = new StringBuilder("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" no se puede agregar al listado por que no tiene stock suficiente, el actual es ").append(producto.getCantidadTotalActual()).
                                                append("\n cantidad ingresada ").append(item.getCantidad()).append(" verifique!".toUpperCase()).toString();

                                }
                            }
                    }
            }
       return resultadoVerificacionStock; 
    }

    private void actualizarStockProducto(Producto producto, DetalleVentaSucursal detalle) {
               producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
                            StockProducto stockProducto = new StockProducto();
                                stockProducto.setCantidadActual(producto.getCantidadTotalActual());
                                stockProducto.setCantidadAgregada(0);
                                stockProducto.setCantidadInicial(0);
                                stockProducto.setDetalle("SE VOLVIO A REPONER STOCK POR PRODUCTO ELIMINADO "+producto.getId()+" DE LA VENTA "+detalle.getVentaSucursal().getId());
                                stockProducto.setFechaAgregadoProducto(producto.getFechaCantidadIngresada());
                                stockProducto.setPorcentajeCompra(detalle.getProducto().getPorcentajeCompra());
                                stockProducto.setPorcentajeVenta(detalle.getProducto().getPorcentajeVenta());
                                stockProducto.setPrecioUnitarioCompra(BigDecimal.ZERO);
                                stockProducto.setPrecioUnitarioVenta(BigDecimal.ZERO);
                                stockProducto.setProducto(producto);
                em.persist(stockProducto);             
                 Query consultaProducto = em.createNamedQuery("findAllStockForIdProduct");
             consultaProducto.setParameter("id", producto.getId());
             
             producto.setStockProductoList(consultaProducto.getResultList());
    }
}
