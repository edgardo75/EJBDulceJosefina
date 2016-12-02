package com.dulcejosefina.ejb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
@LocalBean



public class EJBTimmer {
@PersistenceContext
private EntityManager em;
@Inject
private EJBProductoBean producto;
@Schedule(persistent = false,timezone = "America/Argentina/San_Juan",second = "0",hour = "0",minute = "0")
    private void enviarVentasDiariasPorEmail(){  
  
  }
}
