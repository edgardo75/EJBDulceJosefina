package com.dulcejosefina.ejb;
import com.dulcejosefina.entity.CompraProducto;
import com.dulcejosefina.entity.ImagenProducto;
import com.dulcejosefina.entity.PackProducto;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.entity.Proveedor;
import com.dulcejosefina.entity.StockProducto;
import com.dulcejosefina.entity.Sucursal;
import com.dulcejosefina.entity.VentaProducto;
import com.dulcejosefina.entity.VentaSucursal;
import com.dulcejosefina.utils.DatosCompraProducto;
import com.dulcejosefina.utils.DatosCompraProductoItem;
import com.dulcejosefina.utils.DatosImagenProducto;
import com.dulcejosefina.utils.DatosProducto;
import com.dulcejosefina.utils.DatosProveedor;
import com.dulcejosefina.utils.DatosSucursal;
import com.dulcejosefina.utils.DatosVentaProducto;
import com.dulcejosefina.utils.DatosVentaProductoItem;
import com.dulcejosefina.utils.DatosVentaSucursal;
import com.dulcejosefina.utils.Imagen;
import com.dulcejosefina.utils.ItemDetalleVentaSucursalItem;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.TemporalType;

@Stateless
@LocalBean
@WebService
public class EJBProductoBean {
@PersistenceContext
private EntityManager em;
private final Imagen imagen = new Imagen();
private long codigoProveedor = 0;
 private static final String PATH = ResourceBundle.getBundle("config").getString("PATH_FILE");        
 @Inject
 private EJBProveedorBean proveedor;
  
 
 @WebMethod(operationName = "selectProductoByCodigoBarra")
 public String selectProductoByCodigoBarra(String codigoBarra){
      
      Query consulta = em.createNamedQuery("findProductoByCodigoBarraOnly");
      consulta.setParameter("codigo", codigoBarra.trim());
      List<Producto>lista= consulta.getResultList();
      
     return xmlProducto(lista);
 }
    @WebMethod(operationName = "selectAllProducto")
    public String selectAllProducto(long idSucursal) {
        
        Query consulta = consultarProductosDeSucursal(idSucursal);
        List<Producto>lista = consulta.getResultList();
        
            
            return xmlProducto(lista);
                    
                    
    }
    @WebMethod
     public int getRecorCountProductos(long idSucursal) {
        
        
            Query productos = consultarProductosDeSucursal(idSucursal);
            
            return productos.getResultList().size();    
    }
     @WebMethod
     public String verProductoPaginados(int index, int recordCount,long idSucursal) {        
         
         
         Query consulta = consultarProductosDeSucursal(idSucursal);
         consulta.setMaxResults(recordCount);
         consulta.setFirstResult(index*recordCount);
         List<Producto>lista = consulta.getResultList();
         
         
     
         return xmlProducto(lista);
     }
@WebMethod
    public long crearProducto(String xmlProducto) {
        long retorno=0;
        DatosProducto getDatosProducto = (DatosProducto) transformaAObjetos(xmlProducto);
            if(getDatosProducto.getId()>0){

                retorno = actualizarProducto(getDatosProducto);
                
            }else{
                retorno = crearObjProducto(getDatosProducto);
            }

        em.flush();
        
        return retorno;
    }
    @WebMethod
    public long aplicarPorcentajeProducto(long idProducto,String porcentaje,String modalidad){
        long retorno =0;            
        Producto producto = em.find(Producto.class, idProducto);
        
        switch(modalidad){
                    case "COMPRA":{       
                            retorno = calcularPorcentajeCompra(producto,porcentaje); 
                            producto.setPorcentajeCompra(Double.valueOf(porcentaje));
                            producto.setDetalleCompra(producto.getDetalleCompra().concat(" porcentaje ").concat(porcentaje));
                    }

                break;
                    case "VENTA":{

                        retorno = calcularPorcentajeVenta(producto,porcentaje);
                        producto.setPorcentajeCompra(Double.valueOf(porcentaje));
                        producto.setDetalleVenta(producto.getDetalleVenta().concat(" porcentaje ").concat(porcentaje));
                    }
                }
        return retorno;
    }
@WebMethod
    public long aplicarPorcentajeTodoProductoProveedor(long idProveedor, String porcentaje) {
        long retorno = 0;
        Query consulta = em.createQuery("SELECT p from Producto p WHERE p.proveedorFK.id =:idProveedor");
        consulta.setParameter("idProveedor", idProveedor);
        List<Producto>listado = consulta.getResultList();
        
        if(!listado.isEmpty()){
                
            for (Producto producto : listado) {
                
                retorno = calcularPorcentajeCompra(producto, porcentaje);
                producto.setDetalleCompra(producto.getDetalleCompra()+" Ingreso porcentaje "+porcentaje+" Proveedor "+em.find(Proveedor.class, idProveedor).getNombre());
                producto.setPorcentajeCompra(Double.valueOf(porcentaje));
                
                retorno = calcularPorcentajeVentaProductosProveedor(producto);
                producto.setDetalleVenta(producto.getDetalleVenta()+" Ingreso porcentaje"+porcentaje+" Proveedor "+em.find(Proveedor.class, idProveedor).getNombre());
                producto.setPorcentajeVenta(Double.valueOf(porcentaje));
            }
            
        }
        
        
        
        return retorno;
    }

    private long calcularPorcentajeVentaProductosProveedor(Producto producto){
        
        List<VentaProducto> lista = producto.getVenta();
        BigDecimal resultado;
        
        BigDecimal precioUnitarioVenta = null;
        BigDecimal porcentajeVenta = null;
        
        
        
        for (VentaProducto ventaProducto : lista) {
            Query consultaPrecioCompraProducto = em.createQuery("SELECT c FROM CompraProducto c WHERE c.productoFK.id =:producto");
            consultaPrecioCompraProducto.setParameter("producto", ventaProducto.getProductoFK().getId());
            List<CompraProducto>listaCompra=consultaPrecioCompraProducto.getResultList();
            
            
            
            
            for (CompraProducto compraProducto : listaCompra) {
                if(Objects.equals(compraProducto.getProducto().getId(), ventaProducto.getProductoFK().getId())&&(ventaProducto.getPackFK().getDescripcion() == null ? compraProducto.getPackFK().getDescripcion() == null : ventaProducto.getPackFK().getDescripcion().equals(compraProducto.getPackFK().getDescripcion()))){
                                    if(ventaProducto.getPrecio().intValue()>0){
                                        resultado = calculoPorcentaje(compraProducto.getPrecio(), BigDecimal.valueOf(compraProducto.getPorcentaje()));
                                        
                                        ventaProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue()+resultado.doubleValue()));
                                    }                
                
                }
            }
  
            
            

            
            
