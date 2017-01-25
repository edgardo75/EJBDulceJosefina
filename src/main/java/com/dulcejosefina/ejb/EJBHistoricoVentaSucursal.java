/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

import com.dulcejosefina.entity.HistoricoVentaSucursal;
import com.dulcejosefina.entity.VentaSucursal;
import com.dulcejosefina.utils.DatosVentaSucursal;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Edgardo
 */
@Stateless
@LocalBean
public class EJBHistoricoVentaSucursal {
@PersistenceContext
private EntityManager em;

    public void insertarHistoricoVentaSucursal(VentaSucursal venta,DatosVentaSucursal datos) {
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
        
        Query consulta =em.createQuery("SELECT h FROM HistoricoVentaSucursal h WHERE h.ventaSucursal.id =:id");
        consulta.setParameter("id", venta.getId());
        venta.setHistoricoVentaSucursal(consulta.getResultList());
    }
    
}
