/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.model;

import com.saake.invoicer.entity.User;
import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jn
 */
public class UserInvMonLbrTot implements Serializable{

    private User user;   
    private String month;
    private String year;
    private Double totLaborAmt;
    private Double totAdjLaborAmt;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getTotLaborAmt() {
        return totLaborAmt;
    }

    public void setTotLaborAmt(Double totLaborAmt) {
        this.totLaborAmt = totLaborAmt;
    }

    public Double getTotAdjLaborAmt() {
        return totAdjLaborAmt;
    }

    public void setTotAdjLaborAmt(Double totAdjLaborAmt) {
        this.totAdjLaborAmt = totAdjLaborAmt;
    }

    public boolean empty() {
        return ( this.totAdjLaborAmt == null && this.totLaborAmt == null
                && Utils.isBlank(this.month) && (this.user == null && Utils.isBlank(this.user.getUserId())) && Utils.isBlank(this.year));
                
    } 
       
}
