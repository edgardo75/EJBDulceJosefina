/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ejb;

/**
 *
 * @author Edgardo
 */
public interface EJBPersonaBeanRemote {
    public long crearEmpleado(String xmlEmpleado);
     public String selectAllEmpleadosYJefes();
}
