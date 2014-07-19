/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.controller;

import com.saake.invoicer.entity.User;
import com.saake.invoicer.util.Constants;
import com.saake.invoicer.util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

/**
 *
 * @author jn
 */
@ManagedBean(name = "listCtrl")
@SessionScoped
public class ListsController implements Serializable{
    
    @Inject UserController userCtrl;
    
    private SelectItem[] monthsSI;
    private SelectItem[] vehYearsSI;
    private List<User> users;
    
    @PostConstruct
    public void init(){
        monthsSI = JsfUtil.getSelectItems(getMonths(), true);
        vehYearsSI = JsfUtil.getSelectItems(getVehYears(), true);
        users = userCtrl.getItems();
    }
   
    public List<String> getMonths(){
        return Constants.monthsList;
    }
    
    public List<Integer> getVehYears(){
        return Constants.vehYearsList;
    }
    
    public List<User> getUsers(){
        return users;
    }
    
    public SelectItem[] getMonthsSI(){
        return monthsSI;
    }
    
    public SelectItem[] getVehYearsListSI(){
        return vehYearsSI;
    }
    
}
