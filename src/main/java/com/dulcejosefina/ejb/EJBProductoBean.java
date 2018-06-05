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
import com.dulcejosefina.utils.DatosImagenProducto;
import com.dulcejosefina.utils.DatosProducto;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Stateless
@LocalBean
@WebService
public class EJBProductoBean {
@PersistenceContext
private EntityManager em;
private final Imagen imagen = new Imagen();
private static final String PATH = ResourceBundle.getBundle("config").getString("PATH_FILE");        
@Inject
private EJBProducto productoSupport;
 @WebMethod(operationName = "selectProductoByCodigoBarra")
 public String selectProductoByCodigoBarra(String codigoBarra){      
      Query consulta = em.createNamedQuery("findProductoByCodigoBarraOnly");
      consulta.setParameter("codigo", codigoBarra.trim());
      List<Producto>lista= consulta.getResultList();      
     return productoSupport.xmlProducto(lista);
 }
    @WebMethod(operationName = "selectAllProducto")
    public String selectAllProducto(long idSucursal) {        
        Query consulta = consultarProductosDeSucursal(idSucursal);
        List<Producto>lista = consulta.getResultList();
            return productoSupport.xmlProducto(lista);
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
      return productoSupport.xmlProducto(lista);
     }
@WebMethod
    public long crearProducto(String xmlProducto) {
        long retorno=0;
        
        DatosProducto getDatosProducto = new DatosProducto();
                getDatosProducto= getDatosProducto.transformaAObjetos(xmlProducto);
            if(getDatosProducto.getId()>0){
                retorno = productoSupport.actualizarProducto(getDatosProducto);                
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
                            retorno = productoSupport.calcularPorcentajeCompra(producto,porcentaje); 
                            producto.setPorcentajeCompra(Double.valueOf(porcentaje));
                            producto.setDetalleCompra(producto.getDetalleCompra().concat(" porcentaje ").concat(porcentaje));
                    }
                break;
                    case "VENTA":{
                        retorno = productoSupport.calcularPorcentajeVenta(producto,porcentaje);
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
                retorno = productoSupport.calcularPorcentajeCompra(producto, porcentaje);
                producto.setDetalleCompra("Fecha: "+new Date()+" Ingreso porcentaje "+porcentaje+" Proveedor "+em.find(Proveedor.class, idProveedor).getNombre());
                producto.setPorcentajeCompra(Double.valueOf(porcentaje));                
                retorno = productoSupport.calcularPorcentajeVentaProductosProveedor(producto);
                producto.setDetalleVenta("Fecha: "+new Date()+" Ingreso porcentaje"+porcentaje+" Proveedor "+em.find(Proveedor.class, idProveedor).getNombre());
                producto.setPorcentajeVenta(Double.valueOf(porcentaje));
            }            
        }   
        return retorno;
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
                        productoSupport.persistirListaCompraProducto(producto,datosProducto);
                        productoSupport.persistirListaVentaProducto(producto,datosProducto);
                        productoSupport.persistirListaProductosProveedor(datosProducto);
                        productoSupport.persistirProductosDeSucursal(datosProducto);
                        productoSupport.persistirListaStockProducto(producto);        
            } catch (Exception e) {
            } return producto.getId();
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
    @WebMethod
    public byte[] obtenerImagenProducto(long idProducto) {        
            Query buscarImagenesDelProducto = em.createQuery("SELECT i FROM ImagenProducto i WHERE i.productoFK.id =:id");
                buscarImagenesDelProducto.setParameter("id", idProducto);
            List<ImagenProducto>lista = buscarImagenesDelProducto.getResultList();            
            return imagen.obtenerImagenByteArray(lista);   
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
        if(productoSupport.verificarFechasVecimiento(producto,fecha)){
            producto.setFechaVencimiento(fecha);
            producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());              
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
        productoSupport.obtenerProductosConPrecioDeVenta(xml,lista,venta,0);
        return xml.append("</Lista>").toString();
    }
    @WebMethod
    public String verificarStockDisponibleDeProductoConSuPresentacion(long idProducto,long idPack, int cantidad,long idSucursal){
        String resultado ="Nada";
        Query consultaProducto = em.createNamedQuery("findProductoByIdSucu",Producto.class);
        consultaProducto.setParameter("id", idProducto);
        consultaProducto.setParameter("idSucu", idSucursal);
        List<Producto>lista = consultaProducto.getResultList();        
        PackProducto packproducto = em.find(PackProducto.class, idPack);
        int resto =0;               
        for (Producto producto : lista) {                        
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
                                    resultado= new StringBuilder(5).append("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" para la cantidad ").append(cantidad).append(" no posee stock!!!").toString();
                                }else{
                                    if(resto<0){                    
                                        resultado =new StringBuilder(5).append("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" con ").append(packproducto.getDescripcion()).append(" no posee stock para la cantidad ").append(cantidad).append(" elija otro pack o combinación").toString();
                                    }
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

                                          productoSupport.procesarParaAgregar(provE,codigo,descripcion);
                                }
                resultado = "HECHO";
            } catch (FileNotFoundException ex) {
                Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return resultado;
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
                    productoSupport.agregarColaCompraProductoyColaVentaProducto(producto);    
                productoSupport.persistirListaStockProducto(producto);     
                }        
            em.flush();  
        return 1;
    }
@WebMethod
public String buscarProductoPorCodigoDeBarra(String codigoBarra,int cantidad,long idSucursal){
    StringBuilder xml=new StringBuilder("<Lista>");
    Query consulta = em.createNamedQuery("findProductoByCodigoBarraConVentas");
    consulta.setParameter("codigo", codigoBarra.trim());
    consulta.setParameter("idSucursal", idSucursal);
    List<Producto>lista = consulta.getResultList();
        List<VentaProducto>venta = null;
        
        productoSupport.obtenerProductosConPrecioDeVenta(xml,lista,venta,cantidad);
        return xml.append("</Lista>").toString();
}

  @WebMethod
   public String buscarProductoPorNombre(String nombre,long idSucursal){
          Query consulta = em.createNamedQuery("findProductoByDescripcionAprox",Producto.class);
          consulta.setParameter("descripcion","%"+nombre.toLowerCase().concat("?"));
          consulta.setParameter("descripcion1", "%"+nombre.toLowerCase().concat("%"));
          consulta.setParameter("idSucursal", idSucursal);
          List<Producto>lista = consulta.getResultList();
          return lista.size()>0?productoSupport.xmlProducto(lista):"";
      }
    @WebMethod
    public long borrarProductoPorIdProducto(long idProducto) {
        long retorno=0;
        Producto producto = em.find(Producto.class, idProducto);
        em.remove(producto);
        retorno=1;
        return retorno;
    }   
    @WebMethod
    public String selectListaProductoPorProveedor(long idProveedor,long idSucursal){
        StringBuilder xml = new StringBuilder("<Lista>\n");   
        Query consulta = em.createQuery("SELECT DISTINCT p1 FROM Proveedor p1 inner join p1.producto p2 ON "
                + "p1.id=:id WHERE p2.sucursalFK.id =:id1 AND p2.precioUnitarioCompra >0 or (p2.precioUnitarioCompra>0 AND p2.precioUnitarioCompra<1)");
        consulta.setParameter("id", idProveedor);
        consulta.setParameter("id1", idSucursal);
         List<Proveedor>lista = consulta.getResultList();
        xml.append("<proveedor>");
        if(!lista.isEmpty()){
                    List<CompraProducto>compra = null;
                   for (Proveedor prov: lista) {
                       xml.append("<nombreProveedor><![CDATA[").append(prov.getNombre()).append("]]></nombreProveedor>");
                        List<Producto>producto = prov.getProducto();
                        productoSupport.getCompraProducto(xml, producto, compra, 0);                      
               }
        }else{
            xml.append("<no>").append("no producto").append("</no>");
        }
        xml.append("</proveedor>");        
        return xml.append("</Lista>").toString();
    }
    private Query consultarProductosDeSucursal(long idSucursal) {
        return em.createNamedQuery("Producto.findAllBySucursal").setParameter("id", idSucursal);
    }
}