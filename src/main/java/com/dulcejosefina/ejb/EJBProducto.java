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
import com.dulcejosefina.utils.DatosCompraProductoItem;
import com.dulcejosefina.utils.DatosProducto;
import com.dulcejosefina.utils.DatosVentaProductoItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@Stateless
@WebService(name = "wsProducto", serviceName = "ServiceProducto")
public class EJBProducto {

    @PersistenceContext
    private EntityManager em;
    private long codigoProveedor = 0;
    @Inject
    private EJBProveedorBean proveedor;

    @WebMethod(operationName = "seleccionarProductosAVencer")
    public String seleccionarProductosAConFechaVencimientoEnUnaSemana() {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 7);
        try {
            Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.fechaVencimiento BETWEEN :f1 and :f2 AND Cast(p.precioUnitarioVenta as Integer)>0");
            consulta.setParameter("f1", Calendar.getInstance(), TemporalType.TIMESTAMP);
            consulta.setParameter("f2", calendario, TemporalType.TIMESTAMP);
            List<Producto> listaVenta = consulta.getResultList();
            if (!listaVenta.isEmpty()) {
                for (Producto producto : listaVenta) {
                    xml.append("<fecha1>").append(DateFormat.getDateInstance().format(Calendar.getInstance().getTime())).append("</fecha1>");
                    xml.append("<fecha2>").append(DateFormat.getDateInstance().format(calendario.getTime())).append("</fecha2>");
                    xml.append(producto.toXML());
                }
            }

        } catch (Exception e) {
            Logger.getLogger("Error en metodo seleccionarProductosAConFechaVencimientoEnUnaSemana " + e.getMessage());
        } finally {
            return xml.append("</Lista>").toString();
        }
    }

    @WebMethod(operationName = "seleccionarProductosSinStock")
    public String seleccionarProductosConStockMinimo() {
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createQuery("SELECT p FROM Producto p LEFT JOIN FETCH p.venta WHERE p.cantidadTotalActual <5 AND p.cantidadTotalActual >=0 AND Cast(p.precioUnitarioVenta as Integer) > 0 order by p.cantidadTotalActual desc");
        List<Producto> listaVenta = consulta.getResultList();
        for (Producto producto : listaVenta) {
            if (producto.getVenta() != null) {
                xml.append("<item>").append("<codigo>").append(producto.getCodigoBarra()).append("</codigo>")
                        .append("<descripcion>").append(producto.getDescripcion()).append("</descripcion>")
                        .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>").append("</item>");
            }
        }
        return xml.append("</Lista>").toString();
    }

    @WebMethod
    public String obtenerProductosFraccionadosSinStockPorProveedor(long idProveedor) {
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.proveedorFK.id =:idProveedor AND p.fraccionado = 1 AND Cast(p.precioUnitarioVenta as Integer) > 0 AND p.cantidadTotalActual < 10 AND p.cantidadTotalActual >=0");
        consulta.setParameter("idProveedor", idProveedor);
        List<Producto> listaVenta = consulta.getResultList();
        xml.append("<proveedor>").append("<![CDATA[").append(em.find(Proveedor.class, idProveedor).getNombre()).append("]]>").append("</proveedor>");
        for (Producto producto : listaVenta) {
            xml.append("<item>");
            xml.append("<producto>").append(producto.getDescripcion()).append("</producto>").append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>");
            xml.append("</item>");
        }
        return xml.append("</Lista>").toString();
    }

    @WebMethod
    public String obtenerProductosSinStockPorProveedor(long idProveedor) {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.fraccionado=0 AND p.proveedorFK.id =:idProveedor AND Cast(p.precioUnitarioVenta as Integer) > 0 AND p.cantidadTotalActual < 10 AND p.cantidadTotalActual >=0");
        consulta.setParameter("idProveedor", idProveedor);
        List<Producto> listaVenta = consulta.getResultList();
        xml.append("<proveedor>").append("<![CDATA[").append(em.find(Proveedor.class, idProveedor).getNombre()).append("]]>").append("</proveedor>\n");
        for (Producto producto : listaVenta) {
            xml.append("<item>\n");
            xml.append("<producto>").append(producto.getDescripcion()).append("</producto>\n")
                    .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>\n")
                    .append("</item>\n");
        }
        return xml.append("</Lista>").toString();
    }

    //asign purchase price sale price
    public long calcularPorcentajeVentaYSetearValoresPrecioVenta(Producto producto) {
        List<VentaProducto> listaVenta = producto.getVenta();
        List<CompraProducto> listaCompra = producto.getCompra();
        BigDecimal resultado;

        BigDecimal porcentajeVenta = null;
        for (VentaProducto ventaProducto : listaVenta) {
            for (CompraProducto compraProducto : listaCompra) {
                if (compraProducto.getPrecio().shortValue() > 0 && Objects.equals(compraProducto.getProducto().getId(), ventaProducto.getProductoFK().getId())
                        && Objects.equals(compraProducto.getPackFK().getId(), ventaProducto.getPackFK().getId())) {

                    resultado = calculoPorcentaje(compraProducto.getPrecio(), BigDecimal.valueOf(compraProducto.getPorcentaje()));
                    if (compraProducto.getPorcentaje() > 0) {
                        ventaProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue() + resultado.doubleValue()));
                    } else {
                        ventaProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue() - resultado.doubleValue()));
                    }

                    producto.setPorcentajeVenta(ventaProducto.getPorcentaje());

                    if (ventaProducto.getPackFK().getId() == 1) {
                        producto.setPrecioUnitarioVenta(ventaProducto.getPrecio());
                    }

                    ventaProducto.setProductoFK(producto);
                }
            }
        }
        em.flush();
        registrarOperacionVentaEnStock(producto, String.valueOf(porcentajeVenta));
        em.flush();
        return producto.getId();
    }

    private BigDecimal calculoPorcentaje(BigDecimal precioProducto, BigDecimal percent) {
        return precioProducto.multiply(percent.doubleValue() > 0 ? percent : percent.negate()).divide(new BigDecimal(100), 0);
    }

    private void registrarOperacionVentaEnStock(Producto producto, String porcentaje) {
        StockProducto stock = new StockProducto();
        stock.setDetalle("APLICADO EL PORCENTAJE VENTA" + porcentaje + " fecha " + Calendar.getInstance().getTime());
        stock.setProducto(producto);
        em.persist(stock);
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        producto.setStockProductoList(consulta.getResultList());
        em.merge(producto);
        em.flush();
    }

    private void registrarOperacionCompraEnStock(Producto producto, String porcentaje) {
        StockProducto stock = new StockProducto();
        stock.setDetalle("APLICADO EL PORCENTAJE COMPRA " + porcentaje + " fecha " + Calendar.getInstance().getTime());
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

    public long calcularPorcentajeVenta(Producto producto, String porcentaje) {
        List<VentaProducto> listaVenta = producto.getVenta();
        BigDecimal resultado = null;
        BigDecimal percent = new BigDecimal(porcentaje);
        BigDecimal precioUnitarioVenta = null;
        BigDecimal porcentajeVenta = null;
        for (VentaProducto ventaProducto : listaVenta) {
            switch (percent.signum()) {
                case 1: {
                    if (ventaProducto.getPrecio().doubleValue() > 0) {
                        resultado = calculoPorcentaje(ventaProducto.getPrecio(), percent);
                        ventaProducto.setPrecio(BigDecimal.valueOf(ventaProducto.getPrecio().doubleValue() + resultado.doubleValue()));
                    }
                }
                break;
                case -1: {
                    resultado = calculoPorcentaje(ventaProducto.getPrecio(), percent);
                    ventaProducto.setPrecio(BigDecimal.valueOf(ventaProducto.getPrecio().doubleValue() - resultado.doubleValue()));
                }
            }
            ventaProducto.setProductoFK(producto);
            em.flush();
        }
        Query queryProductPurchaseList = em.createQuery("SELECT v FROM VentaProducto v WHERE v.packFK.id =:pack AND v.productoFK.id =:idProducto");
        queryProductPurchaseList.setParameter("pack", (long) 1);
        queryProductPurchaseList.setParameter("idProducto", producto.getId());
        List<VentaProducto> listaVentaVentaPrecioUnitario = queryProductPurchaseList.getResultList();
        for (VentaProducto ventaProducto : listaVentaVentaPrecioUnitario) {
            precioUnitarioVenta = ventaProducto.getPrecio();
            porcentajeVenta = BigDecimal.valueOf(ventaProducto.getPorcentaje());
        }
        resultado = precioUnitarioVenta.multiply((porcentajeVenta.doubleValue() > 0 ? porcentajeVenta : porcentajeVenta.negate())).divide(new BigDecimal(100));
        if (porcentajeVenta.doubleValue() > 0) {
            producto.setPrecioUnitarioVenta(BigDecimal.valueOf(precioUnitarioVenta.doubleValue() + resultado.doubleValue()));
        } else {
            producto.setPrecioUnitarioVenta(BigDecimal.valueOf(precioUnitarioVenta.doubleValue() - resultado.doubleValue()));
        }
        registrarOperacionVentaEnStock(producto, String.valueOf(porcentajeVenta));
        em.flush();
        return producto.getId();
    }

    public String xmlProducto(List<Producto> listaVenta) {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        for (Producto producto : listaVenta) {
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
                    .append("<fechaIngresoInicial>").append(producto.getFechaIngresoInicial() != null ? DateFormat.getDateInstance().format(producto.getFechaIngresoInicial()) : 0).append("</fechaIngresoInicial>\n")
                    .append("<fechaCantidadIngresada>").append(producto.getFechaCantidadIngresada() != null ? DateFormat.getDateInstance().format(producto.getFechaCantidadIngresada()) : 0).append("</fechaCantidadIngresada>\n")
                    .append("<fechaUltimaActualizacion>").append(producto.getFechaUltimaActualizacion() != null ? DateFormat.getDateInstance().format(producto.getFechaUltimaActualizacion()) : 0).append("</fechaUltimaActualizacion>\n")
                    .append("<fechaUltimaVenta>").append(producto.getFechaUltimaVenta() != null ? new SimpleDateFormat("dd/MM/yyyy").format(producto.getFechaUltimaVenta()) : 0).append("</fechaUltimaVenta>\n")
                    .append("<fechaUltimaIngreso>").append(producto.getFechaUltimoIngreso() != null ? new SimpleDateFormat("dd/MM/yyyy").format(producto.getFechaUltimoIngreso()) : 0).append("</fechaUltimaIngreso>\n")
                    .append("<fechaVencimiento>").append(producto.getFechaVencimiento() != null ? DateFormat.getDateInstance().format(producto.getFechaVencimiento()) : 0).append("</fechaVencimiento>\n")
                    .append("<detalle>").append(producto.getDetalleProducto() != null ? producto.getDetalleProducto() : "").append("</detalle>\n")
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
            if (!producto.getCompra().isEmpty()) {
                xml.append("<detalleCompra>\n");
                StringBuilder detailCompra = new StringBuilder(5);
                detailCompra.append("<detalle>").append(producto.getDetalleCompra() != null ? producto.getDetalleCompra() : "").append("</detalle>")
                        .append("<porcentaje>").append(producto.getPorcentajeCompra()).append("</porcentaje>")
                        .append("<fecha>").append(producto.getFechaUltimaCompra() != null ? new SimpleDateFormat("dd/MM/yyyy").format(producto.getFechaUltimaCompra()) : "").append("</fecha>");
                for (CompraProducto compraProducto : producto.getCompra()) {
                    detailCompra.append(compraProducto.toXML());
                }
                xml.append(detailCompra.append("</detalleCompra>\n"));
            }
            if (!producto.getVenta().isEmpty()) {
                xml.append("<listDetalleVenta>\n");
                StringBuilder detailVenta = new StringBuilder(5);
                detailVenta.append("<detalle>").append(producto.getDetalleVenta() != null ? producto.getDetalleVenta() : "").append("</detalle>")
                        .append("<porcentaje>").append(producto.getPorcentajeVenta()).append("</porcentaje>")
                        .append("<fecha>").append(producto.getFechaUltimaVenta() != null ? DateFormat.getDateInstance().format(producto.getFechaUltimaVenta()) : 0).append("</fecha>");
                for (VentaProducto ventaProducto : producto.getVenta()) {
                    detailVenta.append(ventaProducto.toXML());
                }
                xml.append(detailVenta.append("</listDetalleVenta>\n"));
            }
            if (!producto.getImagenProductoList().isEmpty()) {
                xml.append("<listImagenes>\n");
                StringBuilder detalleImagenes = new StringBuilder(5);
                for (ImagenProducto image : producto.getImagenProductoList()) {
                    detalleImagenes.append(image.toXML());
                }
                xml.append(detalleImagenes.append("</listImagenes>\n"));
            }
            xml.append("</item>");
        }
        return xml.append("</Lista>").toString();
    }

    public void getCompraProducto(StringBuilder xml, List<Producto> listaVenta, List<CompraProducto> compra, int cantidad) {
        for (Producto producto : listaVenta) {
            if (producto.getPrecioUnitarioCompra().doubleValue() > 0) {
                compra = producto.getCompra();
                if (!compra.isEmpty()) {
                    StringBuilder xmlCompra = new StringBuilder(5);
                    for (CompraProducto compraProducto : compra) {
                        if (compraProducto.getPrecio().doubleValue() > 0) {
                            xmlCompra.append("<itemCompra>")
                                    .append("<idCompraProducto>").append(compraProducto.getId()).append("</idCompraProducto>")
                                    .append("<precio>").append(compraProducto.getPrecio().doubleValue()).append("</precio>")
                                    .append("<nombrePack>").append(compraProducto.getPackFK().getDescripcion()).append("</nombrePack>")
                                    .append("<idPack>").append(compraProducto.getPackFK().getId()).append("</idPack>")
                                    .append("<presentacion>").append(compraProducto.getPresentacion()).append("</presentacion>")
                                    .append("</itemCompra>\n");
                        }

                    }

                    xml.append("<item>").append("<id>").append(producto.getId()).append("</id>")
                            .append("<descripcion>").append(producto.getDescripcion()).append("</descripcion>")
                            .append("<codigo>").append(producto.getCodigoBarra()).append("</codigo>")
                            .append("<resto>").append(producto.getCantidadTotalActual() - cantidad).append("</resto>")
                            .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>")
                            .append("<proveedor>")
                            .append("<id>").append(producto.getProveedorFK().getId()).append("</id>\n")
                            .append("<nombre>").append("<![CDATA[").append(producto.getProveedorFK().getNombre()).append("]]>").append("</nombre>")
                            .append("</proveedor>")
                            .append(xmlCompra);
                    xml.append("</item>");

                }

            }

        }

    }

    public void obtenerProductosConPrecioDeVenta(StringBuilder xml, List<Producto> listaVenta, List<VentaProducto> venta, int cantidad) {
        for (Producto producto : listaVenta) {
            if (producto.getPrecioUnitarioVenta().doubleValue() > 0) {
                venta = producto.getVenta();
                if (!venta.isEmpty()) {
                    StringBuilder xmlVentaProducto = new StringBuilder(5);
                    for (VentaProducto ventaProducto : venta) {
                        if (ventaProducto.getPrecio().doubleValue() > 0) {
                            xmlVentaProducto.append("<itemVenta>")
                                    .append("<idVentaProducto>").append(ventaProducto.getId()).append("</idVentaProducto>")
                                    .append("<precio>").append(new BigDecimal(ventaProducto.getPrecio().doubleValue()).setScale(2, RoundingMode.DOWN)).append("</precio>")
                                    .append("<nombrePack>").append(ventaProducto.getPackFK().getDescripcion()).append("</nombrePack>")
                                    .append("<idPack>").append(ventaProducto.getPackFK().getId()).append("</idPack>")
                                    .append("<presentacion>").append(ventaProducto.getPresentacion()).append("</presentacion>")
                                    .append("</itemVenta>\n");
                        }
                    }
                    xml.append("<item>").append("<id>").append(producto.getId()).append("</id>")
                            .append("<descripcion>").append(producto.getDescripcion()).append("</descripcion>")
                            .append("<codigo>").append(producto.getCodigoBarra()).append("</codigo>")
                            .append("<resto>").append(producto.getCantidadTotalActual() - cantidad).append("</resto>")
                            .append("<stock>").append(producto.getCantidadTotalActual()).append("</stock>")
                            .append("<proveedor>")
                            .append("<id>").append(producto.getProveedorFK().getId()).append("</id>\n")
                            .append("<nombre>").append("<![CDATA[").append(producto.getProveedorFK().getNombre()).append("]]>").append("</nombre>")
                            .append("</proveedor>")
                            .append(xmlVentaProducto);
                    xml.append("</item>");
                }
            }
        }
    }

    public void agregarColaCompraProductoyColaVentaProducto(Producto producto) {
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

    public void procesarParaAgregar(String proveedor, String codigo, String descripcion) {
        Proveedor proveedorEntity = null;
        int code = this.proveedor.crearProveedor(proveedor, "");
        if (code > 0) {
            codigoProveedor = code;
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
        producto.setSucursalFK(em.find(Sucursal.class, (long) 1));
        producto.setPrecioUnitarioCompra(BigDecimal.ZERO);
        producto.setPrecioUnitarioVenta(BigDecimal.ZERO);
        producto.setProveedorFK(proveedorEntity);
        producto.setPersonaFK(em.find(Persona.class, (long) 1));
        em.persist(producto);
        Query consulta10 = em.createQuery("SELECT p FROM Producto p WHERE p.proveedorFK.id =:idP");
        consulta10.setParameter("idP", proveedorEntity.getId());
        proveedorEntity.setProducto(consulta10.getResultList());
        Sucursal sucursal = em.find(Sucursal.class, (long) 1);
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

    @SuppressWarnings("empty-statement")
    public boolean verificarFechasVecimiento(Producto producto, Date nuevaFechaVencimiento) {
        Date fechaHoy = new Date();;
        return nuevaFechaVencimiento.after(fechaHoy) && nuevaFechaVencimiento.after(producto.getFechaVencimiento());
    }

    public long calcularPorcentajeCompraYSetearValoresPrecioCompra(Producto producto, String porcentaje) {
        List<CompraProducto> list = producto.getCompra();
        BigDecimal resultado = null;
        BigDecimal percent = new BigDecimal(porcentaje);
        for (CompraProducto compraProducto : list) {
            if (compraProducto.getPrecio().intValue() > 0) {
                resultado = calculoPorcentaje(compraProducto.getPrecio(), percent);
                if (percent.signum() == 1) {
                    compraProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue() + resultado.doubleValue()));
                } else if (percent.signum() == -1) {
                    compraProducto.setPrecio(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue() - resultado.doubleValue()));
                }

                if (compraProducto.getPackFK().getId() == 1) {
                    producto.setPrecioUnitarioCompra(compraProducto.getPrecio());
                }
                producto.setPorcentajeCompra(compraProducto.getPorcentaje());
                compraProducto.setProducto(producto);
            }

            em.flush();
        }
        /*    producto.setPorcentajeCompra(Double.parseDouble(porcentaje));      
            Query queryProductPurchaseList=em.createQuery("SELECT c FROM CompraProducto c WHERE c.productoFK.id =:idProducto ORDER BY c.packFK.id");
            queryProductPurchaseList.setParameter("idProducto", producto.getId());
            List<CompraProducto>listPurchasePrice = queryProductPurchaseList.getResultList();
        for (CompraProducto queryPrice : listPurchasePrice) {
            if(queryPrice.getPrecio().shortValue()>0){
                resultado = queryPrice.getPrecio().multiply((percent.doubleValue()>0?percent:percent.negate())).divide(new BigDecimal(100));
                    if(percent.doubleValue()>0&&queryPrice.getPackFK().getId()==1){
                        producto.setPrecioUnitarioCompra(BigDecimal.valueOf(queryPrice.getPrecio().doubleValue()+resultado.doubleValue()));
                    }else{
                        if(queryPrice.getPackFK().getId()==1)
                            producto.setPrecioUnitarioCompra(BigDecimal.valueOf(queryPrice.getPrecio().doubleValue()-resultado.doubleValue()));
                    }
            }
        }*/
        registrarOperacionCompraEnStock(producto, porcentaje);
        em.flush();
        return producto.getId();
    }

    private void setPurchaseUnitPrice(CompraProducto compraProducto, BigDecimal percent, Producto producto) {
        BigDecimal resultado;
        if (compraProducto.getPrecio().shortValue() > 0) {
            resultado = compraProducto.getPrecio().multiply((percent.doubleValue() > 0 ? percent : percent.negate())).divide(new BigDecimal(100));
            if (percent.doubleValue() > 0 && compraProducto.getPackFK().getId() == 1) {
                producto.setPrecioUnitarioCompra(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue() + resultado.doubleValue()));
            } else {
                if (compraProducto.getPackFK().getId() == 1) {
                    producto.setPrecioUnitarioCompra(BigDecimal.valueOf(compraProducto.getPrecio().doubleValue() - resultado.doubleValue()));
                }
            }
        }
    }

    private Date formatearFecha(String fecha) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
    }

    public long actualizarProducto(DatosProducto datosProducto) {
        Producto producto = em.find(Producto.class, datosProducto.getId());
        Query consulta = em.createNamedQuery("findProductoByDescripcion");
        consulta.setParameter("descripcion".toLowerCase(), datosProducto.getDescripcion().trim());
        if (!datosProducto.getDescripcion().equalsIgnoreCase(producto.getDescripcion()) && consulta.getResultList().isEmpty()) {
            producto.setDescripcion(datosProducto.getDescripcion());
        }
        producto.setDetalleCompra(datosProducto.getCompraProducto().getDetalle());
        producto.setDetalleProducto(datosProducto.getDetalle());
        producto.setDetalleVenta(datosProducto.getVentaProducto().getDetalle());
        producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
        producto.setPrecioUnitarioCompra(BigDecimal.valueOf(datosProducto.getPrecioUnitarioCompra()));
        producto.setPrecioUnitarioVenta(BigDecimal.valueOf(datosProducto.getPrecioUnitarioVenta()));
        producto.setCodigoBarra(datosProducto.getCodigoBarra());
        producto.setPorcentajeCompra(datosProducto.getPorcentajeCompra() > 0 ? datosProducto.getPorcentajeCompra() : 0);
        producto.setPorcentajeVenta(datosProducto.getPorcentajeVenta() > 0 ? datosProducto.getPorcentajeVenta() : 0);
        producto.setFraccionado(datosProducto.getFraccionado());
        if (producto.getCantidadIngresada() == 0) {
            try {
                producto.setCantidadIngresada(datosProducto.getCantidadIngresada());
                producto.setCantidadInicial(datosProducto.getCantidadIngresada());
                producto.setCantidadTotalActual(datosProducto.getCantidadTotalActual());
                producto.setFechaCantidadIngresada(Calendar.getInstance().getTime());
                if (verificarFechasVecimiento(producto, formatearFecha(datosProducto.getFechaVencimiento()))) {
                    producto.setFechaVencimiento(formatearFecha(datosProducto.getFechaVencimiento()));
                }

                producto.setFechaIngresoInicial(Calendar.getInstance().getTime());
                producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
                producto.setFechaUltimoIngreso(Calendar.getInstance().getTime());
                actualizarListaStock(producto, datosProducto);
            } catch (ParseException ex) {
                Logger.getLogger(EJBProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        actualizarListaCompra(datosProducto);
        actualizarListaVenta(datosProducto);
        em.flush();
        return producto.getId();
    }

    public long persistirListaCompraProducto(Producto producto, DatosProducto datosProducto) {
        if (datosProducto.getCompraProducto().getList().size() > 0) {
            List<DatosCompraProductoItem> listaVenta = datosProducto.getCompraProducto().getList();
            for (DatosCompraProductoItem datosCompraProductoItem : listaVenta) {
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
            producto.setFechaUltimaCompra(Calendar.getInstance().getTime());
            producto.setCompra(consulta.getResultList());
            em.merge(producto);
        }
        return producto.getId();
    }

    public void persistirListaVentaProducto(Producto producto, DatosProducto datosProducto) {
        if (!datosProducto.getVentaProducto().getList().isEmpty()) {
            List<DatosVentaProductoItem> listaVenta = datosProducto.getVentaProducto().getList();
            for (DatosVentaProductoItem datosVentaProductoItem : listaVenta) {
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
            em.merge(producto);
        }
    }

    public void persistirListaProductosProveedor(DatosProducto datosProducto) {
        Proveedor prove = em.find(Proveedor.class, datosProducto.getProveedor().getId());
        Query consulta = em.createNamedQuery("proveedorFindAll");
        consulta.setParameter("id", prove.getId());
        prove.setProducto(consulta.getResultList());
        em.merge(prove);
    }

    public void persistirProductosDeSucursal(DatosProducto datosProducto) {
        Sucursal sucursal = em.find(Sucursal.class, datosProducto.getSucursal().getId());
        Query consulta = em.createQuery("SELECT p FROM Producto p WHERE p.sucursalFK.id =:id");
        consulta.setParameter("id", sucursal.getId());
        sucursal.setProductoList(consulta.getResultList());
        em.merge(sucursal);
    }

    public void persistirListaStockProducto(Producto producto) {
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

    private void actualizarListaVenta(DatosProducto datosProducto) {
        List<DatosVentaProductoItem> listaVenta = datosProducto.getVentaProducto().getList();
        for (DatosVentaProductoItem datosVentaProductoItem : listaVenta) {
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
        stock.setDetalle("se ingreso mercaderia " + datosProducto.getCantidadIngresada());
        stock.setFechaAgregadoProducto(producto.getFechaIngresoInicial());
        stock.setPorcentajeCompra(0);
        stock.setPorcentajeVenta(0);
        stock.setPrecioUnitarioCompra(BigDecimal.ZERO);
        stock.setPrecioUnitarioVenta(BigDecimal.ZERO);
        stock.setProducto(producto);
        em.persist(stock);
        Query consulta = em.createNamedQuery("findAllStockForIdProduct");
        consulta.setParameter("id", producto.getId());
        List<StockProducto> listaVenta = consulta.getResultList();
        producto.setStockProductoList(listaVenta);
    }

    private void actualizarListaCompra(DatosProducto datosProducto) {
        List<DatosCompraProductoItem> listaVenta = datosProducto.getCompraProducto().getList();
        for (DatosCompraProductoItem datosCompraProductoItem : listaVenta) {
            CompraProducto compra = em.find(CompraProducto.class, datosCompraProductoItem.getId());
            compra.setPrecio(BigDecimal.valueOf(datosCompraProductoItem.getPrecio()));
            compra.setPresentacion(datosCompraProductoItem.getPresentacion());
            compra.setPorcentaje(Double.valueOf(datosCompraProductoItem.getPorcentaje()));
        }
    }
}
