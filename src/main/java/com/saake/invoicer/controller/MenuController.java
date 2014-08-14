/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
@ManagedBean(name = "menuCtrl")
@ViewScoped
public class MenuController implements Serializable {

    private static final Log log = LogFactory.getLog(MenuController.class);
    
    public String menuItemFocus ;    

    public MenuController() {
        log.info("Inside MenuController!!!");
    }
        
    @PostConstruct
    private void init() {
        
        if (Utils.notBlank(JsfUtil.getViewId())) {
            String[] viewIdArr = JsfUtil.getViewId().split("/");
            
            if(viewIdArr != null && viewIdArr.length > 0){
                String prependStr = "";
                if(viewIdArr[1].contains(".xhtml")){
                    prependStr = viewIdArr[1].split(".xhtml")[0];
                }
                else{
                    prependStr = viewIdArr[1];
                }
                menuItemFocus = prependStr + "Menu";
                log.info("menuItemFocus : " + menuItemFocus);
            }
        }        
    }
    
    public String getMenuItemFocus() {
        return menuItemFocus;
    }

    public void setMenuItemFocus(String menuItemFocus) {
        this.menuItemFocus = menuItemFocus;
    }
    
    
}
