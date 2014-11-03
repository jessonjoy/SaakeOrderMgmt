/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.controller;

import com.saake.invoicer.model.SearchWorkOrderVO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author jn
 */
@ManagedBean
@SessionScoped
public class SearchController implements Serializable{
    private SearchWorkOrderVO searchWorkOrder;   
        
    @PostConstruct
    public void init(){
        searchWorkOrder = new SearchWorkOrderVO();
    }

    public SearchWorkOrderVO getSearchWorkOrder() {
        return searchWorkOrder;
    }

    public void setSearchWorkOrder(SearchWorkOrderVO searchWorkOrder) {
        this.searchWorkOrder = searchWorkOrder;
    }             
}
