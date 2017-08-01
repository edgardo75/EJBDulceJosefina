package com.dulcejosefina.utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
public class DatosVentaSucursal {
    private long id;
    private String fechaVenta;
    private int cantidad;
    private double porcentajeDescuento;
    private double porcentajeRecargo;
    private double totalRecargo;
    private double totalGeneral;
    private double totalAPagar;
    private double totalDescuento;
    private long idUsuarioExpidioVenta;
    private DatosPersona persona;
    private DatosSucursal sucursal;
    private DatosDetalleVentaSucursal detalleVenta;
    private DatosProveedor proveedor;
    public DatosVentaSucursal() {
    }
    public long getId() {
        return id;
    }
    public String getFechaVenta() {
        return fechaVenta;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
    public double getPorcentajeRecargo() {
        return porcentajeRecargo;
    }
    public double getTotalRecargo() {
        return totalRecargo;
    }
    public double getTotalGeneral() {
        return totalGeneral;
    }
    public double getTotalAPagar() {
        return totalAPagar;
    }
    public double getTotalDescuento() {
        return totalDescuento;
    }
    public DatosPersona getPersona() {
        return persona;
    }
    public DatosSucursal getSucursal() {
        return sucursal;
    }
    public DatosDetalleVentaSucursal getDetalleVenta() {
        return detalleVenta;
    }
    public long getIdUsuarioExpidioVenta() {
        return idUsuarioExpidioVenta;
    }
    public DatosProveedor getProveedor() {
        return proveedor;
    }
       public DatosVentaSucursal transformarAObjeto(String datosVenta) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("venta", DatosVentaSucursal.class);
        xstream.alias("persona", DatosPersona.class);
        xstream.alias("sucursal",DatosSucursal.class);
        xstream.alias("detalleVenta",DatosDetalleVentaSucursal.class);
        xstream.alias("itemVenta", ItemDetalleVentaSucursalItem.class);
        xstream.alias("proveedor", DatosProveedor.class);
        xstream.addImplicitCollection(DatosDetalleVentaSucursal.class, "list");
        return (DatosVentaSucursal) xstream.fromXML(datosVenta);
    }
}
