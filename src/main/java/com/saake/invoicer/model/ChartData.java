/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.model;

import java.util.Date;

/**
 *
 * @author jn
 */
public class ChartData {
    private String name;
    private Date date;
    private String day;
    private Integer val;

    public ChartData(String name, Date date, Integer val) {
        this.name = name;
        this.date = date;
        this.val = val;
    }
    
    public ChartData(String name, String day, Integer val) {
        this.name = name;
        this.day = day;
        this.val = val;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day ;
    }    
    
}