             ventaProducto.setProductoFK(producto);
            producto.setPorcentajeVenta(ventaProducto.getPorcentaje());
            em.flush();
            
            
                
        }
           
            
        Query consultaPrecioPackUnitario =em.createQuery("SELECT v FROM VentaProducto v WHERE v.packFK.id =:pack AND v.productoFK.id =:idProducto");
        consultaPrecioPackUnitario.setParameter("pack", (long)1);
        consultaPrecioPackUnitario.setParameter("idProducto", producto.getId());
        List<VentaProducto>listaVentaPrecioUnitario = consultaPrecioPackUnitario.getResultList();
        for (VentaProducto ventaProducto : listaVentaPrecioUnitario) {
            precioUnitarioVenta=ventaProducto.getPrecio();
            porcentajeVenta=BigDecimal.valueOf(ventaProducto.getPorcentaje());
        }
           resultado = precioUnitarioVenta.multiply((porcentajeVenta.doubleValue()>0?porcentajeVenta:porcentajeVenta.negate())).divide(new BigDecimal(100));
            
            if(porcentajeVenta.doubleValue()>0){
                producto.setPrecioUnitarioVenta(BigDecimal.valueOf(precioUnitarioVenta.doubleValue()+resultado.doubleValue()));
            }else{
                producto.setPrecioUnitarioVenta(BigDecimal.valueOf(precioUnitarioVenta.doubleValue()-resultado.doubleValue()));
            }
             registrarOperacionVentaEnStock(producto,String.valueOf(porcentajeVenta));
            em.flush();
        
       return producto.getId();
    
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

   

    private long calcularPorcentajeCompra(Producto producto, String porcentaje) {
        
        List<CompraProducto>lista = producto.getCompra();
        BigDecimal resultado =null;
        BigDecimal percent = new BigDecimal(porcentaje);
        BigDecimal precionUnitarioCompra=null;
        
                
        for (CompraProducto compraProducto : lista) {
           switch(percent.signum()){
               case 1:{
                    if(compraProducto.getPrecio().intValue()>0){
                    resultado = calculoPorcentaje(compraProducto.getPrecio(),percent);
                    
                        compraProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue()+resultado.doubleValue()));
                }
               }break;
               case -1:{
                     if(compraProducto.getPrecio().intValue()>0){
                        resultado=calculoPorcentaje(compraProducto.getPrecio(), percent);
                        compraProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue()-resultado.doubleValue()));
                     }
               }
           }

            
         
            
            compraProducto.setProducto(producto);
            
            em.flush();
           
            
        }
            
            
            producto.setPorcentajeCompra(Double.parseDouble(porcentaje));
            
            
            Query consultaPrecioPackUnitario=em.createQuery("SELECT c FROM CompraProducto c WHERE c.packFK.id =:pack AND c.productoFK.id =:idProducto");
            consultaPrecioPackUnitario.setParameter("pack", (long)1);
            consultaPrecioPackUnitario.setParameter("idProducto", producto.getId());
            List<CompraProducto>listaCompraUnitario = consultaPrecioPackUnitario.getResultList();
            for (CompraProducto compraProducto : listaCompraUnitario) {
                precionUnitarioCompra=compraProducto.getPrecio();
            }

            resultado = precionUnitarioCompra.multiply((percent.doubleValue()>0?percent:percent.negate())).divide(new BigDecimal(100));
            if(percent.doubleValue()>0){
                producto.setPrecioUnitarioCompra(BigDecimal.valueOf(precionUnitarioCompra.doubleValue()+resultado.doubleValue()));  
            }else{
                producto.setPrecioUnitarioCompra(BigDecimal.valueOf(precionUnitarioCompra.doubleValue()-resultado.doubleValue()));
            }
            registrarOperacionCompraEnStock(producto,porcentaje);
            em.flush();
            
        return producto.getId();
    }

    private long calcularPorcentajeVenta(Producto producto, String porcentaje) {
        List<VentaProducto>lista = producto.getVenta();
        BigDecimal resultado =null;
        BigDecimal percent = new BigDecimal(porcentaje);
        BigDecimal precioUnitarioVenta = null;
        BigDecimal porcentajeVenta = null;
        for (VentaProducto ventaProducto : lista) {
            switch(percent.signum()){
                case 1:{
                    if(ventaProducto.getPrecio().doubleValue()>0){
                        resultado=calculoPorcentaje(ventaProducto.getPrecio(), percent);
                        ventaProducto.setPrecio(BigDecimal.valueOf(ventaProducto.getPrecio().doubleValue()+resultado.doubleValue()));
                        
                    
                    }
                }break;
                case -1:{
                    resultado=calculoPorcentaje(ventaProducto.getPrecio(), percent);
                    ventaProducto.setPrecio(BigDecimal.valueOf(ventaProducto.getPrecio().doubleValue()-resultado.doubleValue()));
                }
            
            }
            
             ventaProducto.setProductoFK(producto);
            
            em.flush();
            
        }
        
        
        Query consultaPrecioPackUnitario =em.createQuery("SELECT v FROM VentaProducto v WHERE v.packFK.id =:pack AND v.productoFK.id =:idProducto");
        consultaPrecioPackUnitario.setParameter("pack", (long)1);
        consultaPrecioPackUnitario.setParameter("idProducto", producto.getId());
        List<VentaProducto>listaVentaPrecioUnitario = consultaPrecioPackUnitario.getResultList();
        for (VentaProducto ventaProducto : listaVentaPrecioUnitario) {
            precioUnitarioVenta=ventaProducto.getPrecio();
            porcentajeVenta=BigDecimal.valueOf(ventaProducto.getPorcentaje());
        }
           resultado = precioUnitarioVenta.multiply((porcentajeVenta.doubleValue()>0?porcentajeVenta:porcentajeVenta.negate())).divide(new BigDecimal(100));
            
            if(porcentajeVenta.doubleValue()>0){
                producto.setPrecioUnitarioVenta(BigDecimal.valueOf(precioUnitarioVenta.doubleValue()+resultado.doubleValue()));
            }else{
                producto.setPrecioUnitarioVenta(BigDecimal.valueOf(precioUnitarioVenta.doubleValue()-resultado.doubleValue()));
            }
             registrarOperacionVentaEnStock(producto,String.valueOf(porcentajeVenta));
            em.flush();
        
        
        
        return producto.getId();
    }

    private long crearObjProducto(DatosProducto datosProducto) {   
         Producto producto = new Producto();
        try {  
                    producto.setCantidadIngresada(datosProducto.getCantidadIngresada());
                    producto.setCantidadInicial(datosProducto.getPrimerCantidadInicial());
                    producto.setCantidadTotalActual(datosProducto.getCantidadTotalActual());
                    producto.setCodigoBarra(datosProducto.getCodigoBarra());
                    producto.setDescripcion(datosProducto.getDescripcion());
                    producto.setDetalleProducto(datosProducto.getDetalle());
                    producto.setDetalleCompra(datosProducto.getCompraProducto().getDetalle());
                    producto.setDetalleVenta(datosProducto.getVentaProducto().getDetalle());
                    producto.setFraccionado((char)datosProducto.getFraccionado());
                    producto.setFechaCantidadIngresada(Calendar.getInstance().getTime());
                    producto.setFechaIngresoInicial(Calendar.getInstance().getTime());
                    producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
                    producto.setFechaUltimoIngreso(Calendar.getInstance().getTime());        
                            try {
                                Date fechaVencimiento = new SimpleDateFormat("dd/MM/yyyy").parse(datosProducto.getFechaVencimiento());

                                producto.setFechaVencimiento(fechaVencimiento);
                            } catch (ParseException ex) {
                                Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    producto.setPersonaFK(em.find(Persona.class, datosProducto.getPersona().getId()));
                    producto.setPorcentajeCompra((datosProducto.getPorcentajeCompra()>0?datosProducto.getPorcentajeCompra():0));
                    producto.setPorcentajeVenta((datosProducto.getPorcentajeVenta()>0?datosProducto.getPorcentajeVenta():0));
                    producto.setPrecioUnitarioCompra(datosProducto.getPrecioUnitarioCompra()>0?BigDecimal.valueOf(datosProducto.getPrecioUnitarioCompra()):BigDecimal.ZERO);
                    producto.setPrecioUnitarioVenta(datosProducto.getPrecioUnitarioVenta()>0?BigDecimal.valueOf(datosProducto.getPrecioUnitarioVenta()):BigDecimal.ZERO);
                    producto.setProveedorFK(em.find(Proveedor.class, datosProducto.getProveedor().getId()));
                    producto.setSucursalFK(em.find(Sucursal.class, datosProducto.getSucursal().getId()));
                    em.persist(producto);
        
                        persistirListaCompraProducto(producto,datosProducto);
                        persistirListaVentaProducto(producto,datosProducto);
                        persistirListaProductosProveedor(datosProducto);
                        persistirProductosDeSucursal(datosProducto);
                        persistirListaStockProducto(producto);
        
            } catch (Exception e) {
            }
        
        return producto.getId();
    }

    private Date formatearFecha(String fecha) throws ParseException{
        return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
    
    }
    private long actualizarProducto(DatosProducto datosProducto) {        

        Producto producto = em.find(Producto.class, datosProducto.getId());
        Query consulta = em.createNamedQuery("findProductoByDescripcion");
        consulta.setParameter("descripcion".toLowerCase(), datosProducto.getDescripcion().trim());
        
        if(!datosProducto.getDescripcion().equalsIgnoreCase(producto.getDescripcion())&&consulta.getResultList().isEmpty()){
            
            producto.setDescripcion(datosProducto.getDescripcion());
        }
        producto.setDetalleCompra(datosProducto.getCompraProducto().getDetalle());
        producto.setDetalleProducto(datosProducto.getDetalle());        
        producto.setDetalleVenta(datosProducto.getVentaProducto().getDetalle());
        producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
        producto.setPrecioUnitarioCompra(BigDecimal.valueOf(datosProducto.getPrecioUnitarioCompra()));
        producto.setPrecioUnitarioVenta(BigDecimal.valueOf(datosProducto.getPrecioUnitarioVenta()));
        producto.setCodigoBarra(datosProducto.getCodigoBarra());
        producto.setPorcentajeCompra(datosProducto.getPorcentajeCompra()>0?datosProducto.getPorcentajeCompra():0);
        producto.setPorcentajeVenta(datosProducto.getPorcentajeVenta()>0?datosProducto.getPorcentajeVenta():0);
        producto.setFraccionado(datosProducto.getFraccionado());
        if(producto.getCantidadIngresada()==0){
            
            try {
                producto.setCantidadIngresada(datosProducto.getCantidadIngresada());
                producto.setCantidadInicial(datosProducto.getCantidadIngresada());
                producto.setCantidadTotalActual(datosProducto.getCantidadTotalActual());
                producto.setFechaCantidadIngresada(Calendar.getInstance().getTime());
                
                
                
                if(verificarFechasVecimiento(producto, formatearFecha(datosProducto.getFechaVencimiento()))){
                    producto.setFechaVencimiento(formatearFecha(datosProducto.getFechaVencimiento()));
                }
                
                
                
                producto.setFechaIngresoInicial(Calendar.getInstance().getTime());
                producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
                producto.setFechaUltimoIngreso(Calendar.getInstance().getTime());
                actualizarListaStock(producto,datosProducto);
            } catch (ParseException ex) {
                Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        actualizarListaCompra(datosProducto);
        actualizarListaVenta(datosProducto);
        
        em.flush();
        return producto.getId();
    }

    private long persistirListaCompraProducto(Producto producto, DatosProducto datosProducto) {
        if(datosProducto.getCompraProducto().getList().size()>0){
            List<DatosCompraProductoItem>lista = datosProducto.getCompraProducto().getList();
            for (DatosCompraProductoItem datosCompraProductoItem : lista) {
                    CompraProducto compraProducto = new CompraProducto();
                    
                    compraProducto.setPackFK(em.find(PackProducto.class, datosCompraProductoItem.getPackProductoId()));
                    compraProducto.setPrecio(BigDecimal.valueOf(datosCompraProductoItem.getPrecio()));
                    compraProducto.setPresentacion(datosCompraProductoItem.getPresentacion());
                    compraProducto.setPorcentaje(Double.valueOf(datosCompraProductoItem.getPorcentaje()));
                    compraProducto.setProducto(em.find(Producto.class, producto.getId()));
                    em.persist(compraProducto);
            }
                
                Query consulta = em.createQuery("SELECT c FROM CompraProducto c WHERE c.productoFK.id =:id");
                consulta.setParameter("id", producto.getId());
                producto.setCompra(consulta.getResultList());
                //producto.setFechaUltimaCompra(Calendar.getInstance().getTime());
                em.merge(producto);
                
                
                
        
        }
        return producto.getId();
    }

    private void persistirListaVentaProducto(Producto producto, DatosProducto datosProducto) {
        if(!datosProducto.getVentaProducto().getList().isEmpty()){
            List<DatosVentaProductoItem>lista=datosProducto.getVentaProducto().getList();
            
            for (DatosVentaProductoItem datosVentaProductoItem : lista) {
                VentaProducto ventaProducto = new VentaProducto();
                
                ventaProducto.setPackFK(em.find(PackProducto.class, datosVentaProductoItem.getPackProductoId()));
                ventaProducto.setPrecio(BigDecimal.valueOf(datosVentaProductoItem.getPrecio()));
                ventaProducto.setPresentacion(datosVentaProductoItem.getPresentacion());
                ventaProducto.setProductoFK(em.find(Producto.class, producto.getId()));
                ventaProducto.setPorcentaje(Double.valueOf(datosVentaProductoItem.getPorcentaje()));
                em.persist(ventaProducto);
            }
            Query consulta = em.createQuery("SELECT v FROM VentaProducto v WHERE v.productoFK.id =:id");
            consulta.setParameter("id", producto.getId());
            producto.setVenta(consulta.getResultList());
            //producto.setFechaUltimaVenta(Calendar.getInstance().getTime());
            em.merge(producto);
           
                    
        
        }
    }

    private void persistirListaProductosProveedor(DatosProducto datosProducto) {
        Proveedor prove = em.find(Proveedor.class, datosProducto.getProveedor().getId());
        Query consulta = em.createNamedQuery("proveedorFindAll");
        consulta.setParameter("id", prove.getId());
        prove.setProducto(consulta.getResultList());
        em.merge(prove);
       
    }

    private void persistirProductosDeSucursal(DatosProducto datosProducto) {
        Sucursal sucursal = em.find(Sucursal.class, datosProducto.getSucursal().getId());
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.sucursalFK.id =:id");
        consulta.setParameter("id", sucursal.getId());
        sucursal.setProductoList(consulta.getResultList());
        em.merge(sucursal);
       
    }

    private void persistirListaStockProducto(Producto producto) {
        StockProducto stock = new StockProducto();
        
        stock.setFechaAgregadoProducto(Calendar.getInstance().getTime());
        
        stock.setPrecioUnitarioCompra(producto.getPrecioUnitarioCompra());
        
        stock.setPrecioUnitarioVenta(producto.getPrecioUnitarioVenta());
        
        stock.setCantidadActual(producto.getCantidadTotalActual());
        
        stock.setCantidadInicial(producto.getCantidadInicial());
        
        stock.setCantidadAgregada(producto.getCantidadIngresada());
        
        stock.setDetalle("SE INGRESO EL PRODUCTO AL SISTEMA");
        
        stock.setProducto(producto);
        
        em.persist(stock);
        
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        
        consulta.setParameter("id", producto.getId());
        
        producto.setStockProductoList(consulta.getResultList());
        
        em.merge(producto);
       
    }

    private void registrarOperacionCompraEnStock(Producto producto, String porcentaje) {
        StockProducto stock = new StockProducto();
        stock.setDetalle("APLICADO EL PORCENTAJE COMPRA "+porcentaje+" fecha "+Calendar.getInstance().getTime());
        stock.setProducto(producto);
        stock.setFechaAgregadoProducto(Calendar.getInstance().getTime());
        stock.setPorcentajeCompra(Double.valueOf(porcentaje));
        stock.setPorcentajeVenta(0);
        stock.setPrecioUnitarioCompra(producto.getPrecioUnitarioCompra());
        stock.setPrecioUnitarioVenta(BigDecimal.ZERO);        
        em.persist(stock);
        
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        producto.setStockProductoList(consulta.getResultList());
        em.merge(producto);
        em.flush();
        
    }

    private void registrarOperacionVentaEnStock(Producto producto, String porcentaje) {
        StockProducto stock = new StockProducto();
        stock.setDetalle("APLICADO EL PORCENTAJE VENTA"+porcentaje+" fecha "+Calendar.getInstance().getTime());
        stock.setProducto(producto);
        em.persist(stock);
        
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        producto.setStockProductoList(consulta.getResultList());
        em.merge(producto);
        em.flush();
    }
@WebMethod
    public long aplicarCantidadIngresada(long idProducto, int cantidadIngresada) {
        
            Producto producto = em.find(Producto.class, idProducto);
            int cantidadCalculada = cantidadIngresada+producto.getCantidadTotalActual();
            producto.setCantidadIngresada(cantidadIngresada);                        
            producto.setFechaUltimoIngreso(Calendar.getInstance().getTime());
            producto.setFraccionado(0);            
            producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());           
            producto.setCantidadTotalActual(cantidadCalculada);
            producto.setFechaCantidadIngresada(Calendar.getInstance().getTime());
            producto.setFechaUltimaCompra(Calendar.getInstance().getTime());
            producto.setDetalleCompra("Ultima cantidad ingresada "+cantidadIngresada);
            registrarCantidadIngresadaEnStock(producto,cantidadIngresada);
        
            em.merge(producto);
            em.flush();
            
        return producto.getId();
    }

    private void registrarCantidadIngresadaEnStock(Producto producto, int cantidadIngresada) {
        StockProducto stock = new StockProducto();
        stock.setCantidadAgregada(cantidadIngresada);
        stock.setCantidadActual(producto.getCantidadTotalActual());
        stock.setFechaAgregadoProducto(Calendar.getInstance().getTime());
        stock.setPorcentajeCompra(0);
        stock.setPorcentajeVenta(0);
        stock.setPrecioUnitarioCompra(BigDecimal.ZERO);
        stock.setPrecioUnitarioVenta(BigDecimal.ZERO);
        stock.setProducto(producto);
        stock.setDetalle(" CANTIDAD AGREGADA EL "+Calendar.getInstance().getTime());
        em.persist(stock);
        
        
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        producto.setStockProductoList(consulta.getResultList());
       
    }
    
    @WebMethod
    
     public int grabarImagen(String xmlImagen,byte []longitudImagen) {
        int retorno;
        XStream xstream = new XStream(new StaxDriver() );
        xstream.alias("imagen", DatosImagenProducto.class);
        DatosImagenProducto datosImagenProducto = (DatosImagenProducto) xstream.fromXML(xmlImagen);
        Producto producto = em.find(Producto.class, datosImagenProducto.getIdProducto());
        retorno= grabarPathImagenEnBaseDeDatos(producto,imagen.procesarImagen(longitudImagen, datosImagenProducto.getNameImagen(), datosImagenProducto.getMagnitud()));
        return retorno; 
    }
    @WebMethod
    public byte[] obtenerImagenProducto(long idProducto) {        
            Query buscarImagenesDelProducto = em.createQuery("SELECT i FROM ImagenProducto i WHERE i.productoFK.id =:id");
                buscarImagenesDelProducto.setParameter("id", idProducto);
            List<ImagenProducto>lista = buscarImagenesDelProducto.getResultList();
            
            return imagen.obtenerImagenByteArray(lista);   
    }
    private int grabarPathImagenEnBaseDeDatos(Producto producto, String[] procesarImagen) {
        long retorno;        
            ImagenProducto imgProd =new ImagenProducto();            
                    imgProd.setExtension(procesarImagen[1]);            
                    imgProd.setMagnitud(procesarImagen[2]);            
                    imgProd.setNombreImagen(procesarImagen[3]);            
                    imgProd.setPathImagenEnDisco(procesarImagen[0]);            
                    imgProd.setProductoFK(producto);            
                    em.persist(imgProd);  
                Query obtenerImagenPorIdProducto = em.createQuery("SELECT i FROM ImagenProducto i WHERE i.productoFK.id =:id");
                    obtenerImagenPorIdProducto.setParameter("id", producto.getId());
               List<ImagenProducto>lista = obtenerImagenPorIdProducto.getResultList();               
               producto.setImagenProductoList(lista);               
               em.merge(producto);            
            retorno=producto.getId(); 
            return (int) retorno;
    }
    public String actualizarPathImagenesEnBaseDeDatos(){       
            Query listaDeImagenes = em.createQuery("Select i FROM ImagenProducto i");        
                List<ImagenProducto> listado = listaDeImagenes.getResultList();   
                for (ImagenProducto imagenProducto : listado) {
                    imagenProducto.setPathImagenEnDisco(ResourceBundle.getBundle("config").getString("PATH_IMAGE")+imagenProducto.getNombreImagen());
           }
                
            em.flush();
        return "Echo";
    }
@WebMethod
public long modificarFechaVencimientoProducto(long idProducto,String nuevaFechaVencimiento){
    Producto producto = em.find(Producto.class, idProducto);    
    try {
        Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFechaVencimiento);
        
        if(verificarFechasVecimiento(producto,fecha)){
            producto.setFechaVencimiento(fecha);
            producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());    
            //registrarCambioFechaVencimientoEnStock(producto,nuevaFechaVencimiento);
        }        
    } catch (ParseException ex) {
        Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
    }   
    return producto.getId();
}

   
@WebMethod
    public String buscarTodosLosVentaProducto(long idSucursal) {
        StringBuilder xml=new StringBuilder("<Lista>");
        Query consulta = em.createNamedQuery("findVentaProducto",Producto.class).setParameter("id", idSucursal);       
    
       List<Producto>lista = consulta.getResultList();
        List<VentaProducto>venta = null;       
        ventaProductoMethod(xml,lista,venta,0);
        return xml.append("</Lista>").toString();
    }
    @WebMethod
    public String verificarStockDisponibledeProductoConSuPresentacion(long idProducto,long idPack, int cantidad){
        String resultado ="Nada";
        Producto producto =em.find(Producto.class, idProducto);
        PackProducto packproducto = em.find(PackProducto.class, idPack);
        int resto;
        
        if(producto.getCantidadTotalActual()==0){
            resto=0;
        }else{
            resto = producto.getCantidadTotalActual()-(cantidad);
        }
        
        if(resto>=2000){
                    resultado = "ok";

                }else{
                    
                    if(resto<2000 && resto>0&&!"Precio Unitario".equals(packproducto.getDescripcion())){
                        resultado = new StringBuilder(5).append("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" se esta por quedar sin stock, el actual es ").append(producto.getCantidadTotalActual()).toString();

                    }else{
                        if(resto<=5&&resto>0&&"Precio Unitario".equals(packproducto.getDescripcion())){
                            resultado = new StringBuilder().append("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" se esta por quedar sin stock, el actual es ").append(producto.getCantidadTotalActual()).toString();
                        }else{
                                if(resto==0){
                                    resultado= new StringBuilder(5).append("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" para la cantidad ").append(cantidad).append(" posee stock para más No!!!").toString();
                                }else{

                                    if(resto<0){                    
                                        resultado =new StringBuilder(5).append("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" con ").append(packproducto.getDescripcion()).append(" no posee stock para la cantidad ").append(cantidad).append(" elija otro pack o combinación").toString();
                                    }
                                }
                        }
                    }
                }
    
        
        
        
    
        return resultado;
    }

     @WebMethod
    public long setProductoAFraccionado(Long idProducto, int fraccionado) {
        long retorno=0;
        Producto producto = em.find(Producto.class, idProducto);
        producto.setFraccionado(fraccionado);
        
        
        retorno = producto.getId();
        
        em.flush();
        return retorno;
    }
    private void actualizarListaCompra(DatosProducto datosProducto) {
        List<DatosCompraProductoItem>lista = datosProducto.getCompraProducto().getList();
        
        for (DatosCompraProductoItem datosCompraProductoItem : lista) {
            
            CompraProducto compra = em.find(CompraProducto.class, datosCompraProductoItem.getId());
            compra.setPrecio(BigDecimal.valueOf(datosCompraProductoItem.getPrecio()));
            compra.setPresentacion(datosCompraProductoItem.getPresentacion());
            compra.setPorcentaje(Double.valueOf(datosCompraProductoItem.getPorcentaje()));
            
            
        }
        
        
    }

    private void actualizarListaVenta(DatosProducto datosProducto) {
        List<DatosVentaProductoItem>lista = datosProducto.getVentaProducto().getList();
        
        for (DatosVentaProductoItem datosVentaProductoItem : lista) {
            VentaProducto venta = em.find(VentaProducto.class, datosVentaProductoItem.getId());
            venta.setPrecio(BigDecimal.valueOf(datosVentaProductoItem.getPrecio()));
            venta.setPresentacion(datosVentaProductoItem.getPresentacion());
            venta.setPorcentaje(Double.valueOf(datosVentaProductoItem.getPorcentaje()));
           
        }
    }

    private void actualizarListaStock(Producto producto, DatosProducto datosProducto) {
        StockProducto stock = new StockProducto();
                stock.setCantidadActual(producto.getCantidadTotalActual());
                stock.setCantidadAgregada(producto.getCantidadIngresada());
                stock.setCantidadInicial(producto.getCantidadInicial());
                stock.setDetalle("se ingreso mercaderia "+datosProducto.getCantidadIngresada());
                stock.setFechaAgregadoProducto(producto.getFechaIngresoInicial());
                stock.setPorcentajeCompra(0);
                stock.setPorcentajeVenta(0);
                stock.setPrecioUnitarioCompra(BigDecimal.ZERO);
                stock.setPrecioUnitarioVenta(BigDecimal.ZERO);
                stock.setProducto(producto);
        em.persist(stock);
        
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        List<StockProducto>lista =consulta.getResultList();
        
        producto.setStockProductoList(lista);
        
    }
   @WebMethod
   public long aplicarCodigodeBarra(long idProducto,String codigoBarra){
       Producto producto = em.find(Producto.class, idProducto);
       producto.setCodigoBarra(codigoBarra);
       
       registrarEnStock(producto);
               em.flush();
       return producto.getId();
   }

    private void registrarEnStock(Producto producto) {
        StockProducto stock = new StockProducto();
                stock.setDetalle("SE REGISTRO LA ENTRADA DE CODIGO DE BARRA "+producto.getCodigoBarra()+" para el producto "+producto.getDescripcion());
                stock.setCantidadActual(0);
                stock.setCantidadAgregada(0);
                stock.setCantidadInicial(0);
                stock.setFechaAgregadoProducto(producto.getFechaCantidadIngresada());
                stock.setPorcentajeCompra(0);
                stock.setPorcentajeVenta(0);
                stock.setPrecioUnitarioCompra(BigDecimal.ZERO);
                stock.setPrecioUnitarioVenta(BigDecimal.ZERO);
                stock.setProducto(producto);
        em.persist(stock);
        
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        List<StockProducto>lista = consulta.getResultList();
        producto.setStockProductoList(lista);
        
    }


@SuppressWarnings("empty-statement")

    private boolean verificarFechasVecimiento(Producto producto, Date nuevaFechaVencimiento) {
        
        Date fechaHoy = new Date();;
        
        return nuevaFechaVencimiento.after(fechaHoy)&&nuevaFechaVencimiento.after(producto.getFechaVencimiento());
    }
    public void  actualizarStockProducto(VentaSucursal venta,DatosVentaSucursal datosVentaSucursal){
        int stockItem=0;
        List<ItemDetalleVentaSucursalItem>lista = datosVentaSucursal.getDetalleVenta().getList();
    
        
        for (ItemDetalleVentaSucursalItem item : lista) {
            Producto producto = em.find(Producto.class, item.getId());
             
             if(producto.getCantidadTotalActual()-item.getCantidad()>0){
                            if(item.getNombrePack().equalsIgnoreCase("precio unitario")){

                                producto.setCantidadTotalActual(producto.getCantidadTotalActual()-item.getCantidad());

                            }else{
                                    stockItem = item.getCantidad()*item.getPresentacion();
                                    producto.setCantidadTotalActual(producto.getCantidadTotalActual()-stockItem);
                            }
            
                            producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
                            producto.setFechaUltimaVenta(Calendar.getInstance().getTime());
                            StockProducto stock = new StockProducto();

                                   stock.setCantidadActual(producto.getCantidadTotalActual());
                                   stock.setCantidadAgregada(0);
                                   stock.setCantidadInicial(0);
                                   stock.setDetalle("SE DESCONTO STOCK DEL PRODUCTO POR VENTA N "+venta.getId());
                                   stock.setFechaAgregadoProducto(producto.getFechaCantidadIngresada());
                                   stock.setPorcentajeCompra(0);
                                   stock.setPorcentajeVenta(0);
                                   stock.setPrecioUnitarioCompra(BigDecimal.ZERO);
                                   stock.setPrecioUnitarioVenta(BigDecimal.ZERO);
                                   stock.setProducto(producto);
                                   em.persist(stock);

                            Query consulta = em.createNamedQuery("findAllStockForIdProduct");
                            consulta.setParameter("id", producto.getId());

                            producto.setStockProductoList(consulta.getResultList());
             }
        }
    }
    
    @WebMethod(operationName = "seleccionarProductosAVencer")
    public String seleccionarProductosAConFechaVencimientoEnUnaSemana(){
        StringBuilder xml= new StringBuilder("<Lista>\n");
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 7);
        
      
           try {
            
        
 
        

                            Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.fechaVencimiento BETWEEN :f1 and :f2");        

                             consulta.setParameter("f1", Calendar.getInstance(),TemporalType.TIMESTAMP);
                             consulta.setParameter("f2", calendario,TemporalType.TIMESTAMP);

                            List<Producto> lista =consulta.getResultList();
                             if(!lista.isEmpty()){
                                 for (Producto producto : lista) {
                                      xml.append("<fecha1>").append(DateFormat.getDateInstance().format(Calendar.getInstance().getTime())).append("</fecha1>");
                                      xml.append("<fecha2>").append(DateFormat.getDateInstance().format(calendario.getTime())).append("</fecha2>");
                                      xml.append(producto.toXML());
                                  }
                                 }

                             xml.append("</Lista>");
        } catch (Exception e) {
            Logger.getLogger("Error en metodo seleccionarProductosAConFechaVencimientoEnUnaSemana "+e.getMessage());
        }finally{
            return xml.toString();
        }
    }
    @WebMethod
    public String leerArchivoParaAlmacenarProductosEnLote() throws IOException {
        String resultado = "NADA";
    try {
        
        String fileName="productos.txt";
        FileInputStream fis=null;
        InputStreamReader isr = null;
        
        
        
        
        File file = new File(PATH+fileName);

fis = new FileInputStream(file);
        try {
            isr = new InputStreamReader(fis,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
                        BufferedReader in = new BufferedReader(isr);
                        String sCurrentLine;
                        while((sCurrentLine=in.readLine())!=null){
                            String[] details = sCurrentLine.split("\t");
                            String provE = details[0].trim();
                            String codigo = details[1].trim();
                            String descripcion = details[2].trim();
                            
                                  procesarParaAgregar(provE,codigo,descripcion);

                        }
 resultado = "HECHO";
    } catch (FileNotFoundException ex) {
        Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
    }
    return resultado;
    }

    private void procesarParaAgregar(String proveedor, String codigo, String descripcion) {
        
         Proveedor proveedorEntity = null;
       
        
        int code=this.proveedor.crearProveedor(proveedor, "");
        
        if(code>0){
            codigoProveedor=code;
            
           
        }
        
        proveedorEntity = em.find(Proveedor.class, codigoProveedor);
        
        
        
                        Producto producto = new Producto();

                        producto.setCantidadIngresada(0);
                        
                        producto.setCantidadInicial(0);

                        producto.setCantidadTotalActual(0);

                        producto.setCodigoBarra(codigo);
                        
                        producto.setDetalleProducto("");
                        
                        producto.setDescripcion(descripcion);

                        producto.setFechaCantidadIngresada(Calendar.getInstance().getTime());

                        producto.setFechaIngresoInicial(Calendar.getInstance().getTime());

                        producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());

                        producto.setFechaUltimaCompra(Calendar.getInstance().getTime());
                        
                        producto.setFechaUltimaVenta(Calendar.getInstance().getTime());

                        producto.setFechaUltimoIngreso(Calendar.getInstance().getTime());

                        producto.setFechaVencimiento(Calendar.getInstance().getTime());

                        producto.setPorcentajeCompra(0);

                        producto.setPorcentajeVenta(0);

                        producto.setDetalleCompra("");
                        producto.setDetalleVenta("");
                        producto.setSucursalFK(em.find(Sucursal.class, (long)1));

                        producto.setPrecioUnitarioCompra(BigDecimal.ZERO);

                        producto.setPrecioUnitarioVenta(BigDecimal.ZERO);

                        producto.setProveedorFK(proveedorEntity);

                        producto.setPersonaFK(em.find(Persona.class, (long)1));

                        em.persist(producto);
                        


                        Query consulta10 = em.createQuery("SELECT p FROM Producto p WHERE p.proveedorFK.id =:idP");

                        consulta10.setParameter("idP", proveedorEntity.getId());

                        proveedorEntity.setProducto(consulta10.getResultList());


                         Sucursal sucursal = em.find(Sucursal.class, (long)1);
                        Query consulta11 = em.createQuery("SELECT p FROM Producto p WHERE p.sucursalFK.id =:idS");
                        consulta11.setParameter("idS", sucursal.getId());
                        sucursal.setProductoList(consulta11.getResultList());

                        em.merge(sucursal);


                        persistirListaStockProducto(producto);

                        agregarColaCompraProductoyColaVentaProducto(producto);

                           
                        Query consulta3 = em.createQuery("SELECT c FROM CompraProducto c where c.productoFK.id =:idC");

                        consulta3.setParameter("idC", producto.getId());

                        Query consulta2 = em.createQuery("SELECT v FROM VentaProducto v WHERE v.productoFK.id =:idV");

                        consulta2.setParameter("idV", producto.getId());


                        producto.setVenta(consulta2.getResultList());
                        producto.setCompra(consulta3.getResultList());
                        em.merge(producto);
                        em.flush();
        
    }
    @WebMethod
    public int borrarProductoIDPRoveedor(long idProveedor){
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.proveedorFK.id =:id");
        Query compra = em.createQuery("SELECT c FROM CompraProducto c WHERE c.productoFK.id =:id");
        Query venta = em.createQuery("SELECT v FROM VentaProducto v WHERE v.productoFK.id =:id");
        
        
        
        consulta.setParameter("id", idProveedor);
        List<Producto>lista= consulta.getResultList();
        
        for (Producto producto : lista) {
            Query consulta1 = em.createQuery("select s from StockProducto s where s.productoFK.id =:id");
            consulta1.setParameter("id", producto.getId());
            compra.setParameter("id", producto.getId());
            venta.setParameter("id", producto.getId());
            List<StockProducto>lista1 = consulta1.getResultList();
            List<CompraProducto>listaCompra=compra.getResultList();
            List<VentaProducto>listaVenta=venta.getResultList();
            for (StockProducto stockProducto : lista1) {
                em.remove(stockProducto);
                
            }
            for(CompraProducto com:listaCompra){
                em.remove(com);
             
            }
            for(VentaProducto ven:listaVenta){
                em.remove(ven);
             
            }
            em.remove(producto);
            
        }
        
        em.flush();
        
        return 1;
    
    }

    private void agregarColaCompraProductoyColaVentaProducto(Producto producto) {
        CompraProducto compra = null;
        VentaProducto venta;
        for (int i = 1; i < 5; i++) {
            
            compra = new CompraProducto();
            compra.setPackFK(em.find(PackProducto.class, (long) i));
            compra.setPrecio(BigDecimal.ZERO);
            compra.setPresentacion(0);
            
            compra.setProducto(producto);
            em.persist(compra);
            
            venta = new VentaProducto();
            venta.setPackFK(em.find(PackProducto.class, (long) i));
            venta.setPrecio(BigDecimal.ZERO);
            venta.setPresentacion(0);
            venta.setProductoFK(producto);
            em.persist(venta);
            
        }
       
                
    }
    @WebMethod
    public int agregarColasAProductos(){
        long idProveedor=0;
        Query consulta = em.createQuery("SELECT p FROM Proveedor p WHERE LOWER(p.nombre)  like :nombre");
        consulta.setParameter("nombre".toLowerCase(), "sancor");
        List<Proveedor>lista = consulta.getResultList();
        for (Proveedor proveedor1 : lista) {
            idProveedor=proveedor1.getId();
        }
        
        Query consulta1=em.createQuery("SELECT p FROM Producto p WHERE p.proveedorFK.id =:id");
        consulta1.setParameter("id", idProveedor);
        List<Producto>lista1 = consulta1.getResultList();
        for (Producto producto : lista1) {
            agregarColaCompraProductoyColaVentaProducto(producto);    
        persistirListaStockProducto(producto);     
        }        
        em.flush();  
        return 1;
    }
@WebMethod
public String buscarProductoPorCodigoDeBarra(String codigoBarra,int cantidad){
    StringBuilder xml=new StringBuilder("<Lista>");
    Query consulta = em.createNamedQuery("findProductoByCodigoBarraConVentas");
    consulta.setParameter("codigo", codigoBarra.trim());
    List<Producto>lista = consulta.getResultList();
        List<VentaProducto>venta = null;
        
        ventaProductoMethod(xml,lista,venta,cantidad);
        return xml.append("</Lista>").toString();
}
private void ventaProductoMethod(StringBuilder xml, List<Producto> lista, List<VentaProducto> venta,int cantidad) {    
         
       for (Producto producto : lista) {                   
         
          if(producto.getPrecioUnitarioVenta().doubleValue()>0){
                venta=producto.getVenta();
           
           
                        if(!venta.isEmpty()){
                            StringBuilder  xmlVentaProducto = new StringBuilder(5); 
                             for (VentaProducto ventaProducto : venta) {               

                                         if(ventaProducto.getPrecio().doubleValue()>0){

                                             xmlVentaProducto.append("<itemVenta>")
                                                          .append("<idVentaProducto>").append(ventaProducto.getId()).append("</idVentaProducto>")
                                                          .append("<precio>").append(ventaProducto.getPrecio().doubleValue()).append("</precio>")
                                                          .append("<nombrePack>").append(ventaProducto.getPackFK().getDescripcion()).append("</nombrePack>")
                                                          .append("<idPack>").append(ventaProducto.getPackFK().getId()).append("</idPack>")
                                                          .append("<presentacion>").append(ventaProducto.getPresentacion()).append("</presentacion>")                                 
                                                          .append("</itemVenta>\n");


                                         }                                    

                             }

                                          xml.append("<item>").append("<id>").append(producto.getId()).append("</id>")
                                          .append("<descripcion>").append(producto.getDescripcion()).append("</descripcion>")
                                          .append("<codigo>").append(producto.getCodigoBarra()).append("</codigo>")
                                          .append("<resto>").append(producto.getCantidadTotalActual()-cantidad).append("</resto>")
                                          .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>")
                                          .append(xmlVentaProducto);
                                     xml.append("</item>");


                }
          }
       }
        
    }

    private String xmlProducto(List<Producto> lista) {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        
        for (Producto producto : lista) {    
                xml.append("<item>\n");
                xml.append("<id>").append(producto.getId()).append("</id>\n")
                .append("<descripcion>").append(producto.getDescripcion()).append("</descripcion>\n")
                .append("<precioUnitarioCompra>").append(producto.getPrecioUnitarioCompra()).append("</precioUnitarioCompra>\n")
                .append("<precioUnitarioVenta>").append(producto.getPrecioUnitarioVenta()).append("</precioUnitarioVenta>\n")
                                .append("<codigoBarra>").append("<![CDATA[").append(producto.getCodigoBarra()).append("]]>").append("</codigoBarra>\n")
                                .append("<primerCantidadInicial>").append(producto.getCantidadInicial()).append("</primerCantidadInicial>\n")
                                .append("<cantidadTotalActual>").append(producto.getCantidadTotalActual()).append("</cantidadTotalActual>\n")
                                .append("<fraccionado>").append(producto.getFraccionado()).append("</fraccionado>\n")
                                .append("<cantidadIngresada>").append(producto.getCantidadIngresada()).append("</cantidadIngresada>\n")
                                .append("<fechaIngresoInicial>").append(producto.getFechaIngresoInicial()!=null?DateFormat.getDateInstance().format(producto.getFechaIngresoInicial()):0).append("</fechaIngresoInicial>\n")
                                .append("<fechaCantidadIngresada>").append(producto.getFechaCantidadIngresada()!=null?DateFormat.getDateInstance().format(producto.getFechaCantidadIngresada()):0).append("</fechaCantidadIngresada>\n")
                                .append("<fechaUltimaActualizacion>").append(producto.getFechaUltimaActualizacion()!=null?DateFormat.getDateInstance().format(producto.getFechaUltimaActualizacion()):0).append("</fechaUltimaActualizacion>\n")
                                .append("<fechaUltimaVenta>").append(producto.getFechaUltimaVenta()!=null?new SimpleDateFormat("dd/MM/yyyy").format(producto.getFechaUltimaVenta()):0).append("</fechaUltimaVenta>\n")
                                .append("<fechaUltimaIngreso>").append(producto.getFechaUltimoIngreso()!=null?new SimpleDateFormat("dd/MM/yyyy").format(producto.getFechaUltimoIngreso()):0).append("</fechaUltimaIngreso>\n")
                                .append("<fechaVencimiento>").append(producto.getFechaVencimiento()!=null?DateFormat.getDateInstance().format(producto.getFechaVencimiento()):0 ).append("</fechaVencimiento>\n")
                                
                                .append("<detalle>").append(producto.getDetalleProducto()!=null?producto.getDetalleProducto():"").append("</detalle>\n")
                                .append("<sucursal>")
                                    .append("<id>").append(producto.getSucursalFK().getId()).append("</id>\n")
                                    .append("<nombre>").append(producto.getSucursalFK().getNombre()).append("</nombre>")
                                .append("</sucursal>")
                                .append("<proveedor>")
                                        .append("<id>").append(producto.getProveedorFK().getId()).append("</id>\n")
                                        .append("<nombre>").append("<![CDATA[").append(producto.getProveedorFK().getNombre()).append("]]>").append("</nombre>")
                                .append("</proveedor>")
                                .append("<personaId>").append(producto.getPersonaFK().getId()).append("</personaId>\n")

                                .append("<porcentajeCompra>").append(producto.getPorcentajeCompra()).append("</porcentajeCompra>")
                                .append("<porcentajeVenta>").append(producto.getPorcentajeVenta()).append("</porcentajeVenta>");
                                            if(!producto.getCompra().isEmpty()){
                                                xml.append("<detalleCompra>\n");
                                                StringBuilder detailCompra = new StringBuilder(5);
                                                detailCompra.append("<detalle>").append(producto.getDetalleCompra()!=null?producto.getDetalleCompra():"").append("</detalle>")
                                                        .append("<porcentaje>").append(producto.getPorcentajeCompra()).append("</porcentaje>")
                                                        .append("<fecha>").append(producto.getFechaUltimaCompra()!=null?new SimpleDateFormat("dd/MM/yyyy").format(producto.getFechaUltimaCompra()):"").append("</fecha>");
                                                for (CompraProducto compraProducto : producto.getCompra()) {
                                                    detailCompra.append(compraProducto.toXML());
                                                }
                                                xml.append(detailCompra.append("</detalleCompra>\n"));
                                            }
                                            if(!producto.getVenta().isEmpty()){
                                                xml.append("<listDetalleVenta>\n");
                                                StringBuilder detailVenta = new StringBuilder(5);
                                                detailVenta.append("<detalle>").append(producto.getDetalleVenta()!=null?producto.getDetalleVenta():"").append("</detalle>")
                                                        .append("<porcentaje>").append(producto.getPorcentajeVenta()).append("</porcentaje>")
                                                        .append("<fecha>").append(producto.getFechaUltimaVenta()!=null?DateFormat.getDateInstance().format(producto.getFechaUltimaVenta()):0).append("</fecha>");
                                                for(VentaProducto ventaProducto: producto.getVenta()){                    
                                                    detailVenta.append(ventaProducto.toXML());
                                                }
                                                xml.append(detailVenta.append("</listDetalleVenta>\n"));                
                                            }
                                            
                                              if(!producto.getImagenProductoList().isEmpty()){
                                                    xml.append("<listImagenes>\n");
                                                 StringBuilder detalleImagenes = new StringBuilder(5);

                                                 for(ImagenProducto image: producto.getImagenProductoList()){

                                                     detalleImagenes.append(image.toXML());
                                                 }
                                                 xml.append(detalleImagenes.append("</listImagenes>\n"));

                                             }
                                              xml.append("</item>");
                
        }
        return xml.append("</Lista>").toString();
    }
      @WebMethod
      public String buscarProductoPorNombre(String nombre){
          
          Query consulta = em.createNamedQuery("findProductoByDescripcionAprox",Producto.class);
          consulta.setParameter("descripcion","%"+nombre.toLowerCase().concat("%"));
          consulta.setParameter("descripcion1", "%"+nombre.toLowerCase().concat("%"));
          
          List<Producto>lista = consulta.getResultList();
          return lista.size()>0?xmlProducto(lista):"";
      }
    @WebMethod
    public long borrarProductoPorIdProducto(long idProducto) {
        long retorno=0;
        Producto producto = em.find(Producto.class, idProducto);
        em.remove(producto);
        retorno=1;
        return retorno;
    }

    private BigDecimal calculoPorcentaje(BigDecimal precioProducto, BigDecimal percent) {
                    BigDecimal precio = precioProducto;            
                    BigDecimal multiplication = precio.multiply(percent);
                    
        return multiplication.divide(new BigDecimal(100), 0);
    }

    private Query consultarProductosDeSucursal(long idSucursal) {
        return em.createNamedQuery("Producto.findAllBySucursal").setParameter("id", idSucursal);
         
    }
    @WebMethod
    public String selectListaProductoPorProveedor(long idProveedor,long idSucursal){
        StringBuilder xml = new StringBuilder("<Lista>\n");   
        
        Query consulta = em.createQuery("SELECT DISTINCT p1 FROM Proveedor p1 inner join p1.producto p2 ON "
                + "p1.id=:id WHERE p2.sucursalFK.id =:id1 AND p2.precioUnitarioVenta>0 or (p2.precioUnitarioVenta>0 AND p2.precioUnitarioVenta<1)");
        consulta.setParameter("id", idProveedor);
        consulta.setParameter("id1", idSucursal);
         List<Proveedor>lista = consulta.getResultList();
        xml.append("<proveedor>");
        if(!lista.isEmpty()){
                    List<VentaProducto>venta = null;
                   for (Proveedor prov: lista) {
                       xml.append("<nombreProveedor><![CDATA[").append(prov.getNombre()).append("]]></nombreProveedor>");
                        List<Producto>producto = prov.getProducto();

                        ventaProductoMethod(xml, producto, venta, 0);


                   }
        }else{
            xml.append("<no>").append("no producto").append("</no>");
        }
        xml.append("</proveedor>");
        
        return xml.append("</Lista>").toString();
    }
}
