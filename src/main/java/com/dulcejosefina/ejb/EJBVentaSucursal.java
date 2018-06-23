package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.DetalleVentaSucursal;
import com.dulcejosefina.entity.HistoricoVentaSucursal;
import com.dulcejosefina.entity.Persona;
import com.dulcejosefina.entity.Producto;
import com.dulcejosefina.entity.StockProducto;
import com.dulcejosefina.entity.Sucursal;
import com.dulcejosefina.entity.VentaSucursal;
import com.dulcejosefina.utils.DatosVentaSucursal;
import com.dulcejosefina.utils.ItemDetalleVentaSucursalItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
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

    public EJBVentaSucursal() {
    }

    @WebMethod
    public String crearVenta(String xmlVenta) {
        String resultado = "";
        DatosVentaSucursal datosVentaSucursal = new DatosVentaSucursal();
        datosVentaSucursal = datosVentaSucursal.transformarAObjeto(xmlVenta);
        resultado = VerificarStockProductosDeVentaAntesDeAlmacenar(datosVentaSucursal);
        try {
            if (resultado.equals("")) {
                VentaSucursal venta = new VentaSucursal();
                venta = persistirVenta(venta, datosVentaSucursal);
                persistirDetalleVenta(venta, datosVentaSucursal);
                productoBean.actualizarStockProducto(venta, datosVentaSucursal);
                insertarHistoricoVentaSucursal(venta);
                em.flush();
                resultado = String.valueOf(venta.getId().intValue());
            }
        } catch (Exception e) {
            Logger.getLogger(e.getLocalizedMessage());
        }
        return resultado;
    }

    private void persistirDetalleVenta(VentaSucursal venta, DatosVentaSucursal datosVentaSucursal) {
        List<ItemDetalleVentaSucursalItem> lista = datosVentaSucursal.getDetalleVenta().getList();
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
        if (venta.getAnulado() == 0) {
            xml.append("<venta>").append("<usuario>").append(em.find(Persona.class, venta.getIdUsuarioExpidioVenta()).getNombre()).append("</usuario>")
                    .append(venta.toXML()).append("</venta>\n").append("</Lista>\n");
        }
        return xml.toString();
    }

    @WebMethod
    public String selectVentasHastaElMomento() {
        LocalDateTime timePoint = LocalDateTime.now(ZoneId.systemDefault());
        Query consulta = em.createNamedQuery("findVentasDiaBySucursalAndFechaYHora");
        if (timePoint.getHour() >= 7 && timePoint.getHour() <= 16) {
            String horaManana1 = ResourceBundle.getBundle("config").getString("HORA_MANANA1");
            String horaManana2 = ResourceBundle.getBundle("config").getString("HORA_MANANA2");
            try {
                consulta.setParameter("1", new SimpleDateFormat("hh").parse(horaManana1));
                consulta.setParameter("2", new SimpleDateFormat("HH").parse(horaManana2));
            } catch (ParseException ex) {
                Logger.getLogger(EJBVentaSucursal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (timePoint.getHour() >= 17 && timePoint.getHour() <= 24) {
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
        List<VentaSucursal> lista = consulta.getResultList();
        return recorreLista(lista, null);
    }

    @WebMethod
    public String selectVentasDeUnDiaDeterminado(String fecha, int sucursal) {
        Query consulta = em.createNamedQuery("findVentaDelDiaPorFecha");
        String nuevaFecha = fecha.substring(6, 10).concat("-").concat(fecha.substring(3, 5)).concat("-").concat(fecha.substring(0, 2));
        consulta.setParameter("fecha", nuevaFecha);
        consulta.setParameter("sucursal", sucursal);
        List<VentaSucursal> lista = consulta.getResultList();
        return recorreLista(lista, null);
    }

    private String recorreLista(List<VentaSucursal> lista, Date fecha) {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        double totalTodasLasVentas = 0;
        for (VentaSucursal ventaSucursal : lista) {
            xml.append("<venta>\n")
                    .append("<idVenta>").append(ventaSucursal.getId()).append("</idVenta>\n")
                    .append("<fechaQuery>").append(fecha != null ? new SimpleDateFormat("dd/MM/yyyy").format(fecha) : 0).append("</fechaQuery>\n")
                    .append("<fecha>").append(ventaSucursal.getFechaVenta() != null ? new SimpleDateFormat("dd/MM/yyyy").format(ventaSucursal.getFechaVenta()) : 0).append("</fecha>\n")
                    .append("<hora>").append(ventaSucursal.getHoraVenta() != null ? new SimpleDateFormat("HH:mm ss").format(ventaSucursal.getHoraVenta()) : 0).append("</hora>")
                    .append("<totalApagar>").append(ventaSucursal.getTotalAPagar().setScale(2, RoundingMode.DOWN)).append("</totalApagar>\n")
                    .append("<sucursal>").append(ventaSucursal.getSucursalFK().getNombre()).append("</sucursal>\n")
                    .append("<empleado>").append(em.find(Persona.class, ventaSucursal.getIdUsuarioExpidioVenta()).getNombre()).append("</empleado>\n")
                    .append("<cliente>").append(ventaSucursal.getNombre()).append("</cliente>\n");
            for (DetalleVentaSucursal detalle : ventaSucursal.getListaDetalleVentaSucursal()) {
                xml.append("<item>\n");
                xml.append("<codigo>").append(detalle.getCodigo()).append("</codigo>\n")
                        .append("<nombre>").append(detalle.getDescripcion()).append("</nombre>\n")
                        .append("<presentacion>").append(detalle.getPresentacion()).append("</presentacion>\n")
                        .append("<precio>").append(new BigDecimal(detalle.getPrecio()).setScale(2, RoundingMode.DOWN)).append("</precio>\n")
                        .append("<cantidad>").append(detalle.getCantidad()).append("</cantidad>\n")
                        .append("<subtotal>").append(detalle.getSubtotal().setScale(2, RoundingMode.DOWN).doubleValue()).append("</subtotal>\n");
                xml.append("</item>\n");

            }

            totalTodasLasVentas = totalTodasLasVentas + ventaSucursal.getTotalAPagar().setScale(2, RoundingMode.DOWN).doubleValue();

            xml.append("</venta>\n");
        }
        xml.append("<totalVentas>").append(new BigDecimal(totalTodasLasVentas).setScale(2, RoundingMode.DOWN)).append("</totalVentas>\n");

        return xml.append("</Lista>").toString();
    }

    @WebMethod
    public long anularVenta(long idVenta) {
        long retorno = 0;
        int cantidadItem = 0;
        VentaSucursal venta = em.find(VentaSucursal.class, idVenta);
        venta.setAnulado(1);
        List<DetalleVentaSucursal> lista = venta.getListaDetalleVentaSucursal();
        for (DetalleVentaSucursal detalleVentaSucursal : lista) {
            cantidadItem = 0;
            Producto producto = em.find(Producto.class, detalleVentaSucursal.getProducto().getId());
            cantidadItem = setearCantidadProducto(detalleVentaSucursal, producto);
            producto.setFechaUltimaActualizacion(Calendar.getInstance().getTime());
            StockProducto stockProducto = new StockProducto();
            stockProducto.setCantidadActual(producto.getCantidadTotalActual());
            stockProducto.setCantidadAgregada(0);
            stockProducto.setCantidadInicial(0);
            stockProducto.setDetalle("SE VOLVIO A REPONER STOCK POR VENTA ANULADA " + venta.getId() + " CANTIDAD REPUESTA " + cantidadItem);
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
        retorno = venta.getId();
        return retorno;
    }

    @WebMethod
    public int getRecordCountVentas() {
        Query consulta = em.createNamedQuery("findAllVentas");
        return consulta.getResultList().size();
    }

    @WebMethod
    public String verVentasPaginadas(int index, int recordCount) {
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createNamedQuery("findAllVentas");
        consulta.setMaxResults(recordCount);
        consulta.setFirstResult(index * recordCount);
        List<VentaSucursal> lista = consulta.getResultList();
        for (VentaSucursal ventaSucursal : lista) {
            xml.append("<item>").append(ventaSucursal.toXML()).append("</item>");
        }
        return xml.append("</Lista>").toString();
    }

    @WebMethod(operationName = "selectAllVentas")
    public String selectAllVentas() {
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consulta = em.createNamedQuery("findAllVentas");
        List<VentaSucursal> lista = consulta.getResultList();
        for (VentaSucursal ventaSucursal : lista) {
            xml.append("<item>").append(ventaSucursal.toXML()).append("</item>");
        }
        return xml.append("</Lista>").toString();
    }

    @WebMethod(operationName = "selectVenta")
    public String selectVenta(long idVenta, long idSucursal) {
        StringBuilder xml = new StringBuilder("<Lista>");
        Query consultaVentaBySucursal = em.createNamedQuery("findVentaBySucursal").setParameter("idVenta", idVenta).setParameter("idSucursal", idSucursal);
        List<VentaSucursal> lista = consultaVentaBySucursal.getResultList();
        if (lista.isEmpty()) {
            xml.append("vacio").append("</Lista>");
        } else {
            for (VentaSucursal ventaSucursal : lista) {
                xml.append("<item>").append(ventaSucursal.toXML()).append("</item>").append("</Lista>");
            }
        }
        return xml.toString();
    }

    @WebMethod()
    public long eliminarProductoDeUnaVenta(long idDetalleVenta, long idProducto, long idVenta) {
        int resultadoEliminarProductoDeVenta = 0;
        VentaSucursal venta = em.find(VentaSucursal.class, idVenta);
        if (venta.getAnulado() == 0) {
            Producto producto = em.find(Producto.class, idProducto);
            Query consultaDetalleVentaProductoAEliminar = em.createQuery("SELECT d FROM DetalleVentaSucursal d WHERE d.id =:idDetalleVenta AND d.producto.id =:idProducto");
            consultaDetalleVentaProductoAEliminar.setParameter("idDetalleVenta", idDetalleVenta);
            consultaDetalleVentaProductoAEliminar.setParameter("idProducto", idProducto);
            List<DetalleVentaSucursal> lista = consultaDetalleVentaProductoAEliminar.getResultList();
            for (DetalleVentaSucursal detalle : lista) {
                resultadoEliminarProductoDeVenta = setearCantidadProducto(detalle, producto);
                deleteDetalleVentaAndUpdateList(idDetalleVenta, idVenta, venta);
                actualizarStockProducto(producto, detalle);
                venta.setCantidad(venta.getCantidad() - 1);
                venta.setTotalGeneral(BigDecimal.valueOf(venta.getTotalGeneral().doubleValue() - detalle.getSubtotal().doubleValue()));
                Double subtotalConDescuentosYRecargos = (venta.getTotalGeneral().doubleValue() + venta.getDescuentoPesos().doubleValue()) - venta.getRecargoPesos().doubleValue();
                venta.setTotalAPagar(BigDecimal.valueOf(subtotalConDescuentosYRecargos));
            }
            if (venta.getListaDetalleVentaSucursal().isEmpty()) {
                Query deleteVenta = em.createQuery("DELETE FROM VentaSucursal v WHERE v.id =:idVenta");
                deleteVenta.setParameter("idVenta", idVenta);
                deleteVenta.executeUpdate();
                resultadoEliminarProductoDeVenta = -1;
            }
            em.flush();
        } else {
            resultadoEliminarProductoDeVenta = -2;
        }
        return resultadoEliminarProductoDeVenta;
    }

    private int setearCantidadProducto(DetalleVentaSucursal detalleVentaSucursal, Producto producto) {
        int cantidadItem = 0;
        if (detalleVentaSucursal.getNombrePack().equalsIgnoreCase("precio unitario")) {
            cantidadItem = detalleVentaSucursal.getCantidad();
            producto.setCantidadTotalActual(producto.getCantidadTotalActual() + cantidadItem);

        } else {
            cantidadItem = detalleVentaSucursal.getCantidad() * detalleVentaSucursal.getPresentacion();
            producto.setCantidadTotalActual(producto.getCantidadTotalActual() + cantidadItem);
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
        String resultadoVerificacionStock = "";
        boolean stock = false;
        List<ItemDetalleVentaSucursalItem> lista = datosVentaSucursal.getDetalleVenta().getList();
        if (lista!=null && !lista.isEmpty()) {
            for (int i = 0; i < lista.size() && !stock; i++) {
                ItemDetalleVentaSucursalItem item = lista.get(i);
                Producto producto = em.find(Producto.class, item.getId());
                if (item.getNombrePack().equalsIgnoreCase("precio unitario")) {
                    if (producto.getCantidadTotalActual() - item.getCantidad() < 0) {
                        resultadoVerificacionStock = new StringBuilder("El Producto ").append(producto.getDescripcion().toUpperCase()).append(" no se puede agregar al listado por que no tiene stock suficiente, el actual es ").append(producto.getCantidadTotalActual()).
                                append("\n cantidad ingresada ").append(item.getCantidad()).append(" verifique!".toUpperCase()).toString();
                        stock = true;
                    }
                } else {
                    int stockItem = item.getCantidad() * item.getPresentacion();
                    if (producto.getCantidadTotalActual() < stockItem) {
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
        stockProducto.setDetalle("SE VOLVIO A REPONER STOCK POR PRODUCTO ELIMINADO " + producto.getId() + " DE LA VENTA " + detalle.getVentaSucursal().getId());
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

    private void insertarHistoricoVentaSucursal(VentaSucursal venta) {
        HistoricoVentaSucursal historicoVentaSucursal = new HistoricoVentaSucursal();
        historicoVentaSucursal.setCantidad(venta.getCantidad());
        historicoVentaSucursal.setDescuentoPesos(venta.getDescuentoPesos());
        historicoVentaSucursal.setFechaVenta(Calendar.getInstance().getTime());
        historicoVentaSucursal.setPorcentajeDescuento(venta.getPorcentajeDescuento());
        historicoVentaSucursal.setPorcentajeRecargo(venta.getPorcentajeRecargo());
        historicoVentaSucursal.setRecargoPesos(venta.getRecargoPesos());
        historicoVentaSucursal.setTotalVenta(venta.getTotalAPagar());
        historicoVentaSucursal.setVentaSucursal(venta);
        em.persist(historicoVentaSucursal);
        Query consulta = em.createQuery("SELECT h FROM HistoricoVentaSucursal h WHERE h.ventaSucursal.id =:id");
        consulta.setParameter("id", venta.getId());
        venta.setHistoricoVentaSucursal(consulta.getResultList());
    }

    @WebMethod
    public String sumaTotalVentasMensual(String desde, String hasta) {
        Query consulta = null;
        String resultado = null;
        String nuevoDesde = new String();
        String nuevoHasta = new String();
        try {
            nuevoDesde = desde.substring(6, 10).concat("-").concat(desde.substring(3, 5)).concat("-").concat(desde.substring(0, 2));
            nuevoHasta = hasta.substring(6, 10).concat("-").concat(hasta.substring(3, 5)).concat("-").concat(hasta.substring(0, 2));

            desde = nuevoDesde;
            hasta = nuevoHasta;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (sdf.parse(desde).before(sdf.parse(hasta))) {
                consulta = em.createQuery("SELECT SUM(v.totalAPagar) FROM VentaSucursal v WHERE CAST(v.fechaVenta as DATE) BETWEEN ?1 AND ?2 AND v.anulado=0");
                consulta.setParameter("1", desde);
                consulta.setParameter("2", hasta);

                if (consulta.getSingleResult() != null) {
                    resultado = new BigDecimal(consulta.getSingleResult().toString()).setScale(2, RoundingMode.DOWN).toPlainString();
                }
            } else {
                resultado = "La fecha Desde debe ser anterior a Hasta";
            }
        } catch (ParseException ex) {
            Logger.getLogger(EJBVentaSucursal.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
        return (resultado != null ? resultado : "0");
    }

    private VentaSucursal persistirVenta(VentaSucursal venta, DatosVentaSucursal datosVentaSucursal) {
        venta.setDescuentoPesos(BigDecimal.valueOf(datosVentaSucursal.getTotalDescuento()));
        venta.setFechaVenta(Calendar.getInstance().getTime());
        venta.setIdUsuarioExpidioVenta(datosVentaSucursal.getIdUsuarioExpidioVenta());
        if (datosVentaSucursal.getPersona().getId() > 0) {
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
        return venta;
    }

    @WebMethod
    public String getSalesCanceledTodayAndPrevius() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query query = em.createQuery("SELECT v FROM VentaSucursal v WHERE  v.fechaVenta BETWEEN ?1 AND CAST(CURRENT_DATE as DATE) and v.anulado=1 order by v.id desc");
        query.setParameter("1", sdf.parse(sdf.format(date)));

        List<VentaSucursal> lista = query.getResultList();

        return recorreLista(lista, date);
    }

}
