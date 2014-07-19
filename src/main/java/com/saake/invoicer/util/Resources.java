/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
public class Resources {
    @SuppressWarnings("unused")
    @Produces
    @PersistenceContext(unitName = "ordermgrPU")
    private EntityManager em;
//
    @Produces
    public Log getLog(InjectionPoint injectionPoint) {
        System.out.println("Inside getLog:");
        System.out.println("injectionPoint:" + injectionPoint.toString());
        return LogFactory.getLog(injectionPoint.getMember().getDeclaringClass().getName());
    }

//    @Produces
//    @RequestScoped
//    public FacesContext produceFacesContext() {
//        return FacesContext.getCurrentInstance();
//    }

}
